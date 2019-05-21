package org.javafx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.javafx.process.MongoProcess;

import java.util.List;

public class XplorerController {

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField databaseField;

    @FXML
    private Button btnConnect;

    @FXML
    private Button btnExit;

    @FXML
    private Label bannerError;

    @FXML
    private VBox test;

    @FXML
    private TextArea visualize;

    private MongoProcess mongoProcess;

    @FXML
    protected void connect(ActionEvent event) {
        this.bannerError.setVisible(false);
        if(this.hostField.getText().isEmpty()) {
            //showAlert("The Host is empty !");
            this.bannerError.setText("The Host is empty !");
            this.bannerError.setVisible(true);
            return;
        }

        if(this.portField.getText().isEmpty()) {
            //showAlert("The Port is empty !");
            this.bannerError.setText("The Port is empty !");
            this.bannerError.setVisible(true);
            return;
        }

        if(this.databaseField.getText().isEmpty()) {
            //showAlert("The Port is empty !");
            this.bannerError.setText("The Database name is empty !");
            this.bannerError.setVisible(true);
            return;
        }


        //this.visualize.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.mongoProcess = new MongoProcess(this.hostField.getText(), Integer.parseInt(this.portField.getText()), this.databaseField.getText());

        List<String> collectionsNames = this.mongoProcess.getAllCollectionNames();

        for(String collectionName: collectionsNames) {
            Label label = new Label(collectionName);
            label.setOnMouseClicked(clickEvent -> {
                this.visualize.setText("");
                List<String> documents = this.mongoProcess.getAllEntriesOfCollection(label.getText());
                for(String document: documents) {
                    this.visualize.setText(this.visualize.getText() + document + "\n");
                }
                this.visualize.setVisible(true);

            });

            this.test.getChildren().add(label);
        }
        this.test.setVisible(true);



        //Region contentTextArea = (Region) this.visualize.lookup(".content");
        //contentTextArea.setStyle("-fx-background-color: white;");
    }

    @FXML
    protected void exit(ActionEvent event) {
        Platform.exit();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        //alert.initOwner(this.btnConnect.getScene().getWindow());
        alert.showAndWait();
    }
}
