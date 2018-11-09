package edu.wpi.rbe.rbe2001.fieldsimulator.gui;

import edu.wpi.rbe.rbe2001.fieldsimulator.robot.FireFighterRobot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
public class InterfaceController {

	private static FireFighterRobot fieldSim;

    @FXML // fx:id="robotName"
    private Label robotName; // Value injected by FXMLLoader
    @FXML
    private Tab connectTab;

    @FXML
    private TextField teamName;

    @FXML
    private Button connectToDevice;

    @FXML
    private Tab imutab;

    @FXML
    private Label accelx;

    @FXML
    private Label accely;

    @FXML
    private Label accelz;

    @FXML
    private Label gyrox;

    @FXML
    private Label gyroy;

    @FXML
    private Label gyroz;

    @FXML
    private Label gravx;

    @FXML
    private Label gravy;

    @FXML
    private Label gravz;

    @FXML
    private Label eulx;

    @FXML
    private Label euly;

    @FXML
    private Label eulz;

    @FXML
    private Tab pidTab;

    @FXML
    private LineChart<Double, Integer> pidGraph;

    @FXML
    private TextField kp;

    @FXML
    private TextField ki;

    @FXML
    private TextField kd;

    @FXML
    private Button pidConstUpdate;

    @FXML
    private ChoiceBox<Integer> pidChannel;

    @FXML
    private TextField setpoint;

    @FXML
    private Button setSetpoint;

    @FXML
    private Label position;

	@FXML
	private void initialize() {
        assert connectTab != null : "fx:id=\"connectTab\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert teamName != null : "fx:id=\"teamName\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert connectToDevice != null : "fx:id=\"connectToDevice\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert imutab != null : "fx:id=\"imutab\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert accelx != null : "fx:id=\"accelx\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert accely != null : "fx:id=\"accely\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert accelz != null : "fx:id=\"accelz\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert gyrox != null : "fx:id=\"gyrox\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert gyroy != null : "fx:id=\"gyroy\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert gyroz != null : "fx:id=\"gyroz\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert gravx != null : "fx:id=\"gravx\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert gravy != null : "fx:id=\"gravy\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert gravz != null : "fx:id=\"gravz\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert eulx != null : "fx:id=\"eulx\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert euly != null : "fx:id=\"euly\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert eulz != null : "fx:id=\"eulz\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert pidTab != null : "fx:id=\"pidTab\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert pidGraph != null : "fx:id=\"pidGraph\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert kp != null : "fx:id=\"kp\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert ki != null : "fx:id=\"ki\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert kd != null : "fx:id=\"kd\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert pidConstUpdate != null : "fx:id=\"pidConstUpdate\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert pidChannel != null : "fx:id=\"pidChannel\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert setpoint != null : "fx:id=\"setpoint\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert setSetpoint != null : "fx:id=\"setSetpoint\" was not injected: check your FXML file 'MainScreen.fxml'.";
        assert position != null : "fx:id=\"position\" was not injected: check your FXML file 'MainScreen.fxml'.";


//		choiceBoxWeight.setValue(weights.get(0));
//		choiceBoxWeight.setItems(weights);
//		choiceBoxSide.setValue("25");
//		choiceBoxSide.setItems(sides);
//		choiceBoxPos.setValue("1");
//		choiceBoxPos.setItems(pos);
//
//		choiceBoxWeight.getSelectionModel().select(weights.get(0));
//
//		start.setDisable(true);
//		stop.setDisable(true);
//		// PLE.setDisable(true);
//		// RHE.setDisable(true);
//		send.setDisable(true);
//		approveButton.setDisable(true);
	}

	private void connectToDevice() {
		if (getRobot() == null) {
			connectToDevice.setDisable(true);
			new Thread(() -> {
				try {
					setFieldSim(FireFighterRobot.get(teamName.getText()).get(0));
					// getFieldSim().setReadTimeout(1000);
					if (getRobot() != null) {
						Platform.runLater(() -> {
							robotName.setText(getRobot().getName());
							imutab.setDisable(false);
							pidTab.setDisable(false);
//							start.setDisable(false);
//							stop.setDisable(false);
//							// PLE.setDisable(false);
//							// RHE.setDisable(false);
//							send.setDisable(false);
//							approveButton.setDisable(true);
						});
					}
				} catch (Exception ex) {
					//ex.printStackTrace();
					Platform.runLater(() -> robotName.setText("None Found!"));
				}
				if (getRobot() == null) {
					Platform.runLater(() -> connectToDevice.setDisable(false));
				}
			}).start();
		}
	}

	@FXML
	void onConnect() {
		System.out.println("onConnect");
		connectToDevice();
	}


    @FXML
    void onSetGains() {

    }

    @FXML
    void onSetSetpoint() {

    }
	public static FireFighterRobot getRobot() {
		return fieldSim;
	}

	private static void setFieldSim(FireFighterRobot fieldSim) {
		fieldSim.setReadTimeout(1000);
		InterfaceController.fieldSim = fieldSim;
	}
}
