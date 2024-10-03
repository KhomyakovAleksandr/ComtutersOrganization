package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveDataController {

    @FXML
    private TextField idField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private String selectedTable;

    @FXML
    void initialize() {
        deleteButton.setOnAction(event -> {
            try {
                int id = Integer.parseInt(idField.getText());
                deleteRecordFromDatabase(selectedTable, id);
            } catch (NumberFormatException e) {
                showAlert("Ошибка", "Пожалуйста, введите корректный числовой ID.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        cancelButton.setOnAction(event -> {
            // Закрываем текущее окно
            ((Stage) cancelButton.getScene().getWindow()).close();
        });
    }

    public void setTableName(String tableName) {
        this.selectedTable = tableName;
    }

    private void deleteRecordFromDatabase(String tableName, int id) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?"; // Убедитесь, что 'id' - это имя столбца

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Успех", "Запись успешно удалена.");
            } else {
                showAlert("Ошибка", "Запись с таким ID не найдена.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
