package comp603.project2.dao;

import comp603.project2.models.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    /**
     * Retrieves all rooms from a specific hotel based on the hotel's ID.
     * @param hotelId the ID of the hotel
     * @return a list of rooms belonging to the specified hotel
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */

    public List<Room> getAllRoomsByHotelId(int hotelId) throws SQLException {
        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms WHERE hotelId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, hotelId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomId(resultSet.getInt("roomId"));
                room.setHotelId(resultSet.getInt("hotelId"));
                room.setRoomType(resultSet.getString("roomType"));
                room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                room.setCapacity(resultSet.getInt("capacity"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    public void addRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (hotelId, roomType, pricePerNight, capacity) VALUES (?, ?, ?, ?)";
        System.out.println(room.getHotelId() + "JJJ");
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, room.getHotelId());
            preparedStatement.setString(2, room.getRoomType());
            preparedStatement.setDouble(3, room.getPricePerNight());
            preparedStatement.setInt(4, room.getCapacity());
            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();
        }
    }

    public void updateRoom(Room room) throws SQLException {
        String sql = "UPDATE rooms SET roomType = ?, pricePerNight = ?, capacity = ? WHERE roomId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, room.getRoomType());
            preparedStatement.setDouble(2, room.getPricePerNight());
            preparedStatement.setInt(3, room.getCapacity());
            preparedStatement.setInt(4, room.getRoomId());
            preparedStatement.executeUpdate();
        }
    }
    /**
     * Retrieves a specific room based on its ID.
     * @param roomId the ID of the room
     * @return the Room object if found, null otherwise
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */

    public Room getRoomById(int roomId) throws SQLException {
        Room room = null;
        String sql = "SELECT * FROM rooms WHERE roomId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                room = new Room();
                room.setRoomId(resultSet.getInt("roomId"));
                room.setHotelId(resultSet.getInt("hotelId"));
                room.setRoomType(resultSet.getString("roomType"));
                room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                room.setCapacity(resultSet.getInt("capacity"));
            }
        }
        return room;
    }

    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomId(resultSet.getInt("roomId"));
                room.setHotelId(resultSet.getInt("hotelId"));
                room.setRoomType(resultSet.getString("roomType"));
                room.setPricePerNight(resultSet.getDouble("pricePerNight"));
                room.setCapacity(resultSet.getInt("capacity"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    public void deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE roomId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomId);
            preparedStatement.executeUpdate();
        }
    }
}
