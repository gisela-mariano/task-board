package br.com.persistence.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionConfig {
    public static Connection getConnection() throws SQLException {
        var connection = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/task-board", "root", "board"
        );
        connection.setAutoCommit(false);

        return connection;
    };
}
