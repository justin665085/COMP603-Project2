package comp603.project2.controllers;

import comp603.project2.dao.BookingDAO;
import comp603.project2.dao.HotelDAO;
import comp603.project2.dao.RoomDAO;
import comp603.project2.models.Booking;
import comp603.project2.models.BookingSearchDTO;
import comp603.project2.models.Hotel;
import comp603.project2.models.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingManagementController {

    @FXML
    private ComboBox<Hotel> cmbHotels;
    @FXML
    private TableView<BookingSearchDTO> tblHotels;
    @FXML
    private TableColumn<BookingSearchDTO, Integer> colRoomId;
    @FXML
    private TableColumn<BookingSearchDTO, String> colRoomType;
    @FXML
    private TableColumn<BookingSearchDTO, Double> colPrice;
    @FXML
    private TableColumn<BookingSearchDTO, Integer> colCapacity;
    @FXML
    private TableColumn<BookingSearchDTO, Boolean> colIsAvailable;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    private BookingDAO bookingDAO = new BookingDAO();
    private RoomDAO roomDAO = new RoomDAO();
    private HotelDAO hotelDAO = new HotelDAO();

    private void loadHotelsToComboBox() {
        ObservableList<Hotel> hotels = hotelDAO.getAllHotels();
        cmbHotels.setItems(hotels);
    }

    @FXML
    public void initialize() {
        // Initialize your ComboBox and other components if needed.
        loadHotelsToComboBox();

        // Setting up the table columns
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colIsAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toLocalDate();
    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    @FXML
    private void searchRooms() throws SQLException {
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        Hotel hotel = cmbHotels.getValue();

        if (start == null || end == null) {
            showAlert("Warning", "Please select a valid date range!");
            return;
        }

        // Check if start date is earlier than end date
        if (!start.isBefore(end)) {
            showAlert("Warning", "Start date must be earlier than end date and they cannot be on the same day!");
            return;
        }

        List<Booking> bookings = bookingDAO.getAllBookingsBetweenDates(convertToDateViaSqlDate(start), convertToDateViaSqlDate(end));
        List<Room> rooms = roomDAO.getAllRooms();  // Assuming this method exists

        ObservableList<BookingSearchDTO> results = FXCollections.observableArrayList();

        for (Room room : rooms) {
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
            double totalDays = ChronoUnit.DAYS.between(start, end);
            double price = room.getPricePerNight() * totalDays;
            if (room.getHotelId() == hotel.getHotelId())
                results.add(new BookingSearchDTO(
                        room.getRoomId(),
                        room.getRoomType(),
                        price,
                        availableCapacity,
                        availableCapacity > 0 ? "available" : "unavailable"
                ));
        }

        tblHotels.setItems(results);
    }

    private boolean doRangesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !(end1.minusDays(1).isBefore(start2) || start1.isAfter(end2.minusDays(1)));
    }

    @FXML
    private void showBooking() throws IOException, SQLException {
        BookingSearchDTO selectedBookingDTO = tblHotels.getSelectionModel().getSelectedItem();

        if (selectedBookingDTO == null) {
            showAlert("Warning", "Please select a room to view its bookings!");
            return;
        }

        int selectedRoomId = selectedBookingDTO.getRoomId();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BookingDetailManagement.fxml"));
        Parent root = loader.load();

        // Assume BookingDetailManagementController has a method named 'loadRoomBookings'
        BookingDetailManagementController controller = loader.getController();
        controller.loadRoomBookings(selectedRoomId);
        controller.setPrice(selectedBookingDTO.getPrice());
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        controller.setStart(start);
        controller.setEnd(end);

        // Show new scene
        Scene scene = new Scene(root);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
    }


    @FXML
    private void back() throws IOException {
        // Loading the FXML for the main view
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

         // Setting up the scene and showing the main view
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) tblHotels.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
