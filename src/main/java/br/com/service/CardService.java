package br.com.service;

import br.com.persistence.dao.CardDAO;
import br.com.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

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
}
