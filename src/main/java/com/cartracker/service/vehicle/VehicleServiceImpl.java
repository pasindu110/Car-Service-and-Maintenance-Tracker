package com.cartracker.service.vehicle;

import com.cartracker.model.vehicle.Vehicle;
import com.cartracker.repository.vehicle.VehicleRepository;
import com.cartracker.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * VehicleServiceImpl – implementation of VehicleService.
 */
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {
        // Generate a UUID for the new vehicle
        vehicle.setId(IdGenerator.generateUUID());
        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        vehicle.setCreatedAt(now);
        vehicle.setUpdatedAt(now);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String plate) {
        return vehicleRepository.findByLicensePlate(plate);
    }

    @Override
    public List<Vehicle> findByOwner(String ownerId) {
        return vehicleRepository.findAll().stream()
                .filter(v -> ownerId.equals(v.getOwnerId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.update(vehicle);
    }

    @Override
    public boolean remove(String vehicleId) {
        return vehicleRepository.deleteById(vehicleId);
    }
}
