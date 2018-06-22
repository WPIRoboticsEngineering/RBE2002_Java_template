package com.FieldSimulator.FieldSimulator_2001;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import edu.wpi.SimplePacketComs.device.warehouse.WarehouseRobot;

import java.io.IOException;


public class InterfaceController extends Application {

    WarehouseRobot fieldSim = null;
    ObservableList<String> Weights = FXCollections.observableArrayList("Aluminum", "Plastic");
    ObservableList<String> Sides = FXCollections.observableArrayList("25", "45");
    ObservableList<String> Pos = FXCollections.observableArrayList("1", "2");
    @FXML
    Button connect = new Button();
    @FXML
    ChoiceBox<String> choiceBoxWeight = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> choiceBoxSide = new ChoiceBox<>();
    @FXML
    ChoiceBox<String> choiceBoxPos = new ChoiceBox<>();
    @FXML
    TextField teamName;
    @FXML
    RadioButton heartBeat;
    @FXML
    TextArea response;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @FXML
    private void initialize() {
        choiceBoxWeight.setValue("Aluminum");
        choiceBoxWeight.setItems(Weights);
        choiceBoxSide.setValue("25");
        choiceBoxSide.setItems(Sides);
        choiceBoxPos.setValue("1");
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

    public InterfaceController() throws Exception {

    }

    public void connectToDevice() throws Exception {

        fieldSim = WarehouseRobot.get(teamName.getText()).get(0);
        fieldSim.addEvent(Integer.valueOf(1936), ()->{

        });

    }

    public void sendLocation(){


    }
    public void sendRaisedHigh(){

    }
    public void sendLowered(){

    }
    public void start(){

    }
    public void stop(){

    }


}
