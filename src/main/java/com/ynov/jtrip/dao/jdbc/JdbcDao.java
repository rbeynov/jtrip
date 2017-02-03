package com.ynov.jtrip.dao.jdbc;

import com.ynov.jtrip.dao.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JdbcDao {


    private Connection connection;

    protected JdbcDao(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DataAccessException("Unable to close the connection", e);
        }
    }
}
