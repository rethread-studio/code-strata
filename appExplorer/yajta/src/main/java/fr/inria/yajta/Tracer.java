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
        this(new String[]{}, new String[]{"fr/inria/yajta"});
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
                    "" + PREFIX + "\\\"children\\\":[\");");

            method.insertAfter("System.out.println(\"" + PREFIX + "]},\");");
        //}

    }
}