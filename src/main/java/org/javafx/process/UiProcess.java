package org.javafx.process;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UiProcess {

    private ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();

    public UiProcess() {
    }

    public ObservableList<List<StringProperty>> insertValueInTableView(List<String> documents, TableView<List<StringProperty>> tableView) {
        // clear content of tableview
        this.data = FXCollections.observableArrayList();
        tableView.getColumns().clear();

        // add columns
        this.buildTableView(documents.get(0), tableView);

        // add row
        for(String document: documents) {
            List<StringProperty> row = this.createRowFromJson(document);
            data.add(row);
        }

        return data;
    }

    public void buildTableView(String json, TableView<List<StringProperty>> tableView) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(json);
            int numberOfColumns = jsonObject.keySet().size();
            int i = 0;
            for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext(); i ++) {

                String key = (String) iterator.next();
                TableColumn<List<StringProperty>, String> column = new TableColumn<>(key);
                column.setPrefWidth(tableView.getWidth() / numberOfColumns);
                final int j = i;

                column.setCellValueFactory(data -> data.getValue().get(j));//
                tableView.getColumns().add(column);
            }

        } catch (ParseException e) {
            this.showAlert("An error occured !");
        }
    }

    public List<StringProperty> createRowFromJson(String json){
        JSONParser parser = new JSONParser();
        List<StringProperty> firstRow = new ArrayList<>();
        try {
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            for(Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                Object value = jsonObject.get(key);
                firstRow.add(new SimpleStringProperty("" + value));
            }

        } catch (ParseException e) {
            this.showAlert("An error occured !");
        }

        return firstRow;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
