package fr.inria.yajta;



import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import org.slf4j.instrumentation.JavassistHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;


public class Tracer implements ClassFileTransformer {
    static final String deflog = "private static fr.inria.yajta.Logger _inriaLogger;";


    /** classes to always not to instrument */
    static final String[] DEFAULT_EXCLUDES = new String[] {
            //"com/sun/", "sun/", "java/", "javax/", "org/slf4j"
    };

    /** only this classes should instrument or leave empty to instrument all classes that not excluded */
    static final String[] INCLUDES = new String[] {
            // "org/bouncycastle/crypto/encodings/", "org/bouncycastle/jce/provider/JCERSACipher"
    };

    /** the jul logger definition */
    static final String def = "private static java.util.logging.Logger _log;";

    /** the jul logging if statement  */
    static final String ifLog = "if (_log.isLoggable(java.util.logging.Level.INFO))";

    /** the jul logging if statement  */

    /**
     * instrument class
     */
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

    /**
     * instrument class with javasisst
     */
    private byte[] doClass( final String name, final Class clazz, byte[] b ) {
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;

        try {
            cl = pool.makeClass( new java.io.ByteArrayInputStream( b ) );

            if( cl.isInterface() == false ) {

                /*CtField field = CtField.make( def, cl );
                String getLogger = "java.util.logging.Logger.getLogger(" + name.replace( '/', '.' ) +
                        ".class.getName());";
                cl.addField( field, getLogger );*/
                /*CtField field = CtField.make( def, cl );
                String getLogger = "java.util.logging.Logger.getLogger(" + name.replace( '/', '.' ) +
                        ".class.getName());";
                cl.addField( field, getLogger );*/

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

    /**
     * modify code and add log statements before the original method is called
     * and after the original method was called
     */
    private void doMethod( final CtBehavior method , String className) throws NotFoundException, CannotCompileException {

        //String signature = JavassistHelper.getSignature( method );
        //String returnValue = JavassistHelper.returnValue( method );

        /*method.insertBefore( ifLog + "_log.info(\">> " + signature + "\");" );

        method.insertAfter( ifLog + "_log.info(\"<< " + signature + returnValue + "\");" );*/

        String params = "(";
        boolean first = true;
        for(CtClass c : method.getParameterTypes()) {
            if(first) first = false;
            else params +=", ";
            params += c.getName();
        }
        params += ")";

        /*method.insertBefore( ifLog + "_log.info(\"{ \\\"Name\\\": \\\"" + className.replace("/", ".") + "." + method.getName()  + params +  "\\\",\\n" +
                "\\\"node\\\":[\");" );

        method.insertAfter( ifLog + "_log.info(\"]},\");" );*/

        method.insertBefore("System.out.println(\"[inria filter]{ \\\"Name\\\": \\\"" + className.replace("/", ".") + "." + method.getName()  + params +  "\\\",\\n" +
                "[inria filter]\\\"node\\\":[\");" );

        method.insertAfter("System.out.println(\"[inria filter]]},\");" );


    }
}