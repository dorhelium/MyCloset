package TrackingApp.Repositories;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByProduct(Product product);

    @Query(value = "SELECT * FROM image WHERE product = :productId", nativeQuery = true)
    List<Image> findImagesByProductId(@Param("productId") int productId);


}
