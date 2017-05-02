package fr.inria.yajta;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import java.lang.instrument.ClassFileTransformer;

public class Tracer implements ClassFileTransformer {

    boolean verbose = false;


    public Tracer () {
        this(new String[]{"java.util.ArrayList",
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
                "java.util.WeakHashMap"}, new String[]{"fr/inria/yajta"});
    }

    public Tracer (String[] includes, String excludes[]) {
        INCLUDES = includes;
        DEFAULT_EXCLUDES = excludes;
    }

    String[] DEFAULT_EXCLUDES;// = new String[] {
            /*"java/lang/reflect.Type",
            "java/lang/Class",
            "java/lang/Cloneable",
            "java/lang/ClassLoader",
            "java/lang/System",
            "java/lang/Throwable",
            "java/lang/Error",
            "java/lang/ThreadDeath",
            "java/lang/Exception",
            "java/lang/RuntimeException",
            "java/lang/SecurityManager",
            "java/security",
            "sun/reflect",
            "java/lang/annotation",
            "java/nio",
            "java/lang/Math",
            "sun/nio",
            "java/io",*/

    //};

    String[] INCLUDES;// = new String[] {};

    static final String PREFIX = "[yalta filter]";

    public byte[] transform( final ClassLoader loader, final String className, final Class clazz,
                             final java.security.ProtectionDomain domain, final byte[] bytes ) {

        for( String include : INCLUDES ) {

            if( className.startsWith( include ) ) {
                return doClass( className, clazz, bytes );
            }
        }

        for( int i = 0; i < DEFAULT_EXCLUDES.length; i++ ) {

            if( className.startsWith( DEFAULT_EXCLUDES[i] ) ) {
                return bytes;
            }
        }

        return doClass( className, clazz, bytes );
    }

    public byte[] doClass( final String name, final Class clazz, byte[] b ) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;

        try {
            cl = pool.makeClass( new java.io.ByteArrayInputStream( b ) );
            if( cl.isInterface() == false ) {

                CtBehavior[] methods = cl.getDeclaredBehaviors();

                for( int i = 0; i < methods.length; i++ ) {

                    if( methods[i].isEmpty() == false ) {
                        doMethod( methods[i] , name);
                    }
                }

                b = cl.toBytecode();
                if(verbose) System.err.println( "-> Instrument  " + name);
            }
        } catch( Exception e ) {
            System.err.println( "Could not instrument  " + name + ",  exception : " + e.getMessage() );
        } finally {

            if( cl != null ) {
                cl.detach();
            }
        }

        return b;
    }

    private void doMethod( final CtBehavior method , String className) throws NotFoundException, CannotCompileException {
        String THREAD = "";
        //String THREAD = "\"thread\":\"" + Thread.currentThread().getName() + "\", ";
        String params = "(";
        boolean first = true;
        for(CtClass c : method.getParameterTypes()) {
            if(first) first = false;
            else params +=", ";
            params += c.getName();
        }
        params += ")";
        //if(className.compareTo("java/util/ArrayList") != 0 || method.getModifiers() == 1) {
        method.insertBefore("System.out.println(\"" + PREFIX + "{ \\\"name\\\": \\\"" + className.replace("/", ".") + "." + method.getName() + params + "\\\",\\n" +
                "" + PREFIX + THREAD + "\\\"children\\\":[\");");

        method.insertAfter("System.out.println(\"" + PREFIX + "]},\");");
        //}

    }
}