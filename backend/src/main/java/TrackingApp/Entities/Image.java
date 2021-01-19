package TrackingApp.Entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Image implements Serializable {

    private static final long serialVersionUID = -1449272493960945258L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte[] imageData;

    /*
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

     */

    public Image(byte[] imageData) {
        this.imageData = imageData;
    }
    public Image() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

}
