package comp603.project2.controllers;
import comp603.project2.dao.BookingDAO;
import comp603.project2.dao.RoomDAO;
import comp603.project2.models.Booking;
import comp603.project2.models.Room;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;
public class BookingDialogController {

    private boolean isAdd = false;
    private Stage dialogStage;
    @FXML
    private Label autofillRoomIdLabel, autofillHotelIdLabel, autofillRoomTypeLabel, autofillPriceLabel, autofillBookingIdLabel, autofillStartDateLabel, autofillEndDateLabel, autofillBookingDateLabel;

    @FXML
    private TextField usernameTextField, phoneNumberTextField;

    @FXML
    private Button saveButton, cancelButton;

    private BookingDAO bookingDAO = new BookingDAO();
    private Booking currentBooking;
    private Room currentRoom;



    public void initialize() {
        // You can add any necessary initialization code here
    }

    public void setBooking(Booking booking) throws SQLException {
        this.currentBooking = booking;
        if(booking.getBookingId() == -1){
            isAdd = true;
            autofillBookingIdLabel.setText("Auto Generate");
        }else {
            autofillRoomIdLabel.setText(String.valueOf(booking.getBookingId()));
            usernameTextField.setText(booking.getUsername());
            phoneNumberTextField.setText(booking.getPhoneNumber());
        }

        // Populate other fields as required
        autofillEndDateLabel.setText(booking.getEndDate().toString());
        autofillStartDateLabel.setText(booking.getStartDate().toString());
        autofillBookingDateLabel.setText(booking.getBookingDate().toString());
        autofillPriceLabel.setText(String.valueOf(booking.getPrice()));
        autofillRoomIdLabel.setText(String.valueOf(booking.getRoomId()));
        Room room = new RoomDAO().getRoomById(booking.getRoomId());
        autofillRoomTypeLabel.setText(room.getRoomType());
    }

    public void setRoom(Room room){
        this.currentRoom = room;
    }

    @FXML
    private void saveBooking(ActionEvent event) {
        // Add or update booking as necessary
        if (isAdd) {
            // Set fields from UI
            currentBooking.setUsername(usernameTextField.getText());
            currentBooking.setPhoneNumber(phoneNumberTextField.getText());
            try {
                // Save to DB using DAO
                // Note: You might need an 'addBooking' method in the DAO
                bookingDAO.addBooking(currentBooking);
            } catch (SQLException e) {
                // Handle the error
                e.printStackTrace();
            }
        } else {
            // Update existing booking
            // Set fields from UI
            currentBooking.setUsername(usernameTextField.getText());
            currentBooking.setPhoneNumber(phoneNumberTextField.getText());
            try {
                // Update in DB using DAO
                // Note: You might need an 'updateBooking' method in the DAO
                bookingDAO.updateBooking(currentBooking);
            } catch (SQLException e) {
                // Handle the error
                e.printStackTrace();
            }
        }
        closeDialog();
    }

    @FXML
    private void cancelBooking(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
