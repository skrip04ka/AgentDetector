package org.example.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.AgentDetector;
import org.example.DetectorListener;
import org.example.helper.JsonParser;

@Slf4j
public class MyAgent extends Agent {
    @SneakyThrows
    protected void setup() {
        AgentDetector adet = new AgentDetector(this.getAID(), 40000);
        adet.startDiscovering();
        DetectorListener l = new DetectorListener() {
            @Override
            public void handle(String action, AID agent) {
                log.info("{} {}", action, agent.getLocalName());
            }
        };

        adet.subsribeOnChange(l);

        if (this.getLocalName().equals("A1")) {
            adet.startSending();
        }

        Thread.sleep(3000);
        log.info("{}", adet.getActiveAgents().size());

        Thread.sleep(1000);
        if (this.getLocalName().equals("A1")) {
            adet.stopSending();
        }

        Thread.sleep(5000);
        log.info("{}", adet.getActiveAgents().size());

        if (this.getLocalName().equals("A2")) {
            adet.startSending();
        }

        Thread.sleep(3000);
        log.info("{}", adet.getActiveAgents().size());


    }


}
