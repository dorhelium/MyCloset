package TrackingApp.Services;

import TrackingApp.Entities.Dto.UserItemDto;
import TrackingApp.Entities.Item;
import TrackingApp.Entities.UserAccount;
import TrackingApp.Exceptions.RegistrationException;
import TrackingApp.Repositories.ItemRepository;
import TrackingApp.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    public UserAccount registerNewUserAccount(UserAccount newAccount){
        if (userRepository.exists(newAccount.getUsername())){
            throw new RegistrationException("Username '"+ newAccount.getUsername() + "' has already been used. Try another.");
        }

        if (newAccount.getPassword().length()<8) {
            throw new RegistrationException("Password must have at least 8 characters. Try another.");
        }

        if (!userRepository.findByEmail(newAccount.getEmail()).isEmpty()) {
            throw new RegistrationException("This email has already registered.");
        }

        //TODO: send a email to confirm
        UserAccount registered = userRepository.save(newAccount);
        return registered;
    }

    public UserAccount addNewItemToWishlist(UserItemDto userItemDto){
        Item requestItem = userItemDto.getItem();
        Item storedItem = itemRepository.findItemByProductIdColorAndSize(requestItem.getProduct().getId(),
                requestItem.getColor(), requestItem.getSize());

        if (storedItem==null){
            storedItem = itemRepository.save(requestItem);
        }

        UserAccount userAccount = userRepository.findOne(userItemDto.getUsername());

        if (userAccount!=null) {
            if (!userAccount.getWishlistItems().contains(storedItem)){
                userAccount.getWishlistItems().add(storedItem);
            }
            userAccount = userRepository.save(userAccount);
            return userAccount;
        }else{
            throw new RuntimeException("User does not exist.");
        }

    }

}
