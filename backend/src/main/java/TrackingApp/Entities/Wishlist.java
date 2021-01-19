package TrackingApp.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(orphanRemoval=true, fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinColumn(name="wishlist_id")
    List<Item> items;

}
