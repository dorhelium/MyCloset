package TrackingApp.Repositories;

import TrackingApp.Entities.Image;
import TrackingApp.Entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ItemRepository  extends JpaRepository<Item, Integer> {


}
