package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.*;
import fr.inria.IOs.JSONReader;
import fr.inria.IOs.SimpleReader;
import fr.inria.IOs.VisualvmReader;
import fr.inria.SimpleColor;
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

        size(e.screenSize, e.screenSize+400);

    }

    public void setup() {
        //JSONReader r = new VisualvmReader();
        //JSONReader r = new AgentReader();
        JSONReader r = new SimpleReader();

        CallTree t = r.readFromFile(e.trace);
        int h = e.screenSize / t.depth;

        TreeCallUtils.label(t, e.packages, e.defaultLevel);
        //t= TreeCallUtils.from(t, "java.lang.reflect.Method.invoke");
        //t = TreeCallUtils.from(t, "QuickSortTest.quickSort()");
        t = TreeCallUtils.from(t, "QuickSort.sort(");



        t = TreeCallUtils.trimWrapper(t, e.excludes);
        //TreeCallUtils.removeLibsLeaf(t);

        //picker = new ColorPicker(255,100,0, e.nbLevel, 30);
        //background(0);
        //picker = new ColorPicker(127+64+32,75+12,0, e.nbLevel, 100);
        picker = new SimpleColor(127+64+32,75+12,0, e.nbLevel, 100);
        background(255);

        //String mostFMethod = TreeCallUtils.mostFrequentMethod(TreeCallUtils.frequencies(t));
        //System.out.println("Most frequent method: " + mostFMethod);
        //TreeCallUtils.color(t,mostFMethod);

        /*int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];
        maxWeight = TreeCallUtils.maxWeight(t);

        //fill(204, 102, 0);
        noStroke();
        drawNode(t, e.screenSize/(3*t.depth), 0, width, pop, e.nbLevel);
        if(e.save) save(e.outputDir + "/img/" + e.name + "_a_calltree.png");*/

        drawTree(t, e.outputDir + "/img/" + e.name + "_a_calltree.png");

        //if(e.exit)
            exit();
    }

    public void drawTree(CallTree t, String name) {
        int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];
        maxWeight = TreeCallUtils.maxWeight(t);

        //fill(204, 102, 0);
        noStroke();
        drawNode(t, e.screenSize/(3*t.depth), 0, width, pop, e.nbLevel);
        if(e.save) save(name);
    }

    public void drawNode(CallTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        //int h = Math.max(e.screenSize / width[d], 3);
        float h = (float)e.screenSize / (float)width[d];
        int x = d * w * 3;
        //int y = pop[d] * h;
        int y = Math.round((int)((float) pop[d] * h));
        pop[d]++;

        for (CallTree c : t.children) {
            //int tmpH = Math.max(e.screenSize / width[d + 1], 3);
            float tmpH = (float)e.screenSize / (float)(width[d + 1]);
            if(c.level <= maxLevel) {
                setColors(c.level);
                //line(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
                line(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
            }
            drawNode(c, w, d + 1, width, pop, maxLevel);
        }
        if(t.level <= maxLevel) {
            //rect(x, y, w, h);
            //int r = 3 + ((t.weight * 22)/ maxWeight) ;
            //int r = 10 ;
            int r = 6 ;
            setColors(t.level);
            //rect(x+w/2, y+h/2, r, r);
            ellipse(x+w/2, y+h/2, r, r);
        }
    }

    static int strokeW = 2;

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
        if(false) {
        //if(level > 1) {
        //if(level != 0) {

            fill(0);
            stroke(0);
        } else {
            int[] c = picker.getColor(level);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        }
        strokeWeight(strokeW);
    }

    public void draw(){
    }
}