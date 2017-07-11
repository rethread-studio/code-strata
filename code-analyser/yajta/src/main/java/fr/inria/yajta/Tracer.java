package fr.inria.yajta;

import javassist.*;
import javassist.Modifier;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.*;

import static javassist.CtClass.voidType;

public class Tracer implements ClassFileTransformer {

    boolean verbose = false;
    boolean strictIncludes = false;
    boolean printTree = true;


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
        new Tracer(includes,excludes,new String[0]);
    }

    public Tracer (String[] includes, String excludes[], String isotopes[]) {
        INCLUDES = includes;
        DEFAULT_EXCLUDES = excludes;
        ISOTOPES = isotopes;
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
    String[] ISOTOPES;

    String[] INCLUDES;// = new String[] {};

    static final String PREFIX = "[yalta filter]";

    public byte[] transform( final ClassLoader loader, final String className, final Class clazz,
                             final java.security.ProtectionDomain domain, final byte[] bytes ) {

        for( String isotope : ISOTOPES ) {

            if( className.startsWith( isotope ) ) {
                return doClass( className, clazz, bytes, true);
            }
        }

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

        if(!strictIncludes) return doClass( className, clazz, bytes );
        else return bytes;
    }

    public byte[] doClass( final String name, final Class clazz, byte[] b ) {
        return doClass(name,clazz,b,false);
    }
    public byte[] doClass( final String name, final Class clazz, byte[] b, boolean isIsotope ) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass( new java.io.ByteArrayInputStream( b ) );
            if( cl.isInterface() == false ) {
                //doMethod(cl.getClassInitializer() , name);

                /*CtConstructor[] constructors = cl.getConstructors();

                for( int i = 0; i < constructors.length; i++ ) {

                    if( constructors[i].isEmpty() == false ) {
                        doMethod( constructors[i] , name);
                    }
                }*/

                CtBehavior[] methods = cl.getDeclaredBehaviors();

                /*for( int i = 0; i < methods.length; i++ ) {


                    if(Modifier.isNative(methods[i].getModifiers()) && !methods[i].getName().startsWith("wrapped__native__method__")) {
                        System.err.println( "Class  " + name + ", m : " + methods[i].getName() );
                        CtMethod m = (CtMethod) methods[i];
                        String mName = m.getName();
                        m.setName("wrapped__native__method__" + m.getName());
                        String body = "{";
                        if(!m.getReturnType().getName().equals("java.lang.void")) {
                            body += "return ";
                        }
                        body += m.getName() + "($$);}";
                        System.err.println( "1");

                        CtMethod newM = CtNewMethod.make(
                                m.getModifiers() & (~java.lang.reflect.Modifier.NATIVE),
                                m.getReturnType(),
                                mName,
                                m.getParameterTypes(),
                                m.getExceptionTypes(),
                                body,
                                cl
                        );
                        System.err.println( "2");
                        cl.addMethod(newM);
                        System.err.println( "3");

                    }
                }
                methods = cl.getDeclaredBehaviors();*/

                for( int i = 0; i < methods.length; i++ ) {

                    if( methods[i].isEmpty() == false ) {
                        if(isIsotope)
                            doMethod( methods[i] , name, isIsotope, "fr.inria.singleusagedemo.collections.MyMap");
                        else
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
        doMethod(method,className,false,null);
    }

    private void doMethod( final CtBehavior method , String className, boolean isIsotope, String isotope) throws NotFoundException, CannotCompileException {

        if(!Modifier.isNative(method.getModifiers())) {
            String pprefix = "", ppostfix = "";
            if(isIsotope && !Modifier.isStatic(method.getModifiers())) {
                //System.err.println("[Isotope] " + className + " " + method.getName());
                pprefix = "if(getClass().getName().equalsIgnoreCase(\"" + isotope + "\")) {";
                ppostfix = "}";
            } else if(isIsotope) {
                pprefix = "if(false) {";
                ppostfix = "}";
            } else {
                //System.err.println("[Vanilla] " + className + " " + method.getName());
            }
            String THREAD = "";
            //String THREAD = "\"thread\":\"" + Thread.currentThread().getName() + "\", ";
            String params = "(";
            boolean first = true;
            for (CtClass c : method.getParameterTypes()) {
                if (first) first = false;
                else params += ", ";
                params += c.getName();
            }
            params += ")";

            if (printTree) {
                //method.insertBefore(pprefix + "System.out.println(\"" + PREFIX + "{ \\\"name\\\": \\\"" + className.replace("/", ".") + "." + method.getName() + params + "\\\",\\n" +
                //        "" + PREFIX + THREAD + "\\\"children\\\":[\");" + ppostfix);

                //method.insertAfter(pprefix + "System.out.println(\"" + PREFIX + "]},\");" + ppostfix);

                method.insertBefore(pprefix + "fr.inria.yajta.Logger.getInstance().log(Thread.currentThread().getName(),\"" + className.replace("/", ".") + "." + method.getName() + params + "\");" + ppostfix);
                method.insertAfter(pprefix + "fr.inria.yajta.Logger.getInstance().done(Thread.currentThread().getName());" + ppostfix);
            } else {
                method.insertBefore(pprefix + "System.out.println(\"" + PREFIX + className.replace("/", ".") + "." + method.getName() + params + "\");" + ppostfix);
            }

            //}
        } else {
            if(verbose) System.err.println("Method: " + className.replace("/", ".") + "." + method.getName() + " is native");
        }
    }

    /*public static void print(byte[] b, File f) {
        try {

            try {
                PrintWriter w = new PrintWriter(f);
                w.print(b);
                w.close();
            } catch (Exception ex) {
                System.err.println("Problem writing log");
                ex.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}