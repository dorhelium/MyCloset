package TrackingApp.Entities.Dto;

import TrackingApp.Entities.User;
import TrackingApp.Entities.Wishlist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    String username;

    String email;

    Wishlist wishlist;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.wishlist = user.getWishlist();
    }
}
