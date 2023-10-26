package comp603.project2.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class MainController {

    @FXML
    private Button btnHotelManagement;
    @FXML
    private Button btnRoomManagement;
    @FXML
    private Button btnBookingManagement;
    @FXML
    private Button btnReviewManagement;
    @FXML
    private Button btnLogout;

    @FXML
    public void handleHotelManagement() throws Exception {
        changeScene("HotelManagement.fxml");
    }

    @FXML
    public void handleRoomManagement() throws Exception {
        changeScene("RoomManagement.fxml");
    }

    @FXML
    public void handleBookingManagement() throws Exception {
        changeScene("BookingManagement.fxml");
    }

    @FXML
    public void handleReviewManagement() throws Exception {
        changeScene("ReviewManagement.fxml");
    }

    @FXML
    public void handleLogout() throws Exception {
        changeScene("Login.fxml");
    }

    private void changeScene(String fxml) throws Exception {
        Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/" + fxml)));
        Stage currentStage = (Stage) btnLogout.getScene().getWindow();
        currentStage.setScene(newScene);
    }
}
