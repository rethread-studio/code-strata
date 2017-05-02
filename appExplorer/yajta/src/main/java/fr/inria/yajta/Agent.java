package fr.inria.yajta;


import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;


public class Agent {
    static String[] INCLUDES = new String[] {
    };

    public static void premain(String agentArgs, Instrumentation inst) {
        //java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();
        System.err.println("[Premain] Begin");
        Args a = new Args();
        a.parseArgs(agentArgs);

        final Tracer transformer = new Tracer(format(a.INCLUDES),format(a.EXCLUDES));
        INCLUDES = a.INCLUDES;
        inst.addTransformer(transformer, true);

        Class cl[] = inst.getAllLoadedClasses();

        //System.err.println("isRedefineClassesSupported: " + inst.isRedefineClassesSupported());

        for(int i = cl.length-1; i >= 0; i--) {
            for( String include : INCLUDES ) {

                if (cl[i].getName().startsWith(include)) {
                    try {
                        //System.err.println(cl[i].getName());
                        inst.retransformClasses(cl[i]);

                    } catch (UnmodifiableClassException e) {
                        System.err.println("err: " + cl[i].getName());
                    }
                } else {

                }
            }
        }
        System.err.println("[Premain] Done");


    }

    public static String[] format(String[] ar) {
        String[] res = new String[ar.length];
        for(int i = 0; i < ar.length; i++) res[i] = ar[i].replace(".","/");
        return res;
    }



}
