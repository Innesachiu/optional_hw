package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides JDBC MySQL connections for the application.
 */
public final class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DBConnection() {
    }

    /**
     * Creates a new SQL connection.
     *
     * @return mysql connection
     * @throws SQLException if database is unavailable
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
