package com.cartracker.service.user;

import com.cartracker.model.user.User;
import com.cartracker.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserServiceImpl – placeholder implementation of UserService.
 *
 * Inject UserRepository here and implement each method.
 *
 * Team member: assign to the User Management module owner.
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        // Validate
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        
        // Ensure email uniqueness (using email as username)
        if (userRepository.findByUsername(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        user.setUsername(user.getEmail()); // Using email as username

        // Generate ID if not present
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(java.util.UUID.randomUUID().toString());
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public boolean deactivate(String userId) {
        return userRepository.deleteById(userId);
    }

    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Note: In a real app, use BCrypt to hash/verify passwords.
            // For this student project, we compare plain text.
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
