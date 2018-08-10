package com.FieldSimulator.FieldSimulator_2001;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import edu.wpi.SimplePacketComs.device.warehouse.WarehouseRobot;
import edu.wpi.SimplePacketComs.device.warehouse.WarehouseRobotStatus;
import javafx.event.ActionEvent;

public class InterfaceController {

	private static WarehouseRobot fieldSim = null;
	WarehouseRobotStatus status= WarehouseRobotStatus.Fault_E_Stop_pressed;
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
	    private Button approveButton;
	@FXML
	private void initialize() {
		assert connectToDevice != null : "fx:id=\"connectToDevice\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert start != null : "fx:id=\"start\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert stop != null : "fx:id=\"stop\" was not injected: check your FXML file 'MainScreen.fxml'.";
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
		//PLE.setDisable(true);
		//RHE.setDisable(true);
		send.setDisable(true);
		approveButton.setDisable(true);
	}

	public void connectToDevice() {
		if (getRobot() == null) {
			connectToDevice.setDisable(true);
			new Thread(() -> {
				try {
					setFieldSim(WarehouseRobot.get(teamName.getText()).get(0));
					// getFieldSim().setReadTimeout(1000);
					if (getRobot() != null) {
						getRobot().addEvent(Integer.valueOf(2012), () -> {
							WarehouseRobotStatus tmp = getRobot().getStatus();
							if (status != tmp) {
								status = tmp;
								System.out.println(" New Status = " + status.name());
								Platform.runLater(() -> {
									heartBeat.setText(status.name());
								});
								Platform.runLater(() -> {
									approveButton.setDisable(status!=WarehouseRobotStatus.Waiting_for_approval_to_pickup);
								});
								
							}
						});

						Platform.runLater(() -> {
							start.setDisable(false);
							stop.setDisable(false);
							//PLE.setDisable(false);
							//RHE.setDisable(false);
							send.setDisable(false);
							approveButton.setDisable(true);
						});
					}
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (getRobot() == null)
					Platform.runLater(() -> {
						connectToDevice.setDisable(false);
					});
			}).start();
		}
	}
	@FXML
    void onApprove(ActionEvent event) {
		System.out.println("approve");
		if (getRobot() != null)
			getRobot().approve();
    }


	@FXML
	void sendLocation(ActionEvent event) {
		System.out.println("sendLocation");
		try {
			double material = 0;
			if (choiceBoxWeight.getSelectionModel().getSelectedItem().contains(Weights.get(0))) {
				material = 1;
			} else {
				material = 2;
			}
			double angle = Double.parseDouble(choiceBoxSide.getSelectionModel().getSelectedItem());
			double position = Double.parseDouble(choiceBoxPos.getSelectionModel().getSelectedItem());
			if (getRobot() != null)
				getRobot().pickOrder(material, angle, position);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}


	@FXML
	void start(ActionEvent event) {
		System.out.println("start");
		if (getRobot() != null)
			getRobot().clearFaults();
	}

	@FXML
	void stop(ActionEvent event) {
		System.out.println("stop");
		if (getRobot() != null)
			getRobot().estop();
	}

	@FXML
	void onConnect(ActionEvent event) {
		System.out.println("onConnect");
		connectToDevice();
	}

	public static WarehouseRobot getRobot() {
		return fieldSim;
	}

	private static void setFieldSim(WarehouseRobot fieldSim) {
		fieldSim.setReadTimeout(1000);
		InterfaceController.fieldSim = fieldSim;
	}
}
