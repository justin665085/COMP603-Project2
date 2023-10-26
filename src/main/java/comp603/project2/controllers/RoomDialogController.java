package comp603.project2.controllers;

import comp603.project2.dao.RoomDAO;
import comp603.project2.models.Hotel;
import comp603.project2.models.Room;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RoomDialogController {

    @FXML
    private TextField roomTypeField, priceField, capacityField;
    @FXML
    private Label roomIdLabel, hotelIdLabel;
    private boolean addingMode = false;

    private RoomDAO roomDAO;
    private Room currentRoom;

    @FXML
    public void initialize() {
        roomDAO = new RoomDAO();
    }

    public void setRoom(Room room, Hotel hotel) {
        if(room != null) {
            // Editing mode
            currentRoom = room;
            roomIdLabel.setText(String.valueOf(room.getRoomId()));
            hotelIdLabel.setText(String.valueOf(hotel.getHotelId()));
            roomTypeField.setText(room.getRoomType());
            priceField.setText(String.valueOf(room.getPricePerNight()));
            capacityField.setText(String.valueOf(room.getCapacity()));
        } else {
            // Adding mode
            addingMode = true;
            roomIdLabel.setText("Auto Generated");
            hotelIdLabel.setText(String.valueOf(hotel.getHotelId()));
            currentRoom = new Room();
            currentRoom.setHotelId(hotel.getHotelId());
        }
    }

    @FXML
    private void save() throws SQLException {
        // Update the room object
        currentRoom.setRoomType(roomTypeField.getText());
        currentRoom.setPricePerNight(Double.parseDouble(priceField.getText()));
        currentRoom.setCapacity(Integer.parseInt(capacityField.getText()));
        if(addingMode) {
            roomDAO.addRoom(currentRoom);
        } else {
            roomDAO.updateRoom(currentRoom);
        }
        closeDialog();
    }

    @FXML
    private void cancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) roomTypeField.getScene().getWindow();
        stage.close();
    }
}
