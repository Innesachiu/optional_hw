package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for creating JDBC connections to MySQL.
 */
public final class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=Asia/Taipei";
    private static final String USER = "root";
    private static final String PASSWORD = "Innesa1234554321!";

    private DBConnection() {
    }

    /**
     * Returns a new JDBC connection.
     *
     * @return JDBC connection
     * @throws SQLException when connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
