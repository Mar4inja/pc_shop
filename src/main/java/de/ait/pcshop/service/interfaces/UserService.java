package de.ait.pcshop.service.interfaces;

import de.ait.pcshop.model.User;
import org.springframework.security.core.Authentication;

public interface UserService {

    User addUser(User user);

    User getUserById(Long id);

    User deleteUser(Authentication authentication);

    User disableUser(Authentication authentication);

    User enableUser(Authentication authentication);

    User updateUser(User user);

    User findByEmail(String username);
}
