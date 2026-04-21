package com.cartracker.dto;

/**
 * RegisterUserRequest – Data Transfer Object for user registration input.
 *
 * Keeps raw user input separate from the domain User model.
 * Validate fields here (or in a validator class) before creating the entity.
 *
 * Team member: used by UserController when collecting registration data.
 */
public class RegisterUserRequest {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String fullName;
    private String phone;
    private String role;      // "ADMIN" | "CUSTOMER" | "MECHANIC"

    // ── Constructors ──────────────────────────────────────────────────────────

    public RegisterUserRequest() {}

    public RegisterUserRequest(String username, String password, String confirmPassword,
                               String email, String fullName, String phone, String role) {
        this.username        = username;
        this.password        = password;
        this.confirmPassword = confirmPassword;
        this.email           = email;
        this.fullName        = fullName;
        this.phone           = phone;
        this.role            = role;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getUsername()                         { return username; }
    public void   setUsername(String username)          { this.username = username; }

    public String getPassword()                         { return password; }
    public void   setPassword(String password)          { this.password = password; }

    public String getConfirmPassword()                  { return confirmPassword; }
    public void   setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getEmail()                            { return email; }
    public void   setEmail(String email)                { this.email = email; }

    public String getFullName()                         { return fullName; }
    public void   setFullName(String fullName)          { this.fullName = fullName; }

    public String getPhone()                            { return phone; }
    public void   setPhone(String phone)                { this.phone = phone; }

    public String getRole()                             { return role; }
    public void   setRole(String role)                  { this.role = role; }
}
