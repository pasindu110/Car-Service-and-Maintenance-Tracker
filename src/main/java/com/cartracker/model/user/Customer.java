package com.cartracker.model.user;

import com.cartracker.model.common.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Customer – a registered vehicle owner who books appointments and
 * receives service invoices.
 *
 * Responsibilities:
 *   - Own one or more vehicles
 *   - Book, view, and cancel appointments
 *   - View own invoices and payment history
 *
 * Team member: assign to the team member responsible for User Management module.
 */
public class Customer extends User {

    private String       address;
    private List<String> vehicleIds; // IDs of the customer's registered vehicles

    // ── Constructors ──────────────────────────────────────────────────────────

    public Customer() {
        super();
        setRole(UserRole.CUSTOMER);
        this.vehicleIds = new ArrayList<>();
    }

    public Customer(String id, String username, String email,
                    String fullName, String phone, String address) {
        super(id, username, email, fullName, phone, UserRole.CUSTOMER);
        this.address    = address;
        this.vehicleIds = new ArrayList<>();
    }

    // ── Polymorphic method ────────────────────────────────────────────────────

    @Override
    public String getPermissionsDescription() {
        return "Customer: Can view own vehicles, book appointments, and view invoices.";
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        return super.toFileString() + "|" + address + "|" +
               String.join(",", vehicleIds);
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String       getAddress()                { return address; }
    public void         setAddress(String address)   { this.address = address; }

    public List<String> getVehicleIds()             { return vehicleIds; }
    public void         setVehicleIds(List<String> vehicleIds) { this.vehicleIds = vehicleIds; }

    public void         addVehicleId(String vehicleId) {
        if (!vehicleIds.contains(vehicleId)) vehicleIds.add(vehicleId);
    }

    @Override
    public String toString() {
        return "Customer{id='" + getId() + "', username='" + getUsername() +
               "', vehicles=" + vehicleIds.size() + '}';
    }
}
