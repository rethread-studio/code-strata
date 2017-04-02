package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.Execution;
import fr.inria.DataStructure.TreeCallUtils;
import fr.inria.Inputs.PropertiesReader;
import fr.inria.Inputs.VisualvmReader;
import processing.core.PApplet;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nharrand on 21/03/17.
 */
public class CallTreeView extends PApplet {
    public static ColorPicker picker;

    static int strokeLight = 255;

    public static Execution e;

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

        background(0);
        fill(204, 102, 0);
        stroke(strokeLight);
        drawNode(t, e.screenSize/(3*t.depth), 0, width, pop, 0);
        if(e.save) save(e.outputDir + "/img/" + e.name + "_calltree_app.png");

        width = t.getWidthArray();
        pop = new int[t.depth];

        background(0);
        fill(204, 102, 0);
        stroke(strokeLight);
        drawNode(t, e.screenSize/(3*t.depth), 0, width, pop, e.nbLevel);
        if(e.save) save(e.outputDir + "/img/" + e.name + "_calltree.png");
    }

    public void drawNode(CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(e.screenSize / width[d], 3);
        int x = d * w * 3;
        int y = pop[d] * h;
        pop[d]++;
        /*if (t.level == 0) {
            fill(0, 102, 204);
        } else if (t.level == 1) {
            fill(0, 204, 102);
        } else {
            fill(204, 102, 0);
        }*/
        setColors(t.level);
        if(t.level <= maxLevel) {
            rect(x, y, w, h);
        }


        for (CallTree c : t.children) {
                int tmpH = Math.max(e.screenSize / width[d + 1], 3);
                if(c.level <= maxLevel) {
                    line(x + w, y + (h / 2), x + 3 * w, tmpH * pop[d + 1] + (tmpH / 2));
                }
                drawNode(c, w, d + 1, width, pop, maxLevel);
        }
    }

    public void setColors(int level) {
        int[] c = picker.getColor(level);
        fill(c[0], c[1], c[2]);
    }

    public void draw(){
    }
}
