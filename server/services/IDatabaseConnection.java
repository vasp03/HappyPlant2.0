package se.mau.grupp7.server.services;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for defining methods needed in a DatabaseConnection class
 * Created by: Frida Jacobsson 2021-05-21
 */
public interface IDatabaseConnection {

    Connection getConnection();

    void closeConnection();

    void createTables(String databaseName) throws SQLException;
}
