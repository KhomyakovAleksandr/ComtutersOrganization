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

public class Request1Controller {

    @FXML
    private TextField supplierField;

    private TableView<Map<String, Object>> resultTable;

    // Метод для привязки таблицы из Controller3
    public void setResultTable(TableView<Map<String, Object>> resultTable) {
        this.resultTable = resultTable;
    }

    @FXML
    void executeRequest() {
        String supplier = supplierField.getText();

        if (supplier == null || supplier.isEmpty()) {
            System.out.println("Пожалуйста, введите поставщика");
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM Монитор WHERE поставщик = '" + supplier + "'";
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
            Stage stage = (Stage) supplierField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



