package com.example.demo2;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Request4Controller {

    @FXML
    private TextField roomNumberField;

    @FXML
    private TextField osVersionField;

    private TableView<Map<String, Object>> resultTable;

    // Привязка таблицы из Controller3
    public void setResultTable(TableView<Map<String, Object>> resultTable) {
        this.resultTable = resultTable;
    }

    @FXML
    void executeRequest() {
        String roomNumber = roomNumberField.getText();
        String osVersion = osVersionField.getText();

        if (roomNumber == null || roomNumber.isEmpty() || osVersion == null || osVersion.isEmpty()) {
            System.out.println("Пожалуйста, введите номер комнаты и версию ОС");
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {

            // Подготовка SQL-запроса
            String query = "SELECT c.* FROM Компьютер c " +
                    "JOIN Комната k ON c.FK_Комната = k.id " +
                    "JOIN Операционная_система os ON c.FK_Операционная_система = os.id " +
                    "WHERE k.номер = " + roomNumber + " " +
                    "AND os.название = '" + osVersion + "'";

            ResultSet rs = stmt.executeQuery(query);

            ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

            // Очистка старых столбцов и данных
            resultTable.getColumns().clear();
            resultTable.getItems().clear();

            // Получение метаданных для создания столбцов
            int columnCount = rs.getMetaData().getColumnCount();

            // Добавление данных
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                data.add(row);
            }

            // Создание столбцов автоматически
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnName(i);
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> {
                    Map<String, Object> rowData = cellData.getValue();
                    return new SimpleObjectProperty<>(rowData.get(columnName));
                });
                resultTable.getColumns().add(column);
            }

            resultTable.setItems(data);

            // Закрыть окно после выполнения запроса
            Stage stage = (Stage) roomNumberField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}