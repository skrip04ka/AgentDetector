//package org.example.helper;
//
//import jade.core.AID;
//import lombok.extern.slf4j.Slf4j;
//import org.pcap4j.core.PacketListener;
//import org.pcap4j.packet.Packet;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//public class PListener implements PacketListener {
//
//    private PacketHelper ph;
//    private AID aid;
//
//    private Map<AID, Date> activeAgent;
//
//    public PListener(String ifaceName, AID aid, Map<AID, Date> activeAgents) {
//        this.ph = new PacketHelper(ifaceName);
//        this.aid = aid;
//        this.activeAgent = activeAgents;
//    }
//
//    @Override
//    public void gotPacket(Packet packet) {
//        String packetData = ph.parse(packet.getRawData());
//        AID otherAid = JsonParser.parseData(packetData, AID.class);
//        if (!aid.equals(otherAid)) {
//            System.out.println(JsonParser.dataToString(otherAid));
//            activeAgent.put(otherAid, new Date());
//        }
//
////        log.info("packet data: {}", ph.parse(packet.getRawData()));
//
//    }
//
//}
