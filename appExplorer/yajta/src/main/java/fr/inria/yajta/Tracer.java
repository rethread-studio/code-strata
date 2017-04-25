package fr.inria.yajta;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import java.lang.instrument.ClassFileTransformer;


public class Tracer implements ClassFileTransformer {


    static final String[] DEFAULT_EXCLUDES = new String[] {};

    static final String[] INCLUDES = new String[] {};

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

    private byte[] doClass( final String name, final Class clazz, byte[] b ) {
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

        method.insertBefore("System.out.println(\"[inria filter]{ \\\"name\\\": \\\"" + className.replace("/", ".") + "." + method.getName()  + params +  "\\\",\\n" +
                "[inria filter]\\\"children\\\":[\");" );

        method.insertAfter("System.out.println(\"[inria filter]]},\");" );


    }
}