package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.*;
import fr.inria.Inputs.VisualvmReader;
import fr.inria.Inputs.x86Parser;
import processing.core.PApplet;

/**
 * Created by nharrand on 29/03/17.
 */
public class x86AlterView extends PApplet {
    public static ColorPicker picker;

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

            TreeCallUtils.label(t, e.packages, e.defaultLevel);

            x86Parser p = new x86Parser();
            p.readFromFile(e.x86log);
            picker = new ColorPicker(255,100,0, x86Instructions.instructionSet.size(),1);


            int[] width = t.getWidthArray();
            int[] pop = new int[t.depth];

            stroke(strokeLight);
            maxWeight = maxWeight(p,t);
            //drawMethod(j, t, s/(t.depth*3), 0, width, pop, 3);
            drawMethod(p, t, e.screenSize/(t.depth), 0, width, pop, e.nbLevel, 0, 0, 0, 0);
            if(e.save) save(e.outputDir + "/img/" + e.name + "_x86.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawMethod(x86Parser p, CallTree t, int w, int d, int[] width, int[] pop, int maxLevel, int px, int py, int pw, int ph) {
        int h = Math.max(e.screenSize / width[d], 3);
        //int x = d * w * 3;
        int x = d * w;
        int y = pop[d] * h;

        stroke(50);
        if((px != 0) || (py != 0) || (pw != 0) || (ph != 0))
            line(px + (pw/2), py + (ph / 2), x + w/2, y + (h / 2));

        pop[d]++;
        drawMethodX86(p, t, x, y, w, h);
        for (CallTree c : t.children) {
            if(c.level <= maxLevel) {
                drawMethod(p, c, w, d + 1, width, pop, maxLevel, x, y, w, h);
            }
        }
    }

    public void drawMethodX86(x86Parser p, CallTree t, int x, int y, int w, int h) {
        String method = t.name;
        if(method.compareTo("Self time") == 0) method = t.parent.name;
        int[] bytes = p.instructions.getInstructions(method);
        if(bytes != null){
            for(int i = 0; i < bytes.length; i++){
                noStroke();
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
            }
        } else {
            System.out.println("method '" + method + "' not found");
        }
        noStroke();
        fill(50,50,50);
        ellipse(x+w/2, y+h/2, 10, 10);
    }

    public int maxWeight(x86Parser p, CallTree t) {
        int max = 0;
        String method = t.name;
        if(method.compareTo("Self time") == 0) method = t.parent.name;
        int[] bytes = p.instructions.getInstructions(method);
        if(bytes != null) max = bytes.length;
        for(CallTree c : t.children) max = Math.max(max, maxWeight(p, c));
        return max;
    }

}