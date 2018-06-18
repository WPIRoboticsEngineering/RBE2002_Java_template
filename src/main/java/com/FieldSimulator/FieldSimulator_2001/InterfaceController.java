package com.FieldSimulator.FieldSimulator_2001;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class InterfaceController extends Application {

    ObservableList<String> Weights = FXCollections.observableArrayList("Heavy", "Light");
    ObservableList<String> Sides = FXCollections.observableArrayList("Left", "Right");
    ObservableList<String> Pos = FXCollections.observableArrayList("1", "2", "3", "4");

    @FXML
    Button connect = new Button();
    @FXML
    ChoiceBox<String> choiceBoxWeight = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> choiceBoxSide = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> choiceBoxPos = new ChoiceBox<>();
    @FXML
    TextField idNum;
    @FXML
    TextField teamName;
    @FXML
    RadioButton heartBeat;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @FXML
    private void initialize() {
        choiceBoxWeight.setValue("Heavy");
        choiceBoxWeight.setItems(Weights);
        choiceBoxSide.setValue("Left");
        choiceBoxSide.setItems(Sides);
        choiceBoxPos.setValue("2");
        choiceBoxPos.setItems(Pos);


        choiceBoxWeight.getSelectionModel().select("Heavy");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScreen.fxml"));
        primaryStage.setTitle("Field Simulator");
        root.getStylesheets().add("/materialfx-material-design-for-javafx/material-fx-v0_3.css");
        primaryStage.setScene(new Scene(root, 1000, 750));

        primaryStage.show();
        primaryStage.getScene().setRoot(root);

    }

    public InterfaceController() {
    }

    public void connectToDevice() throws Exception {

        FieldSimulator Field = FieldSimulator.get().get(0);
        Field.addEvent(Integer.valueOf(idNum.getText()), () -> {

        });
    }

    public void sendLocation(){

    }
    public void sendRaisedHigh(){

    }
    public void sendLowered(){

    }
    public void FStart(){

    }
    public void FStop(){

    }
    public void start(){

    }
    public void stop(){
        
    }


}
