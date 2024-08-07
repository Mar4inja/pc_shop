package de.ait.pcshop.service.interfaces;

import de.ait.pcshop.model.User;
import org.springframework.security.core.Authentication;

public interface UserService {

    User addUser(User user);

    User deleteUser(Authentication authentication);

    User disableUser(Authentication authentication);

    User enableUser(Authentication authentication);

    User updateUser(Authentication authentication, User updatedUser);

    User getUserInfo(Authentication authentication);

    User findByEmail(String username);
}
