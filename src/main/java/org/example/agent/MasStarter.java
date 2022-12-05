package org.example.agent;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MasStarter {
    public static void main(String[] args) {

        Map<String, String> createdAgents = new HashMap<>();
        Reflections r = new Reflections(MasStarter.class);

        Set<Class<?>> annotatedWith = r.getTypesAnnotatedWith(AutorunnableAgent.class);
        for (Class<?> aClass : annotatedWith) {
            AutorunnableAgent annotation = aClass.getDeclaredAnnotation(AutorunnableAgent.class);
            String agentName = annotation.name();
            int copy = annotation.count();
            int startIndex = annotation.starIndex();
            for (int i =startIndex; i < copy+startIndex; i++){
                createdAgents.put(agentName+""+i, aClass.getName());
            }
        }


        Properties pp = parseCmdLineArgs(createdAgents);
        ProfileImpl p = new ProfileImpl(pp);
        Runtime.instance().setCloseVM(true);
        Runtime.instance().createMainContainer(p);
    }

    private static Properties parseCmdLineArgs(Map<String, String> createdAgents) {
        Properties props = new Properties();
        props.setProperty("gui", "true");

        StringBuilder angets = new StringBuilder();
        for (Map.Entry<String, String> entry : createdAgents.entrySet()) {
            angets.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
        }

        props.setProperty("agents", angets.toString());
        return props;
    }

}
