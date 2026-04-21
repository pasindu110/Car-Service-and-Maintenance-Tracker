package com.cartracker.controller.vehicle;

import com.cartracker.service.vehicle.VehicleService;

/**
 * VehicleController – handles user-facing interactions for Vehicle Management.
 *
 * Team member: assign to the Vehicle Management module owner.
 */
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public void showMenu()         { /* TODO: Display vehicle menu */ }
    public void addVehicle()       { /* TODO: Collect input → vehicleService.addVehicle() */ }
    public void viewVehicle()      { /* TODO: Input ID → vehicleService.findById() → display */ }
    public void listMyVehicles()   { /* TODO: Get logged-in user ID → findByOwner() */ }
    public void updateVehicle()    { /* TODO */ }
    public void removeVehicle()    { /* TODO */ }
}
