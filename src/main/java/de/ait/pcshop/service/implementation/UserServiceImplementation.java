package de.ait.pcshop.service.implementation;


import de.ait.pcshop.model.User;
import de.ait.pcshop.repository.RoleRepository;
import de.ait.pcshop.repository.UserRepository;
import de.ait.pcshop.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Override
    public User addUser(User user) {
        user.setId(null);

        if (user.getTitle() == null || user.getTitle().isEmpty() || user.getFirstName() == null || user.getFirstName().isEmpty() || user.getLastName() == null || user.getLastName().isEmpty() || user.getEmail() == null || user.getEmail().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty() || user.getCountry() == null || user.getCountry().isEmpty() || user.getCity() == null || user.getCity().isEmpty() || user.getStreet() == null || user.getStreet().isEmpty() || user.getPostIndex() == null || user.getPostIndex().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with" + user.getEmail() + "already exists");
        }

        user.setRoles(Collections.singleton(roleRepository.findByTitle("ROLE_USER")));
        user.setRegistrationDate(LocalDateTime.now());
        validatePassword(user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        User savedUser = userRepository.save(user);
        logger.info("User with" + user.getEmail() + "successfully registered");
        return savedUser;
    }

    public void validatePassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }
        if (!newPassword.matches(".*\\d.*")) {
            throw new IllegalArgumentException("New password must contain at least one digit");
        }
        if (!newPassword.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("New password must contain at least one letter");
        }
        if (!newPassword.matches(".*[.,?!@#$%^&+=].*")) {
            throw new IllegalArgumentException("New password must contain at least one special character (.,?!@#$%^&+=)");
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User deleteUser(Authentication authentication) {
        User currentUser = findByEmail(authentication.getName());

        if (currentUser != null) {
            userRepository.delete(currentUser);
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
        return currentUser;
    }

    @Override
    public User disableUser(Authentication authentication) {
        User currentUser = findByEmail(authentication.getName());
        if (currentUser != null) {
            currentUser.setActive(false);
            userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public User enableUser(Authentication authentication) {
        User curentUser = findByEmail(authentication.getName());
        if (curentUser != null) {
            curentUser.setActive(true);
            userRepository.save(curentUser);
        }
        return curentUser;
    }

    @Override
    public User updateUser(User updatedUser) {
        User currentUser = userRepository.findById(updatedUser.getId()).orElse(null);
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        Long id = currentUser.getId();

        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setTitle(currentUser.getTitle());
            existingUser.setFirstName(currentUser.getFirstName());
            existingUser.setLastName(currentUser.getLastName());

            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User WITH id " + id + " not found");
        }
    }

    @Override
    public User findByEmail(String username) {
        if (userRepository.findByEmail(username) != null) {
            return userRepository.findByEmail(username);
        }
        return null;
    }
}
