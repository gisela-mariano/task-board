package br.com.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.persistence.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockedCardDAO {
    private final Connection connection;

    public void block(final String reason, final Long cardId) throws SQLException {
        var query = "INSERT INTO blocked_cards (blocked_at, block_reason, card_id) VALUES (?, ?, ?);";
        try (var statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            statement.setString(2, reason);
            statement.setLong(3, cardId);
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        var query = """
                    UPDATE
                        blocked_cards
                    SET
                        unblocked_at = ?,
                        unblock_reason = ?
                    WHERE
                        card_id = ?
                    AND
                        unblock_reason IS NULL;
                    """;
        try (var statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, toTimestamp(OffsetDateTime.now()));
            statement.setString(2, reason);
            statement.setLong(3, cardId);
            statement.executeUpdate();
        }
    }
}
