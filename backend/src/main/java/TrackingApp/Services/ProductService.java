package TrackingApp.Services;

import TrackingApp.Entities.Dto.ImageDto;
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Item;
import TrackingApp.Entities.Product;
import TrackingApp.Exceptions.InvalidDataException;
import TrackingApp.Repositories.ImageRepository;
import TrackingApp.Repositories.ProductRepository;

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
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository;

    public Product getProductById(int id){
        return productRepository.findById(id).orElse(null);
    }

    public Product scrapeAndAddProduct(String url) {
        if (!validateUrl(url)){
            throw new InvalidDataException("This URL is not valid. Please check the URL and re-enter.");
        }
        List<Product> existingProduct = productRepository.findByUrl(url);
        if (!existingProduct.isEmpty()){
            return existingProduct.get(0);
        }
        switch(getCompanyName(url)) {
            case "zara":
                return scrapeZara(url);
            case "aritzia":
                return scrapeAritzia(url);
            default:
                throw new InvalidDataException("Sorry, we don't support this website yet. More features coming soon...");
        }
    }


    private Product scrapeZara(String url) {
        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("price__amount")));

        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        // basic infos
        List<Product> storedProduct = productRepository.findByUrl(url);
        Product product = storedProduct.isEmpty()? new Product(url): storedProduct.get(0);
        product.setProductName(doc.title());
        product.setBrand("ZARA");

        //scrape prices
        Elements priceElements = doc.getElementsByClass("price__amount");
        String originalPriceStr = priceElements.get(0).text();
        String salePriceStr = priceElements.size()==2? priceElements.get(1).text():"";
        product.setOriginalPrice(Float.parseFloat(originalPriceStr.split(" ")[0]));
        product.setOnSale(!salePriceStr.equals(""));
        if (product.isOnSale()){
            product.setSalePrice(Float.parseFloat(salePriceStr.split(" ")[0]));
        }

        // scrape sizes
        Elements sizeElements = doc.getElementsByClass("product-size-info__name");
        ArrayList<String> sizes = new ArrayList<>();
        for (Element s: sizeElements){
            String size = s.text();
            sizes.add(size);
        }
        product.setSizes(sizes);

        //scrape colors
        ArrayList<String> colors = new ArrayList<>();
        Elements colorElements = doc.getElementsByClass("product-detail-info-color-selector__color-button");
        for (Element c: colorElements){
            String color = c.text();
            colors.add(color);
        }

        Elements defaultColorElement = doc.getElementsByClass("product-detail-info__color");
        if (defaultColorElement.size()>0){
            String defaultColor = defaultColorElement.first().text().split(" ")[1];
            if (colors.size()==0){
                colors.add(defaultColor);
            }
        }
        product.setColors(colors);

        //scrape images
        Elements elements = doc.getElementsByClass("media-image__image media__wrapper--media");
        Elements productImageElements = doc.getElementsByClass("product-detail-images__images");
        if (productImageElements.size()>0) {
            Elements imageElements = productImageElements.get(0).getElementsByClass("media-image__image media__wrapper--media");
            for (Element element : imageElements) {
                String src = element.absUrl("src");
                Image image = getImage(src);
                product.getImages().add(image);
            }
        }

        product = productRepository.save(product);
        driver.close();
        return product;
    }


    private Product scrapeAritzia(String url) {
        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get(url);

        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        Element repository = doc.getElementsByClass("product-price").first();

       //basic info
        List<Product> storedProduct = productRepository.findByUrl(url);
        Product product = storedProduct.isEmpty()? new Product(url): storedProduct.get(0);
        product.setProductName(doc.title());
        product.setBrand("ARITZIA");

        //scrape prices
        String priceWithoutDiscountStr = repository.getElementsByClass("price-default").text();
        String originalPriceStr = repository.getElementsByClass("price-standard").text();
        String salePriceStr = repository.getElementsByClass("price-sales").text();

        if (!priceWithoutDiscountStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(priceWithoutDiscountStr.substring(1)));
            product.setOnSale(false);
        }
        if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(originalPriceStr.substring(1)));
            product.setSalePrice(Float.parseFloat(salePriceStr.substring(1)));
            product.setOnSale(true);
        }

        //scrape sizes
        ArrayList<String> sizes = new ArrayList<>();
        Element sizeHTML = doc.getElementsByClass("swatches swatches-size js-swatches__size cf mb3 mb0-ns").first();
        Elements sizerepositories = sizeHTML.getElementsByClass("sizeanchor js-swatches__size-anchor");
        for (Element s: sizerepositories){
            String size = s.text();
            sizes.add(size);
        }
        product.setSizes(sizes);

        //scrape colors
        ArrayList<String> colors = new ArrayList<>();
        Elements colorrepositories = doc.getElementsByClass("swatchanchor");
        for (Element c: colorrepositories){
            String color = c.text();
            colors.add(color);
        }
        product.setColors(colors);

        //scrape images
        Elements imgs = doc.getElementsByClass("ar-product-images__image-media js-product-images__image-media lazyr lazy");
        for (Element el : imgs) {
            String src = el.absUrl("src");
            Image image = getImage(src);
            product.getImages().add(image);
        }

        product = productRepository.save(product);
        driver.close();
        return product;
    }


    private Image getImage(String src) {
        try {
            URL url = new URL(src);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read()) != -1) {
                byteArrayOutputStream.write(read);
            }

            byte[] imageData = byteArrayOutputStream.toByteArray();
            Image img = new Image(imageData);
            Image savedImage = imageRepository.save(img);
            inputStream.close();
            byteArrayOutputStream.close();
            return savedImage;
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
            con.disconnect();
            return (code >= 200 && code <= 299) || code == 301 || code == 302 || code == 303;
        } catch (IOException e) {
            return false;
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
            throw new InvalidDataException("This URL is not valid. Please check the URL and re-enter.");
        }
    }



}
