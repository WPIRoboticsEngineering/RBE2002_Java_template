package com.FieldSimulator.FieldSimulator_2001;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import edu.wpi.SimplePacketComs.device.warehouse.WarehouseRobot;

import javafx.event.ActionEvent;

public class InterfaceController {

	WarehouseRobot fieldSim = null;
	ObservableList<String> Weights = FXCollections.observableArrayList("Aluminum", "Plastic");
	ObservableList<String> Sides = FXCollections.observableArrayList("25", "45");
	ObservableList<String> Pos = FXCollections.observableArrayList("1", "2");
	@FXML
	private Button connectToDevice;

	@FXML
	private Button start;

	@FXML
	private Button stop;

	@FXML
	private Button PLE;

	@FXML
	private Button RHE;

	@FXML
	private TextArea response;

	@FXML
	private Button send;

	@FXML
	private RadioButton heartBeat;

	@FXML
	private ChoiceBox<String> choiceBoxWeight;

	@FXML
	private ChoiceBox<String> choiceBoxSide;

	@FXML
	private ChoiceBox<String> choiceBoxPos;
	@FXML
	private TextField teamName;

	@FXML
	private void initialize() {
		assert connectToDevice != null : "fx:id=\"connectToDevice\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert start != null : "fx:id=\"start\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert stop != null : "fx:id=\"stop\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert PLE != null : "fx:id=\"PLE\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert RHE != null : "fx:id=\"RHE\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert teamName != null : "fx:id=\"teamName\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert response != null : "fx:id=\"response\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert send != null : "fx:id=\"send\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert heartBeat != null : "fx:id=\"heartBeat\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert choiceBoxWeight != null : "fx:id=\"choiceBoxWeight\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert choiceBoxSide != null : "fx:id=\"choiceBoxSide\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert choiceBoxPos != null : "fx:id=\"choiceBoxPos\" was not injected: check your FXML file 'MainScreen.fxml'.";

		choiceBoxWeight.setValue(Weights.get(0));
		choiceBoxWeight.setItems(Weights);
		choiceBoxSide.setValue("25");
		choiceBoxSide.setItems(Sides);
		choiceBoxPos.setValue("1");
		choiceBoxPos.setItems(Pos);

		choiceBoxWeight.getSelectionModel().select(Weights.get(0));

		start.setDisable(true);
		stop.setDisable(true);
		PLE.setDisable(true);
		RHE.setDisable(true);
		send.setDisable(true);

	}

	public InterfaceController() throws Exception {

	}

	public void connectToDevice() {
		if (fieldSim == null) {
			connectToDevice.setDisable(true);
			new Thread(() -> {
				try {
					fieldSim = WarehouseRobot.get(teamName.getText()).get(0);
					if (fieldSim != null) {
						fieldSim.addEvent(Integer.valueOf(1936), () -> {

						});

						Platform.runLater(() -> {
							start.setDisable(false);
							stop.setDisable(false);
							PLE.setDisable(false);
							RHE.setDisable(false);
							send.setDisable(false);
						});
					}
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (fieldSim == null)
					Platform.runLater(() -> {
						connectToDevice.setDisable(false);
					});
			}).start();
		}
	}

	@FXML
	void sendLocation(ActionEvent event) {
		System.out.println("sendLocation");
		try {
			double material =0;
			if(choiceBoxWeight.getSelectionModel().getSelectedItem().contains(Weights.get(0))) {
				material=1;
			}else {
				material=2;
			}
			double angle = Double.parseDouble(choiceBoxSide.getSelectionModel().getSelectedItem());
			double position = Double.parseDouble(choiceBoxPos.getSelectionModel().getSelectedItem());
			if (fieldSim != null)
				fieldSim.pickOrder(material, angle, position);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	@FXML
	void sendLowered(ActionEvent event) {
		System.out.println("sendLowered");
	}

	@FXML
	void sendRaisedHigh(ActionEvent event) {
		System.out.println("sendRaisedHigh");
	}

	@FXML
	void start(ActionEvent event) {
		System.out.println("start");
		if (fieldSim != null)
			fieldSim.clearFaults();
	}

	@FXML
	void stop(ActionEvent event) {
		System.out.println("stop");
		if (fieldSim != null)
			fieldSim.estop();
	}

	@FXML
	void onConnect(ActionEvent event) {
		System.out.println("onConnect");
		connectToDevice();
	}
}
