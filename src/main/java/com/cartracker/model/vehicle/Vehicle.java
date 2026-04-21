package com.cartracker.model.vehicle;

import com.cartracker.model.common.BaseEntity;

/**
 * Vehicle – represents a customer's car registered in the system.
 *
 * Demonstrates ENCAPSULATION: all fields are private.
 * A customer can own multiple vehicles (one-to-many relationship).
 *
 * Fields:
 *   - ownerId      : links back to the Customer who owns this vehicle
 *   - licensePlate : unique identifier on the road (unique key)
 *   - make         : brand name   (e.g. "Toyota")
 *   - model        : model name   (e.g. "Corolla")
 *   - year         : manufacture year
 *   - color        : vehicle colour
 *   - mileage      : current odometer reading in km
 *   - fuelType     : "Petrol" | "Diesel" | "Electric" | "Hybrid"
 *
 * Team member: assign to the team member responsible for Vehicle Management module.
 */
public class Vehicle extends BaseEntity {

    private String ownerId;
    private String licensePlate;
    private String make;
    private String model;
    private int    year;
    private String color;
    private double mileage;
    private String fuelType;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Vehicle() {
        super();
    }

    public Vehicle(String id, String ownerId, String licensePlate,
                   String make, String model, int year,
                   String color, double mileage, String fuelType) {
        super(id);
        this.ownerId       = ownerId;
        this.licensePlate  = licensePlate;
        this.make          = make;
        this.model         = model;
        this.year          = year;
        this.color         = color;
        this.mileage       = mileage;
        this.fuelType      = fuelType;
    }

    // ── toFileString ──────────────────────────────────────────────────────────

    @Override
    public String toFileString() {
        // Format: id|ownerId|licensePlate|make|model|year|color|mileage|fuelType|createdAt
        return String.join("|", id, ownerId, licensePlate, make, model,
                String.valueOf(year), color, String.valueOf(mileage),
                fuelType, createdAt.toString());
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getOwnerId()                    { return ownerId; }
    public void   setOwnerId(String ownerId)      { this.ownerId = ownerId; }

    public String getLicensePlate()                    { return licensePlate; }
    public void   setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getMake()               { return make; }
    public void   setMake(String make)    { this.make = make; }

    public String getModel()              { return model; }
    public void   setModel(String model)  { this.model = model; }

    public int    getYear()               { return year; }
    public void   setYear(int year)       { this.year = year; }

    public String getColor()              { return color; }
    public void   setColor(String color)  { this.color = color; }

    public double getMileage()              { return mileage; }
    public void   setMileage(double mileage) { this.mileage = mileage; }

    public String getFuelType()               { return fuelType; }
    public void   setFuelType(String fuelType) { this.fuelType = fuelType; }

    @Override
    public String toString() {
        return "Vehicle{id='" + id + "', plate='" + licensePlate +
               "', " + year + " " + make + " " + model + '}';
    }
}
