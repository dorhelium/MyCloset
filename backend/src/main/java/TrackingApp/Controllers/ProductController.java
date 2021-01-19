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

    private class SearchQuery{
        String searchUrl;
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    String[] zaraURL = {
            "https://www.zara.com/ca/en/long-coat-p01255708.html?v1=70967258&v2=1691225",
            //"https://www.zara.com/ca/en/water-resistant-quilted-vest-p07522057.html?v1=81553190&v2=1718076",
            //"https://www.zara.com/ca/en/combination-pocket-puffer-jacket-p05320305.html?v1=78866101&v2=1690038"
    };


    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @ResponseBody
    public Product addProduct(@RequestBody String url){
        return productService.scrapeAndAddProduct(url);
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProduct(@PathVariable int id){
        return productService.getProductById(id);
    }

    /*
    @RequestMapping(value = "/product/{product_id}/images", method = RequestMethod.GET)
    @ResponseBody
    public List<ImageDto> getProductImages(@PathVariable(name="product_id") int id){
        return productService.getImageByProductId(id);
    }

     */


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
