package comp603.project2.controllers;

import comp603.project2.dao.HotelDAO;
import comp603.project2.models.Hotel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class HotelDialogController {

    @FXML
    private TextField hotelNameField, locationField, starRatingField, descriptionField;
    @FXML
    private Label hotelIDLabel;
    private boolean addingMode = false;

    private HotelDAO hotelDAO;
    private Hotel currentHotel;

    @FXML
    public void initialize() {
        hotelDAO = new HotelDAO();
    }

    public void setHotel(Hotel hotel) {
        if(hotel != null) {
            // Editing mode
            currentHotel = hotel;
            hotelIDLabel.setText(String.valueOf(hotel.getHotelId()));
            hotelNameField.setText(hotel.getHotelName());
            locationField.setText(hotel.getLocation());
            starRatingField.setText(String.valueOf(hotel.getStarRating()));
            descriptionField.setText(hotel.getDescription());
        } else {
            // Adding mode
            addingMode = true;
            currentHotel = new Hotel();
            int newId = hotelDAO.generateNewId(); // Method to generate a new ID
            hotelIDLabel.setText(String.valueOf(newId));
            currentHotel.setHotelId(newId);
        }
    }

    @FXML
    private void save() throws SQLException {
        // Update the hotel object
        currentHotel.setHotelName(hotelNameField.getText());
        currentHotel.setLocation(locationField.getText());
        currentHotel.setStarRating(Integer.parseInt(starRatingField.getText()));
        currentHotel.setDescription(descriptionField.getText());
        if(addingMode) {
            hotelDAO.addHotel(currentHotel);
        } else {
            hotelDAO.updateHotel(currentHotel);
        }
        closeDialog();
    }

    @FXML
    private void cancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) hotelNameField.getScene().getWindow();
        stage.close();
    }
}
