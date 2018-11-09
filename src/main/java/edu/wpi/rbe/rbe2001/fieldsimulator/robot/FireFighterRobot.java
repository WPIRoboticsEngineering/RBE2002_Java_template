package edu.wpi.rbe.rbe2001.fieldsimulator.robot;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import edu.wpi.SimplePacketComs.BytePacketType;
import edu.wpi.SimplePacketComs.FloatPacketType;
import edu.wpi.SimplePacketComs.PacketType;
import edu.wpi.SimplePacketComs.device.UdpDevice;
import edu.wpi.SimplePacketComs.phy.UDPSimplePacketComs;

public class FireFighterRobot extends UdpDevice  {
	private PacketType estop = new BytePacketType(1989, 64);
	private PacketType getStatus = new BytePacketType(2012, 64);
	private PacketType clearFaults = new BytePacketType(1871, 64);
	private PacketType pickOrder = new FloatPacketType(1936, 64);
	private PacketType approve = new BytePacketType(1994, 64);
	private byte[] status = new byte[1];
	private double[] pickOrderData = new double[3];
	private double[] driveStatus = new double[1];
	private FireFighterRobot(InetAddress add) throws Exception {
		super(add);
		
		for (PacketType pt : Arrays.asList(clearFaults, pickOrder, getStatus, approve,estop)) {
			addPollingPacket(pt);
		}

		addEvent(getStatus.idOfCommand, new Runnable() {
			@Override
			public void run() {
				readBytes(getStatus.idOfCommand, status);
			}
		});


		pickOrder.waitToSendMode();
		clearFaults.waitToSendMode();
		estop.waitToSendMode();
		approve.waitToSendMode();
		
		
	}
	public static List<FireFighterRobot> get(String name) throws Exception {
		HashSet<InetAddress> addresses = UDPSimplePacketComs.getAllAddresses(name);
		ArrayList<FireFighterRobot> robots = new ArrayList<>();
		if (addresses.size() < 1) {
			System.out.println("No "+FireFighterRobot.class.getSimpleName()+" found named "+name);
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
	
	public static List<FireFighterRobot> get() throws Exception {
		return get("Warehouse*");
	}

	@Override
	public String toString() {
		return getName();
	}


	public void estop() {
		estop.oneShotMode();
	}
	public double getDriveStatus() {
		return driveStatus[0];
	}
	
	public void pickOrder(double material, double angle,double dropLocation) {
		pickOrderData[0]=material;
		pickOrderData[1]=angle;
		pickOrderData[2]=dropLocation;
		writeFloats(pickOrder.idOfCommand, pickOrderData);
		pickOrder.oneShotMode();

	}


	public void clearFaults() {
		clearFaults.oneShotMode();

	}
	public void approve() {
		approve.oneShotMode();

	}


}
