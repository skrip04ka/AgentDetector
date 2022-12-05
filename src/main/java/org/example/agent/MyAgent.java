package org.example.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.AgentDetector;
import org.example.DetectorListener;

@Slf4j
@AutorunnableAgent(name = "A", count = 2)
public class MyAgent extends Agent {
    @SneakyThrows
    protected void setup() {
        AgentDetector adet = new AgentDetector(this.getAID(), 40000);
        adet.startDiscovering();
        adet.startSending();
        adet.subscribeOnChange(new DetectorListener() {
            @Override
            public void handle(String action, AID agent) {
                log.info("{} {}", action, agent.getLocalName());
            }
        });

        this.doWait(1000);

        addBehaviour(new PongBehaviour());
        addBehaviour(new ReceivePongBehaviour());
        addBehaviour(new PingBehaviour(this, 2000, adet));

        addBehaviour(new WakerBehaviour(this, 5000) {
            @Override
            protected void onWake() {
                adet.stopSending();
                log.info("agent detector: stop sending");
            }
        });




    }


}
