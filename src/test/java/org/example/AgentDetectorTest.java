package org.example;

import jade.core.AID;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentDetectorTest {

    long time = 100;

    /* container jade for work json to AID parser */
    @BeforeAll
    static void startJade() {
        ProfileImpl p = new ProfileImpl();
        Runtime.instance().setCloseVM(true);
        Runtime.instance().createMainContainer(p);
    }

    /* Запустить 2 AgentDetector, убедиться, что activeAgents =1 */
    @Test
    @SneakyThrows
    void activeAgentTest1() {

        AID aid1 = new AID("A1@192.168.3.2:1099/JADE",true);
        AID aid2 = new AID("A2@192.168.3.2:1099/JADE",true);

        AgentDetector adet1 = new AgentDetector(aid1, 40000);
        AgentDetector adet2 = new AgentDetector(aid2, 40000);

        adet1.startDiscovering();
        adet2.startDiscovering();

        assertEquals(0, adet1.getActiveAgents().size());
        assertEquals(0, adet2.getActiveAgents().size());

        adet1.startSending();
        adet2.startSending();

        Thread.sleep(time*2);

        assertEquals(1, adet1.getActiveAgents().size());
        assertEquals(1, adet2.getActiveAgents().size());

    }

    /* Запустить 2 AgentDetector, убедиться, что activeAgents =1, остановить отправку у 1 AgentDetector,
    убедиться, что у activeAgents =1 и  activeAgents =0 */
    @Test
    @SneakyThrows
    void activeAgentTest2() {

        AID aid1 = new AID("A1@192.168.3.2:1099/JADE",true);
        AID aid2 = new AID("A2@192.168.3.2:1099/JADE",true);

        AgentDetector adet1 = new AgentDetector(aid1, 40001);
        AgentDetector adet2 = new AgentDetector(aid2, 40001);

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

    }

    /* Запустить 4 AgentDetector, убедиться, что activeAgents =3 у всех экземпляров */
    @Test
    @SneakyThrows
    void activeAgentTest3() {

        AID aid1 = new AID("A1@192.168.3.2:1099/JADE",true);
        AID aid2 = new AID("A2@192.168.3.2:1099/JADE",true);
        AID aid3 = new AID("A3@192.168.3.2:1099/JADE",true);
        AID aid4 = new AID("A4@192.168.3.2:1099/JADE",true);

        AgentDetector adet1 = new AgentDetector(aid1, 40002);
        AgentDetector adet2 = new AgentDetector(aid2, 40002);
        AgentDetector adet3 = new AgentDetector(aid3, 40002);
        AgentDetector adet4 = new AgentDetector(aid4, 40002);

        adet1.startDiscovering();
        adet2.startDiscovering();
        adet3.startDiscovering();
        adet4.startDiscovering();

        adet1.startSending();
        adet2.startSending();
        adet3.startSending();
        adet4.startSending();

        Thread.sleep(time*3);

        Assertions.assertEquals(3, adet1.getActiveAgents().size());
        Assertions.assertEquals(3, adet2.getActiveAgents().size());
        Assertions.assertEquals(3, adet3.getActiveAgents().size());
        Assertions.assertEquals(3, adet4.getActiveAgents().size());


    }

    /* another test */
    @Test
    @SneakyThrows
    void startDiscoveringTest() {
        AID aid1 = new AID("A1@192.168.3.2:1099/JADE",true);
        AID aid2 = new AID("A2@192.168.3.2:1099/JADE",true);

        AgentDetector adet1 = new AgentDetector(aid1, 40003);
        AgentDetector adet2 = new AgentDetector(aid2, 40003);

        adet1.startSending();
        Thread.sleep(time*2);
        Assertions.assertEquals(0, adet2.getActiveAgents().size());

        adet2.startDiscovering();
        Thread.sleep(time*3);
        Assertions.assertEquals(1, adet2.getActiveAgents().size());


    }

    @Test
    @SneakyThrows
    void startSendingTest() {
        AID aid1 = new AID("A1@192.168.3.2:1099/JADE",true);
        AID aid2 = new AID("A2@192.168.3.2:1099/JADE",true);

        AgentDetector adet1 = new AgentDetector(aid1, 40004);
        AgentDetector adet2 = new AgentDetector(aid2, 40004);

        adet1.startDiscovering();
        Thread.sleep(time*2);
        Assertions.assertEquals(0, adet1.getActiveAgents().size());

        adet2.startSending();
        Thread.sleep(time*2);
        Assertions.assertEquals(1, adet1.getActiveAgents().size());
    }
    @Test
    @SneakyThrows
    void stopSending() {
        AID aid1 = new AID("A1@192.168.3.2:1099/JADE",true);
        AID aid2 = new AID("A2@192.168.3.2:1099/JADE",true);

        AgentDetector adet1 = new AgentDetector(aid1, 40005);
        AgentDetector adet2 = new AgentDetector(aid2, 40005);

        adet1.startDiscovering();
        adet2.startSending();
        Thread.sleep(time*2);
        Assertions.assertEquals(1, adet1.getActiveAgents().size());

        adet2.stopSending();
        Thread.sleep(time*3);
        Assertions.assertEquals(0, adet1.getActiveAgents().size());

    }


}