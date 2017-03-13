package fr.inria;

import javafx.scene.paint.Color;
import processing.core.PApplet;

import java.io.File;
import java.util.*;
import java.util.List;
import java.util.jar.JarFile;

import static fr.inria.SysCall.types;

/**
 * Hello world!
 *
 */
public class App extends PApplet
{
    final int s = 1200;

    public void settings(){
        size(s, s);

    }

    public void setup() {
        //drawByteCode();
        //drawCallTree();
        drawSysCall();
    }

    public void drawS(int x, int y, int w, int h) {
        fill(h,100,100);
        rect(x,y,w+2,w+2);
    }

    public void drawSysCall() {
        background(0);
        colorMode(HSB, 360, 100, 100);
        SysCallReader r = new SysCallReader();
        List<SysCall> sysCalls = r.readFromFile(new File("inputsFiles/syscall.log"));
        List<String> colors = new ArrayList<>(SysCall.types);
        //Map<String, Color> colors = new HashMap();
        //int i = 0;
        int step = 360 / SysCall.types.size();
        /*for(String s : SysCall.types) {
            colors.put(s, Color.hsb((float) i*step,1.0f,1.0f));
            i++;
        }*/
        int n = (int) Math.floor(Math.sqrt(sysCalls.size()));
        n++;
        int w = s / n;

        int x = 0, y = 0;
        for(SysCall s : sysCalls) {
            if(x >= n) {
                x = 0;
                y++;
            }
            fill(colors.indexOf(s.name) * step,100,100);
            System.out.println("(" + x + ", " + y + ", " + w + ") - (" + colors.indexOf(s.name) * step + ")");
            rect(x*(w+1), y*(w+1), w+2, w+2);
            x++;
        }

        save("syscall.png");
    }

    public void drawCallTree() {
        background(0);
        VisualvmReader r = new VisualvmReader();
        CallTree t = r.readFromFile(new File("inputsFiles/trace.json"));
        int h = s / t.depth;
        fill(204, 102, 0);
        //rect(0, 10, 100, 400);
        Set<String> appPackages = new HashSet<>();
        appPackages.add("Controllers");
        appPackages.add("Main");
        appPackages.add("Models");
        appPackages.add("Styling");
        appPackages.add("Views");

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

        stroke(255);
        drawNode(t, s/(3*t.depth), 0, width, pop, 3);
        //save("calltree.png");

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

    public void drawByteCode() {
        try {
            background(0);
            VisualvmReader r = new VisualvmReader();
            CallTree t = r.readFromFile(new File("inputsFiles/trace.json"));
            Set<String> appPackages = new HashSet<>();
            appPackages.add("Controllers");
            appPackages.add("Main");
            appPackages.add("Models");
            appPackages.add("Styling");
            appPackages.add("Views");

            Set<String> libs = new HashSet<>();
            libs.add("java.util");

            TreeCallUtils.trim(t, appPackages, libs);
            JarParser j = new JarParser();
            j.printMethodStubs(new JarFile("src/test/resources/SimpleTextEditor.jar"));
            j.printMethodStubs(new JarFile("/usr/lib/jvm/java-8-oracle/jre/lib/ext/jfxrt.jar"));
            int[] width = t.getWidthArray();
            int[] pop = new int[t.depth];

            stroke(0);
            drawMethod(j, t, s/t.depth, 0, width, pop, 3);
            save("bytecode.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMethod(JarParser j, CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(s / width[d], 3);
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
            fill(255, 0, 0);
            rect(nx, ny, w, h);
            for(int i = 0; i < bytes.length; i++){
                fill(255, 0, 0);
                noStroke();
                if(bytes[i] != -1) {
                    fill(255 - (bytes[i] >> 5), 255 - ((bytes[i] - (bytes[i] >> 5)) >> 2), 255 - (bytes[i] - (bytes[i] >> 2)));
                } else{
                    fill(0,255,0);
                }
                for(int k = (availaiblePixels * i) / (bytes.length); k < (availaiblePixels * (i+1)) / (bytes.length); k++) {
                    if(ny >= y + h) break;
                    if(nx >= x + w) {
                        nx = x;
                        ny++;
                    }
                    rect(nx, ny, 1, 1);
                    nx++;
                }
                if(ny >= y + h) break;

                stroke(0);
            }
            System.out.println("l: " + bytes.length + " (" + x + ", " + y + ")" + " available: " + availaiblePixels + ", filled:" + (availaiblePixels * (bytes.length)) / (bytes.length));
        } else {
            //System.out.println("method not found");
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
    }

    public void draw(){
        //background(0);
        //ellipse(mouseX, mouseY, 20, 20);
    }

    public static void main(String... args){
        PApplet.main("fr.inria.App");
    }
}
