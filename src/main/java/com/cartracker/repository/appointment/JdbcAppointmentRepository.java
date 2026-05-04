package com.cartracker.repository.appointment;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.appointment.Appointment;
import com.cartracker.model.common.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAppointmentRepository implements AppointmentRepository {

    private final Connection conn;

    public JdbcAppointmentRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Appointment save(Appointment appt) {
        String sql = "INSERT INTO appointments (id, customer_id, vehicle_id, mechanic_id, scheduled_at, service_type, notes, status, service_record_id, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  appt.getId());
            ps.setString(2,  appt.getCustomerId());
            ps.setString(3,  appt.getVehicleId());
            ps.setString(4,  appt.getMechanicId());
            ps.setTimestamp(5, Timestamp.valueOf(appt.getScheduledAt()));
            ps.setString(6,  appt.getServiceType());
            ps.setString(7,  appt.getNotes());
            ps.setString(8,  appt.getStatus().name());
            ps.setString(9,  appt.getServiceRecordId());
            ps.setTimestamp(10, Timestamp.valueOf(appt.getCreatedAt()));
            ps.setTimestamp(11, Timestamp.valueOf(appt.getUpdatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment: " + e.getMessage(), e);
        }
        return appt;
    }

    @Override
    public Optional<Appointment> findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointments WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching appointments: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Appointment update(Appointment appt) {
        String sql = "UPDATE appointments SET mechanic_id=?, scheduled_at=?, service_type=?, notes=?, status=?, service_record_id=?, updated_at=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  appt.getMechanicId());
            ps.setTimestamp(2, Timestamp.valueOf(appt.getScheduledAt()));
            ps.setString(3,  appt.getServiceType());
            ps.setString(4,  appt.getNotes());
            ps.setString(5,  appt.getStatus().name());
            ps.setString(6,  appt.getServiceRecordId());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8,  appt.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment: " + e.getMessage(), e);
        }
        return appt;
    }

    @Override
    public boolean deleteById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM appointments WHERE id = ?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        Appointment a = new Appointment(
                rs.getString("id"),
                rs.getString("customer_id"),
                rs.getString("vehicle_id"),
                rs.getTimestamp("scheduled_at").toLocalDateTime(),
                rs.getString("service_type"),
                rs.getString("notes")
        );
        a.setMechanicId(rs.getString("mechanic_id"));
        a.setStatus(Status.valueOf(rs.getString("status")));
        a.setServiceRecordId(rs.getString("service_record_id"));
        a.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        a.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return a;
    }
}
