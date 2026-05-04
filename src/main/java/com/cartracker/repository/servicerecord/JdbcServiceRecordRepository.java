package com.cartracker.repository.servicerecord;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.common.Status;
import com.cartracker.model.servicerecord.MaintenanceTask;
import com.cartracker.model.servicerecord.ServiceRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcServiceRecordRepository implements ServiceRecordRepository {

    private final Connection conn;

    public JdbcServiceRecordRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public ServiceRecord save(ServiceRecord record) {
        String sql = "INSERT INTO service_records (id, vehicle_id, mechanic_id, service_date, description, status, total_cost, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getId());
            ps.setString(2, record.getVehicleId());
            ps.setString(3, record.getMechanicId());
            ps.setDate(4, Date.valueOf(record.getServiceDate()));
            ps.setString(5, record.getDescription());
            ps.setString(6, record.getStatus().name());
            ps.setDouble(7, record.getTotalCost());
            ps.setTimestamp(8, Timestamp.valueOf(record.getCreatedAt()));
            ps.setTimestamp(9, Timestamp.valueOf(record.getUpdatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving service record: " + e.getMessage(), e);
        }
        return record;
    }

    @Override
    public Optional<ServiceRecord> findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM service_records WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRecordRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding service record: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<ServiceRecord> findAll() {
        List<ServiceRecord> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM service_records")) {
            while (rs.next()) list.add(mapRecordRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching service records: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public ServiceRecord update(ServiceRecord record) {
        String sql = "UPDATE service_records SET mechanic_id=?, service_date=?, description=?, status=?, total_cost=?, updated_at=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getMechanicId());
            ps.setDate(2, Date.valueOf(record.getServiceDate()));
            ps.setString(3, record.getDescription());
            ps.setString(4, record.getStatus().name());
            ps.setDouble(5, record.getTotalCost());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, record.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service record: " + e.getMessage(), e);
        }
        return record;
    }

    @Override
    public boolean deleteById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM service_records WHERE id = ?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting service record: " + e.getMessage(), e);
        }
    }

    @Override
    public MaintenanceTask saveTask(MaintenanceTask task) {
        String sql = "INSERT INTO maintenance_tasks (id, service_record_id, task_name, task_details, parts_cost, labour_cost, completed, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getId());
            ps.setString(2, task.getServiceRecordId());
            ps.setString(3, task.getTaskName());
            ps.setString(4, task.getTaskDetails());
            ps.setDouble(5, task.getPartsCost());
            ps.setDouble(6, task.getLabourCost());
            ps.setBoolean(7, task.isCompleted());
            ps.setTimestamp(8, Timestamp.valueOf(task.getCreatedAt()));
            ps.setTimestamp(9, Timestamp.valueOf(task.getUpdatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving task: " + e.getMessage(), e);
        }
        return task;
    }

    @Override
    public Optional<MaintenanceTask> findTaskById(String taskId) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM maintenance_tasks WHERE id = ?")) {
            ps.setString(1, taskId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapTaskRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding task: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<MaintenanceTask> findTasksByRecord(String serviceRecordId) {
        List<MaintenanceTask> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM maintenance_tasks WHERE service_record_id = ?")) {
            ps.setString(1, serviceRecordId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapTaskRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding tasks: " + e.getMessage(), e);
        }
        return list;
    }

    private ServiceRecord mapRecordRow(ResultSet rs) throws SQLException {
        ServiceRecord r = new ServiceRecord(
                rs.getString("id"),
                rs.getString("vehicle_id"),
                rs.getString("mechanic_id"),
                rs.getDate("service_date").toLocalDate(),
                rs.getString("description")
        );
        r.setStatus(Status.valueOf(rs.getString("status")));
        r.setTotalCost(rs.getDouble("total_cost"));
        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return r;
    }

    private MaintenanceTask mapTaskRow(ResultSet rs) throws SQLException {
        MaintenanceTask t = new MaintenanceTask(
                rs.getString("id"),
                rs.getString("service_record_id"),
                rs.getString("task_name"),
                rs.getString("task_details"),
                rs.getDouble("parts_cost"),
                rs.getDouble("labour_cost")
        );
        t.setCompleted(rs.getBoolean("completed"));
        t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        t.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return t;
    }
}
