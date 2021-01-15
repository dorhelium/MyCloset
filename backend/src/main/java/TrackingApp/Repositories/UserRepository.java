package TrackingApp.Repositories;

import TrackingApp.Entities.Product;
import TrackingApp.Entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserAccount, String> {
    List<UserAccount> findByEmail(String email);
}
