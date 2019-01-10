package edu.wpi.rbe.rbe2001.fieldsimulator.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.rbe.rbe2001.fieldsimulator.robot.FireFighterRobot;
import edu.wpi.rbe.rbe2001.fieldsimulator.robot.RBE2001Robot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class InterfaceController {

	@FXML
	private Tab connectTab;

	@FXML
	private TextField teamName;

	@FXML
	private Button connectToDevice;

	@FXML
	private Label robotName;

	@FXML
	private Tab pidVelTab;

	@FXML
	private TextField kpVel;

	@FXML
	private TextField kdVel;

	@FXML
	private Button pidConstUpdateVelocity;

	@FXML
	private ChoiceBox<?> pidChannelVelocity;

	@FXML
	private TextField setpointVelocity;

	@FXML
	private Button setSetpointVelocity;

	@FXML
	private Label velocityVal;

	@FXML
	private Tab pidTab;

	@FXML
	private TextField kp;

	@FXML
	private TextField ki;

	@FXML
	private TextField kd;

	@FXML
	private Button pidConstUpdate;

	@FXML
	private TextField setpoint;

	@FXML
	private Button setSetpoint;

	@FXML
	private Label position;

	@FXML
	private ChoiceBox<Integer> pidChannel;

	@FXML // fx:id="setDuration"
	private TextField setDuration; // Value injected by FXMLLoader

	@FXML // fx:id="setType"
	private ChoiceBox<String> setType; // Value injected by FXMLLoader

	@FXML
	private LineChart<Double, Double> pidGraph;
	@FXML
	private LineChart<Double, Double> pidGraphVel;
	private ArrayList<XYChart.Series> pidGraphSeriesVel = new ArrayList<>();
	private ArrayList<XYChart.Series> pidGraphSeries = new ArrayList<>();

	private double piddata[] = null;
	private double pidConfig[] = null;
	private double currentSetpoint[] = null;
	private DecimalFormat formatter = new DecimalFormat();
	private double start = ((double) System.currentTimeMillis()) / 1000.0;
	private long lastPos;
	private long lastSet;
	static InterfaceController me;
	private static RBE2001Robot fieldSim;
	private int numPIDControllers = 0;
	private int currentIndex = 0;
	private static final int numPIDControllersOnDevice = 3;

	@FXML
	private void initialize() {
		me = this;
		formatter.setMaximumFractionDigits(3);

		assert connectTab != null : "fx:id=\"connectTab\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert teamName != null : "fx:id=\"teamName\" was not injected: check your FXML file 'MainScreen.fxml'.";
		assert connectToDevice != null : "fx:id=\"connectToDevice\" was not injected: check your FXML file 'MainScreen.fxml'.";

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
		teamName.setText("IMU-Team21");

		for (int i = 0; i < numPIDControllersOnDevice; i++) {
			Series e = new XYChart.Series();

			pidGraphSeries.add(i, e);
			pidGraph.getData().add(e);
		}
		pidGraphVel.getXAxis().autoRangingProperty().set(true);
		for (int i = 0; i < numPIDControllersOnDevice; i++) {
			Series e = new XYChart.Series();

			pidGraphSeriesVel.add(i, e);
			pidGraphVel.getData().add(e);
		}
		pidGraphVel.getXAxis().autoRangingProperty().set(true);
	}

	private void connectToDevice() {
		if (getRobot() == null) {
			connectToDevice.setDisable(true);
			new Thread(() -> {
				try {
					setFieldSim(RBE2001Robot.get(teamName.getText()).get(0));
					Thread.sleep(1000);
					// getFieldSim().setReadTimeout(1000);
					if (getRobot() != null) {
						Platform.runLater(() -> {
							robotName.setText(getRobot().getName());
							pidTab.setDisable(false);
							pidVelTab.setDisable(false);
						});
					}
				} catch (Exception ex) {
					// ex.printStackTrace();
					Platform.runLater(() -> robotName.setText(teamName.getText() + " Not Found!"));
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
		double kpv = Double.parseDouble(kp.getText());
		double kiv = Double.parseDouble(ki.getText());
		double kdv = Double.parseDouble(kd.getText());
		for (int i = 0; i < numPIDControllers; i++)
			fieldSim.setPidGains(i, kpv, kiv, kdv);
	}

	@FXML
	void onSetSetpoint() {
		double down[] = new double[2 + 2 * numPIDControllersOnDevice];
		down[0] = Integer.parseInt(setDuration.getText());
		down[1] = setType.getSelectionModel().getSelectedItem().equals("LIN") ? 0 : 1;
		for (int i = 0; i < numPIDControllersOnDevice; i++) {
			if (i == currentIndex) {
				down[2 + i] = Double.parseDouble(setpoint.getText());
			} else {
				down[2 + i] = currentSetpoint[i];
			}
		}
		fieldSim.setPidSetpoints(down);

	}

	public RBE2001Robot getRobot() {
		return fieldSim;
	}

	private void setFieldSim(RBE2001Robot fieldSim) {
		fieldSim.setReadTimeout(1000);
		InterfaceController.fieldSim = fieldSim;

		fieldSim.addEvent(1910, () -> {
			try {
				if (piddata == null) {
					piddata = new double[1 + 2 * numPIDControllersOnDevice];
					currentSetpoint = new double[numPIDControllersOnDevice];
				}
				fieldSim.readFloats(1910, piddata);
				int myNumPid = (int) piddata[0];
				if (numPIDControllers != myNumPid) {
					numPIDControllers = myNumPid;
					setUpPid();
				}
				double pos = piddata[1 + currentIndex * 2 + 1];
				double set = piddata[1 + currentIndex * 2 + 0];
				for(int i=0;i<numPIDControllersOnDevice;i++) {
					currentSetpoint[i]=piddata[1 + i * 2 + 0];
				}
				String positionVal = formatter.format(pos);
				// System.out.println(positionVal+"
				// "+DoubleStream.of(piddata).boxed().collect(Collectors.toCollection(ArrayList::new)));
				;
				Platform.runLater(() -> position.setText(positionVal));
				Platform.runLater(() -> updateGraph(pos, set));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		fieldSim.addEvent(1857, () -> {
			try {
				if (pidConfig == null)
					pidConfig = new double[3 * numPIDControllersOnDevice];
				fieldSim.readFloats(1857, pidConfig);

				// System.out.println("
				// "+DoubleStream.of(pidConfig).boxed().collect(Collectors.toCollection(ArrayList::new)));

				Platform.runLater(() -> kp.setText(formatter.format(pidConfig[currentIndex * 3 + 0])));

				Platform.runLater(() -> ki.setText(formatter.format(pidConfig[currentIndex * 3 + 1])));

				Platform.runLater(() -> kd.setText(formatter.format(pidConfig[currentIndex * 3 + 2])));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		fieldSim.updatConfig();

	}

	@SuppressWarnings("unchecked")
	private void updateGraph(double pos, double set) {
		if (pidGraphSeries.size() == 0)
			return;
		double now = ((double) System.currentTimeMillis()) / 1000.0 - start;
		long thispos = (long) pos;
		long thisSet = (long) set;
		if (thispos != lastPos || thisSet != lastSet) {
			pidGraphSeries.get(0).getData().add(new XYChart.Data(now - 0.0001, lastPos));
			pidGraphSeries.get(1).getData().add(new XYChart.Data(now - 0.0001, lastSet));
			lastSet = thisSet;
			lastPos = thispos;
			pidGraphSeries.get(0).getData().add(new XYChart.Data(now, pos));
			pidGraphSeries.get(1).getData().add(new XYChart.Data(now, set));
		}
		for (Series s : pidGraphSeries) {
			while (s.getData().size() > 200) {
				s.getData().remove(0);
			}
		}
	}

	private void setUpPid() {
		System.out.println("PID controller has " + fieldSim.getNumPid() + " controllers");
		if (fieldSim.getNumPid() > 0) {
			for (int i = 0; i < fieldSim.getNumPid(); i++) {
				int index = i;
				Platform.runLater(() -> pidChannel.getItems().add(index));
			}
			pidChannel.getSelectionModel().selectedIndexProperty().addListener((obs, old, newVal) -> {
				System.out.println("Set to channel " + newVal);
				currentIndex = newVal.intValue();
				fieldSim.updatConfig();
				for (Series s : pidGraphSeries) {
					s.getData().clear();

				}
			});
			Platform.runLater(() -> pidChannel.setValue(0));
			Platform.runLater(() -> setType.getItems().add("LIN"));
			Platform.runLater(() -> setType.getItems().add("SIN"));
			Platform.runLater(() -> setType.setValue("LIN"));
		}
	}

	public static void disconnect() {
		if (me.getRobot() != null)
			me.getRobot().disconnect();
	}

	@FXML
	void onSetVelocity() {

	}

	@FXML
	void onSetGainsVelocity() {

	}

}
