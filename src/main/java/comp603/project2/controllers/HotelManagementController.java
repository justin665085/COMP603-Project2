package comp603.project2.controllers;

import comp603.project2.dao.HotelDAO;
import comp603.project2.models.Hotel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HotelManagementController {

    @FXML
    private TableView<Hotel> hotelTable;
    @FXML
    private TableColumn<Hotel, Integer> hotelIDCol;
    @FXML
    private TableColumn<Hotel, String> hotelNameCol;
    @FXML
    private TableColumn<Hotel, String> locationCol;
    @FXML
    private TableColumn<Hotel, Integer> starRatingCol;
    @FXML
    private TableColumn<Hotel, String> descriptionCol;

    private HotelDAO hotelDAO;

    @FXML
    public void initialize() {
        hotelDAO = new HotelDAO(); // Assume you have a HotelDAO class

        // Initialize the columns with the correct properties
        hotelIDCol.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        hotelNameCol.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        starRatingCol.setCellValueFactory(new PropertyValueFactory<>("starRating"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadHotels();
    }

    private void loadHotels() {
        // Fetch the list of hotels from the database and add them to the table
        hotelTable.setItems(hotelDAO.getAllHotels());
    }

    @FXML
    private void deleteHotel() throws SQLException {
        Hotel selectedHotel = hotelTable.getSelectionModel().getSelectedItem();
        if(selectedHotel == null) {
            showAlert("Please select a hotel to delete.");
            return;
        }
        hotelDAO.deleteHotel(selectedHotel);
        loadHotels();
    }

    @FXML
    private void addHotel() {
        // Show the dialog with null as the parameter to indicate adding
        showAddOrEditDialog(null);
    }

    @FXML
    private void updateHotel() {
        Hotel selectedHotel = hotelTable.getSelectionModel().getSelectedItem();
        if(selectedHotel == null) {
            showAlert("Please select a hotel to update.");
            return;
        }
        showAddOrEditDialog(selectedHotel);
    }

    private void showAddOrEditDialog(Hotel hotel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HotelDialog.fxml")); // 修改为你的FXML路径
            Parent root = loader.load();
            HotelDialogController controller = loader.getController();
            controller.setHotel(hotel);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.setTitle(hotel == null ? "Add Hotel" : "Edit Hotel");

            
            dialogStage.showAndWait();

            // Reload hotels to reflect any changes made in the dialog
            loadHotels();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening the dialog.");
        }
    }

    // Method to handle going back to the main screen
    @FXML
    private void goBack() throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

        
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) hotelTable.getScene().getWindow();
        primaryStage.setScene(scene);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
