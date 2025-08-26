package br.com.service;

import br.com.persistence.dao.BoardColumnDAO;
import br.com.persistence.dao.BoardDAO;
import br.com.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {
    private final Connection connection;

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        var dao = new BoardDAO(connection);
        var boardColumnDao = new BoardColumnDAO(connection);

        var optional = dao.findById(id);

        if (optional.isPresent()) {
            var entity = optional.get();

            entity.setBoardColumns(boardColumnDao.findByBoardId(entity.getId()));

            return Optional.of(entity);
        }

        return Optional.empty();
    }
}
