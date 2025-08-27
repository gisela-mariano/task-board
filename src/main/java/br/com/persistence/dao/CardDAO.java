package br.com.persistence.dao;

import br.com.dto.CardDetailsDTO;
import br.com.persistence.entity.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static br.com.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {
    private final Connection connection;

    public CardEntity create(final CardEntity entity) throws SQLException {
        var query = "INSERT INTO cards (title, description, board_column_id) VALUES (?, ?, ?);";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getTitle());
            statement.setString(2, entity.getDescription());
            statement.setLong(3, entity.getBoardColumn().getId());

            statement.executeUpdate();

            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
        }

        return entity;
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var query = """
                    SELECT
                        cd.id,
                        cd.title,
                        cd.description,
                        bcd.blocked_at,
                        bcd.block_reason,
                        cd.board_column_id,
                        bcl.name,
                        (
                            SELECT COUNT(sub_bcd.id) FROM blocked_cards sub_bcd WHERE sub_bcd.card_id = cd.id
                        ) blocks_amount
                    FROM
                        cards cd
                    LEFT JOIN
                        blocked_cards bcd
                    ON
                        cd.id = bcd.card_id
                    AND
                        bcd.unblocked_at IS NULL
                    INNER JOIN
                        board_columns bcl
                    ON
                        bcl.id = cd.board_column_id
                    WHERE
                        cd.id = ?;
                    """;
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("cd.id"),
                        resultSet.getString("cd.title"),
                        resultSet.getString("cd.description"),
                        nonNull(resultSet.getString("bcd.block_reason")),
                        toOffsetDateTime(resultSet.getTimestamp("bcd.blocked_at")),
                        resultSet.getString("bcd.block_reason"),
                        resultSet.getInt("blocks_amount"),
                        resultSet.getLong("cd.board_column_id"),
                        resultSet.getString("bcl.name")
                );

                return Optional.of(dto);
            }
        }
        return Optional.empty();
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        var query = "UPDATE cards SET board_column_id = ? WHERE id = ?;";

        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, columnId);
            statement.setLong(2, cardId);

            statement.executeUpdate();
        }
    }
}
