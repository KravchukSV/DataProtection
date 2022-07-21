package com.example.dataprotection.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HelloController {

    private boolean isInfo = false;

    @FXML
    private ImageView info;

    @FXML
    private AnchorPane infoPanel;

    @FXML
    private TextArea textInfo;

    @FXML
    private Button CreateButton;

    @FXML
    private Button OpenButton;

    @FXML
    private Button closeInfoPanel;

    @FXML
    void onHelloButtonClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        OpenButton.setOnAction(event -> {
            NewWindow("open-file.fxml");

        });

        CreateButton.setOnAction(event -> {
            NewWindow("create-file.fxml");
        });

        closeInfoPanel.setOnAction(event -> {
            infoPanel.setVisible(false);
            isInfo = false;
        });

        info.setOnMouseClicked(event -> {
            if(!isInfo){
                infoPanel.setVisible(true);
                textInfo.setText("\tІнформація про розробника\nП. І. Б: Кравчук Сергій Вікторович\n" +
                        "Дата народження: 10.07.2000\nМісце роботи/навчання: КНУ імені Тараса Шевченка\n" +
                        "Посада/спеціальінсть: комп'ютерна механіка, 1 курс, магістратура");
                isInfo = true;
            }
            else{
                infoPanel.setVisible(false);
                isInfo = false;
            }
        });
    }

    private void NewWindow(String nameWindow){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/example/dataprotection/" + nameWindow));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}