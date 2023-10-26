package comp603.project2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initialize() throws SQLException {
        createUsersTable();
        createHotelsTable();
        createRoomsTable();
        createBookingsTable();
        createReviewsTable();
    }

    private static void createUsersTable() throws SQLException {
        String sql = "CREATE TABLE Users (" +
                "userId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "username VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +  
                "email VARCHAR(255) NOT NULL, " +
                "role VARCHAR(50) NOT NULL)";
        executeTableCreation(sql);
        sql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, ROLE) VALUES ('admin','admin','admin','admin')";
        executeTableCreation(sql);
    }

    private static void createHotelsTable() throws SQLException {
        String sql = "CREATE TABLE Hotels (" +
                "hotelId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                "hotelName VARCHAR(255) NOT NULL, " +
                "location VARCHAR(255) NOT NULL, " +
                "starRating INT NOT NULL, " +
                "description VARCHAR(1000))";
        executeTableCreation(sql);
    }

private static void createRoomsTable() throws SQLException {
    String sql = "CREATE TABLE Rooms (" +
            "roomId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "hotelId INT NOT NULL, " +
            "roomType VARCHAR(255) NOT NULL, " +
            "pricePerNight DECIMAL(10,2) NOT NULL, " +
            "capacity INT NOT NULL, " +
            "FOREIGN KEY (hotelId) REFERENCES Hotels(hotelId) ON DELETE CASCADE)";  // ADD ON DELETE CASCADE
    executeTableCreation(sql);
}

private static void createBookingsTable() throws SQLException {
    String sql = "CREATE TABLE Bookings (" +
            "bookingId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "roomId INT NOT NULL, " +
            "startDate DATE NOT NULL, " +
            "endDate DATE NOT NULL, " +
            "price DECIMAL(10,2) NOT NULL, " +
            "username VARCHAR(255) NOT NULL, " +
            "phoneNumber VARCHAR(255) NOT NULL, " +
            "bookingDate DATE NOT NULL, " +
            "FOREIGN KEY (roomId) REFERENCES Rooms(roomId) ON DELETE CASCADE)";  // ADD ON DELETE CASCADE
    executeTableCreation(sql);
}

private static void createReviewsTable() throws SQLException {
    String sql = "CREATE TABLE Reviews (" +
            "reviewId INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "userId INT NOT NULL, " +
            "hotelId INT NOT NULL, " +
            "rating INT NOT NULL, " +  // example 1 to 5
            "comment VARCHAR(1000), " +
            "FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE, " +  // ADD ON DELETE CASCADE
            "FOREIGN KEY (hotelId) REFERENCES Hotels(hotelId) ON DELETE CASCADE)";  // ADD ON DELETE CASCADE
    executeTableCreation(sql);
}

    private static void executeTableCreation(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            // Catch SQL exceptions
            // Check if the SQL state is "X0Y32" which means "table already exists"
            if (!e.getSQLState().equals("X0Y32")) {
                throw e;
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        initialize();
    }
}
