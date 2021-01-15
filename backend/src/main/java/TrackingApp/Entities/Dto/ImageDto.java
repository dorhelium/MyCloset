package TrackingApp.Entities.Dto;

import TrackingApp.Entities.Product;

public class ImageDto {

    private final int id;
    private final String imageData;
    private final Product product;


    public ImageDto(int id, String imageData, Product product) {
        this.id = id;
        this.imageData = imageData;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public String getImageData() {
        return imageData;
    }

    public Product getProduct() {
        return product;
    }
}
