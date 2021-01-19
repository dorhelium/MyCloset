package TrackingApp.Entities;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    String username;

    String password;

    @Column(unique = true)
    String email;

    @OneToOne(orphanRemoval=true, cascade= CascadeType.ALL)
    Wishlist wishlist;

}
