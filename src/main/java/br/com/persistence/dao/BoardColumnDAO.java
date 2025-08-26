package br.com.persistence.dao;

import br.com.dto.BoardColumnDTO;
import br.com.persistence.entity.BoardColumnEntity;
import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.com.persistence.entity.BoardColumnTypeEnum.findByName;

@AllArgsConstructor
public class BoardColumnDAO {
    private final Connection connection;

    public BoardColumnEntity create(final BoardColumnEntity entity) throws SQLException {
        var query = "INSERT INTO board_columns (name, `order`, type, board_id) VALUES (?, ?, ?, ?);";
        try (var statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getOrder());
            statement.setString(3, entity.getType().name());
            statement.setLong(4, entity.getBoard().getId());

            statement.executeUpdate();

            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }

            return entity;
        }
    }

    public List<BoardColumnEntity> findByBoardId(final Long id) throws SQLException {
        List<BoardColumnEntity> entities = new ArrayList<>();

        var query = "SELECT id, name, `order`, type FROM board_columns WHERE board_id = ? ORDER BY `order`;";
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var entity = new BoardColumnEntity();

                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setType(findByName(resultSet.getString("type")));

                entities.add(entity);
            }
            return entities;
        }
    }

    public List<BoardColumnDTO> findByBoardIdWithDetails(final Long id) throws SQLException {
        List<BoardColumnDTO> dtos = new ArrayList<>();

        var query = """
                        SELECT 
                            bc.id,
                            bc.name,
                            bc.type
                            COUNT(
                                SELECT c.id FROM cards c WHERE c.board_column_id = bc.id
                            ) cards_amount
                        FROM 
                            board_columns bc
                        WHERE 
                            board_id = ? 
                        ORDER BY 
                            `order`;
                    """;
        try (var statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var dto = new BoardColumnDTO(
                        resultSet.getLong("bc.id"),
                        resultSet.getString("bc.name"),
                        findByName(resultSet.getString("bc.type")),
                        resultSet.getInt("cards_amount")
                );

                dtos.add(dto);
            }
            return dtos;
        }
    }
}
