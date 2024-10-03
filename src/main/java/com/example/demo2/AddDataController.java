package com.example.demo2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddDataController {

    @FXML
    private VBox fieldsContainer; // Контейнер для динамически создаваемых полей

    @FXML
    private Button submitButton;

    private String tableName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
        loadFields();
    }

    private void loadFields() {
        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1")) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Создаем текстовые поля для каждой колонки
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                TextField textField = new TextField();
                textField.setPromptText(columnName);
                textField.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5; -fx-font-size: 14;"); // Устанавливаем стиль для рамки

                // Создаем HBox для обертывания TextField
                HBox fieldContainer = new HBox();

                // Добавляем текстовое поле в HBox
                fieldContainer.getChildren().add(textField);
                fieldsContainer.getChildren().add(fieldContainer); // Добавляем HBox в VBox
            }

            // Обработка события нажатия кнопки отправки
            submitButton.setOnAction(event -> submitData());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitData() {
        ObservableList<String> values = FXCollections.observableArrayList();

        // Получаем значения из всех текстовых полей
        for (var node : fieldsContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                for (var fieldNode : hBox.getChildren()) {
                    if (fieldNode instanceof TextField) {
                        values.add(((TextField) fieldNode).getText());
                    }
                }
            }
        }

        // Здесь вы можете добавить логику для вставки данных в выбранную таблицу
        insertDataIntoTable(values);
    }

    private void insertDataIntoTable(ObservableList<String> values) {
        // Здесь вам нужно реализовать логику для вставки данных в базу данных
        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement()) {

            StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " VALUES (");

            for (int i = 0; i < values.size(); i++) {
                query.append("'").append(values.get(i)).append("'");
                if (i < values.size() - 1) {
                    query.append(", ");
                }
            }
            query.append(")");

            statement.executeUpdate(query.toString());
            System.out.println("Данные успешно добавлены!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


