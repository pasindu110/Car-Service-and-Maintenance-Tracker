package com.cartracker.repository.user;

import com.cartracker.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository – data access interface for User entities.
 *
 * Implement this with FileUserRepository (flat-file) now.
 * Replace later with a JdbcUserRepository when adding a database.
 *
 * Team member: assign to the User Management module owner.
 */
public interface UserRepository {

    User             save(User user);
    Optional<User>   findById(String id);
    Optional<User>   findByUsername(String username);
    List<User>       findAll();
    User             update(User user);
    boolean          deleteById(String id);
}
