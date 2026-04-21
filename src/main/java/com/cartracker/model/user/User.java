package com.cartracker.model.user;

import com.cartracker.model.common.BaseEntity;
import com.cartracker.model.common.UserRole;

/**
 * User – abstract base class for all user types.
 *
 * Demonstrates INHERITANCE: Admin, Customer, and Mechanic extend this.
 * ENCAPSULATION: all fields are private; access via getters/setters.
 *
 * Fields:
 *   - username  : login username (unique)
 *   - password  : hashed password (do not store plain text in production)
 *   - email     : contact email
 *   - fullName  : display name
 *   - phone     : contact phone number
 *   - role      : UserRole enum (ADMIN / CUSTOMER / MECHANIC)
 *   - active    : soft-delete flag
 */
public abstract class User extends BaseEntity {

    private String   username;
    private String   password;   // TODO: hash before storing
    private String   email;
    private String   fullName;
    private String   phone;
    private UserRole role;
    private boolean  active;

    // ── Constructors ──────────────────────────────────────────────────────────

    protected User() {
        super();
        this.active = true;
    }

    protected User(String id, String username, String email,
                   String fullName, String phone, UserRole role) {
        super(id);
        this.username = username;
        this.email    = email;
        this.fullName = fullName;
        this.phone    = phone;
        this.role     = role;
        this.active   = true;
    }

    // ── Abstract method (POLYMORPHISM) ────────────────────────────────────────

    /**
     * Each user type describes its permissions differently.
     */
    public abstract String getPermissionsDescription();

    // ── toFileString (from BaseEntity) ────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|username|email|fullName|phone|role|active|createdAt
        return String.join("|", id, username, email, fullName, phone,
                role.name(), String.valueOf(active), createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String   getUsername()                { return username; }
    public void     setUsername(String username) { this.username = username; }

    public String   getPassword()                { return password; }
    public void     setPassword(String password) { this.password = password; }

    public String   getEmail()                   { return email; }
    public void     setEmail(String email)        { this.email = email; }

    public String   getFullName()                { return fullName; }
    public void     setFullName(String fullName)  { this.fullName = fullName; }

    public String   getPhone()                   { return phone; }
    public void     setPhone(String phone)        { this.phone = phone; }

    public UserRole getRole()                    { return role; }
    public void     setRole(UserRole role)        { this.role = role; }

    public boolean  isActive()                   { return active; }
    public void     setActive(boolean active)     { this.active = active; }

    @Override
    public String toString() {
        return "User{id='" + id + "', username='" + username +
               "', role=" + role + ", active=" + active + '}';
    }
}
