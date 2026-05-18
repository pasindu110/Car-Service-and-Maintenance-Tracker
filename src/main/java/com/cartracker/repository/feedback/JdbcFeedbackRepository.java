package com.cartracker.repository.feedback;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.feedback.Feedback;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcFeedbackRepository – MySQL implementation of FeedbackRepository.
 *
 * Demonstrates POLYMORPHISM: same interface as FileFeedbackRepository,
 * so the service layer works identically regardless of which is injected.
 *
 * Requires: the `feedback` table to exist (see schema.sql / migration V2).
 *
 * Thread-safety: uses a shared Connection from DatabaseConnection singleton.
 * For production, switch to a connection pool (HikariCP / c3p0).
 */
public class JdbcFeedbackRepository implements FeedbackRepository {

    private static final String TABLE = "feedback";

    private final Connection conn;

    public JdbcFeedbackRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ── Save (createFeedback) ─────────────────────────────────────────────────

    @Override
    public Feedback save(Feedback feedback) {
        String sql = "INSERT INTO " + TABLE +
                " (id, user_id, service_id, rating, comment, created_at, updated_at)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feedback.getId());
            ps.setString(2, feedback.getUserId());
            ps.setString(3, feedback.getServiceId());
            ps.setInt   (4, feedback.getRating());
            ps.setString(5, feedback.getComment());
            ps.setTimestamp(6, toTimestamp(feedback.getCreatedAt()));
            ps.setTimestamp(7, toTimestamp(feedback.getUpdatedAt()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("JdbcFeedbackRepository: error saving feedback – " + e.getMessage(), e);
        }
        return feedback;
    }

    // ── FindById (getFeedback) ────────────────────────────────────────────────

    @Override
    public Optional<Feedback> findById(String feedbackId) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feedbackId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("JdbcFeedbackRepository: error finding feedback by id – " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    // ── FindAll (getAllFeedbacks) ──────────────────────────────────────────────

    @Override
    public List<Feedback> findAll() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY created_at DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("JdbcFeedbackRepository: error fetching all feedback – " + e.getMessage(), e);
        }
        return list;
    }

    // ── FindByUserId ──────────────────────────────────────────────────────────

    @Override
    public List<Feedback> findByUserId(String userId) {
        return queryList("SELECT * FROM " + TABLE + " WHERE user_id = ? ORDER BY created_at DESC", userId);
    }

    // ── FindByServiceId ───────────────────────────────────────────────────────

    @Override
    public List<Feedback> findByServiceId(String serviceId) {
        return queryList("SELECT * FROM " + TABLE + " WHERE service_id = ? ORDER BY created_at DESC", serviceId);
    }

    // ── Update (updateFeedback) ───────────────────────────────────────────────

    @Override
    public Feedback update(Feedback feedback) {
        String sql = "UPDATE " + TABLE +
                " SET rating = ?, comment = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, feedback.getRating());
            ps.setString(2, feedback.getComment());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, feedback.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("JdbcFeedbackRepository: no feedback found to update – id=" + feedback.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("JdbcFeedbackRepository: error updating feedback – " + e.getMessage(), e);
        }
        return feedback;
    }

    // ── DeleteById (deleteFeedback) ───────────────────────────────────────────

    @Override
    public boolean deleteById(String feedbackId) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("JdbcFeedbackRepository: error deleting feedback – " + e.getMessage(), e);
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Maps one ResultSet row to a Feedback entity. */
    private Feedback mapRow(ResultSet rs) throws SQLException {
        Feedback f = new Feedback(
                rs.getString("id"),
                rs.getString("user_id"),
                rs.getString("service_id"),
                rs.getInt   ("rating"),
                rs.getString("comment")
        );
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (createdAt != null) f.setCreatedAt(createdAt.toLocalDateTime());
        if (updatedAt != null) f.setUpdatedAt(updatedAt.toLocalDateTime());
        return f;
    }

    /** Runs a SELECT with one String parameter and returns the mapped list. */
    private List<Feedback> queryList(String sql, String param) {
        List<Feedback> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("JdbcFeedbackRepository: query error – " + e.getMessage(), e);
        }
        return list;
    }

    /** Null-safe LocalDateTime → Timestamp conversion. */
    private Timestamp toTimestamp(LocalDateTime ldt) {
        return ldt != null ? Timestamp.valueOf(ldt) : Timestamp.valueOf(LocalDateTime.now());
    }
}