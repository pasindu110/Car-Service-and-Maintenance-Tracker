package com.cartracker.repository.vehicle;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.vehicle.Vehicle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcVehicleRepository implements VehicleRepository {

    private final Connection conn;

    public JdbcVehicleRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (id, owner_id, license_plate, make, model, year, color, mileage, fuel_type, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  vehicle.getId());
            ps.setString(2,  vehicle.getOwnerId());
            ps.setString(3,  vehicle.getLicensePlate());
            ps.setString(4,  vehicle.getMake());
            ps.setString(5,  vehicle.getModel());
            ps.setInt(6,     vehicle.getYear());
            ps.setString(7,  vehicle.getColor());
            ps.setDouble(8,  vehicle.getMileage());
            ps.setString(9,  vehicle.getFuelType());
            ps.setTimestamp(10, Timestamp.valueOf(vehicle.getCreatedAt()));
            ps.setTimestamp(11, Timestamp.valueOf(vehicle.getUpdatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle: " + e.getMessage(), e);
        }
        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicles WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicle: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String plate) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicles WHERE license_plate = ?")) {
            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vehicle by plate: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM vehicles")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vehicles: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET owner_id=?, license_plate=?, make=?, model=?, year=?, color=?, mileage=?, fuel_type=?, updated_at=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  vehicle.getOwnerId());
            ps.setString(2,  vehicle.getLicensePlate());
            ps.setString(3,  vehicle.getMake());
            ps.setString(4,  vehicle.getModel());
            ps.setInt(5,     vehicle.getYear());
            ps.setString(6,  vehicle.getColor());
            ps.setDouble(7,  vehicle.getMileage());
            ps.setString(8,  vehicle.getFuelType());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, vehicle.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vehicle: " + e.getMessage(), e);
        }
        return vehicle;
    }

    @Override
    public boolean deleteById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM vehicles WHERE id = ?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle: " + e.getMessage(), e);
        }
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        Vehicle v = new Vehicle(
                rs.getString("id"),
                rs.getString("owner_id"),
                rs.getString("license_plate"),
                rs.getString("make"),
                rs.getString("model"),
                rs.getInt("year"),
                rs.getString("color"),
                rs.getDouble("mileage"),
                rs.getString("fuel_type")
        );
        v.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        v.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return v;
    }
}
