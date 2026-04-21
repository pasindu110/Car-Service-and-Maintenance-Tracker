package com.cartracker.service.vehicle;

import com.cartracker.model.vehicle.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * VehicleService – service interface for the Vehicle Management module.
 */
public interface VehicleService {

    Vehicle          addVehicle(Vehicle vehicle);
    Optional<Vehicle> findById(String id);
    Optional<Vehicle> findByLicensePlate(String plate);
    List<Vehicle>    findByOwner(String ownerId);
    List<Vehicle>    findAll();
    Vehicle          update(Vehicle vehicle);
    boolean          remove(String vehicleId);
}
