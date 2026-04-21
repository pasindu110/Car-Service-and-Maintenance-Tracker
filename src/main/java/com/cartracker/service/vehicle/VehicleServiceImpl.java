package com.cartracker.service.vehicle;

import com.cartracker.model.vehicle.Vehicle;
import com.cartracker.repository.vehicle.VehicleRepository;

import java.util.List;
import java.util.Optional;

/**
 * VehicleServiceImpl – placeholder implementation of VehicleService.
 *
 * Team member: assign to the Vehicle Management module owner.
 */
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        // TODO: Validate vehicle fields (licensePlate not blank, year > 1900, etc.)
        // TODO: Check license plate uniqueness
        // TODO: Generate ID via IdGenerator.generateSequential("VEH")
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        // TODO: Delegate to vehicleRepository.findById(id)
        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String plate) {
        // TODO: Delegate to vehicleRepository.findByLicensePlate(plate)
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findByOwner(String ownerId) {
        // TODO: Filter all vehicles by ownerId
        return List.of();
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        // TODO: Validate and update
        return vehicleRepository.update(vehicle);
    }

    @Override
    public boolean remove(String vehicleId) {
        // TODO: Check no active appointments before removing
        return vehicleRepository.deleteById(vehicleId);
    }
}
