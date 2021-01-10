package TrackingApp.Repositories;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ItemRepository  extends JpaRepository<Item, Integer> {

    @Query(value = "SELECT * FROM item " +
            "WHERE product_id = :productId AND color = :color AND size = :size LIMIT 1", nativeQuery = true)
    Item findItemByProductIdColorAndSize(@Param("productId") int productId,
                                                @Param("color") String color,
                                                @Param("size") String size);

}
