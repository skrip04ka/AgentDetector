package org.example;

import jade.core.AID;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
class AgentDetectorTest {

    private final long time = 100;
    private int port = 40000;

    /* Запустить 2 AgentDetector, убедиться, что activeAgents =1 */
    @Test
    @SneakyThrows
    void activeAgentTest1() {

        AgentDetector adet1 = new AgentDetector(new AID("A1@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet2 = new AgentDetector(new AID("A2@192.168.3.2:1099/JADE",true), port);

        adet1.startDiscovering();
        adet2.startDiscovering();

        assertEquals(0, adet1.getActiveAgents().size());
        assertEquals(0, adet2.getActiveAgents().size());

        adet1.startSending();
        adet2.startSending();

        Thread.sleep(time*2);

        assertEquals(1, adet1.getActiveAgents().size());
        assertEquals(1, adet2.getActiveAgents().size());

        adet1.stopSending();
        adet2.stopSending();

    }

    /* Запустить 2 AgentDetector, убедиться, что activeAgents =1, остановить отправку у 1 AgentDetector,
    убедиться, что у activeAgents =1 и  activeAgents =0 */
    @Test
    @SneakyThrows
    void activeAgentTest2() {

        AgentDetector adet1 = new AgentDetector(new AID("A1@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet2 = new AgentDetector(new AID("A2@192.168.3.2:1099/JADE",true), port);

        adet1.startDiscovering();
        adet2.startDiscovering();

        adet1.startSending();
        adet2.startSending();

        Thread.sleep(time*2);

        Assertions.assertEquals(1, adet1.getActiveAgents().size());
        Assertions.assertEquals(1, adet2.getActiveAgents().size());

        adet1.stopSending();

        Thread.sleep(time*3);

        Assertions.assertEquals(1, adet1.getActiveAgents().size());
        Assertions.assertEquals(0, adet2.getActiveAgents().size());

        adet2.stopSending();

    }

    /* Запустить 4 AgentDetector, убедиться, что activeAgents =3 у всех экземпляров */
    @Test
    @SneakyThrows
    void activeAgentTest3() {

        AgentDetector adet1 = new AgentDetector(new AID("A1@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet2 = new AgentDetector(new AID("A2@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet3 = new AgentDetector(new AID("A3@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet4 = new AgentDetector(new AID("A4@192.168.3.2:1099/JADE",true), port);

        adet1.startDiscovering();
        adet2.startDiscovering();
        adet3.startDiscovering();
        adet4.startDiscovering();

        adet1.startSending();
        adet2.startSending();
        adet3.startSending();
        adet4.startSending();

        Thread.sleep(time*2);

        Assertions.assertEquals(3, adet1.getActiveAgents().size());
        Assertions.assertEquals(3, adet2.getActiveAgents().size());
        Assertions.assertEquals(3, adet3.getActiveAgents().size());
        Assertions.assertEquals(3, adet4.getActiveAgents().size());

        adet1.stopSending();
        adet2.stopSending();
        adet3.stopSending();
        adet4.stopSending();

    }

    /* another test */
    @Test
    @SneakyThrows
    void startDiscoveringTest() {

        AgentDetector adet1 = new AgentDetector(new AID("A1@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet2 = new AgentDetector(new AID("A2@192.168.3.2:1099/JADE",true), port);

        adet1.startSending();
        Thread.sleep(time*2);
        Assertions.assertEquals(0, adet2.getActiveAgents().size());

        adet2.startDiscovering();
        Thread.sleep(time*3);
        Assertions.assertEquals(1, adet2.getActiveAgents().size());

        adet1.stopSending();

    }

    @Test
    @SneakyThrows
    void startSendingTest() {

        AgentDetector adet1 = new AgentDetector(new AID("A1@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet2 = new AgentDetector(new AID("A2@192.168.3.2:1099/JADE",true), port);

        adet1.startDiscovering();
        Thread.sleep(time);
        Assertions.assertEquals(0, adet1.getActiveAgents().size());

        adet2.startSending();
        Thread.sleep(time*2);
        Assertions.assertEquals(1, adet1.getActiveAgents().size());

        adet2.stopSending();

    }
    @Test
    @SneakyThrows
    void stopSending() {

        AgentDetector adet1 = new AgentDetector(new AID("A1@192.168.3.2:1099/JADE",true), port);
        AgentDetector adet2 = new AgentDetector(new AID("A2@192.168.3.2:1099/JADE",true), port);

        adet1.startDiscovering();
        adet2.startSending();
        Thread.sleep(time*2);
        Assertions.assertEquals(1, adet1.getActiveAgents().size());

        adet2.stopSending();
        Thread.sleep(time*3);
        Assertions.assertEquals(0, adet1.getActiveAgents().size());

    }


}