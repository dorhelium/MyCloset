package TrackingApp.Controllers;

import TrackingApp.Entities.Dto.UserDto;
import TrackingApp.Entities.User;
import TrackingApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/register_user", method = RequestMethod.POST)
    public UserDto registerNewUser(@RequestBody User user){
        return userService.registerNewUser(user);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public User authenticarteUser(@RequestBody User user){
        return userService.authenticateUser(user);
    }






}
