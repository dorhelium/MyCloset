package TrackingApp.Services;

import TrackingApp.Entities.*;
import TrackingApp.Exceptions.InvalidDataException;
import TrackingApp.Repositories.ItemRepository;
import TrackingApp.Repositories.UserRepository;
import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import utils.MailUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private final static Logger LOGGER = Logger.getLogger(MailUtil.class.getName());


    public void trackZaraItem (Item zaraItem){

    }



    class TrackResult{
        boolean isAvailable;
        boolean priceDrop;
    }

    public Wishlist addItemToWishlist(Item item, String username) {
        boolean available = getItemAvailability(item);
        item.setAvailableWhenAdded(available);
        Item savedItem = itemRepository.save(item);
        User user = userRepository.findById(username).orElse(null);
        user.getWishlist().getItems().add(savedItem);
        User savedUser = userRepository.save(user);

        return savedUser.getWishlist();
    }

    public Wishlist deleteItemFromWishlist(Item item, String name) {
        User user = userRepository.findById(name).orElse(null);
        Item storedItem = itemRepository.findById(item.getId()).orElse(null);
        if (!user.getWishlist().getItems().contains(storedItem)) {
            throw new InvalidDataException("User "+name+" does not have this item in the wish list.");
        }
        user.getWishlist().getItems().remove(storedItem);
        User savedUser = userRepository.save(user);
        return savedUser.getWishlist();
    }

    //every day at 6pm
    @Scheduled (cron = "0 0 18 * * *")
    public void checkItemAvailability() {
        System.out.println("Start scheduled job");
        List<User> allUsers = userRepository.findAll();
        for (User user: allUsers){
            List<Item> items = user.getWishlist().getItems();
            for (Item item: items){
                if (item.isTracking()){
                    //track procedrop and availability
                    System.out.println("Start tracking item"+ item.getProduct().getProductName()+ " for user "+user.getUsername());
                    TrackResult result = track(item);
                    System.out.println("Finish tracking item"+ item.getProduct().getProductName()+ " for user "+user.getUsername()+". " +
                            "Track result: isAvailable:"+result.isAvailable+"; priceDrop:"+result.priceDrop);
                    if (result.isAvailable && !item.isAvailableWhenAdded()){
                        MailUtil.sendMail(user.getEmail(), "Wish List Item Back In Stock!",
                                "Hi "+user.getUsername()+",\nGood news! Your wish list item: "
                                        +item.getProduct().getProductName() + " " + item.getColor()+ " "+ item.getSize()+
                                        " is back in stock! Go check it out!");
                    }
                    if (result.priceDrop){
                        MailUtil.sendMail(user.getEmail(), "Price Drop On Wish List Item!",
                                "Hi "+user.getUsername()+",\nGood news! Your wish list item: "
                                        +item.getProduct().getProductName() + " " + item.getColor()+ " "+ item.getSize()+
                                        " has a price drop! Go check it out! ");

                    }
                }
            }

        }

    }

    private TrackResult track(Item item){
        TrackResult result = new TrackResult();
        result.isAvailable = false;
        result.priceDrop = false;
        if (item.getProduct().getBrand().equals("ZARA")){
            WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
            driver.get(item.getProduct().getUrl());

            String currentColor = driver.findElements(By.className("_colorName")).get(0).getText();

            if (!currentColor.equals(item.getColor().toUpperCase())){
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
                    if (item.getSize().equals(size)){
                        result.isAvailable = true;
                    }
                }
            }

            Elements repositories = doc.getElementsByClass("price");
            Element repository = repositories.first();

            String priceWithoutDiscountStr = repository.getElementsByClass("main-price").text();
            String originalPriceStr = repository.getElementsByClass("line-through").text();
            String salePriceStr = repository.getElementsByClass("sale").text();

            if (!priceWithoutDiscountStr.isEmpty()) {
                result.priceDrop = item.getAddedPrice()>Float.parseFloat(priceWithoutDiscountStr.split(" ")[0]);
            }

            if (!originalPriceStr.isEmpty() && !salePriceStr.isEmpty()) {
                result.priceDrop = item.getAddedPrice()>Float.parseFloat(salePriceStr.split(" ")[0]);
            }
        }
        return result;
    }

    private boolean getItemAvailability(Item item){
        if (item.getProduct().getBrand().equals("ZARA")){
            WebDriver driver = new PhantomJSDriver(new DesiredCapabilities());
            driver.get(item.getProduct().getUrl());

            String currentColor = driver.findElements(By.className("_colorName")).get(0).getText();

            if (!currentColor.equals(item.getColor().toUpperCase())){
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
                    if (item.getSize().equals(size)){
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public Wishlist getWishlist(String name) {
        User user = userRepository.findById(name).orElse(null);
        return user.getWishlist();
    }

    public Item addTracking(Item item, String username) {
        Item itemInDb = itemRepository.findById(item.getId()).orElse(null);
        if (itemInDb==null){
            throw new InvalidDataException("Item does not exist.");
        }
        User user = userRepository.findById(username).orElse(null);
        if (!user.getWishlist().getItems().contains(itemInDb)){
            throw new InvalidDataException("User "+username+" does not have this item in the wishlist.");
        }

        itemInDb.setTracking(item.isTracking());
        itemRepository.save(itemInDb);
        return itemRepository.save(itemInDb);
    }
}
