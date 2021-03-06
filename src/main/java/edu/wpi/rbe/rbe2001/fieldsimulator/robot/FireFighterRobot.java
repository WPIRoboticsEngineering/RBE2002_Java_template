package edu.wpi.rbe.rbe2001.fieldsimulator.robot;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import edu.wpi.SimplePacketComs.BytePacketType;
import edu.wpi.SimplePacketComs.FloatPacketType;
import edu.wpi.SimplePacketComs.PacketType;
import edu.wpi.SimplePacketComs.device.UdpDevice;
import edu.wpi.SimplePacketComs.phy.UDPSimplePacketComs;
import javafx.application.Platform;

public class FireFighterRobot extends UdpDevice {

	private FloatPacketType IMU = new FloatPacketType(1804, 64);
	private FloatPacketType pidStatus = new FloatPacketType(1910, 64);
	private FloatPacketType getConfig = new FloatPacketType(1857, 64);
	private FloatPacketType setConfig = new FloatPacketType(1900, 64);
	private FloatPacketType getIR = new FloatPacketType(1590, 64);
	double [] numPID = new double[1];
	double [] pidStatusData=null;
	double [] pidConfigData=new double[3*2];
	private FireFighterRobot(InetAddress add) throws Exception {
		super(add);

		for (PacketType pt : Arrays.asList(IMU, pidStatus, getConfig, setConfig,getIR)) {
			addPollingPacket(pt);
		}
		getConfig.oneShotMode();
		setConfig.waitToSendMode();
		addEvent(1857, () -> {
			try {	
				readFloats(1857, pidConfigData);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	public static List<FireFighterRobot> get(String name) throws Exception {
		HashSet<InetAddress> addresses = UDPSimplePacketComs.getAllAddresses(name);
		ArrayList<FireFighterRobot> robots = new ArrayList<>();
		if (addresses.size() < 1) {
			System.out.println("No " + FireFighterRobot.class.getSimpleName() + " found named " + name);
			return robots;
		}
		for (InetAddress add : addresses) {
			System.out.println("Got " + add.getHostAddress());
			FireFighterRobot e = new FireFighterRobot(add);
			e.connect();
			robots.add(e);
		}
		return robots;
	}

	@Override
	public String toString() {
		return getName();
	}

	public double getNumPid() {
		readFloats(1910, numPID);
		return  numPID[0];
	}

	public double getPidSetpoint(int index) {
		double [] d = new double[1];
		readFloats(1910, d);
		return pidStatus.getUpstream()[1 + index * 2 + 0].doubleValue();
	}

	public double getPidPosition(int index) {
		return pidStatus.getUpstream()[1 + index * 2 + 1].doubleValue();
	}

	public void updatConfig() {
		getConfig.oneShotMode();
	}
	public void setPidGains(int index, double kp, double ki, double kd) {
		pidConfigData[3*index+0]=kp;
		pidConfigData[3*index+1]=ki;
		pidConfigData[3*index+2]=kd;
		writeFloats(1900, pidConfigData);
		setConfig.oneShotMode();
		
	}
	
}
