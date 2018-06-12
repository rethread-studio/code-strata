package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.*;
import fr.inria.IOs.JSONReader;
import fr.inria.IOs.VisualvmReader;
import fr.inria.IOs.YajtaReader;
import processing.core.PApplet;

import java.util.jar.JarFile;

/**
 * Created by nharrand on 21/03/17.
 */
public class ByteCodeView extends PApplet {
    public ColorPicker picker = new ColorPicker(255,100,0,256,1);

    static int strokeLight = 255;

    public static Execution e;

    public void settings(){

        e = Context.currentExec;
        //e = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));
        size(e.screenSize, e.screenSize);

    }

    public void setup() {
        try {
            background(0);
            //VisualvmReader r = new VisualvmReader();
            JSONReader r = new YajtaReader();
            CallTree t = r.readFromFile(e.trace);
            int h = e.screenSize / t.depth;
            fill(204, 102, 0);

            TreeCallUtils.label(t, e.packages, e.defaultLevel);
            //t = TreeCallUtils.from(t, "fr.inria");
            t = TreeCallUtils.from(t, "null.main");

            JarParser j = new JarParser();
            for(JarFile jar :e.jars) {
                j.printMethodStubs(jar);
            }
            int[] width = t.getWidthArray();
            int[] pop = new int[t.depth];

            stroke(strokeLight);
            drawMethod(j, t, e.screenSize/(t.depth), 0, width, pop, e.nbLevel);
            if(e.save) save(e.outputDir + "/img/" + e.name + "_bytecode.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMethod(JarParser j, CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(e.screenSize / width[d], 3);
        //int x = d * w * 3;
        int x = d * w;
        int y = pop[d] * h;
        pop[d]++;
        fill(0, 0, 0, 0);
        rect(x, y, w, h);
        drawMethodByteCode(j, t, x, y, w, h);
        for (CallTree c : t.children) {
            if(c.level <= maxLevel) {
                int tmpH = Math.max(e.screenSize / width[d + 1], 3);
                drawMethod(j, c, w, d + 1, width, pop, maxLevel);
            }
        }
    }

    public void drawMethodByteCode(JarParser j, CallTree t, int x, int y, int w, int h) {
        String method = t.name;
        if(method.compareTo("Self time") == 0) method = t.parent.name;
        int[] bytes = j.methodsByteCode.get(method);
        /*if(bytes == null) {
            method = method.replace("Object", "java.lang.Object");
            method = method.replace("String", "java.lang.String");
            bytes = j.methodsByteCode.get(method);
        }*/
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
}
