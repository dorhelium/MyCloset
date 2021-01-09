package TrackingApp.Controllers;

import TrackingApp.Entities.Product;
import TrackingApp.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    String[] zaraURL = {
            "https://www.zara.com/ca/en/long-coat-p01255708.html?v1=70967258&v2=1691225",
            //"https://www.zara.com/ca/en/water-resistant-quilted-vest-p07522057.html?v1=81553190&v2=1718076",
            //"https://www.zara.com/ca/en/combination-pocket-puffer-jacket-p05320305.html?v1=78866101&v2=1690038"
    };


    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @ResponseBody
    public Product addProduct(@RequestBody String url){
        Product product = productService.scrapeAndAddProduct(url);
        return product;
    }


    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Product addProduct(@PathVariable int id){
        return productService.getProductById(id);
    }



}
