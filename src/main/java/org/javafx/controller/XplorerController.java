package org.javafx.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.apache.commons.lang3.StringUtils;
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

    @FXML
    private BorderPane nothing;

    @FXML
    private Label nothingLabel;

    private MongoProcess mongoProcess;

    @FXML
    protected void connect(ActionEvent event) {
        this.checkInput();

        //this.visualize.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.mongoProcess = new MongoProcess(this.hostField.getText(), Integer.parseInt(this.portField.getText()), this.databaseField.getText());
        this.nothingLabel.setText("Select a collection on the left to see the content.");

        List<String> collectionsNames = this.mongoProcess.getAllCollectionNames();

        for(String collectionName: collectionsNames) {
            displayCollection(collectionName);
        }
        this.test.setVisible(true);
    }

    @FXML
    protected void exit(ActionEvent event) {
        Platform.exit();
    }

    private void checkInput() {
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

        if(!StringUtils.isNumeric(this.portField.getText())) {
            this.bannerError.setText("The Port must be numeric !");
            this.bannerError.setVisible(true);
            return;
        }


        if(this.databaseField.getText().isEmpty()) {
            //showAlert("The Port is empty !");
            this.bannerError.setText("The Database name is empty !");
            this.bannerError.setVisible(true);
            return;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error !");
        alert.setHeaderText(null);
        alert.setContentText(message);
        //alert.initOwner(this.btnConnect.getScene().getWindow());
        alert.showAndWait();
    }

    private void displayCollection(String collectionName) {
        Label label = new Label(collectionName);
        label.setFont(new Font(18));
        label.setId("collectionName");
        label.setOnMouseClicked(clickEvent -> {
            this.resetActiveCollection();
            label.setStyle("-fx-text-fill: #5E5A80;");
            this.visualize.clear();
            List<String> documents = this.mongoProcess.getAllEntriesOfCollection(label.getText());
            System.out.println("nb documents " + documents.size());
            if(documents.size() > 0) {
                for(String document: documents) {
                    this.visualize.setText(document + "\n");
                }

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

    private String renderJson(String json) {
        int openBracket = 0;
        int closeBracket = 0;
        String newJson = "";

        for(int i = 0; i < json.length(); i ++) {
            if('{' == json.charAt(i) || '[' == json.charAt(i)) {
                openBracket ++;
                newJson = newJson + json.charAt(i) + "\n";
                for(int j = 0; j < openBracket; j++ ) {
                    newJson = newJson + "\t";
                }
            } else if('}' == json.charAt(i) || ']' == json.charAt(i)) {
                newJson = newJson + "\n";
                openBracket --;
                for(int j = 0; j < openBracket; j++ ) {
                    newJson = newJson + "\t";
                }
                newJson = newJson + json.charAt(i);
            } else if( ',' == json.charAt(i)) {
                newJson = newJson + ",\n";
                for(int j = 0; j < openBracket; j++ ) {
                    newJson = newJson + "\t";
                }
            } else {
                newJson = newJson + json.charAt(i);
            }
        }

        return newJson;
    }
}
