package TrackingApp.Controllers;

import TrackingApp.Entities.Dto.UserItemDto;
import TrackingApp.Entities.Item;
import TrackingApp.Entities.UserAccount;
import TrackingApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public UserAccount registerNewUser(@RequestBody UserAccount newAccount){
        UserAccount registered = userService.registerNewUserAccount(newAccount);
        return registered;
    }

    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    @ResponseBody
    public UserAccount registerNewUser(@RequestBody UserItemDto userItemDto){
        return userService.addNewItemToWishlist(userItemDto);
    }


}
