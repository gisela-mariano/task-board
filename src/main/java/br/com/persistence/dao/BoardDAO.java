package br.com.persistence.dao;

import br.com.persistence.entity.BoardEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {
    private final Connection connection;

    public BoardEntity create(final BoardEntity entity) throws SQLException {
        var query = "INSERT INTO boards (name) VALUES (?);";

        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();

            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
        }

        return entity;
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var query = "SELECT id, name FROM boards WHERE id = ?;";

        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var board = new BoardEntity();

                board.setId(resultSet.getLong("id"));
                board.setName(resultSet.getString("name"));

                return Optional.of(board);
            }

            return Optional.empty();
        }

    }

    public boolean exists(final Long id) throws SQLException {
        var query = "SELECT 1 FROM boards WHERE id = ?;";

        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();

            return statement.getResultSet().next();
        }
    }

    public void delete(final Long id) throws SQLException {
        var query = "DELETE FROM boards WHERE id = ?;";

        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();
        }
    }
}
