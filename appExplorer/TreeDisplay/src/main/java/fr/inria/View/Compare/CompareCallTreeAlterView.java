package fr.inria.View.Compare;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.Compare.CompareCallTree;
import fr.inria.DataStructure.Compare.CompareExecution;
import fr.inria.DataStructure.Compare.CompareTree;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.TreeCallUtils;
import fr.inria.IOs.JSONReader;
import fr.inria.IOs.SimpleReader;
import processing.core.PApplet;

/**
 * Created by nharrand on 25/04/17.
 */
public class CompareCallTreeAlterView extends PApplet {
    public static ColorPicker picker;

    static int strokeLight = 255;
    public static int maxWeight;

    public static CompareExecution e;

    public void settings(){

        e = Context.currentCompareExec;
        //e = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));

        size(e.e1.screenSize, e.e1.screenSize);

    }

    public void setup() {
        //JSONReader r = new VisualvmReader();
        JSONReader r = new SimpleReader();
        CallTree t1 = r.readFromFile(e.e1.trace);
        CallTree t2 = r.readFromFile(e.e2.trace);

        TreeCallUtils.label(t1, e.e1.packages, e.e1.defaultLevel);
        TreeCallUtils.label(t2, e.e2.packages, e.e2.defaultLevel);

        //CompareCallTree t = new CompareCallTree(t1,t2);
        CompareTree<CallTree> t = new CompareTree(t1,t2);

        int h = e.e1.screenSize / t.depth;

        //String mostFMethod = TreeCallUtils.mostFrequentMethod(TreeCallUtils.frequencies(t));
        //System.out.println("Most frequent method: " + mostFMethod);
        //TreeCallUtils.color(t,mostFMethod);


        picker = new ColorPicker(255,100,0, 4, 30);

        int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];

        background(0);
        fill(204, 102, 0);
        stroke(strokeLight);
        drawNode(t, e.e1.screenSize/(3*t.depth), 0, width, pop, 0);
        if(e.e1.save) save(e.e1.outputDir + "/img/" + e.e1.name + "_comp_" + e.e2.name +"_calltree_app.png");

        width = t.getWidthArray();
        pop = new int[t.depth];

        background(0);
        fill(204, 102, 0);
        stroke(strokeLight);
        drawNode(t, e.e1.screenSize/(3*t.depth), 0, width, pop, 10);
        if(e.e1.save) save(e.e1.outputDir + "/img/" + e.e1.name + "_comp_" + e.e2.name +"_calltree.png");
    }

    /*public void drawNode(CompareCallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(e.e1.screenSize / width[d], 3);
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
        if(!t.areNodeEquals) {
            fill(200, 0, 0);
        }


        //if(t.level <= maxLevel) {
        rect(x, y, w, h);
        //}

        for (CompareCallTree c : t.children) {
            int tmpH = Math.max(e.e1.screenSize / width[d + 1], 3);
            if(c.level <= maxLevel) {
                line(x + w, y + (h / 2), x + 3 * w, tmpH * pop[d + 1] + (tmpH / 2));
            }
            drawNode(c, w, d + 1, width, pop, maxLevel);
        }
    }*/

    public void drawNode(CompareTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        int h = Math.max(e.e1.screenSize / width[d], 3);
        int x = d * w * 3;
        int y = pop[d] * h;
        pop[d]++;

        for (Object child : t.children) {
            CompareTree<CallTree> c = (CompareTree<CallTree>) child;
            int tmpH = Math.max(e.e1.screenSize / width[d + 1], 3);
            if(c.level <= maxLevel) {
                setColors(c);
                //stroke(255);
                line(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
            }
            drawNode(c, w, d + 1, width, pop, maxLevel);
        }
        if(t.level <= maxLevel) {
            //int r = 3 + ((t.weight * 22)/ maxWeight) ;
            int r = 10 ;
            setColors(t);
            ellipse(x+w/2, y+h/2, r, r);
        }
    }

    static int strokeW = 2;

    public void setColors(CompareTree t) {
        if(t.areNodeEquals) {
            int[] c = picker.getColor(0);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        } else if (t.t1 != null && t.t2 != null) {
            int[] c = picker.getColor(1);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        } else if (t.t1 != null) {
            int[] c = picker.getColor(2);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        } else {
            int[] c = picker.getColor(3);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        }
        strokeWeight(strokeW);
    }

    public void draw(){
    }
}