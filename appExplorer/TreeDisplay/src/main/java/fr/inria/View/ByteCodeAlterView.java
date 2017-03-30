package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.*;
import fr.inria.Inputs.VisualvmReader;
import processing.core.PApplet;

import java.util.jar.JarFile;

/**
 * Created by nharrand on 29/03/17.
 *
 */
public class ByteCodeAlterView extends PApplet {
    public ColorPicker picker = new ColorPicker(255,100,0,256,1);

    static int strokeLight = 255;

    public static Execution e;

    static int maxWeight;

    public void settings(){

        e = Context.currentExec;
        //e = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));
        size(e.screenSize, e.screenSize);

    }

    public void setup() {
        try {
            background(0);
            VisualvmReader r = new VisualvmReader();
            CallTree t = r.readFromFile(e.trace);
            int h = e.screenSize / t.depth;
            fill(204, 102, 0);

            TreeCallUtils.label(t, e.packages, e.defaultLevel);

            JarParser j = new JarParser();
            for(JarFile jar :e.jars) {
                j.printMethodStubs(jar);
            }
            int[] width = t.getWidthArray();
            int[] pop = new int[t.depth];
            maxWeight = maxWeight(j,t);
            stroke(strokeLight);
            drawMethod(j, t, e.screenSize/(t.depth), 0, width, pop, e.nbLevel, 0, 0, 0, 0);
            if(e.save) save(e.outputDir + "/img/" + e.name + "_bytecode.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMethod(JarParser j, CallTree t, int w, int d, int[] width, int[] pop, int maxLevel, int px, int py, int pw, int ph) {
        int h = Math.max(e.screenSize / width[d], 3);
        //int x = d * w * 3;
        int x = d * w;
        int y = pop[d] * h;

        stroke(50);
        if((px != 0) || (py != 0) || (pw != 0) || (ph != 0))
            line(px + (pw/2), py + (ph / 2), x + w/2, y + (h / 2));

        pop[d]++;

        drawMethodByteCode(j, t, x, y, w, h);

        for (CallTree c : t.children) {
            if(c.level <= maxLevel) {
                int tmpH = Math.max(e.screenSize / width[d + 1], 3);
                drawMethod(j, c, w, d + 1, width, pop, maxLevel, x, y, w, h);

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
            for(int i = 0; i < bytes.length; i++){
                fill(255, 0, 0);
                if(bytes[i] != -1) {
                    int[] c = picker.getColor(bytes[i]);
                    fill(c[0], c[1], c[2]);
                } else{
                    fill(128,0,0);
                }
                //int r = 25;
                noStroke();
                int r = 12 + ((bytes.length * 18)/ maxWeight) ;
                arc(x+w/2, y+h/2, r, r, (2 * PI * i) / bytes.length, (2 * PI * (i + 1)) / bytes.length, PIE);
                //stroke(strokeLight);
            }
        } else {
            System.out.println("method '" + method + "' not found");
        }
        noStroke();
        fill(50,50,50);
        ellipse(x+w/2, y+h/2, 10, 10);
    }

    public int maxWeight(JarParser j, CallTree t) {
        int max = 0;
        String method = t.name;
        if(method.compareTo("Self time") == 0) method = t.parent.name;
        int[] bytes = j.methodsByteCode.get(method);
        if(bytes != null) max = bytes.length;
        for(CallTree c : t.children) max = Math.max(max, maxWeight(j, c));
        return max;
    }
}
