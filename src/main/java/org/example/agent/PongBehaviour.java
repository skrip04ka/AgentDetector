package org.example.agent;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PongBehaviour extends Behaviour {
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
        if (msg != null) {
            log.info("received {} from {}", msg.getContent(), msg.getSender().getLocalName());
            ACLMessage m = msg.createReply();
            m.setPerformative(ACLMessage.AGREE);
            m.setContent("pong");
            myAgent.send(m);
            log.info("send {} to {}", m.getContent(), msg.getSender().getLocalName());
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
