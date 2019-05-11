package org.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class XplorerController {

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private Button btnConnect;

    @FXML
    private Label bannerError;

    @FXML
    private TextArea visualize;

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

        for(int i = 0; i < 100; i ++) {
            this.visualize.setText(this.visualize.getText() + "\n Test" );
        }

        //this.visualize.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Region contentTextArea = (Region) this.visualize.lookup(".content");
        contentTextArea.setStyle("-fx-background-color: white;");
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