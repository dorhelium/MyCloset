package TrackingApp.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class UserAccount {

    @Id
    String username;
    String password;
    String email;

    @OneToMany
    List<Item> wishlistItems;

    public List<Item> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(List<Item> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
