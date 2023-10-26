package comp603.project2.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DERBY_URL = "jdbc:derby:db;create=true";
    static {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Derby JDBC driver.", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DERBY_URL);
    }

}
