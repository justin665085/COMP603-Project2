package comp603.project2;

import comp603.project2.dao.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Project2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    static {
        try {
            DatabaseInitializer.initialize();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Hotel Booking System - Login");

        // 加载 Login.fxml 作为主场景
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
}
