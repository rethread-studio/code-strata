package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.*;
import fr.inria.Inputs.VisualvmReader;
import processing.core.PApplet;

/**
 * Created by nharrand on 29/03/17.
 */
public class CallTreeAlterView  extends PApplet {
    public static ColorPicker picker;

    static int strokeLight = 255;

    public static Execution e;
    public static int maxWeight;

    public void settings(){

        e = Context.currentExec;
        //e = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));

        size(e.screenSize, e.screenSize);

    }

    public void setup() {
        VisualvmReader r = new VisualvmReader();
        CallTree t = r.readFromFile(e.trace);
        int h = e.screenSize / t.depth;

        TreeCallUtils.label(t, e.packages, e.defaultLevel);
        picker = new ColorPicker(255,100,0, e.nbLevel, 30);

        //String mostFMethod = TreeCallUtils.mostFrequentMethod(TreeCallUtils.frequencies(t));
        //System.out.println("Most frequent method: " + mostFMethod);
        //TreeCallUtils.color(t,mostFMethod);

        int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];
        maxWeight = TreeCallUtils.maxWeight(t);

        background(0);
        fill(204, 102, 0);
        noStroke();
        drawNode(t, e.screenSize/(3*t.depth), 0, width, pop, e.nbLevel);
        if(e.save) save(e.outputDir + "/img/" + e.name + "_a_calltree.png");
    }

    public void drawNode(CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(e.screenSize / width[d], 3);
        int x = d * w * 3;
        int y = pop[d] * h;
        pop[d]++;

        for (CallTree c : t.children) {
            int tmpH = Math.max(e.screenSize / width[d + 1], 3);
            if(c.level <= maxLevel) {
                setColors(c.level);
                //stroke(255);
                line(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
            }
            drawNode(c, w, d + 1, width, pop, maxLevel);
        }
        if(t.level <= maxLevel) {
            //rect(x, y, w, h);
            int r = 3 + ((t.weight * 22)/ maxWeight) ;
            //int r = 10 ;
            setColors(t.level);
            //rect(x+w/2, y+h/2, r, r);
            ellipse(x+w/2, y+h/2, r, r);
        }
    }

    public void setColors(int level) {
        /*if (level == 0) {
            fill(0, 102, 204);
            stroke(0, 102, 204);
        } else if (level == 1) {
            fill(0, 204, 102);
            stroke(0, 204, 102);
        } else {
            fill(204, 102, 0);
            stroke(204, 102, 0);
        }*/
        int[] c = picker.getColor(level);
        fill(c[0], c[1], c[2]);
        stroke(c[0], c[1], c[2]);
    }

    public void draw(){
    }
}