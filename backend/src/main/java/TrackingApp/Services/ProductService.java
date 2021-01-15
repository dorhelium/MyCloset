package TrackingApp.Services;

import TrackingApp.Entities.Dto.ImageDto;
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import TrackingApp.Exceptions.DataViolationException;
import TrackingApp.Repositories.ImageRepository;
import TrackingApp.Repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;


    public Product saveOrUpdate(Product product) {
        productRepository.save(product);
        return product;
    }

    public Product delete(int id) {
        Product product = productRepository.getOne(id);
        productRepository.delete(id);
        return product;
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    public Product getProductById(int id){
        return productRepository.findOne(id);
    }

    public Product scrapeAndAddProduct(String url) {
        url = url.split("\\?")[0];
        if (!validateUrl(url)){
            throw new DataViolationException("This URL is not valid. Please check the URL and re-enter.");
        }

        switch(getCompanyName(url)) {
            case "zara":
                return scrapeZara(url);
            case "aritzia":
                return scrapeAritzia(url);
            default:
                throw new DataViolationException("Sorry, we don't support this website yet. More features coming soon...");
        }
    }

    public List<ImageDto> getImageByProductId (int productId){
        List<Image> images = imageRepository.findImagesByProductId(productId);
        Product product = productRepository.findOne(productId);
        return images.stream().map(image -> new ImageDto(image.getId(),
                Base64.getEncoder().encodeToString(image.getImageData()),
                product)).collect(Collectors.toList());
    }


    private Product scrapeZara(String url) {

        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("price")));// instead of id u can use cssSelector or xpath of ur element.

        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        Elements repositories = doc.getElementsByClass("price");
        Element repository = repositories.first();

        String priceWithoutDiscountStr = repository.getElementsByClass("main-price").text();
        String originalPriceStr = repository.getElementsByClass("line-through").text();
        String salePriceStr = repository.getElementsByClass("sale").text();

        List<Product> storedProduct = productRepository.findByUrl(url);
        Product product = storedProduct.isEmpty()? new Product(url): storedProduct.get(0);
        product.setProductName(doc.title());
        product.setBrand("ZARA");

        if (!priceWithoutDiscountStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(priceWithoutDiscountStr.split(" ")[0]));
            product.setOnSale(false);
        }

        if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(originalPriceStr.split(" ")[0]));
            product.setSalePrice(Float.parseFloat(salePriceStr.split(" ")[0]));
            product.setOnSale(true);
        }


        ArrayList<String> sizes = new ArrayList<>();
        Elements sizerepositories = doc.getElementsByClass("product-size");
        for (Element s: sizerepositories){
            String size = s.getElementsByClass("size-name").first().text();
            sizes.add(size);
            /*
            String classes = s.attr("class");
            if (Arrays.stream(classes.split(" ")).
                    filter(str->str.equals("disabled")).
                    collect(Collectors.toList()).isEmpty()){
                availableSizes.add(size);
            }

             */
        }

        ArrayList<String> colors = new ArrayList<>();
        Elements colorrepositories = doc.getElementsByClass("_color");
        colors.add(doc.getElementsByClass("_colorName").first().text());
        for (Element c: colorrepositories){
            String color = c.text();
            colors.add(color);
        }

        product.setSizes(sizes);
        product.setColors(colors);
        //product.setAvailableSizes(availableSizes);

        saveOrUpdate(product);

        //save all images
        List<Image> images = imageRepository.findByProduct(product);
        if (images.isEmpty()){
            Elements imgs = doc.getElementsByClass("image-big _img-zoom _main-image");
            for (Element el : imgs) {
                String src = el.absUrl("src");
                getImage(src, product);
            }
        }

        driver.close();
        return product;
    }

    private Product scrapeAritzia(String url) {

        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get(url);

        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        Element repository = doc.getElementsByClass("product-price").first();

        String priceWithoutDiscountStr = repository.getElementsByClass("price-default").text();
        String originalPriceStr = repository.getElementsByClass("price-standard").text();
        String salePriceStr = repository.getElementsByClass("price-sales").text();

        List<Product> storedProduct = productRepository.findByUrl(url);
        Product product = storedProduct.isEmpty()? new Product(url): storedProduct.get(0);
        product.setProductName(doc.title());
        product.setBrand("ARITZIA");

        if (!priceWithoutDiscountStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(priceWithoutDiscountStr.substring(1)));
            product.setOnSale(false);
        }

        if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(originalPriceStr.substring(1)));
            product.setSalePrice(Float.parseFloat(salePriceStr.substring(1)));
            product.setOnSale(true);
        }

        ArrayList<String> sizes = new ArrayList<>();

        Element sizeHTML = doc.getElementsByClass("swatches swatches-size js-swatches__size cf mb3 mb0-ns").first();
        Elements sizerepositories = sizeHTML.getElementsByClass("sizeanchor js-swatches__size-anchor");
        for (Element s: sizerepositories){
            String size = s.text();
            sizes.add(size);
            /*
            String availability = s.parent().attr("class");
            if (!availability.equals(" unavailable")){
                availableSizes.add(size);
            }
             */
        }
        product.setSizes(sizes);
        //product.setAvailableSizes(availableSizes);

        ArrayList<String> colors = new ArrayList<>();
        Elements colorrepositories = doc.getElementsByClass("swatchanchor");
        for (Element c: colorrepositories){
            String color = c.text();
            colors.add(color);
        }
        product.setColors(colors);

        product = saveOrUpdate(product);

        //save all images
        List<Image> images = imageRepository.findByProduct(product);
        if (images.isEmpty()){
            Elements imgs = doc.getElementsByClass("ar-product-images__image-media js-product-images__image-media lazyr lazy");
            for (Element el : imgs) {
                String src = el.absUrl("src");
                getImage(src, product);
            }
        }

        driver.close();
        return product;
    }


    private Image getImage(String src, Product product) {
        try {
            URL url = new URL(src);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                byteArrayOutputStream.write(read);
            }

            byte[] imageData = byteArrayOutputStream.toByteArray();
            Image img = new Image(imageData, product);
            imageRepository.save(img);
            inputStream.close();
            byteArrayOutputStream.close();
            return img;
        }catch (IOException e){
            System.out.println("Failed to download image.");
            return null;
        }
    }

    private boolean validateUrl(String link){
        HttpURLConnection con = null;
        try {
            URL url = new URL(link);
            con =  (HttpURLConnection)  url.openConnection ();
            con.setRequestMethod ("HEAD");
            con.connect();
            int code = con.getResponseCode() ;
            return (code >= 200 && code <= 299) || code == 301 || code == 302 || code == 303;
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.close(con);
        }
    }

    private String getCompanyName(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            String domain = host.startsWith("www.") ? host.substring(4) : host;
            String company = domain.split(".com")[0];
            return company;
        } catch (URISyntaxException e) {
            throw new DataViolationException("This URL is not valid. Please check the URL and re-enter.");
        }
    }





}