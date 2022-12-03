package org.example.agent;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.AgentDetector;

@Slf4j
@AutorunnableAgent(name = "A", count = 2)
public class MyAgent extends Agent {
    @SneakyThrows
    protected void setup() {
        AgentDetector adet = new AgentDetector(this.getAID(), 40000);
        adet.startDiscovering();
        adet.startSending();

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
