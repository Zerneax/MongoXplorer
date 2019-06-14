package org.javafx.controller;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.javafx.process.MongoProcess;
import org.javafx.process.UiProcess;

import java.io.IOException;
import java.util.List;

public class XplorerController {

    private UiProcess uiProcess;
    private String mongoUri;
    private String database;
    private MongoProcess mongoProcess;
    private Stage connectWindow;
    private Stage primaryStage;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnDisconnect;

    @FXML
    private Button btnExit;

    @FXML
    private Label bannerError;

    @FXML
    private VBox collections;

    @FXML
    private TableView<List<StringProperty>> visualize;

    @FXML
    private BorderPane nothing;

    @FXML
    private Label nothingLabel;


    public XplorerController() {
        this.uiProcess = new UiProcess();
        this.mongoProcess = null;

    }

    public void connect(String mongoUri, String database) throws IOException {

        this.mongoProcess = new MongoProcess(mongoUri, database);
        this.connectWindow.close();
        this.nothingLabel.setText("Select a collection on the left to see the content.");

        List<String> collectionsNames = this.mongoProcess.getAllCollectionNames();

        for(String collectionName: collectionsNames) {
            displayCollection(collectionName);
        }
        this.collections.setVisible(true);
        this.btnConnect.setVisible(false);
        this.btnDisconnect.setVisible(true);
    }

    @FXML
    protected void openConnectWindow(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(XplorerController.class.getResource("/fxml/connect.fxml"));
        Parent parentRoot = (Parent) fxmlLoader.load();
        ConnectController connectController = fxmlLoader.getController();

        Scene connectScene = new Scene(parentRoot);
        String test = this.getClass().getResource("/style/connect.css").toExternalForm();
        connectScene.getStylesheets().add(test);

        this.connectWindow = new Stage();
        this.connectWindow.setTitle("Connect");
        this.connectWindow.setScene(connectScene);
        this.connectWindow.setResizable(false);

        this.connectWindow.initModality(Modality.WINDOW_MODAL);

        this.primaryStage = (Stage) this.nothing.getScene().getWindow();
        this.connectWindow.initOwner(this.primaryStage);
        this.connectWindow.setX(this.primaryStage.getX() + (this.primaryStage.getWidth() / 2));
        this.connectWindow.setY(this.primaryStage.getY() + (this.primaryStage.getHeight() / 3));

        connectController.setPrimaryStage(this.primaryStage);
        connectController.setController(this);

        this.connectWindow.show();

    }

    @FXML
    protected void disconnect(ActionEvent event) {
        this.mongoProcess.disconnect();

        // clear input value
        //this.hostField.clear();
        //this.portField.clear();
        //this.databaseField.clear();

        // clear left panel
        this.collections.getChildren().clear();

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
        this.collections.getChildren().add(vbox);
    }

    private void resetActiveCollection() {
        for(Node vb: this.collections.getChildren()) {
            VBox vbox = (VBox) vb;
            for(Node label: vbox.getChildren())
                label.setStyle("-fx-text-fill: white;");
        }
    }





}
