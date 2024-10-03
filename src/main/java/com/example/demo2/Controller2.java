package com.example.demo2;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Controller2 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button add1;

    @FXML
    private Button back;

    @FXML
    private TableView<ObservableList<String>> tableView;

    @FXML
    private Button read1;

    @FXML
    private Button remove1;

    @FXML
    private Button tables1;

    @FXML
    private Button update1;

    private String selectedTable; // Переменная для хранения выбранной таблицы

    @FXML
    void initialize() {
        back.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/mainPage.fxml"));
                Parent root = loader.load();
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
                back.getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tables1.setOnAction(event -> {
            try {
                // Получение соединения с базой данных
                Connection connection = DatabaseConnector.connect();

                // Получение списка таблиц
                List<String> tableNames = DatabaseUtils.getTableNames(connection);
                connection.close();

                // Создание диалога для выбора таблицы
                ChoiceDialog<String> dialog = new ChoiceDialog<>(tableNames.get((0)), tableNames);
                dialog.setTitle("Выбор таблицы");
                dialog.setHeaderText("Выберите таблицу из базы данных");
                dialog.setContentText("Таблица:");

                dialog.showAndWait().ifPresent(selected -> {
                    selectedTable = selected; // Сохранение выбранной таблицы
                    tables1.setText(selectedTable); // Обновление текста кнопки
                });

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        read1.setOnAction(event -> {
            if (selectedTable != null) {
                loadTableData(selectedTable);
            } else {
                System.out.println("Таблица не выбрана");
            }
        });

        add1.setOnAction(event -> {
            if (selectedTable == null) {
                System.out.println("Сначала выберите таблицу!");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/AddData.fxml"));
                Parent root = loader.load();

                // Получаем контроллер для передачи имени таблицы
                AddDataController addDataController = loader.getController();
                addDataController.setTableName(selectedTable);  // Передаем выбранную таблицу

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        update1.setOnAction(event -> {
            if (selectedTable == null) {
                System.out.println("Сначала выберите таблицу!");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/UpdateData.fxml"));
                Parent root = loader.load();

                // Получаем контроллер для передачи имени таблицы
                UpdateDataController updateDataController = loader.getController();
                updateDataController.setTableName(selectedTable);  // Передаем выбранную таблицу

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        remove1.setOnAction(event -> {
            if (selectedTable == null) {
                System.out.println("Сначала выберите таблицу!");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo2/RemoveData.fxml"));
                Parent root = loader.load();

                // Получаем контроллер для передачи имени таблицы
                RemoveDataController deleteDataController = loader.getController();
                deleteDataController.setTableName(selectedTable);  // Передаем выбранную таблицу

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
    private void loadTableData(String tableName) {
        tableView.getItems().clear();  // Очищаем таблицу перед загрузкой новых данных
        tableView.getColumns().clear(); // Очищаем колонки перед загрузкой новых

        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement();
             // Измените запрос, добавив сортировку по столбцу id или другому уникальному идентификатору
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " ORDER BY id")) {

            // Получаем метаданные результата
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Создаем колонки динамически на основе метаданных
            for (int i = 1; i <= columnCount; i++) {
                final int columnIndex = i;
                TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(i));
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(columnIndex - 1)));
                tableView.getColumns().add(column);
            }

            // Заполняем строки данными
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }

            // Устанавливаем данные в таблицу
            tableView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}