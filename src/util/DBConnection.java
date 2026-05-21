package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Provides database connections. */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/campus_marketplace?useSSL=false&serverTimezone=Asia/Taipei";
    private static final String USER = "root";
    private static final String PASSWORD = "請在本機自行填入";

    /** @return JDBC connection */
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL, USER, PASSWORD); }
}
