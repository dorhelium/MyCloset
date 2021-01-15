package TrackingApp.Services;

import TrackingApp.Entities.Dto.ImageDto;
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Item;
import TrackingApp.Entities.Product;
import TrackingApp.Exceptions.DataViolationException;
import TrackingApp.Repositories.ImageRepository;
import TrackingApp.Repositories.ItemRepository;
import TrackingApp.Repositories.ProductRepository;
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
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public void trackZaraItem (Item zaraItem){

    }





}
