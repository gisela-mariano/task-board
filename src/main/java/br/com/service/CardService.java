package br.com.service;

import br.com.dto.BoardColumnInfoDTO;
import br.com.exception.CardBlockedException;
import br.com.exception.EntityNotFoundException;
import br.com.exception.FinishedCardException;
import br.com.persistence.dao.BlockedCardDAO;
import br.com.persistence.dao.CardDAO;
import br.com.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static br.com.persistence.entity.BoardColumnTypeEnum.CANCEL;
import static br.com.persistence.entity.BoardColumnTypeEnum.FINAL;

@AllArgsConstructor
public class CardService {
    private final Connection connection;

    public CardEntity create(final CardEntity entity) throws SQLException {
        try {
            var dao = new CardDAO(connection);

            dao.create(entity);
            connection.commit();

            return entity;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void moveToNextColumn(
            final Long cardId,
            final List<BoardColumnInfoDTO> boardColumnsInfo
    ) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);

            var dto = optional.orElseThrow(() -> {
                return new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId));
            });

            if (dto.isBlocked()) {
                var message = "O card %s está bloqueado. É necessário desbloquea-lo para mover".formatted(cardId);

                throw new CardBlockedException(message);
            }

            var currentColumn = boardColumnsInfo.stream()
                                                .filter(bc -> bc.id().equals(dto.columnId()))
                                                .findFirst()
                                                .orElseThrow(() -> new IllegalStateException(
                                                        "O card informado pertence a outro board."));

            if (currentColumn.type().equals(FINAL)) {
                throw new FinishedCardException("O card %s já foi finalizado".formatted(cardId));
            }

            var nextColumn = boardColumnsInfo.stream()
                                             .filter(bc -> bc.order() == currentColumn.order() + 1)
                                             .findFirst()
                                             .orElseThrow();

            dao.moveToColumn(nextColumn.id(), cardId);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void cancel(
            final Long cardId,
            final Long cancelColumnId,
            final List<BoardColumnInfoDTO> boardColumnsInfo
    ) throws SQLException {

        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);

            var dto = optional.orElseThrow(() -> {
                return new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId));
            });

            if (dto.isBlocked()) {
                var message = "O card %s está bloqueado. É necessário desbloquea-lo para mover".formatted(cardId);

                throw new CardBlockedException(message);
            }

            var currentColumn = boardColumnsInfo.stream()
                                                .filter(bc -> bc.id().equals(dto.columnId()))
                                                .findFirst()
                                                .orElseThrow(() -> new IllegalStateException(
                                                        "O card informado pertence a outro board."));

            if (currentColumn.type().equals(FINAL)) {
                throw new FinishedCardException("O card %s já foi finalizado".formatted(cardId));
            }

            boardColumnsInfo.stream().filter(bc -> bc.order() == currentColumn.order() + 1).findFirst().orElseThrow();

            dao.moveToColumn(cancelColumnId, cardId);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void block(
            final Long cardId,
            final String reason,
            final List<BoardColumnInfoDTO> boardColumnsInfo
    ) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);

            var dto = optional.orElseThrow(() -> {
                return new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId));
            });

            if (dto.isBlocked()) {
                throw new CardBlockedException("O card %s já está bloqueado".formatted(cardId));
            }

            var currentColumn =
                    boardColumnsInfo.stream().filter(bc -> bc.id().equals(dto.columnId())).findFirst().orElseThrow();

            if (currentColumn.type().equals(FINAL) || currentColumn.type().equals(CANCEL)) {
                var message =
                        "O card está na coluna do tipo %s e não pode ser bloqueado".formatted(currentColumn.type());

                throw new IllegalStateException(message);
            }

            var blockedCardDao = new BlockedCardDAO(connection);
            blockedCardDao.block(reason, cardId);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    public void unblock(
            final Long cardId,
            final String reason) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);

            var dto = optional.orElseThrow(() -> {
                return new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId));
            });

            if (!dto.isBlocked()) {
                throw new CardBlockedException("O card %s não está bloqueado".formatted(cardId));
            }

            var blockedCardDao = new BlockedCardDAO(connection);
            blockedCardDao.unblock(reason, cardId);

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}
