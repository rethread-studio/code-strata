package fr.inria;

import fr.inria.DataStructure.*;
import fr.inria.DataStructure.Compare.CompareExecution;
import fr.inria.Inputs.*;
import fr.inria.View.WebStrataView;
import org.apache.commons.io.FileUtils;
import processing.core.PApplet;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.jar.JarFile;

import static fr.inria.DataStructure.SysCall.types;


/**
 * Hello world!
 *
 */
public class App
{

    public static void main(String... args){
        //generateCodeStrata(new File(args[0]));

        /*Context.currentCompareExec = new CompareExecution();
        Context.currentCompareExec.e1 = PropertiesReader.readProperties(new File("inputsFiles/Base32/Base32Test.properties"));
        Context.currentCompareExec.e2 = PropertiesReader.readProperties(new File("inputsFiles/Base32/Base32Test_addMI.properties"));
        PApplet.main("fr.inria.View.Compare.CompareCallTreeView");*/

        //generateCodeStrata(new File("inputsFiles/Base32/Base32InputStreamTest.properties"));
        //generateCodeStrata(new File("inputsFiles/Base32/Base32InputStreamTest_addMI.properties"));

        //generateCodeStrata(new File("inputsFiles/Base32/Base32OutputStreamTest.properties"));
        //generateCodeStrata(new File("inputsFiles/Base32/Base32OutputStreamTest_addMI.properties"));

        //generateCodeStrata(new File("inputsFiles/Base32/Base32Test.properties"));
        //generateCodeStrata(new File("inputsFiles/Base32/Base32Test_addMI.properties"));


        generateCodeStrata(new File("inputsFiles/Sort/QuickSortTest.properties"));
        //generateCodeStrata(new File("inputsFiles/Sort/QuickSortTest_full.properties"));
        //generateCodeStrata(new File("inputsFiles/Sort/BoGoSortTest.properties"));
        //generateCodeStrata(new File("inputsFiles/Sort/BoGoSortTest_full.properties"));

        /*Context.currentExec = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));
        ExecutionWritter w = new ExecutionWritter();
        w.toJSON();*/
        //Context.currentExec = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));
        //Context.currentExec = PropertiesReader.readProperties(new File("inputsFiles/Base32/Base32Test_addMI.properties"));
        //PApplet.main("fr.inria.View.CallTreeAlterView");
        //PApplet.main("fr.inria.View.ByteCodeAlterView");
        //PApplet.main("fr.inria.View.x86AlterView");
    }

    public static void generateCodeStrata(File properties) {
        Context.currentExec = PropertiesReader.readProperties(properties);

        try {
            FileUtils.copyFile(new File(App.class.getClassLoader().getResource("question-mark.png").getFile()),
                    new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_default.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int strataNb = 8;
        Map<Integer, File> strataImages = new HashMap<>();
        strataImages.put(7, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_default.png"));
        //strataImages.put(7, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_demo.gif"));
        strataImages.put(6, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_calltree_app.png"));
        strataImages.put(5, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_calltree.png"));
        strataImages.put(4, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_bytecode.png"));
        strataImages.put(3, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_syscall.png"));
        strataImages.put(2, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_default.png"));
        strataImages.put(1, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_default.png"));
        strataImages.put(0, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_x86.png"));

        Map<Integer, String> strataDesc = new HashMap<>();
        strataDesc.put(7,"\t\t\t\t\t\t\t<h3>Application</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tApplication as seen by the user.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(6,"\t\t\t\t\t\t\t<h3>Application calls</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tEach rectangle represents the invocation of a java method written by the application author(s). White links represent the way each method call sub methods.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(5,"\t\t\t\t\t\t\t<h3>Application and libraries calls</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tEach rectangle represents the invocation of a java method, in blue are methods of the application, whereas in green are methods from the library javafx (handling the graphical interface). White links represents the way each method call sub methods.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(4,"\t\t\t\t\t\t\t<h3>Byte code</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tThe jvm (java virtual machine) doesn't read java code. Instead this code is transformed in a more compact form called bytecode. Each color segment represents a bytecode instruction.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(3,"\t\t\t\t\t\t\t<h3>System calls</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tEach square represent a system call. A system call is an operation handled by the operating system.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(2, "");
        strataDesc.put(1, "");
        strataDesc.put(0, "\t\t\t\t\t\t\t<h3>x86</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tIn order to executed by micro processor, a program must be converted in a serie of instructions that the processor understand. A common instruction set nodays is x86.\n" +
                "\t\t\t\t\t\t\t\tEach color segment represents an x86 instruction.\n" +
                "\t\t\t\t\t\t\t</p>");

        if(!Context.currentExec.outputDir.exists()) Context.currentExec.outputDir.mkdirs();

        WebStrataView web = new WebStrataView(Context.currentExec, strataNb, strataImages, strataDesc);
        web.generateWeb();

        PApplet.main("fr.inria.View.CallTreeAlterView");
        PApplet.main("fr.inria.View.CallTreeView");
        PApplet.main("fr.inria.View.ByteCodeView");
        PApplet.main("fr.inria.View.SysCallView");
        PApplet.main("fr.inria.View.X86View");
        PApplet.main("fr.inria.View.MiniMapView");
    }
}
