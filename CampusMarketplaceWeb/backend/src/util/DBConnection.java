package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for creating JDBC connections to MySQL.
 */
public final class DBConnection {
<<<<<<< HEAD
    private static final String URL = "jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true";
    private static final String USER = "campus_app";
    private static final String PASSWORD = "demo1234";
=======
    private static final String URL = "jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=Asia/Taipei";
    private static final String USER = "root";
    private static final String PASSWORD = "請在本機自行填入";
>>>>>>> 454b49db5c1a75089a134ab89933f98f18b59338

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
