package fr.inria;

import fr.inria.DataStructure.*;
import fr.inria.Inputs.PropertiesReader;
import fr.inria.Inputs.SysCallReader;
import fr.inria.Inputs.VisualvmReader;
import fr.inria.Inputs.x86Parser;
import fr.inria.View.WebStrataView;
import processing.core.PApplet;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.jar.JarFile;

import static fr.inria.DataStructure.SysCall.types;


/**
 * Hello world!
 *
 */
public class App extends PApplet
{
    /*
    final int s = 1200;
    boolean save = true;
    int strokeLight = 255;

    public void settings(){
        size(s, s);

    }

    public void setup() {
        drawX86Code();
        //drawByteCode();
        drawCallTree();
        drawSysCall();
    }

    public void drawS(int x, int y, int w, int h) {
        fill(h,100,100);
        rect(x,y,w+2,w+2);
    }

    public void drawSysCall() {
        background(0);
        //colorMode(HSB, 360, 100, 100);
        SysCallReader r = new SysCallReader();
        List<SysCall> sysCalls = r.readFromFile(new File("inputsFiles/syscall.log"));
        List<String> colors = new ArrayList<>(SysCall.types);
        //Map<String, Color> colors = new HashMap();
        //int i = 0;
        int step = 360 / SysCall.types.size();


        ColorPicker colorPicker = new ColorPicker(255,100,0,types.size());
        int n = (int) Math.floor(Math.sqrt(sysCalls.size()));
        n++;
        int w = s / n;

        int x = 0, y = 0;
        for(SysCall s : sysCalls) {
            if(x >= n) {
                x = 0;
                y++;
            }
            //fill(colors.indexOf(s.name) * step,100,100);
            int[] c = colorPicker.getColor(colors.indexOf(s.name));
            fill(c[0], c[1], c[2]);
            System.out.println("(" + x + ", " + y + ", " + w + ") - (" + colors.indexOf(s.name) * step + ")");
            rect(x*(w+1), y*(w+1), w+2, w+2);
            x++;
        }

        if(save) save("syscall.png");
    }

    public void drawCallTree() {
        background(0);
        VisualvmReader r = new VisualvmReader();
        //CallTree t = r.readFromFile(new File("inputsFiles/trace.json"));
        CallTree t = r.readFromFile(new File("inputsFiles/CharsetsTest.json"));
        int h = s / t.depth;
        fill(204, 102, 0);
        //rect(0, 10, 100, 400);
        Set<String> appPackages = new HashSet<>();
        appPackages.add("org.apache.commons.codec");

        Set<String> libs = new HashSet<>();
        //libs.add("javafx");

        TreeCallUtils.trim(t, appPackages, libs);
        String mostFMethod = TreeCallUtils.mostFrequentMethod(TreeCallUtils.frequencies(t));
        System.out.println("Most frequent method: " + mostFMethod);
        TreeCallUtils.color(t,mostFMethod);

        //drawNode(t,0,0,h,s);
        //int d = t.computeDepth();
        int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];

        stroke(strokeLight);
        drawNode(t, s/(3*t.depth), 0, width, pop, 3);
        if(save) save("calltree.png");

    }

    public void drawNode(CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(s / width[d], 3);
        int x = d * w * 3;
        int y = pop[d] * h;
        pop[d]++;
        if (t.level == 0) {
            fill(0, 102, 204);
        } else if (t.level == 1) {
            fill(0, 204, 102);
        } else {
            fill(204, 102, 0);
        }
        rect(x, y, w, h);

        for (CallTree c : t.children) {
            if(c.level <= maxLevel) {
                int tmpH = Math.max(s / width[d + 1], 3);
                line(x + w, y + (h / 2), x + 3 * w, tmpH * pop[d + 1] + (tmpH / 2));
                drawNode(c, w, d + 1, width, pop, maxLevel);
            }
        }
    }

    public ColorPicker picker = new ColorPicker(255,100,0,256,1);

    public void drawByteCode() {
        try {
            background(0);
            VisualvmReader r = new VisualvmReader();
            //CallTree t = r.readFromFile(new File("inputsFiles/trace.json"));
            CallTree t = r.readFromFile(new File("inputsFiles/CharsetsTest.json"));
            Set<String> appPackages = new HashSet<>();*/
            /*appPackages.add("Controllers");
            appPackages.add("Main");
            appPackages.add("Models");
            appPackages.add("Styling");
            appPackages.add("Views");*/
            /*
            appPackages.add("org.apache.commons.codec");

            Set<String> libs = new HashSet<>();
            libs.add("java.util");

            TreeCallUtils.trim(t, appPackages, libs);
            JarParser j = new JarParser();
            //j.printMethodStubs(new JarFile("src/test/resources/SimpleTextEditor.jar"));
            //j.printMethodStubs(new JarFile("/usr/lib/jvm/java-8-oracle/jre/lib/ext/jfxrt.jar"));
            //j.printMethodStubs(new JarFile("/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar"));

            j.printMethodStubs(new JarFile("/usr/share/java/junit4.jar"));
            j.printMethodStubs(new JarFile("/home/nharrand/Documents/sosie/commons-codec/target/commons-codec-1.10-tests.jar"));
            j.printMethodStubs(new JarFile("/home/nharrand/Documents/sosie/commons-codec/target/commons-codec-1.10.jar"));

            int[] width = t.getWidthArray();
            int[] pop = new int[t.depth];

            stroke(strokeLight);
            //drawMethod(j, t, s/(t.depth*3), 0, width, pop, 3);
            drawMethod(j, t, s/(t.depth), 0, width, pop, 3);
            if(save) save("bytecode.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMethod(JarParser j, CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(s / width[d], 3);
        //int x = d * w * 3;
        int x = d * w;
        int y = pop[d] * h;
        pop[d]++;
        fill(0, 0, 0, 0);
        rect(x, y, w, h);
        drawMethodByteCode(j, t, x, y, w, h);
        for (CallTree c : t.children) {
            if(c.level <= maxLevel) {
                int tmpH = Math.max(s / width[d + 1], 3);
                drawMethod(j, c, w, d + 1, width, pop, maxLevel);
            }
        }
    }

    public void drawMethodByteCode(JarParser j, CallTree t, int x, int y, int w, int h) {
        String method = t.name;
        if(method.compareTo("Self time") == 0) method = t.parent.name;
        int[] bytes = j.methodsByteCode.get(method);

        int nx = x+1, ny = y+1;
        if(bytes != null){
            int availaiblePixels = w * h;
            fill(0, 0, 0, 0);
            rect(nx-1, ny-1, w, h);
            for(int i = 0; i < bytes.length; i++){
                fill(255, 0, 0);
                noStroke();
                if(bytes[i] != -1) {
                    //fill(255 - (bytes[i] >> 5), 255 - ((bytes[i] - (bytes[i] >> 5)) >> 2), 255 - (bytes[i] - (bytes[i] >> 2)));
                    int[] c = picker.getColor(bytes[i]);
                    fill(c[0], c[1], c[2]);
                } else{
                    fill(128,0,0);
                }
                for(int k = (availaiblePixels * i) / (bytes.length); k < (availaiblePixels * (i+1)) / (bytes.length); k++) {
                    if(ny >= y + h - 1) break;
                    if(nx >= x + w - 1) {
                        nx = x+1;
                        ny++;
                    }
                    rect(nx, ny, 1, 1);
                    nx++;
                }
                if(ny >= y + h) break;

                stroke(strokeLight);
            }
            //System.out.println("l: " + bytes.length + " (" + x + ", " + y + ")" + " available: " + availaiblePixels + ", filled:" + (availaiblePixels * (bytes.length)) / (bytes.length));
        } else {
            System.out.println("method '" + method + "' not found");
        }
    }

    public void drawNode(CallTree t, int x, int y, int h, int w) {
        System.out.println("(" + x + ", " + y + ", " + h + ", " + w +")");
        if(t.level == 0) {
            fill(0, 102, 204);
        } else if(t.level == 1){
            fill(0, 204, 102);
        } else {
            fill(204, 102, 0);
        }
        if(w > 0) {
            rect(x, y, h, w);
        } else {
            rect(x, y, h, 3);
        }
        List<CallTree> children = t.getChildren();
        int i = 0;
        if(children.size() > 0) {
            int nw = w / children.size();
            for (CallTree c : children) {

                drawNode(c, x + h, y + i * nw, h, nw);
                i++;
            }
        }
    }*/

    public void draw(){
        //background(0);
        //ellipse(mouseX, mouseY, 20, 20);
    }

    public static void main(String... args){

        //PApplet.main("fr.inria.App");
        //PApplet.main("fr.inria.View.CallTreeView");
        //PApplet.main("fr.inria.View.ByteCodeView");
        //PApplet.main("fr.inria.View.SysCallView");
        //PApplet.main("fr.inria.View.X86View");
        //PApplet.main("fr.inria.View.MiniMapView");
        generateCodeStrata(new File(args[0]));

    }

    public static void generateCodeStrata(File properties) {
        Context.currentExec = PropertiesReader.readProperties(properties);


        int strataNb = 8;
        Map<Integer, File> strataImages = new HashMap<>();
        strataImages.put(7, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_demo.gif"));
        strataImages.put(6, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_calltree_app.png"));
        strataImages.put(5, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_calltree.png"));
        strataImages.put(4, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_bytecode.png"));
        strataImages.put(3, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_syscall.png"));
        strataImages.put(2, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_x86.png"));
        strataImages.put(1, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_x86.png"));
        strataImages.put(0, new File(Context.currentExec.outputDir, "img/" + Context.currentExec.name + "_x86.png"));

        Map<Integer, String> strataDesc = new HashMap<>();
        strataDesc.put(7,"\t\t\t\t\t\t\t<h3>Application</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tApplication as seen by the user.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(6,"\t\t\t\t\t\t\t<h3>Application calls</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tEach rectangle represents the invocation of a java method written by the application author(s). White links represents the way each method call sub methods.\n" +
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
        strataDesc.put(2, "\t\t\t\t\t\t\t<h3>x86</h3>\n" +
                "\t\t\t\t\t\t\t<p>\n" +
                "\t\t\t\t\t\t\t\tIn order to executed by micro processor, a program must be converted in a serie of instructions that the processor understand. A common instruction set nodays is x86.\n" +
                "\t\t\t\t\t\t\t\tEach color segment represents an x86 instruction.\n" +
                "\t\t\t\t\t\t\t</p>");
        strataDesc.put(1,"");
        strataDesc.put(0,"");

        if(!Context.currentExec.outputDir.exists()) Context.currentExec.outputDir.mkdirs();

        WebStrataView web = new WebStrataView(Context.currentExec, strataNb, strataImages, strataDesc);
        web.generateWeb();

        //PApplet.main("fr.inria.View.CallTreeView");
        //PApplet.main("fr.inria.View.ByteCodeView");
        //PApplet.main("fr.inria.View.SysCallView");
        //PApplet.main("fr.inria.View.X86View");
        PApplet.main("fr.inria.View.MiniMapView");
    }


/*

    public void drawX86Code() {
        try {
            background(0);
            VisualvmReader r = new VisualvmReader();

            CallTree t = r.readFromFile(new File("inputsFiles/CharsetsTest.json"));
            Set<String> appPackages = new HashSet<>();
            appPackages.add("org.apache.commons.codec");

            Set<String> libs = new HashSet<>();
            libs.add("java.util");

            TreeCallUtils.trim(t, appPackages, libs);
            x86Parser p = new x86Parser();
            p.readFromFile(new File("inputsFiles/CharsetsTest_x86.log"));
            picker = new ColorPicker(255,100,0, x86Instructions.instructionSet.size(),1);


            int[] width = t.getWidthArray();
            int[] pop = new int[t.depth];

            stroke(strokeLight);
            //drawMethod(j, t, s/(t.depth*3), 0, width, pop, 3);
            drawMethod(p, t, s/(t.depth), 0, width, pop, 3);
            if(save) save("x86.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMethod(x86Parser p, CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(s / width[d], 3);
        //int x = d * w * 3;
        int x = d * w;
        int y = pop[d] * h;
        pop[d]++;
        fill(0, 0, 0, 0);
        rect(x, y, w, h);
        drawMethodX86(p, t, x, y, w, h);
        for (CallTree c : t.children) {
            if(c.level <= maxLevel) {
                int tmpH = Math.max(s / width[d + 1], 3);
                drawMethod(p, c, w, d + 1, width, pop, maxLevel);
            }
        }
    }

    public void drawMethodX86(x86Parser p, CallTree t, int x, int y, int w, int h) {
        String method = t.name;
        if(method.compareTo("Self time") == 0) method = t.parent.name;
        int[] bytes = p.instructions.getInstructions(method);
        int nx = x+1, ny = y+1;
        if(bytes != null){
            int availaiblePixels = w * h;
            fill(0, 0, 0, 0);
            rect(nx-1, ny-1, w, h);
            for(int i = 0; i < bytes.length; i++){
                fill(255, 0, 0);
                noStroke();
                if(bytes[i] != -1) {
                    //fill(255 - (bytes[i] >> 5), 255 - ((bytes[i] - (bytes[i] >> 5)) >> 2), 255 - (bytes[i] - (bytes[i] >> 2)));
                    int[] c = picker.getColor(bytes[i]);
                    fill(c[0], c[1], c[2]);
                } else{
                    fill(128,0,0);
                }
                for(int k = (availaiblePixels * i) / (bytes.length); k < (availaiblePixels * (i+1)) / (bytes.length); k++) {
                    if(ny >= y + h - 1) break;
                    if(nx >= x + w - 1) {
                        nx = x+1;
                        ny++;
                    }
                    rect(nx, ny, 1, 1);
                    nx++;
                }
                if(ny >= y + h) break;

                stroke(strokeLight);
            }
            //System.out.println("l: " + bytes.length + " (" + x + ", " + y + ")" + " available: " + availaiblePixels + ", filled:" + (availaiblePixels * (bytes.length)) / (bytes.length));
        } else {
            System.out.println("method '" + method + "' not found");
        }
    }*/
}
