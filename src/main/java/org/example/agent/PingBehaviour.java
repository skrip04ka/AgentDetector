package org.example.agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import lombok.extern.slf4j.Slf4j;
import org.example.AgentDetector;

import java.util.List;
@Slf4j
public class PingBehaviour extends TickerBehaviour {
    private AgentDetector adet;
    public PingBehaviour(Agent a, long period, AgentDetector adet) {
        super(a, period);
        this.adet = adet;
    }

    @Override
    protected void onTick() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        List<AID> agents = adet.getActiveAgents();
        if (!agents.isEmpty()) {
            agents.forEach(msg::addReceiver);
            msg.setContent("ping");
            myAgent.send(msg);
            log.info("send {} to {} agents", msg.getContent(), agents.size());
        } else {
            log.info("no active agents");
        }
    }
}
