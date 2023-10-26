package comp603.project2.dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import comp603.project2.models.Hotel;
public class HotelDAO {

    public ObservableList<Hotel> getAllHotels() {
        // Initialize an observable list to store hotel data
        ObservableList<Hotel> hotels = FXCollections.observableArrayList();
        // SQL query to select all data from the hotels table
        String sql = "SELECT * FROM Hotels";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Hotel hotel = new Hotel();
                // Set the properties of the Hotel object based on the data from the database
                hotel.setHotelId(resultSet.getInt("hotelId"));
                hotel.setHotelName(resultSet.getString("hotelName"));
                hotel.setLocation(resultSet.getString("location"));
                hotel.setStarRating(resultSet.getInt("starRating"));
                hotel.setDescription(resultSet.getString("description"));
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }
    public ObservableList<String> getAllHotelNames() {
        ObservableList<String> hotelNames = FXCollections.observableArrayList();
        String sql = "SELECT hotelName FROM Hotels";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                hotelNames.add(resultSet.getString("hotelName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotelNames;
    }
    public void addHotel(Hotel hotel) throws SQLException {
        // SQL command to insert a new row into the hotels table
        String sql = "INSERT INTO Hotels (hotelName, location, starRating, description) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, hotel.getHotelName());
            preparedStatement.setString(2, hotel.getLocation());
            preparedStatement.setInt(3, hotel.getStarRating());
            preparedStatement.setString(4, hotel.getDescription());
            preparedStatement.executeUpdate();
        }
    }

    public void updateHotel(Hotel hotel) throws SQLException {
        // SQL command to update an existing row in the hotels table
        String sql = "UPDATE Hotels SET hotelName=?, location=?, starRating=?, description=? WHERE hotelId=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, hotel.getHotelName());
            preparedStatement.setString(2, hotel.getLocation());
            preparedStatement.setInt(3, hotel.getStarRating());
            preparedStatement.setString(4, hotel.getDescription());
            preparedStatement.setInt(5, hotel.getHotelId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteHotel(Hotel hotel) throws SQLException {
        // SQL command to delete a row from the hotels table
        String sql = "DELETE FROM Hotels WHERE hotelId=?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, hotel.getHotelId());
            preparedStatement.executeUpdate();
        }
    }

    public int generateNewId() {
        //Generates a new unique ID for a hotel.
        String sql = "SELECT MAX(hotelId) FROM Hotels";
        int maxId = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }
}
