package TrackingApp.Controllers;

import TrackingApp.Entities.Dto.ImageDto;
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Item;
import TrackingApp.Entities.Product;
import TrackingApp.Entities.Wishlist;
import TrackingApp.Services.ItemService;
import TrackingApp.Services.ProductService;
import TrackingApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ItemService itemService;



    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Product addSearchProduct(@RequestBody String url){
        return productService.scrapeAndAddProduct(url);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public Wishlist addItemToWishlist(@RequestBody Item item, Principal principal){
        return itemService.addItemToWishlist(item, principal.getName());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
    public Wishlist deleteItemFromWishlist(@RequestBody Item item, Principal principal){
        return itemService.deleteItemFromWishlist(item, principal.getName());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/wishlist", method = RequestMethod.GET)
    public Wishlist getWishlist(Principal principal){
        return itemService.getWishlist(principal.getName());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT})
    @RequestMapping(value = "/track", method = RequestMethod.PUT)
    public Item addTracking(@RequestBody Item item, Principal principal){
        return itemService.addTracking(item, principal.getName());
    }










}
