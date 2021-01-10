package TrackingApp.Repositories;

import TrackingApp.Entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserAccount, Integer> {
}
