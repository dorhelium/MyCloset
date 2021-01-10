
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import TrackingApp.Services.MailUtil;
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



    public static void main (String[] args) {

        MailUtil.sendMail("doreen.he9884@gmail.com", "Sending From ShopMyCloset",
                "Hi there, \nWelcome to ShopMyCloset.");


    }
}
