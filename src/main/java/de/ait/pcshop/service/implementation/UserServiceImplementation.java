package de.ait.pcshop.service.implementation;


import de.ait.pcshop.model.User;
import de.ait.pcshop.repository.RoleRepository;
import de.ait.pcshop.repository.UserRepository;
import de.ait.pcshop.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User addUser(User user) {
        user.setId(null);

        if (user.getTitle() == null || user.getTitle().isEmpty()
                || user.getFirstName() == null || user.getFirstName().isEmpty()
                || user.getLastName() == null || user.getLastName().isEmpty()
                || user.getEmail() == null || user.getEmail().isEmpty()
                || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("User with" + user.getEmail() + "already exists");
        }

        user.setRoles(Collections.singleton(roleRepository.findByTitle("ROLE_USER")));
        user.setRegistrationDate(LocalDateTime.now());
        validatePassword(user.getPassword());
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
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public void disableUserById(Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

            user.setActive(false);
            userRepository.save(user);

            logger.info("User with ID " + id + " successfully disabled");
        } else {
            logger.error("User with ID " + id + " not found");
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void enableUserById(Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
            user.setActive(true);

            userRepository.save(user);
            logger.info("User with ID " + id + " successfully enabled");
        } else {
            logger.error("User with ID " + id + " not found");
            throw new RuntimeException("User not found");
        }
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
}
