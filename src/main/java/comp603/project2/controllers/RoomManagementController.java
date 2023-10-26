package comp603.project2.controllers;

import comp603.project2.dao.HotelDAO;
import comp603.project2.dao.RoomDAO;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RoomManagementController {

    @FXML
    private ComboBox<Hotel> cmbHotels;
    @FXML
    private TableView<Room> tblHotels;
    private Stage dialogStage;
    private RoomDAO roomDAO;
    private HotelDAO hotelDAO;
    private Hotel selectedHotel;
    @FXML
    private TableColumn<Room, Integer> colRoomId;
    @FXML
    private TableColumn<Room, String> colRoomType;
    @FXML
    private TableColumn<Room, Double> colPrice;
    @FXML
    private TableColumn<Room, Integer> colCapacity;
    @FXML
    public void initialize() {
        roomDAO = new RoomDAO();
        hotelDAO = new HotelDAO();
        // Initialize the table columns
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        loadHotelsToComboBox();
    }
    private void loadHotelsToComboBox() {
        ObservableList<Hotel> hotels = hotelDAO.getAllHotels();
        cmbHotels.setItems(hotels);
    }


    @FXML
    //Searches for rooms of the selected hotel and updates the table view.
    private void searchRoomsByHotel() throws SQLException {
        selectedHotel = cmbHotels.getSelectionModel().getSelectedItem();
        if (selectedHotel != null) {
            int selectedHotelId = selectedHotel.getHotelId();
            ObservableList<Room> rooms = FXCollections.observableArrayList(roomDAO.getAllRoomsByHotelId(selectedHotelId));
            tblHotels.setItems(rooms);
        } else {
            // Handle case where no hotel is selected if necessary
            tblHotels.setItems(FXCollections.observableArrayList());
        }
    }


    private void showRoomDialog(Room room) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/RoomDialog.fxml"));
        AnchorPane page = loader.load();

        dialogStage = new Stage();
        dialogStage.setTitle("Edit Room");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        RoomDialogController controller = loader.getController();
        controller.setRoom(room,selectedHotel);

        dialogStage.showAndWait();

        // Refresh the rooms
        searchRoomsByHotel();
    }

    @FXML
    private void addRoom() throws IOException, SQLException {
        if (selectedHotel == null) {
            showAlert("Warning", "Please select a hotel first!");
            return;
        }
        showRoomDialog(null);
    }



    @FXML
    private void updateRoom() throws SQLException, IOException {
        Room selectedRoom = tblHotels.getSelectionModel().getSelectedItem();
        if (selectedRoom != null) {
            showRoomDialog(selectedRoom);
        } else {
            showAlert("Warning","Please select a room first!");
        }
    }

    @FXML
    private void deleteRoom() throws SQLException {
        Room selectedRoom = tblHotels.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Warning", "Please select a room first!");
            return;
        }
        roomDAO.deleteRoom(selectedRoom.getRoomId());
        searchRoomsByHotel();
    }

    @FXML
    private void back() throws IOException {
        // 加载Main.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));

        // 设置新的场景并显示
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) tblHotels.getScene().getWindow(); // 获取当前窗口
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
