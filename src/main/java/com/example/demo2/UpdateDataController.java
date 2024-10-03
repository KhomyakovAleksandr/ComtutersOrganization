package com.example.demo2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.*;

public class UpdateDataController {

    @FXML
    private VBox fieldsContainer; // Контейнер для полей и названий колонок

    @FXML
    private TextField idField; // Поле для ввода ID

    @FXML
    private Button submitButton; // Кнопка для подтверждения изменений

    @FXML
    private Button loadButton; // Кнопка для загрузки данных

    private String tableName; // Выбранная таблица
    private ObservableList<TextField> textFields = FXCollections.observableArrayList(); // Список текстовых полей

    // Привязка кнопки к методу
    @FXML
    private void initialize() {
        loadButton.setOnAction(event -> loadDataForUpdate());
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void loadDataForUpdate() {
        System.out.println("Загрузка данных для обновления...");

        String id = idField.getText();
        if (id.isEmpty()) {
            System.out.println("Введите ID для изменения данных!");
            return;
        }

        fieldsContainer.getChildren().clear(); // Очищаем контейнер перед загрузкой данных

        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id = " + id)) {

            if (resultSet.next()) {
                System.out.println("Запись найдена, создаем поля...");

                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Создаем метки для названий колонок и текстовые поля для ввода данных
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);

                    // Создаем метку для названия колонки
                    Label label = new Label(columnName);
                    fieldsContainer.getChildren().add(label);

                    // Создаем текстовое поле для ввода значения
                    TextField textField = new TextField(columnValue);
                    fieldsContainer.getChildren().add(textField);
                    textFields.add(textField); // Добавляем текстовое поле в список
                }

                // Обработка нажатия кнопки для обновления данных
                submitButton.setOnAction(event -> updateData(id));

            } else {
                System.out.println("Запись с таким ID не найдена.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateData(String id) {
        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement()) {

            StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");

            ResultSetMetaData metaData;
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE id = " + id)) {
                metaData = resultSet.getMetaData();
            }

            for (int i = 0; i < textFields.size(); i++) {
                String columnName = metaData.getColumnName(i + 1);
                String newValue = textFields.get(i).getText();

                // Если текстовое поле не пустое, добавляем изменение
                if (!newValue.isEmpty()) {
                    updateQuery.append(columnName).append(" = '").append(newValue).append("'");
                    if (i < textFields.size() - 1) {
                        updateQuery.append(", ");
                    }
                }
            }

            updateQuery.append(" WHERE id = ").append(id);

            // Выполняем обновление
            statement.executeUpdate(updateQuery.toString());
            System.out.println("Данные успешно обновлены!");

            // Перезагружаем данные таблицы в интерфейсе (без изменения порядка)
            reloadTableData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void reloadTableData() {
        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " ORDER BY id")) {

            // Очистка и перезагрузка данных в TableView (если используется)
            // tableView.getItems().clear();

            while (resultSet.next()) {
                // Логика обновления данных в интерфейсе
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
