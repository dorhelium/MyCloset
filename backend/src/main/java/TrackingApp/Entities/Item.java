package TrackingApp.Entities;

import javax.persistence.*;

@Table(uniqueConstraints= {
        @UniqueConstraint(columnNames={"product_id", "color", "size"})}
)
@Entity
public class Item {

    @Id
    int id;

    @ManyToOne
    @JoinColumn(name="product_id")
    Product product;
    String color;
    String size;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
