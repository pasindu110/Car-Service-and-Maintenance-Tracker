package com.cartracker.repository.paymentcard;

import com.cartracker.config.DatabaseConnection;
import com.cartracker.model.paymentcard.CardType;
import com.cartracker.model.paymentcard.PaymentCard;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JdbcPaymentCardRepository – MySQL-backed implementation of PaymentCardRepository.
 *
 * Requires the payment_cards table defined in schema.sql.
 *
 * Column mapping:
 *   id, cardholder_name, last4_digits, masked_number,
 *   expiry_month, expiry_year, card_type, country,
 *   created_at, updated_at
 */
public class JdbcPaymentCardRepository implements PaymentCardRepository {

    private final Connection conn;

    public JdbcPaymentCardRepository() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @Override
    public PaymentCard save(PaymentCard card) {
        String sql = "INSERT INTO payment_cards "
                + "(id, cardholder_name, last4_digits, masked_number, "
                + " expiry_month, expiry_year, card_type, country, created_at, updated_at) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindAllFields(ps, card);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment card: " + e.getMessage(), e);
        }
        return card;
    }

    @Override
    public Optional<PaymentCard> findById(String id) {
        String sql = "SELECT * FROM payment_cards WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment card by id: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<PaymentCard> findAll() {
        List<PaymentCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM payment_cards ORDER BY created_at DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) cards.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching payment cards: " + e.getMessage(), e);
        }
        return cards;
    }

    @Override
    public PaymentCard update(PaymentCard card) {
        String sql = "UPDATE payment_cards SET "
                + "cardholder_name=?, last4_digits=?, masked_number=?, "
                + "expiry_month=?, expiry_year=?, card_type=?, country=?, updated_at=? "
                + "WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, card.getCardholderName());
            ps.setString(2, card.getLast4Digits());
            ps.setString(3, card.getMaskedNumber());
            ps.setInt   (4, card.getExpiryMonth());
            ps.setInt   (5, card.getExpiryYear());
            ps.setString(6, card.getCardType().name());
            ps.setString(7, card.getCountry());
            ps.setTimestamp(8, Timestamp.valueOf(card.getUpdatedAt()));
            ps.setString(9, card.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment card: " + e.getMessage(), e);
        }
        return card;
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM payment_cards WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting payment card: " + e.getMessage(), e);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Binds all 10 INSERT parameters from the given PaymentCard. */
    private void bindAllFields(PreparedStatement ps, PaymentCard card) throws SQLException {
        ps.setString   (1, card.getId());
        ps.setString   (2, card.getCardholderName());
        ps.setString   (3, card.getLast4Digits());
        ps.setString   (4, card.getMaskedNumber());
        ps.setInt      (5, card.getExpiryMonth());
        ps.setInt      (6, card.getExpiryYear());
        ps.setString   (7, card.getCardType().name());
        ps.setString   (8, card.getCountry());
        ps.setTimestamp(9,  Timestamp.valueOf(card.getCreatedAt()));
        ps.setTimestamp(10, Timestamp.valueOf(card.getUpdatedAt()));
    }

    /** Maps a single ResultSet row to a PaymentCard instance. */
    private PaymentCard mapRow(ResultSet rs) throws SQLException {
        PaymentCard card = new PaymentCard(rs.getString("id"));
        card.setCardholderName(rs.getString("cardholder_name"));
        card.setLast4Digits   (rs.getString("last4_digits"));
        card.setMaskedNumber  (rs.getString("masked_number"));
        card.setExpiryMonth   (rs.getInt   ("expiry_month"));
        card.setExpiryYear    (rs.getInt   ("expiry_year"));
        card.setCardType      (CardType.valueOf(rs.getString("card_type")));
        card.setCountry       (rs.getString("country"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        card.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : LocalDateTime.now());
        card.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : LocalDateTime.now());
        return card;
    }
}
