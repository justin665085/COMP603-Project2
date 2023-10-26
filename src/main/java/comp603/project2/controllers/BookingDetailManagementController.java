package comp603.project2.controllers;

import comp603.project2.dao.BookingDAO;
import comp603.project2.dao.RoomDAO;
import comp603.project2.models.Booking;
import comp603.project2.models.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class BookingDetailManagementController {

    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, Integer> bookingIDCol;
    @FXML
    private TableColumn<Booking, String> startDateCol;
    @FXML
    private TableColumn<Booking, String> endDateCol;
    @FXML
    private TableColumn<Booking, Double> totalPriceCol;
    @FXML
    private TableColumn<Booking, String> usernameCol;
    @FXML
    private TableColumn<Booking, String> phoneNumberCol;
    @FXML
    private TableColumn<Booking, String> bookingDateCol;

    private BookingDAO bookingDAO = new BookingDAO();
    private RoomDAO roomDAO = new RoomDAO();

    @Setter
    private LocalDate start;
    @Setter
    private LocalDate end;

    private int roomId;
    @Setter
    private double price;

    @FXML
    public void initialize() {
        // Set cell value factories for the table columns
        bookingIDCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        bookingDateCol.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
    }

    public void loadRoomBookings(int roomId) throws SQLException {
        this.roomId = roomId;
        List<Booking> bookings = bookingDAO.getBookingsByRoomId(roomId); // Assuming you have or will implement a method like this in BookingDAO
        ObservableList<Booking> bookingData = FXCollections.observableArrayList(bookings);
        bookingTable.setItems(bookingData);
    }

    private boolean doRangesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.minusDays(1).isBefore(start2) || start1.isAfter(end2.minusDays(1)));
    }

    @FXML
    private void deleteBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                bookingDAO.deleteBooking(selectedBooking.getBookingId());
                loadRoomBookings(selectedBooking.getRoomId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toLocalDate();
    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    @FXML
    private void addBooking() throws SQLException {
        // Retrieve all bookings within the specified date range from the database.
        List<Booking> bookings = bookingDAO.getAllBookingsBetweenDates(convertToDateViaSqlDate(start), convertToDateViaSqlDate(end));
        Room room = roomDAO.getRoomById(roomId);
        int overlappingBookings = 0;
        for (Booking booking : bookings) {
            if (doRangesOverlap(start, end, convertToLocalDateViaInstant(booking.getStartDate()),
                    convertToLocalDateViaInstant(booking.getEndDate()))
                    && booking.getRoomId() == room.getRoomId()
            ) {
                overlappingBookings++;
            }
        }

        int availableCapacity = room.getCapacity() - overlappingBookings;
        if (availableCapacity > 0) {
            try {
                Booking newBooking = new Booking();
                newBooking.setBookingId(-1);
                newBooking.setStartDate(convertToDateViaSqlDate(start));
                newBooking.setEndDate(convertToDateViaSqlDate(end));
                newBooking.setBookingDate(convertToDateViaSqlDate(LocalDate.now()));
                newBooking.setRoomId(roomId);
                newBooking.setPrice(price);

                showBookingDialog(newBooking);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Error","No room capacity available");
        }


    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void updateBooking() {
        Booking selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                showBookingDialog(selectedBooking);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showBookingDialog(Booking booking) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/BookingDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Booking");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Set the booking into the controller.
        BookingDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setBooking(booking);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        loadRoomBookings(booking.getRoomId());
    }
}
