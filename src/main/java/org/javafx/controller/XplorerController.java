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


        //this.visualize.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.mongoProcess = new MongoProcess(this.hostField.getText(), Integer.parseInt(this.portField.getText()));

        List<String> collectionsNames = this.mongoProcess.getAllCollectionNames();

        for(String collectionName: collectionsNames) {
            this.test.getChildren().add(new Label(collectionName));
        }
        this.test.setVisible(true);

        List<String> documents = this.mongoProcess.getAllEntriesOfCollection();
        for(String document: documents) {
            this.visualize.setText(this.visualize.getText() + renderJson(document) + ",\n");
        }
        this.visualize.setVisible(true);

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
