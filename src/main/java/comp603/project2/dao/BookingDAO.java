package comp603.project2.dao;

import comp603.project2.models.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingDAO {
    public List<Booking> getAllBookingsBetweenDates(Date startDate, Date endDate) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings WHERE startDate <= ? AND endDate >= ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, new java.sql.Date(endDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                bookings.add(booking);
            }
        }
        return bookings;
    }
    public List<Booking> getBookingsByRoomId(int roomId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();

        String sql = "SELECT * FROM bookings WHERE roomId = ? ORDER BY startDate";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = extractBookingFromResultSet(resultSet);
                bookings.add(booking);
            }
        }
        return bookings;
    }
    public void addBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (roomId, startDate, endDate, username, phoneNumber, bookingDate, price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, booking.getRoomId());
            preparedStatement.setDate(2, new java.sql.Date(booking.getStartDate().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(booking.getEndDate().getTime()));
            preparedStatement.setString(4, booking.getUsername());
            preparedStatement.setString(5, booking.getPhoneNumber());
            preparedStatement.setDate(6, new java.sql.Date(booking.getBookingDate().getTime()));
            preparedStatement.setDouble(7, booking.getPrice());

            preparedStatement.executeUpdate();
        }
    }
    public void deleteBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE bookingId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookingId);
            preparedStatement.executeUpdate();
        }
    }
    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET roomId = ?, startDate = ?, endDate = ?, username = ?, phoneNumber = ?, bookingDate = ?, price = ? WHERE bookingId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, booking.getRoomId());
            preparedStatement.setDate(2, new java.sql.Date(booking.getStartDate().getTime()));
            preparedStatement.setDate(3, new java.sql.Date(booking.getEndDate().getTime()));
            preparedStatement.setString(4, booking.getUsername());
            preparedStatement.setString(5, booking.getPhoneNumber());
            preparedStatement.setDate(6, new java.sql.Date(booking.getBookingDate().getTime()));
            preparedStatement.setDouble(7, booking.getPrice());
            preparedStatement.setInt(8, booking.getBookingId());

            preparedStatement.executeUpdate();
        }
    }

    private Booking extractBookingFromResultSet(ResultSet resultSet) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(resultSet.getInt("bookingId"));
        booking.setRoomId(resultSet.getInt("roomId"));
        booking.setStartDate(resultSet.getDate("startDate"));
        booking.setEndDate(resultSet.getDate("endDate"));
        booking.setUsername(resultSet.getString("username"));
        booking.setPhoneNumber(resultSet.getString("phoneNumber"));
        booking.setBookingDate(resultSet.getDate("bookingDate"));
        booking.setPrice(resultSet.getDouble("price"));
        return booking;
    }
}
