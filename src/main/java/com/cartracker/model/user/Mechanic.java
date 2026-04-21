package com.cartracker.model.user;

import com.cartracker.model.common.UserRole;

/**
 * Mechanic – a staff member who performs vehicle services and maintenance.
 *
 * Responsibilities:
 *   - View assigned appointments and service records
 *   - Update service record status
 *   - Log completed maintenance tasks
 *
 * Team member: assign to the team member responsible for Service module.
 */
public class Mechanic extends User {

    private String specialisation; // e.g. "Engine", "Electrical", "Body Work"
    private int    experienceYears;
    private boolean available;     // Is the mechanic free to take new jobs?

    // ── Constructors ──────────────────────────────────────────────────────────

    public Mechanic() {
        super();
        setRole(UserRole.MECHANIC);
        this.available = true;
    }

    public Mechanic(String id, String username, String email,
                    String fullName, String phone,
                    String specialisation, int experienceYears) {
        super(id, username, email, fullName, phone, UserRole.MECHANIC);
        this.specialisation  = specialisation;
        this.experienceYears = experienceYears;
        this.available       = true;
    }

    // ── Polymorphic method ────────────────────────────────────────────────────

    @Override
    public String getPermissionsDescription() {
        return "Mechanic [" + specialisation + "]: Can view and update assigned " +
               "service records and maintenance tasks.";
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return super.toFileString() + "|" + specialisation + "|" +
               experienceYears + "|" + available;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String  getSpecialisation()                      { return specialisation; }
    public void    setSpecialisation(String specialisation)  { this.specialisation = specialisation; }

    public int     getExperienceYears()                     { return experienceYears; }
    public void    setExperienceYears(int experienceYears)   { this.experienceYears = experienceYears; }

    public boolean isAvailable()                            { return available; }
    public void    setAvailable(boolean available)           { this.available = available; }

    @Override
    public String toString() {
        return "Mechanic{id='" + getId() + "', username='" + getUsername() +
               "', specialisation='" + specialisation + "', available=" + available + '}';
    }
}
