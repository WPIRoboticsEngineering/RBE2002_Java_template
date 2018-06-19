package com.FieldSimulator.FieldSimulator_2001;

import edu.wpi.SimplePacketComs.BytePacketType;
import edu.wpi.SimplePacketComs.PacketType;
import edu.wpi.SimplePacketComs.phy.UDPSimplePacketComs;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FieldSimulator extends UDPSimplePacketComs {
    private static InterfaceController interfaceController;
    private PacketType FieldSim = new BytePacketType(1863, 64);
    private final double[] status = new double[15];

    private final double[] data = new double[15];

    private FieldSimulator(InetAddress add) throws Exception {
        super(add);
        addPollingPacket(FieldSim);
        addEvent(FieldSim.idOfCommand, () -> {
            readFloats(FieldSim.idOfCommand, getData());
            writeFloats(FieldSim.idOfCommand, getStatus());
            if (interfaceController.heartBeat.isSelected()) {
                interfaceController.heartBeat.setSelected(false);
            } else {
                interfaceController.heartBeat.setSelected(true);
            }
            interfaceController.response.appendText(String.valueOf(data));

        });
    }

    public static List<FieldSimulator> get(String name) throws Exception {
        HashSet<InetAddress> addresses = UDPSimplePacketComs.getAllAddresses(name);
        ArrayList<FieldSimulator> robots = new ArrayList<>();
        if (addresses.size() < 1) {
            System.out.println("No Robot found named " + name);
            return robots;
        }
        for (InetAddress add : addresses) {
            System.out.println("Got " + add.getHostAddress());
            FieldSimulator e = new FieldSimulator(add);
            e.connect();
            robots.add(e);
        }

        return robots;
    }

    public static List<FieldSimulator> get() throws Exception {
        if (interfaceController.teamName.getText() == null) {
            return get("*");
        } else {
            return get(interfaceController.teamName.getText());
        }
    }

    public double[] getStatus() {
        return status;
    }

    public double[] getData() {
        return data;
    }

    public void setPacketIndex(int index, double value) {
        if (index >= 15) {
            return;
        }
        status[index] = value;
    }
}

