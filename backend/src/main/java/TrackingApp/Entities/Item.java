package TrackingApp.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name="product_id")
    Product product;

    String color;
    String size;
    Float addedPrice;
    boolean availableWhenAdded;

    boolean tracking = false;

}
