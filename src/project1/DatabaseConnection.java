package project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // SECURITY — never hardcode real credentials before pushing to GitHub
    private static final String URL      = "jdbc:mysql://localhost:3306/carstore_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "pawan@2005"; // change locally, never push real password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
