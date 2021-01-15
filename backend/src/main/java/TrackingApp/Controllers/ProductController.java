package TrackingApp.Controllers;

import TrackingApp.Entities.Dto.ImageDto;
import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import TrackingApp.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return productService.scrapeAndAddProduct(url);
    }

    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProduct(@PathVariable int id){
        return productService.getProductById(id);
    }

    @RequestMapping(value = "/product/{product_id}/images", method = RequestMethod.GET)
    @ResponseBody
    public List<ImageDto> getProductImages(@PathVariable(name="product_id") int id){
        return productService.getImageByProductId(id);
    }


}
