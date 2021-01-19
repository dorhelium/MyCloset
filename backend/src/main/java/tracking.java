
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class tracking {



    public static void main (String[] args) throws InterruptedException {

        WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.get("https://www.zara.com/ca/en/fleece-coat-p04360041.html?v1=78482745&v2=1718076");

        String currentColor = driver.findElements(By.className("_colorName")).get(0).getText();

        if (!currentColor.equals("PEARL GRAY".toUpperCase())){
            Actions actions = new Actions(driver);
            List<WebElement> colorElements = driver.findElements(By.className("_color-image"));
            for (WebElement element: colorElements){
                if (element.getAttribute("alt").equals("White")){
                    actions.moveToElement(element).click().perform();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("price")));// instead of id u can use cssSelector or xpath of ur element.

        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);


        Elements sizerepositories = doc.getElementsByClass("product-size");
        for (Element s: sizerepositories){
            String size = s.getElementsByClass("size-name").first().text();
            String classes = s.attr("class");
            if (Arrays.stream(classes.split(" ")).
                    filter(str->str.equals("_disabled")).
                    collect(Collectors.toList()).isEmpty()){
                        if ("S".equals(size)){
                            System.out.print(true);
                        }
            }
        }

    }
}
