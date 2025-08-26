package br.com.service;

import br.com.persistence.dao.BoardColumnDAO;
import br.com.persistence.dao.BoardDAO;
import br.com.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {
    private final Connection connection;

    public BoardEntity create(final BoardEntity entity) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);

        try {
            dao.create(entity);
            var columns = entity.getBoardColumns().stream().peek(column -> column.setBoard(entity)).toList();

            for (var column : columns) {
                boardColumnDao.create(column);
            }

            connection.commit();
        } catch (SQLException e ){
            connection.rollback();
            throw e;
        }

        return entity;
    }

    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);

        try {
            if (!dao.exists(id)) return false;

            dao.delete(id);
            connection.commit();

            return true;
        } catch (SQLException e ){
            connection.rollback();
            throw e;
        }
    }
}
