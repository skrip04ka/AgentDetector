package org.example;

import jade.core.AID;
import lombok.extern.slf4j.Slf4j;
import org.example.helper.*;
import org.pcap4j.core.PacketListener;
import org.pcap4j.packet.Packet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class AgentDetector {
    public static final String ADD="add", REMOVE="remove";
    private final String ifaceName = "\\Device\\NPF_Loopback";
    private final long timeDelay = 100;
    private final AID myAgent;
    private byte[] packet;
    private final long port;
    private ScheduledFuture<?> sendTask;
    private ScheduledFuture<?> discoverTask;
    private final ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(3);
    private final PcapHelper pcapHelper = new PcapHelper(ifaceName, 50);
    private final Map<AID, Date> activeAgents = new ConcurrentHashMap<>();
    private final List<DetectorListener> subscribers = new ArrayList<>();


    public AgentDetector(AID myAgent, long port) {
        this.port = port;
        this.myAgent = myAgent;
        packet = new PacketBuilder()
                .addHeader(ifaceName)
                .addUdpPart(port)
                .addPayload(JsonParser.dataToString(new AidDTO(myAgent)))
                .build();
    }

    public void startDiscovering() {
        if (discoverTask == null) {
            log.debug("Discovering start");
            discoverTask = pcapHelper.startPacketsCapturing(port,
                    new PListener(ifaceName, myAgent, activeAgents, subscribers), ses);
            ses.scheduleWithFixedDelay(this::deadAgentRemoving, 0, 50, TimeUnit.MILLISECONDS);
        } else {
            log.warn("Discovering is start");
        }
    }

    public void startSending() {
        if (sendTask == null || sendTask.isCancelled()) {
            log.debug("Sending start");
            sendTask = ses.scheduleWithFixedDelay(() -> {
                pcapHelper.sendPacket(packet);
            }, 0, timeDelay, TimeUnit.MILLISECONDS);
        } else {
            log.warn("Sending is start");
        }
    }

    private void deadAgentRemoving() {
        Date curDate = new Date();
        for (AID agent : activeAgents.keySet()) {
            if (curDate.getTime() - activeAgents.get(agent).getTime() > timeDelay * 2) {
                activeAgents.remove(agent);
                log.debug("remove {}", agent.getLocalName());
                subscribers.forEach( el -> el.handle(AgentDetector.REMOVE, agent));
            }
        }
    }

    public List<AID> getActiveAgents() {
        return new ArrayList<>(activeAgents.keySet());
    }

    public void subscribeOnChange(DetectorListener subscriber) {
        subscribers.add(subscriber);
    }

    public void stopSending() {
        log.debug("Sending stop");
        sendTask.cancel(true);
    }

    private class PListener implements PacketListener {
        private AID aid;
        private Map<AID, Date> activeAgent;
        private final String iface;
        private List<DetectorListener> subscribers;

        public PListener(String ifaceName, AID aid,
                         Map<AID, Date> activeAgents,
                         List<DetectorListener> subscribers) {
            this.iface = ifaceName;
            this.aid = aid;
            this.activeAgent = activeAgents;
            this.subscribers = subscribers;
        }

        @Override
        public void gotPacket(Packet packet) {
            String packetData = parse(packet.getRawData());
            AID otherAid = JsonParser.parseData(packetData, AidDTO.class).toAid();
            if (!aid.equals(otherAid)) {
                log.debug("received msg {}", JsonParser.dataToString(otherAid));
                if (!activeAgent.containsKey(otherAid)) {
                    subscribers.forEach( el -> el.handle(AgentDetector.ADD, otherAid));
                }
                activeAgent.put(otherAid, new Date());
            }
        }

        private String parse(byte[] data){
            if (data.length < 14) return null;
            int offset = (iface.equals("\\Device\\NPF_Loopback") ? 4 /*local*/ : 14 /*ethernet*/) + 20 /*ipv4*/ + 8 /* udp */;

            byte[]dataByte = new byte[data.length-offset];
            System.arraycopy(data,offset,dataByte,0,dataByte.length);
            return new String(dataByte);
        }
    }
}
