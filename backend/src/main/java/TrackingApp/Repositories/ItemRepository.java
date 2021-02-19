package TrackingApp.Repositories;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Item;
import TrackingApp.Entities.Product;
import TrackingApp.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ItemRepository  extends JpaRepository<Item, Integer> {

    long countAllByProduct(Product product);
}
