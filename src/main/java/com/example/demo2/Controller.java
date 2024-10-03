package com.example.demo2;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button CRUD1;

    @FXML
    private Button requests1;

    @FXML
    void initialize() {
        CRUD1.setOnAction(event -> {
            try {
                // Загрузка нового окна (CRUD.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/CRUD.fxml"));
                Parent root = loader.load();

                // Создание новой сцены и открытие нового окна
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();

                // Закрытие текущего окна
                CRUD1.getScene().getWindow().hide();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        requests1.setOnAction(event -> {
            try {

                // Загрузка нового окна
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/requestsPage.fxml"));
                Parent root = loader.load();

                // Создание новой сцены и открытие нового окна
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();

                requests1.getScene().getWindow().hide();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}

