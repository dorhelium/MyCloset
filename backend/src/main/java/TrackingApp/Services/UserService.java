package TrackingApp.Services;



import TrackingApp.Entities.Dto.UserDto;
import TrackingApp.Entities.User;
import TrackingApp.Entities.Wishlist;
import TrackingApp.Exceptions.InvalidDataException;
import TrackingApp.Exceptions.UnauthorizedActionException;
import TrackingApp.Repositories.UserRepository;
import TrackingApp.Repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utils.MailUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WishlistRepository wishlistRepository;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        User user =  userRepository.findById(username).orElse(null);
        if (user==null){
            throw new UsernameNotFoundException("Username not found.");
        }
        return new MyUserDetails(user);
    }

    public UserDto registerNewUser(User user) {
        User storedUser = userRepository.findById(user.getUsername()).orElse(null);
        if (storedUser != null){
            throw new InvalidDataException("Username "+ user.getUsername()+ " is already used. Choose another one.");
        }

        List<User> userByEmail = userRepository.findByEmail(user.getUsername());
        if (!userByEmail.isEmpty()){
            throw new InvalidDataException("Email "+ user.getEmail() + " is already registered.");
        }

        Wishlist wishlist = new Wishlist();
        wishlistRepository.save(wishlist);

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setWishlist(wishlist);
        newUser = userRepository.save(newUser);

        MailUtil.sendMail(newUser.getEmail(), "Welcom To MyCloset",
                "Hi there,\nWelcome to MyCloset. You want to be more intentional with shopping? We are here to help you.");


        return new UserDto(newUser);
    }

    public User authenticateUser(User user) {
        User storedUserByUsername = userRepository.findById(user.getUsername()).orElse(null);

        List<User> storedUserByEmail = userRepository.findByEmail(user.getEmail());
        if (storedUserByUsername== null && storedUserByEmail.isEmpty()){
            throw new UnauthorizedActionException("Incorrect username or password.");
        }

        User existingUser = storedUserByUsername==null? storedUserByEmail.get(0):storedUserByUsername;

        if (existingUser.getPassword().equals(user.getPassword())){
            return existingUser;
        }else{
            throw new UnauthorizedActionException("Incorrect username or password.");
        }
    }

    /*
    public void cancelUser(String username) {
        User storedUser = userRepository.findById(username).orElse(null);
        if (storedUser==null){
            throw new InvalidDataException("User "+ username+ " does not exist");
        }
        userRepository.delete(storedUser);
    }

     */

    public class MyUserDetails implements UserDetails {

        private String username;
        private String password;
        private List<GrantedAuthority> authorities;

        public MyUserDetails(User user) {
            this.username = user.getUsername();
            this.password = user.getPassword();
            this.authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
