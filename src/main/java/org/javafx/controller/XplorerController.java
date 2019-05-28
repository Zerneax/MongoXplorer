package org.javafx.controller;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.commons.lang3.StringUtils;
import org.javafx.process.MongoProcess;
import org.javafx.process.UiProcess;

import java.util.List;

public class XplorerController {

    private UiProcess uiProcess;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField databaseField;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnDisconnect;

    @FXML
    private Button btnExit;

    @FXML
    private Label bannerError;

    @FXML
    private VBox test;

    @FXML
    private TableView<List<StringProperty>> visualize;

    @FXML
    private BorderPane nothing;

    @FXML
    private Label nothingLabel;

    private MongoProcess mongoProcess;

    public XplorerController() {
        this.uiProcess = new UiProcess();
        this.mongoProcess = null;
    }

    @FXML
    protected void connect(ActionEvent event) {
        if(this.checkInput())
            return;

        this.mongoProcess = new MongoProcess(this.hostField.getText(), Integer.parseInt(this.portField.getText()), this.databaseField.getText());
        this.nothingLabel.setText("Select a collection on the left to see the content.");

        List<String> collectionsNames = this.mongoProcess.getAllCollectionNames();

        for(String collectionName: collectionsNames) {
            displayCollection(collectionName);
        }
        this.test.setVisible(true);
        this.btnConnect.setVisible(false);
        this.btnDisconnect.setVisible(true);
    }

    @FXML
    protected void disconnect(ActionEvent event) {
        this.mongoProcess.disconnect();

        // clear input value
        //this.hostField.clear();
        //this.portField.clear();
        //this.databaseField.clear();

        // clear left panel
        this.test.getChildren().clear();

        // clear tableview
        this.visualize.getItems().clear();
        this.visualize.getColumns().clear();
        this.visualize.refresh();
        this.visualize.setVisible(false);

        // display standard view
        this.nothing.setVisible(true);
        this.nothingLabel.setVisible(true);
        this.nothingLabel.setText("Connect to Mongo database and explore it.");

        this.btnConnect.setVisible(true);
        this.btnDisconnect.setVisible(false);
    }

    @FXML
    protected void exit(ActionEvent event) {
        Platform.exit();
    }

    private boolean checkInput() {
        this.bannerError.setVisible(false);
        if(this.hostField.getText().isEmpty()) {
            this.bannerError.setText("The Host is empty !");
            this.bannerError.setVisible(true);
            return true;
        }

        if(this.portField.getText().isEmpty()) {
            this.bannerError.setText("The Port is empty !");
            this.bannerError.setVisible(true);
            return true;
        }

        if(!StringUtils.isNumeric(this.portField.getText())) {
            this.bannerError.setText("The Port must be numeric !");
            this.bannerError.setVisible(true);
            return true;
        }


        if(this.databaseField.getText().isEmpty()) {
            this.bannerError.setText("The Database name is empty !");
            this.bannerError.setVisible(true);
            return true;
        }

        return false;
    }

    private void displayCollection(String collectionName) {
        Label label = new Label(collectionName);
        label.setFont(new Font(18));
        label.setId("collectionName");
        label.setOnMouseClicked(clickEvent -> {
            this.resetActiveCollection();
            label.setStyle("-fx-text-fill: #5E5A80;");
            List<String> documents = this.mongoProcess.getAllEntriesOfCollection(label.getText());
            if(documents.size() > 0) {
                this.visualize.setItems(this.uiProcess.insertValueInTableView(documents, this.visualize));
                this.visualize.setVisible(true);
                this.nothing.setVisible(false);
            } else {
                this.visualize.setVisible(false);
                this.nothing.setVisible(true);
                this.nothingLabel.setText("This collection is empty !");
            }


        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        this.test.getChildren().add(vbox);
    }

    private void resetActiveCollection() {
        for(Node vb: this.test.getChildren()) {
            VBox vbox = (VBox) vb;
            for(Node label: vbox.getChildren())
                label.setStyle("-fx-text-fill: white;");
        }
    }





}
