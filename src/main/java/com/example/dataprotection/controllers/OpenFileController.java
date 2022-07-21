package com.example.dataprotection.controllers;

import java.io.*;
import java.util.Scanner;

import com.example.dataprotection.code.Cycle743;
import com.example.dataprotection.code.HammingCoder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class OpenFileController {

    String nameAlgorithm = new String();

    ObservableList<String> listAlgorithm;

    byte[] buffer;
    ObservableList<String> nameFiles;

    @FXML
    private AnchorPane message;

    @FXML
    private Button EncryptionButton, decryptButton, saveButton, okButton;

    @FXML
    private TextArea textFile, TextFileProtection, textMessage;


    @FXML
    private ComboBox<String> listNameFiles, codingAlgorithm;

    @FXML
    void initialize() {
        nameFiles = FXCollections.observableArrayList();
        listFile();
        listNameFiles.setItems(nameFiles);

        listNameFiles.setOnAction(event ->{
            readFile(listNameFiles.getValue());
        });

        listAlgorithm = FXCollections.observableArrayList("код Хеммінга", "Двійковий (7,4,3) код");
        codingAlgorithm.setItems(listAlgorithm);
        codingAlgorithm.setOnAction(event ->{
            nameAlgorithm = codingAlgorithm.getValue();
        });

        EncryptionButton.setOnAction(event -> {
            if(textFile.getText().length() > 0)
                encryptFile();
        });

        decryptButton.setOnAction(event -> {
            decryptFile();
        });

        saveButton.setOnAction(event -> {
            if(!TextFileProtection.getText().equals("")){
                message.setVisible(true);
                textMessage.setText("Файл збережено");
                saveFile(listNameFiles.getValue());
            }
        });

        okButton.setOnAction(event -> {
            message.setVisible(false);
        });
    }

    private void readFile(String nameFile){
        try(FileInputStream fin=new FileInputStream(nameFile))
        {
            buffer = new byte[fin.available()];
            fin.read(buffer, 0, fin.available());
            TextFileProtection.setText(new String(buffer));

        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    private void decryptFile(){
        String decoderStr;
        try {
            switch (nameAlgorithm){
                case "код Хеммінга":
                    HammingCoder hammingCoder = new HammingCoder();
                    decoderStr = hammingCoder.decoder(TextFileProtection.getText());
                    textFile.setText(decoderStr);
                    break;
                case "Двійковий (7,4,3) код":
                    Cycle743 cycle743 = new Cycle743();
                    decoderStr = cycle743.decoder(TextFileProtection.getText());
                    textFile.setText(decoderStr);
                    break;
                default:
                    message.setVisible(true);
                    if(TextFileProtection.getText().equals(""))
                        textMessage.setText("Оберіть файл");
                    else
                        textMessage.setText("Оберіть алгорит кодування");
                    break;
            }
        }catch (ArrayIndexOutOfBoundsException | NumberFormatException exception){
            message.setVisible(true);
            textMessage.setText("Помилка, оберіть інший алгоритм.");
        }
    }
    private void encryptFile(){
        String mes = textFile.getText();
        switch (nameAlgorithm){
            case "код Хеммінга":
                HammingCoder hammingCoder = new HammingCoder();
                TextFileProtection.setText(hammingCoder.encoder(mes));
                break;
            case "Двійковий (7,4,3) код":
                Cycle743 cycle743 = new Cycle743();
                TextFileProtection.setText(cycle743.encoder(mes));
        }


    }
    private void listFile(){
        FileReader reader = null;
        try {
            String[] names = new String[0];
            if(new File("NameFile.txt").exists()){
                reader = new FileReader("NameFile.txt");
                Scanner scanner = new Scanner(reader);
                String s = null;
                while (scanner.hasNextLine()){
                    s  = scanner.nextLine();
                }
                if(s != null)
                    names = s.split(" ");
                for(int i = 0; i < names.length; i++){
                    File file = new File(names[i]);
                    if(file.exists())
                        nameFiles.add(names[i]);

                }
                reader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveFile(String nameFile){
        if(TextFileProtection.getText().length() > 0){
            try(FileOutputStream fos=new FileOutputStream(nameFile))
            {
                byte[] buffer = TextFileProtection.getText().getBytes();

                fos.write(buffer, 0, buffer.length);
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }
    }
}