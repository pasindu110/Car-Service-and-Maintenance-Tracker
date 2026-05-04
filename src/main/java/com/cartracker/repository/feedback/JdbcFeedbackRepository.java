package com.cartracker.repository.feedback;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.feedback.Feedback;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcFeedbackRepository implements FeedbackRepository {

    private final Connection conn;

    public JdbcFeedbackRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Feedback save(Feedback feedback) {
        String sql = "INSERT INTO feedback (id, customer_id, service_record_id, rating, comment, created_at, updated_at) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feedback.getId());
            ps.setString(2, feedback.getCustomerId());
            ps.setString(3, feedback.getServiceRecordId());
            ps.setInt(4,    feedback.getRating());
            ps.setString(5, feedback.getComment());
            ps.setTimestamp(6, Timestamp.valueOf(feedback.getCreatedAt()));
            ps.setTimestamp(7, Timestamp.valueOf(feedback.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving feedback: " + e.getMessage(), e);
        }
        return feedback;
    }

    @Override
    public Optional<Feedback> findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM feedback WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding feedback: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Feedback> findAll() {
        List<Feedback> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM feedback")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching feedback: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Feedback> findByCustomerId(String customerId) {
        List<Feedback> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM feedback WHERE customer_id = ?")) {
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding feedback by customer: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Feedback> findByServiceRecordId(String serviceRecordId) {
        List<Feedback> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM feedback WHERE service_record_id = ?")) {
            ps.setString(1, serviceRecordId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding feedback by service record: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Feedback update(Feedback feedback) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE feedback SET rating=?, comment=?, updated_at=? WHERE id=?")) {
            ps.setInt(1,    feedback.getRating());
            ps.setString(2, feedback.getComment());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, feedback.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating feedback: " + e.getMessage(), e);
        }
        return feedback;
    }

    @Override
    public boolean deleteById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM feedback WHERE id = ?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting feedback: " + e.getMessage(), e);
        }
    }

    private Feedback mapRow(ResultSet rs) throws SQLException {
        Feedback f = new Feedback(
                rs.getString("id"),
                rs.getString("customer_id"),
                rs.getString("service_record_id"),
                rs.getInt("rating"),
                rs.getString("comment")
        );
        f.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        f.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return f;
    }
}
