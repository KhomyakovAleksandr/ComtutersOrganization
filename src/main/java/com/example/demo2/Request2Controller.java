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

public class Request2Controller {

    @FXML
    private TextField memoryField;

    private TableView<Map<String, Object>> resultTable;

    // Привязка таблицы из Controller3
    public void setResultTable(TableView<Map<String, Object>> resultTable) {
        this.resultTable = resultTable;
    }

    @FXML
    void executeRequest() {
        String memory = memoryField.getText();

        if (memory == null || memory.isEmpty()) {
            System.out.println("Пожалуйста, введите объем видеокарты");
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {

            // Подготовка SQL-запроса
            String query = "SELECT " +
                    "c.id, " +
                    "c.тип_компьютера AS название_компьютера, " +
                    "os.название AS операционная_система, " +
                    "monitor.марка AS марка_монитора, " +
                    "processor.марка AS марка_процессора, " +
                    "memory.объем AS объем_ОЗУ, " +
                    "videocard.модель AS модель_видеокарты, " +
                    "room.номер AS номер_комнаты " +
                    "FROM Компьютер c " +
                    "JOIN Видеокарта videocard ON c.FK_Видеокарта = videocard.id " +
                    "JOIN Операционная_система os ON c.FK_Операционная_система = os.id " +
                    "JOIN Монитор monitor ON c.FK_Монитор = monitor.id " +
                    "JOIN Процессор processor ON c.FK_Процессор = processor.id " +
                    "JOIN Память_ОЗУ memory ON c.FK_Память = memory.id " +
                    "JOIN Комната room ON c.FK_Комната = room.id " +
                    "WHERE videocard.объем_памяти > " + memory;

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
            Stage stage = (Stage) memoryField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
