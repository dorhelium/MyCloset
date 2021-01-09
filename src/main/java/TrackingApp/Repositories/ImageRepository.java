package TrackingApp.Repositories;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByProduct(Product product);

}
