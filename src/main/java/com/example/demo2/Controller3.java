package com.example.demo2;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Controller3 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back2;

    @FXML
    private Button request1;

    @FXML
    private Button request2;

    @FXML
    private TableView<Map<String, Object>> resultTable; // Измените тип на Map<String, Object>

    @FXML
    private Button request3;

    @FXML
    private Button request4;

    @FXML
    private Button request5;

    @FXML
    void initialize() {
        // Кнопка "Назад"
        back2.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/mainPage.fxml"));
                Parent root = loader.load();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
                back2.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        request1.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/request1.fxml"));
                Parent root = loader.load();

                // Установка текущей таблицы в Request1Controller
                Request1Controller request1Controller = loader.getController();
                request1Controller.setResultTable(resultTable); // Передача resultTable

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        request2.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/request2.fxml"));
                Parent root = loader.load();

                // Установка текущей таблицы в Request2Controller
                Request2Controller request2Controller = loader.getController();
                request2Controller.setResultTable(resultTable); // Передача resultTable

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        request3.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/request3.fxml"));
                Parent root = loader.load();

                // Установка текущей таблицы в Request3Controller
                Request3Controller request3Controller = loader.getController();
                request3Controller.setResultTable(resultTable); // Передача resultTable

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        request4.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/request4.fxml"));
                Parent root = loader.load();

                // Установка текущей таблицы в Request4Controller
                Request4Controller request4Controller = loader.getController();
                request4Controller.setResultTable(resultTable); // Передача resultTable

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        request5.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/request5.fxml"));
                Parent root = loader.load();

                // Установка текущей таблицы в Request5Controller
                Request5Controller request5Controller = loader.getController();
                request5Controller.setResultTable(resultTable); // Передача resultTable

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}



