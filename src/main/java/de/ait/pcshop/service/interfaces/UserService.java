package de.ait.pcshop.service.interfaces;

import de.ait.pcshop.model.User;

public interface UserService {

    User addUser(User user);

    User getUserById(Long id);

    void deleteUserById(Long id);

    void disableUserById(Long id);

    void enableUserById(Long id);

    User updateUser(User user);
}
