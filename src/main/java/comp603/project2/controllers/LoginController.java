package comp603.project2.controllers;

import comp603.project2.dao.UserDAO;
import comp603.project2.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> handleLoginButton());
    }

    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = userDAO.findByUsernameAndPassword(username, password);// Attempting to find a user with the provided username and password
            if (user != null) {
                
                loadMainView();
            } else {
                // If no user is found, show an error dialog
                showErrorDialog("Invalid username or password.");
            }
        } catch (SQLException ex) {
            // Handle SQL or other exceptions
            ex.printStackTrace();
            showErrorDialog("An error occurred while trying to log in. Please try again later.");
        }
    }

    private void loadMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Stage mainStage = new Stage();
            mainStage.setScene(new Scene(loader.load()));
            mainStage.setTitle("Main View");
            mainStage.show();

            
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Error loading the main view. Please try again later.");
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
