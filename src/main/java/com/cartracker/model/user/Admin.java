package com.cartracker.model.user;

import com.cartracker.model.common.UserRole;

/**
 * Admin – a User with system-wide administrative privileges.
 *
 * Responsibilities:
 *   - Manage all users (create, update, deactivate)
 *   - View all reports and invoices
 *   - Configure system settings
 *
 * Team member: assign to the team member responsible for User Management module.
 */
public class Admin extends User {

    private String adminLevel; // e.g. "SUPER_ADMIN", "MODERATOR"

    // ── Constructors ──────────────────────────────────────────────────────────

    public Admin() {
        super();
        setRole(UserRole.ADMIN);
    }

    public Admin(String id, String username, String email,
                 String fullName, String phone, String adminLevel) {
        super(id, username, email, fullName, phone, UserRole.ADMIN);
        this.adminLevel = adminLevel;
    }

    // ── Polymorphic method ────────────────────────────────────────────────────

    @Override
    public String getPermissionsDescription() {
        return "Admin [" + adminLevel + "]: Full system access – manage users, " +
               "view all records, generate reports.";
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return super.toFileString() + "|" + adminLevel;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getAdminLevel()                 { return adminLevel; }
    public void   setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }

    @Override
    public String toString() {
        return "Admin{id='" + getId() + "', username='" + getUsername() +
               "', adminLevel='" + adminLevel + "'}";
    }
}
