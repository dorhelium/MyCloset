
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.MailUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class tracking {



    public static void main (String[] args) throws InterruptedException {

        String url = "https://www.aritzia.com/en/product/conan-pant/69829.html?dwvar_69829_color=15088";
        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get(url);

        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);

        /*
        //scrape current price
        Element repository = doc.getElementsByClass("product-price").first();
        double currentPrice = item.getProduct().getOriginalPrice();
        String priceWithoutDiscountStr = repository.getElementsByClass("price-default").text();
        String originalPriceStr = repository.getElementsByClass("price-standard").text();
        String salePriceStr = repository.getElementsByClass("price-sales").text();
        if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()) {
            currentPrice = Float.parseFloat(salePriceStr.substring(1));
        }
        */




        //scrape sizes
        Element sizeHTML = doc.getElementsByClass("swatches swatches-size js-swatches__size cf mb3 mb0-ns").first();
        Elements sizerepositories = sizeHTML.getElementsByClass("sizeanchor js-swatches__size-anchor");
        for (Element s: sizerepositories){
            String size = s.text();
            if (size.equals("12")){
                String name = s.parent().className();

                System.out.println(name);
            }
        }

    }

    private static Image getImage(String src) {
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
            //Image savedImage = imageRepository.save(img);
            inputStream.close();
            byteArrayOutputStream.close();
            return img;
        }catch (IOException e){
            System.out.println("Failed to download image.");
            return null;
        }
    }
}
