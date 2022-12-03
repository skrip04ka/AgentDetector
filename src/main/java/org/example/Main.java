package org.example;

import jade.core.AID;
import org.pcap4j.core.Inets;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.factory.PacketFactories;

import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws UnknownHostException, SocketException, InterruptedException, ExecutionException {
//        System.out.println("Hello world!");

        String data = "hello";
        String iface = "\\Device\\NPF_Loopback";

        byte[] platformDescBytes = data.getBytes();
        int dataLength = platformDescBytes.length;
        int totalLength = dataLength + 28;

        byte [] packet = new byte[totalLength + (iface.equals("\\Device\\NPF_Loopback") ? 4 : 14)];
        /* send to all*/
        byte[] ipDestinationBytes = new byte[0];
        byte[] ipSourceBytes = new byte[0];
        ipDestinationBytes = InetAddress.getByName("255.255.255.255").getAddress();
        ipSourceBytes = InetAddress.getByName("127.0.0.1").getAddress();
        int addCount = 0;

        /* set NPF_Loopback as iface to use. WORKS ONLY FOR WINDOWS*/
        for (int i = 0, j = 7; i < 1; i++, j++) packet[i] = longToBytes(0x02)[j];
        System.out.println(Arrays.toString(longToBytes(0x02)));




//
//        String data = "hello";
//        String iface = "\\Device\\NPF_Loopback";
//
//        AID a1 = new AID();
//        a1.setName("A1@192.168.3.2:1099/JADE");
//
//        AID a2 = new AID();
//        a2.setName("A2@192.168.3.2:1099/JADE");

//        AgentDetector adet1 = new AgentDetector(a1, 4000);
//        AgentDetector adet2 = new AgentDetector(a2, 4000);
//        AgentDetector adet3 = new AgentDetector("adet3", 4002, 4001);
//        AgentDetector adet4 = new AgentDetector("adet4", 4003, 4000);


//        adet1.startDiscovering();
//        adet2.startDiscovering();
//        adet3.startDiscovering();
//        adet4.startDiscovering();

//        adet1.startSending();
//
//        Thread.sleep(1000);
//        System.out.println(adet2.getActiveAgents());

//        adet2.startSending();
//        adet3.startSending();
//        adet4.startSending();


//        Thread.sleep(10000);
//        System.out.println("adet2 stop send");
//        adet2.stopSending();
//
//        Thread.sleep(10000);
//        System.out.println("adet2 start send");
//        adet2.startSending();

//        Thread.sleep(10000);
//
//        adet1.startSending();
//        Thread.sleep(10000);

    }

    static  byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}