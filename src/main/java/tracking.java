
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.*;
import java.net.URL;
import java.io.IOException;


import java.util.ArrayList;

public class tracking {

    private static void getImages(String src, String name) {

        String folderPath = "/Users/doreenhe/Desktop/imageTest";

/*
        try {
            //Open a URL Stream
            URL url = new URL(src);
            InputStream in = url.openStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(folderPath + name));

            for (int b; (b = in.read()) != -1; ) {
                out.write(b);
            }
            out.close();
            in.close();
        }catch (IOException e){
            System.out.println("failed to download image.");
        }

 */

        try {
            //String destinationFile = "File path where you want ot store the image";
            URL url = new URL(src);
            InputStream inputStream = url.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //os = new FileOutputStream(destinationFile);
            int read;
            //String barcode = null;
            while ((read = inputStream.read()) != -1) {
                //os.write(read);
                byteArrayOutputStream.write(read);
                //barcode = byteArrayOutputStream.toString();
            }


            //byte[] imageData = byteArrayOutputStream.toByteArray();
            //Image img = new Image(imageData);


            inputStream.close();
            //os.close();
            byteArrayOutputStream.close();
        }catch (IOException e){
            System.out.println("failed to download image.");
        }


    }

    public static void main (String[] args) {

        // ZARA Price

        String[] zaraURLs = {
                "https://www.zara.com/ca/en/long-coat-p01255708.html?v1=70967258&v2=1691225",
                //"https://www.zara.com/ca/en/water-resistant-quilted-vest-p07522057.html?v1=81553190&v2=1718076",
                //"https://www.zara.com/ca/en/combination-pocket-puffer-jacket-p05320305.html?v1=78866101&v2=1690038"
        };

        ArrayList<Product> products = new ArrayList<>();

        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());

        for (String url: zaraURLs){
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

            Product product = new Product(url);
            product.setProductName(doc.title());

            //Get all elements with img tag ,
            Elements imgs = doc.getElementsByClass("image-big _img-zoom _main-image");
            for (Element el : imgs) {
                String src = el.absUrl("src");
                System.out.println("src attribute is : "+src);
                getImages(src, product.getProductName());
            }

            if (!priceWithoutDiscountStr.isEmpty()){
                product.setOriginalPrice(Float.parseFloat(priceWithoutDiscountStr.split(" ")[0]));
            }

            if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()){
                product.setOriginalPrice(Float.parseFloat(originalPriceStr.split(" ")[0]));
                product.setSalePrice(Float.parseFloat(salePriceStr.split(" ")[0]));
                int discount = (int) ((product.getOriginalPrice() -  product.getSalePrice())/product.getOriginalPrice() * 100);
            }

            products.add(product);
        }

        for (Product p: products) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                String jsonStr = mapper.writeValueAsString(p);
                System.out.println(jsonStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }


    }
}
