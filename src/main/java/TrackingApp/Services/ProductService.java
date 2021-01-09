package TrackingApp.Services;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import TrackingApp.Repositories.ImageRepository;
import TrackingApp.Repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get(url);

        WebDriverWait wait = new WebDriverWait(driver, 20);
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

        if (!priceWithoutDiscountStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(priceWithoutDiscountStr.split(" ")[0]));
        }

        if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()) {
            product.setOriginalPrice(Float.parseFloat(originalPriceStr.split(" ")[0]));
            product.setSalePrice(Float.parseFloat(salePriceStr.split(" ")[0]));
            int discount = (int) ((product.getOriginalPrice() - product.getSalePrice()) / product.getOriginalPrice() * 100);
        }

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




}
