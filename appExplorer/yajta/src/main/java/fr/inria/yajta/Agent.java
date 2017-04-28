package fr.inria.yajta;


import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;


public class Agent {
    static final String[] INCLUDES = new String[] {
            "java.util.ArrayList",
            "java.util.LinkedList",
            "java.util.Stack",
            "java.util.Vector",
            "java.util.concurrent.CopyOnWriteArrayList",
            "java.util.concurrent.LinkedBlockingDeque",
            "java.util.ArrayDeque",
            "java.util.LinkedList",
            "java.util.concurrent.ConcurrentLinkedDeque",
            "java.util.concurrent.ArrayBlockingQueue",
            "java.util.concurrent.DelayQueue",
            "java.util.concurrent.LinkedBlockingQueue",
            "java.util.concurrent.LinkedTransferQueue",
            "java.util.concurrent.PriorityBlockingQueue",
            "java.util.concurrent.SynchronousQueue",
            "java.util.concurrent.LinkedTransferQueue",
            "java.util.concurrent.ConcurrentLinkedQueue",
            "java.util.concurrent.PriorityBlockingQueue",
            "java.util.concurrent.SynchronousQueue",
            "java.util.PriorityQueue",
            "java.util.concurrent.ConcurrentSkipListSet",
            "java.util.TreeSet",
            "java.util.concurrent.CopyOnWriteArraySet",
            "java.util.HashSet",
            "java.util.LinkedHashSet",
            "java.util.concurrent.ConcurrentSkipListMap",
            "java.util.concurrent.ConcurrentHashMap",
            "java.util.TreeMap",
            "java.util.HashMap",
            "java.util.Hashtable",
            "java.util.HashMap",
            "java.util.LinkedHashMap",
            "java.util.WeakHashMap"
    };

    public static void premain(String agentArgs, Instrumentation inst) {
        //java.util.logging.Logger logger = java.util.logging.Logger.getGlobal();
        System.err.println("[Premain] Begin");
        final Tracer transformer = new Tracer();
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
                }
            }
        }
        System.err.println("[Premain] Done");


    }

    public class Args {
        public String[] INCLUDES, EXCLUDES;

        public void parseArgs(String args) {

        }
    }

    public static String[] format(String[] ar) {
        String[] res = new String[ar.length];
        for(int i = 0; i < ar.length; i++) res[i] = ar[i].replace(".","/");
        return res;
    }



}
