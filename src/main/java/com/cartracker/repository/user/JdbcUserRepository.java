package com.cartracker.repository.user;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.common.UserRole;
import com.cartracker.model.user.Admin;
import com.cartracker.model.user.Customer;
import com.cartracker.model.user.Mechanic;
import com.cartracker.model.user.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final Connection conn;

    public JdbcUserRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (id, username, password, email, full_name, phone, role, active, admin_level, address, specialisation, experience_years, available, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setCommonFields(ps, user);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM users")) {
            while (rs.next()) users.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET username=?, password=?, email=?, full_name=?, phone=?, role=?, active=?, admin_level=?, address=?, specialisation=?, experience_years=?, available=?, updated_at=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,  user.getUsername());
            ps.setString(2,  user.getPassword());
            ps.setString(3,  user.getEmail());
            ps.setString(4,  user.getFullName());
            ps.setString(5,  user.getPhone());
            ps.setString(6,  user.getRole().name());
            ps.setBoolean(7, user.isActive());
            ps.setString(8,  user instanceof Admin ? ((Admin) user).getAdminLevel() : null);
            ps.setString(9,  user instanceof Customer ? ((Customer) user).getAddress() : null);
            ps.setString(10, user instanceof Mechanic ? ((Mechanic) user).getSpecialisation() : null);
            ps.setObject(11, user instanceof Mechanic ? ((Mechanic) user).getExperienceYears() : null);
            ps.setObject(12, user instanceof Mechanic ? ((Mechanic) user).isAvailable() : null);
            ps.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(14, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
        return user;
    }

    @Override
    public boolean deleteById(String id) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE users SET active = FALSE WHERE id = ?")) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    private void setCommonFields(PreparedStatement ps, User user) throws SQLException {
        ps.setString(1,  user.getId());
        ps.setString(2,  user.getUsername());
        ps.setString(3,  user.getPassword());
        ps.setString(4,  user.getEmail());
        ps.setString(5,  user.getFullName());
        ps.setString(6,  user.getPhone());
        ps.setString(7,  user.getRole().name());
        ps.setBoolean(8, user.isActive());
        ps.setString(9,  user instanceof Admin ? ((Admin) user).getAdminLevel() : null);
        ps.setString(10, user instanceof Customer ? ((Customer) user).getAddress() : null);
        ps.setString(11, user instanceof Mechanic ? ((Mechanic) user).getSpecialisation() : null);
        ps.setObject(12, user instanceof Mechanic ? ((Mechanic) user).getExperienceYears() : null);
        ps.setObject(13, user instanceof Mechanic ? ((Mechanic) user).isAvailable() : null);
        ps.setTimestamp(14, Timestamp.valueOf(user.getCreatedAt()));
        ps.setTimestamp(15, Timestamp.valueOf(user.getUpdatedAt()));
    }

    private User mapRow(ResultSet rs) throws SQLException {
        UserRole role = UserRole.valueOf(rs.getString("role"));
        User user;
        if (role == UserRole.ADMIN) {
            user = new Admin(rs.getString("id"), rs.getString("username"), rs.getString("email"), rs.getString("full_name"), rs.getString("phone"), rs.getString("admin_level"));
        } else if (role == UserRole.CUSTOMER) {
            user = new Customer(rs.getString("id"), rs.getString("username"), rs.getString("email"), rs.getString("full_name"), rs.getString("phone"), rs.getString("address"));
        } else {
            Mechanic m = new Mechanic(rs.getString("id"), rs.getString("username"), rs.getString("email"), rs.getString("full_name"), rs.getString("phone"), rs.getString("specialisation"), rs.getInt("experience_years"));
            m.setAvailable(rs.getBoolean("available"));
            user = m;
        }
        user.setPassword(rs.getString("password"));
        user.setActive(rs.getBoolean("active"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    }
}
