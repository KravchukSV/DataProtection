package com.example.dataprotection.controllers;

import java.io.*;
import java.util.Scanner;

import com.example.dataprotection.code.Cycle743;
import com.example.dataprotection.code.HammingCoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


public class CreateFileController {

    String nameAlgorithm = new String();

    ObservableList<String> listAlgorithm;

    @FXML
    private AnchorPane savePanel, message;

    @FXML
    private TextArea nameFile, TextFileProtection, TextFile, textMessage;

    @FXML
    private Button saveFileButton, EncryptionButton, Create, cancelButton, okButton;

    @FXML
    private ComboBox<String> codingAlgorithm;

    @FXML
    void initialize() {

        listAlgorithm = FXCollections.observableArrayList("код Хеммінга", "Двійковий (7,4,3) код");

        EncryptionButton.setOnAction(event -> {
            encryptFile();
        });

        Create.setOnAction(event -> {
            if(TextFileProtection.getText().length() != 0){
                savePanel.setVisible(true);
            }
            else{
                message.setVisible(true);
                textMessage.setText("Файл не зашифровано!!!");
            }
        });

        codingAlgorithm.setItems(listAlgorithm);
        codingAlgorithm.setOnAction(event ->{
            nameAlgorithm = codingAlgorithm.getValue();
        });

        cancelButton.setOnAction(event -> {
            savePanel.setVisible(false);
        });

        saveFileButton.setOnAction(event -> {
            savePanel.setVisible(false);
            saveFile(nameFile.getText());
            saveNameFile();
            textMessage.setText("Файл збережено");

        });

        okButton.setOnAction(event -> {
            message.setVisible(false);
        });


    }

    private void saveFile(String nameFile){
        try(FileOutputStream fos=new FileOutputStream(nameFile))
        {
            byte[] buffer = TextFileProtection.getText().getBytes();

            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    private void encryptFile(){
        String mes = TextFile.getText();
        switch (nameAlgorithm){
            case "код Хеммінга":
                HammingCoder hammingCoder = new HammingCoder();
                TextFileProtection.setText(hammingCoder.encoder(mes));
                break;
            case "Двійковий (7,4,3) код":
                Cycle743 cycle743 = new Cycle743();
                TextFileProtection.setText(cycle743.encoder(mes));
                break;
            default:
                message.setVisible(true);
                if(TextFile.getText().equals(""))
                    textMessage.setText("Введіть повідомлення");
                else
                    textMessage.setText("Оберіть алгорит кодування");
                break;
        }
    }

    private void saveNameFile(){
        String[] names = new String[0];
        boolean isExist = false;
        try (FileWriter writer = new FileWriter("NameFile.txt", true)){
            FileReader reader = new FileReader("NameFile.txt");
            Scanner scanner = new Scanner(reader);
            String s = null;
            while (scanner.hasNextLine()){
                s  = scanner.nextLine();
            }
            if(s != null)
                names = s.split(" ");
            for(int i = 0; i < names.length; i++){
                if(names[i].equals(nameFile.getText())){
                    isExist = true;
                    break;
                }
            }
            if(!isExist){
                writer.write(nameFile.getText() + " ");
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
