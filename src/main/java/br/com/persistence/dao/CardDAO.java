package br.com.persistence.dao;

import br.com.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static br.com.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {
    private final Connection connection;

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
}
