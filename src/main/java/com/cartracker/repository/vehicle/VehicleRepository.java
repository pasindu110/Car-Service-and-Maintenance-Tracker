package com.cartracker.repository.vehicle;

import com.cartracker.model.vehicle.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * VehicleRepository – data access interface for Vehicle entities.
 *
 * Team member: assign to the Vehicle Management module owner.
 */
public interface VehicleRepository {

    Vehicle             save(Vehicle vehicle);
    Optional<Vehicle>   findById(String id);
    Optional<Vehicle>   findByLicensePlate(String plate);
    List<Vehicle>       findAll();
    Vehicle             update(Vehicle vehicle);
    boolean             deleteById(String id);
}
