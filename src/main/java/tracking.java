
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

import java.util.ArrayList;

public class tracking {
    public static void main (String[] args) {

        // ZARA Price

        String[] zaraURLs = {
                "https://www.zara.com/ca/en/long-coat-p01255708.html?v1=70967258&v2=1691225",
                "https://www.zara.com/ca/en/water-resistant-quilted-vest-p07522057.html?v1=81553190&v2=1718076",
                "https://www.zara.com/ca/en/combination-pocket-puffer-jacket-p05320305.html?v1=78866101&v2=1690038"
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
            product.productName = doc.title();

            if (!priceWithoutDiscountStr.isEmpty()){
                product.originalPrice = Float.parseFloat(priceWithoutDiscountStr.split(" ")[0]);
            }

            if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()){
                product.originalPrice = Float.parseFloat(originalPriceStr.split(" ")[0]);
                product.salePrice = Float.parseFloat(salePriceStr.split(" ")[0]);
                product.discountPercentage = (int) ((product.originalPrice -  product.salePrice)/product.originalPrice * 100);
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
