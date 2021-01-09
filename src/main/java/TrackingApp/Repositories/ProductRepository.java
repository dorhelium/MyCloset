package TrackingApp.Repositories;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByUrl(String url);
}
