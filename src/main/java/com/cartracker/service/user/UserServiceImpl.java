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
        // TODO: Validate user fields (username not empty, email format, etc.)
        // TODO: Hash password before saving
        // TODO: Ensure username uniqueness via userRepository.findByUsername()
        // TODO: Generate ID via IdGenerator.generateSequential("USR")
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(String id) {
        // TODO: Delegate to userRepository.findById(id)
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // TODO: Delegate to userRepository.findByUsername(username)
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        // TODO: Return userRepository.findAll()
        return List.of();
    }

    @Override
    public User update(User user) {
        // TODO: Validate user, set updatedAt, delegate to userRepository.update()
        return userRepository.update(user);
    }

    @Override
    public boolean deactivate(String userId) {
        // TODO: Find user, set active = false, save, return success flag
        return false;
    }

    @Override
    public Optional<User> login(String username, String password) {
        // TODO: Find user by username, compare hashed password, return if match
        return Optional.empty();
    }
}
