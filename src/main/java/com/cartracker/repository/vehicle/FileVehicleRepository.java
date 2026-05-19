package com.cartracker.repository.vehicle;

import com.cartracker.model.vehicle.Vehicle;
import com.cartracker.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FileVehicleRepository – flat-file implementation of VehicleRepository.
 * Reads and writes vehicles.txt in src/main/resources/data/.
 *
 * Team member: assign to the Vehicle Management module owner.
 */
public class FileVehicleRepository implements VehicleRepository {

    private static final String     FILE_NAME = "vehicles.txt";
    private final        List<Vehicle> cache  = new ArrayList<>();

    public FileVehicleRepository() {
        // TODO: loadFromFile()
    }

    private void loadFromFile() {
        // TODO: FileUtil.readAllLines(FILE_NAME) → parse each line → add to cache
    }

    private void persistToFile() {
        List<String> lines = new ArrayList<>();
        cache.forEach(v -> lines.add(v.toFileString()));
        FileUtil.writeAllLines(FILE_NAME, lines);
    }

    @Override public Vehicle           save(Vehicle v)               { cache.add(v); FileUtil.appendLine(FILE_NAME, v.toFileString()); return v; }
    @Override public Optional<Vehicle> findById(String id)          { return cache.stream().filter(v -> v.getId().equals(id)).findFirst(); }
    @Override public Optional<Vehicle> findByLicensePlate(String p) { return cache.stream().filter(v -> v.getLicensePlate().equals(p)).findFirst(); }
    @Override public List<Vehicle>     findAll()                    { return List.copyOf(cache); }
    @Override public List<Vehicle>     findByOwnerId(String ownerId){ return cache.stream().filter(v -> ownerId.equals(v.getOwnerId())).collect(java.util.stream.Collectors.toList()); }
    @Override public Vehicle           update(Vehicle v)             { deleteById(v.getId()); cache.add(v); persistToFile(); return v; }
    @Override public boolean           deleteById(String id)        { boolean r = cache.removeIf(v -> v.getId().equals(id)); if(r) persistToFile(); return r; }
}
