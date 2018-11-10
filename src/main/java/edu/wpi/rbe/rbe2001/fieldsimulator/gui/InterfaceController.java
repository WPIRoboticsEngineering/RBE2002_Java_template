package edu.wpi.rbe.rbe2001.fieldsimulator.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import edu.wpi.rbe.rbe2001.fieldsimulator.robot.FireFighterRobot;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InterfaceController {
	static InterfaceController me;
	private static FireFighterRobot fieldSim;
	private int numPIDControllers = 0;
	private int currentIndex=0;
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
	private LineChart<Double, Double> pidGraph;
	private ArrayList<XYChart.Series> pidGraphSeries = new ArrayList<>();
	
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
	private double datas[] = null;
	private double piddata[] = null;
	private double pidConfig[] = null;
	private DecimalFormat formatter = new DecimalFormat();
	private double start  =((double)System.currentTimeMillis())/1000.0;
	private long lastPos;
	private long lastSet;
	@FXML
	private void initialize() {
		me = this;
		formatter.setMaximumFractionDigits(3);

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
		teamName.setText("IMU-Team21");

		// choiceBoxWeight.setValue(weights.get(0));
		// choiceBoxWeight.setItems(weights);
		// choiceBoxSide.setValue("25");
		// choiceBoxSide.setItems(sides);
		// choiceBoxPos.setValue("1");
		// choiceBoxPos.setItems(pos);
		//
		// choiceBoxWeight.getSelectionModel().select(weights.get(0));
		//
		// start.setDisable(true);
		// stop.setDisable(true);
		// // PLE.setDisable(true);
		// // RHE.setDisable(true);
		// send.setDisable(true);
		// approveButton.setDisable(true);
		
		for(int i=0;i<2;i++) {
			Series e = new XYChart.Series();
			
			pidGraphSeries.add(i,e);
			pidGraph.getData().add(e);
		}
		pidGraph.getXAxis().autoRangingProperty().set(true);
	}

	private void connectToDevice() {
		if (getRobot() == null) {
			connectToDevice.setDisable(true);
			new Thread(() -> {
				try {
					setFieldSim(FireFighterRobot.get(teamName.getText()).get(0));
					Thread.sleep(1000);
					// getFieldSim().setReadTimeout(1000);
					if (getRobot() != null) {
						Platform.runLater(() -> {
							robotName.setText(getRobot().getName());
							imutab.setDisable(false);
							pidTab.setDisable(false);
							// start.setDisable(false);
							// stop.setDisable(false);
							// // PLE.setDisable(false);
							// // RHE.setDisable(false);
							// send.setDisable(false);
							// approveButton.setDisable(true);
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
		for(int i=0;i<numPIDControllers;i++)
			fieldSim.setPidGains(i, kpv, kiv, kdv);
	}

	@FXML
	void onSetSetpoint() {
		
	}

	public FireFighterRobot getRobot() {
		return fieldSim;
	}

	private void setFieldSim(FireFighterRobot fieldSim) {
		fieldSim.setReadTimeout(1000);
		InterfaceController.fieldSim = fieldSim;
		fieldSim.addEvent(1804, () -> {
			if (datas == null)
				datas = new double[12];
			fieldSim.readFloats(1804, datas);
			Platform.runLater(() -> {
				int base = 0;
				accelx.setText(formatter.format(datas[base + 0]));
				accely.setText(formatter.format(datas[base + 1]));
				accelz.setText(formatter.format(datas[base + 2]));
				base = 3;
				gyrox.setText(formatter.format(datas[base + 0]));
				gyroy.setText(formatter.format(datas[base + 1]));
				gyroz.setText(formatter.format(datas[base + 2]));
				base = 6;
				gravx.setText(formatter.format(datas[base + 0]));
				gravy.setText(formatter.format(datas[base + 1]));
				gravz.setText(formatter.format(datas[base + 2]));
				base = 9;
				eulx.setText(formatter.format(datas[base + 0]));
				euly.setText(formatter.format(datas[base + 1]));
				eulz.setText(formatter.format(datas[base + 2]));

			});
		});
		fieldSim.addEvent(1910, () -> {
			try {
				if (piddata == null)
					piddata = new double[5];
				fieldSim.readFloats(1910, piddata);
				int myNumPid = (int) piddata[0];
				if (numPIDControllers != myNumPid) {
					numPIDControllers=myNumPid;
					setUpPid();
				}
				double pos = piddata[1+currentIndex*2+1];
				double set = piddata[1+currentIndex*2+0];
				String positionVal = formatter.format(pos);
				//System.out.println(positionVal+" "+DoubleStream.of(piddata).boxed().collect(Collectors.toCollection(ArrayList::new)));
;
				Platform.runLater(() ->position.setText(positionVal ));
				Platform.runLater(() ->updateGraph(pos, set));
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		fieldSim.addEvent(1857, () -> {
			try {
				if (pidConfig == null)
					pidConfig = new double[3*2];
				fieldSim.readFloats(1857, pidConfig);
				
				//System.out.println(" "+DoubleStream.of(pidConfig).boxed().collect(Collectors.toCollection(ArrayList::new)));

				Platform.runLater(() ->kp.setText(formatter.format(pidConfig[currentIndex*3+0]) ));
				
				Platform.runLater(() ->ki.setText(formatter.format(pidConfig[currentIndex*3+1]) ));

				Platform.runLater(() ->kd.setText(formatter.format(pidConfig[currentIndex*3+2]) ));

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		fieldSim.updatConfig();

	}
	@SuppressWarnings("unchecked")
	private void updateGraph(double pos,double set) {
		if(pidGraphSeries.size()==0)
			return;
		double now  =((double)System.currentTimeMillis())/1000.0-start;
		long thispos = (long) pos;
		long thisSet=(long) set;
		if(thispos != lastPos || thisSet!=lastSet) {
			pidGraphSeries.get(0).getData().add(new XYChart.Data( now-0.0001, lastPos));
			pidGraphSeries.get(1).getData().add(new XYChart.Data( now-0.0001, lastSet));
			lastSet=thisSet;
			lastPos=thispos;
			pidGraphSeries.get(0).getData().add(new XYChart.Data( now, pos));
			pidGraphSeries.get(1).getData().add(new XYChart.Data( now, set));
		}
		for(Series s:pidGraphSeries) {		
			while(s.getData().size()>500) {
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
			pidChannel.getSelectionModel().selectedIndexProperty().addListener((obs,old,newVal)->{
				System.out.println("Set to channel "+newVal);
				currentIndex=newVal.intValue();
				fieldSim.updatConfig();

			});
		}
	}

	public static void disconnect() {
		if (me.getRobot() != null)
			me.getRobot().disconnect();
	}
}
