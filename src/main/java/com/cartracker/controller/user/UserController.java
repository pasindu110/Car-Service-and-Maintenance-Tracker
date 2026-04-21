package com.cartracker.controller.user;

import com.cartracker.service.user.UserService;

/**
 * UserController – handles user-facing interactions for the User Management module.
 *
 * In a console application, this class reads user input and invokes UserService.
 * In a web application, this becomes a REST controller with HTTP endpoints.
 *
 * Responsibilities:
 *   - Register new users (Admin / Customer / Mechanic)
 *   - Login / Logout
 *   - View and update user profile
 *   - Admin: list all users, deactivate accounts
 *
 * Team member: assign to the User Management module owner.
 */
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void showMenu() {
        // TODO: Print User Management menu options
        // TODO: Read user choice and delegate to appropriate userService method
    }

    public void registerUser()  { /* TODO */ }
    public void loginUser()     { /* TODO */ }
    public void viewProfile()   { /* TODO */ }
    public void updateProfile() { /* TODO */ }
    public void listAllUsers()  { /* TODO – admin only */ }
}
