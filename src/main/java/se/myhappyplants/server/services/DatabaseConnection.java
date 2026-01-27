package se.myhappyplants.server.services;

import se.myhappyplants.server.PasswordsAndKeys;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for handling connection with a specific database
 * Created by: Frida Jacobsson 2021-05-21
 */
public class DatabaseConnection implements IDatabaseConnection {

    private java.sql.Connection conn;
    private String databaseName;

    public DatabaseConnection(String databaseName) {
        this.databaseName = databaseName;
    }

    private java.sql.Connection createConnection() throws SQLException, UnknownHostException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ignored) {
            throw new SQLException("SQLite JDBC driver not found");
        }

        this.conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        return conn;
    }

    public void createTables(String databaseName) throws SQLException {
        java.sql.Connection c = getConnection();
        if (databaseName.equals("MyHappyPlants")) {
            String createUserTable = "CREATE TABLE IF NOT EXISTS [User] (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "email TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL, " +
                    "notification_activated INTEGER NOT NULL DEFAULT 1, " +
                    "fun_facts_activated INTEGER NOT NULL DEFAULT 1" +
                    ");";

            String createUserPlantTable = "CREATE TABLE IF NOT EXISTS [Plant] (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER NOT NULL, " +
                    "nickname TEXT, " +
                    "last_watered TEXT NOT NULL, " +
                    "plant_id TEXT NOT NULL, " +
                    "image_url TEXT NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES [User](id), " +
                    "UNIQUE(user_id, nickname)" +
                    ");";

            try {
                c.createStatement().executeUpdate(createUserTable);
                c.createStatement().executeUpdate(createUserPlantTable);
            } catch (SQLException sqlException) {
                throw new SQLException("Could not create tables", sqlException);
            }
        } else if (databaseName.equals("Species")) {
            String createSpeciesTable = "CREATE TABLE IF NOT EXISTS species (" +
                    "id TEXT PRIMARY KEY, " +
                    "common_name TEXT, " +
                    "scientific_name TEXT, " +
                    "family TEXT, " +
                    "image_url TEXT, " +
                    "genus TEXT, " +
                    "light TEXT, " +
                    "water_frequency TEXT" +
                    ");";

            try {
                c.createStatement().executeUpdate(createSpeciesTable);
            } catch (SQLException sqlException) {
                throw new SQLException("Could not create tables", sqlException);
            }
        }
    }

    @Override
    public java.sql.Connection getConnection() {
        if (conn == null) {
            try {
                conn = createConnection();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return conn;
    }

    @Override
    public void closeConnection() {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException sqlException) {
            // ignore
        }
        conn = null;
    }
}
