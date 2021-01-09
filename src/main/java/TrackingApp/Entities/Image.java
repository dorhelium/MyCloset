package TrackingApp.Entities;


import javax.persistence.*;

@Entity
//@Table(name = "org_user_avatar")
public class Image {

    private static final long serialVersionUID = -1449272493960945258L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte[] imageData;

    @ManyToOne
    private Product product;

    public Image(byte[] imageData, Product product) {
        this.imageData = imageData;
        this.product = product;
    }
    public Image() { }
}
