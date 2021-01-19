package TrackingApp.Entities;

import lombok.Getter;
import lombok.Setter;
import utils.StringListConverterUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String url;
    String productName;
    String brand;
    float originalPrice;
    float salePrice;
    boolean isOnSale;

    @Convert(converter = StringListConverterUtil.class)
    List<String> sizes = new ArrayList<>();

    @Convert(converter = StringListConverterUtil.class)
    List<String> colors= new ArrayList<>();

    @OneToMany (orphanRemoval=true, cascade=CascadeType.ALL)
    @JoinColumn(name="image_id")
    List<Image> images= new ArrayList<>();

    public Product(String url) {
        this.url = url;
    }

    public Product() {

    }


}
