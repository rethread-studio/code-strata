package fr.inria.yajta;

import java.lang.instrument.Instrumentation;


public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        //java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();
        //logger.s
        System.out.println("premain");
        final Tracer transformer = new Tracer();
        inst.addTransformer(transformer);
    }

}
