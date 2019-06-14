package org.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ConnectController {

    @FXML
    private Button btnConnect;

    @FXML
    private TextField hostField;

    @FXML
    private TextField portField;

    @FXML
    private TextField dbField;

    @FXML
    private TextField userField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label bannerError;



    private Stage primaryStage;

    private XplorerController xplorerController;

    @FXML
    protected void connect(ActionEvent event) throws IOException {
        if(!this.checkInput()) {
            String mongoUri = new StringBuilder("mongodb://")
                    .append(this.userField.getText())
                    .append(this.passwordField.getText())
                    .append(this.userField.getText().equals("") && this.passwordField.getText().equals("") ? "" : "@")
                    .append(this.hostField.getText() + ":")
                    .append(this.portField.getText()).toString();

            this.xplorerController.connect(mongoUri, this.dbField.getText());
        }
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


        if(this.dbField.getText().isEmpty()) {
            this.bannerError.setText("The Database name is empty !");
            this.bannerError.setVisible(true);
            return true;
        }

        return false;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setController(XplorerController xplorerController) {
        this.xplorerController = xplorerController;
    }
}
