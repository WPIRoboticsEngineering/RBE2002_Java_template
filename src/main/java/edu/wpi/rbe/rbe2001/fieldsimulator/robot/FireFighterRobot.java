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

    private FloatPacketType IMU = new FloatPacketType(1804, 64);

	private FireFighterRobot(InetAddress add) throws Exception {
		super(add);
		
		for (PacketType pt : Arrays.asList(IMU)) {
			addPollingPacket(pt);
		}
	
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
	

	@Override
	public String toString() {
		return getName();
	}


}
