package com.cartracker.service.user;

import com.cartracker.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * UserService – service interface for the User Management module.
 *
 * Defines the business operations available. The implementation class
 * (UserServiceImpl) contains the actual logic.
 *
 * Team member: assign to the User Management module owner.
 */
public interface UserService {

    /** Register a new user (Admin, Customer, or Mechanic). */
    User register(User user);

    /** Find a user by their unique ID. Returns empty if not found. */
    Optional<User> findById(String id);

    /** Find a user by username (for login). */
    Optional<User> findByUsername(String username);

    /** Return all active users. */
    List<User> findAll();

    /** Update an existing user's details. */
    User update(User user);

    /** Soft-delete: mark user as inactive instead of removing permanently. */
    boolean deactivate(String userId);

    /** Authenticate username + password. Returns user if credentials match. */
    Optional<User> login(String username, String password);
}
