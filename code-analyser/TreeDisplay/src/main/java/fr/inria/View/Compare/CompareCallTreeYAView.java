package fr.inria.View.Compare;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.Compare.CompareExecution;
import fr.inria.DataStructure.Compare.CompareTree;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.TreeCallUtils;
import fr.inria.IOs.JSONReader;
import fr.inria.IOs.SimpleReader;
import fr.inria.IOs.YajtaReader;
import fr.inria.SimpleColor;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nharrand on 05/05/17.
 */
public class CompareCallTreeYAView extends PApplet {
    public static ColorPicker picker;

    static int strokeLight = 255;
    public static int maxWeight;

    public static CompareExecution e;

    public void settings(){

        e = Context.currentCompareExec;
        //e = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));

        size(e.e1.screenSize+300, e.e1.screenSize+800);

    }

    public void setup() {
        //JSONReader r = new VisualvmReader();
        //JSONReader r = new SimpleReader();
        JSONReader r = new YajtaReader();
        System.out.println("read f1");
        CallTree t1 = r.readFromFile(e.e1.trace);
        System.out.println("read f2");
        CallTree t2 = r.readFromFile(e.e2.trace);

        System.out.println("label 1");
        TreeCallUtils.label(t1, e.e1.packages, e.e1.defaultLevel);
        System.out.println("label 2");
        TreeCallUtils.label(t2, e.e2.packages, e.e2.defaultLevel);

        System.out.println("from 1");
        if(e.e1.from != null && !e.e1.from.equals("")) t1 = TreeCallUtils.from(t1, e.e1.from);
        System.out.println("from 2");
        if(e.e2.from != null && !e.e2.from.equals("")) t2 = TreeCallUtils.from(t2, e.e2.from);


        //CompareCallTree t = new CompareCallTree(t1,t2);
        CompareTree<CallTree> t = new CompareTree(t1,t2);

        int h = e.e1.screenSize / t.depth;

        //String mostFMethod = TreeCallUtils.mostFrequentMethod(TreeCallUtils.frequencies(t));
        //System.out.println("Most frequent method: " + mostFMethod);
        //TreeCallUtils.color(t,mostFMethod);


        picker = new SimpleColor(255,100,0, 4, 30);

        int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];

        width = t.getWidthArray();
        pop = new int[t.depth];

        background(255);
        fill(204, 102, 0);
        stroke(strokeLight);
        annotateNode(t, e.e1.screenSize/(3*t.depth), 0, width, pop, 10);
        drawNode(t, false);
        drawNode(t, true);


        List<String> labels = new ArrayList<>();
        for(Set<String> s : e.e1.packages.values()) {
            String str = "";
            boolean first = true;
            for(String ss : s) {
                if(first) first = false;
                else str += ", ";
                str += ss;
            }
            labels.add(str);
        }
        drawLegend(labels, e.e1.screenSize,0, 32);
        if(e.e1.save) save(e.e1.outputDir + "/img/" + e.e1.name + "_comp_" + e.e2.name +"_calltree.png");
    }

    public void annotateNode(CompareTree t, int w, int d, int[] width, int[] pop, int maxLevel) {
        float h = (float)e.e1.screenSize / (float)width[d];
        int x = d * w * 3;
        int y = Math.round((int)((float) pop[d] * h));
        pop[d]++;

        for (Object child : t.children) {
            CompareTree<CallTree> c = (CompareTree<CallTree>) child;
            annotateNode(c, w, d + 1, width, pop, maxLevel);
        }

        if(t.areNodeEquals)
            t.level = ((CallTree) t.t1).level;
        if(t.level <= maxLevel) {
            t.x = x+w/2;
            t.y = (int)(y+h/2);
        }
    }

    public void drawNode(CompareTree t, boolean select) {
        for (Object child : t.children) {
            CompareTree<CallTree> c = (CompareTree<CallTree>) child;
            if(!c.areNodeEquals || !select) {
                setColors(c);
                line(t.x, t.y, c.x, c.y);
            }
            drawNode(c,select);
        }

        if(!t.areNodeEquals || !select) {
            int r = 6 ;
            setColors(t);
            ellipse(t.x, t.y, r, r);
        }
    }

    static int strokeW = 2;

    public void setColors(CompareTree<CallTree> t) {
        if(t.areNodeEquals) {
            int[] c = picker.getColor(t.level);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        } else {
            fill(diffColor[0], diffColor[1], diffColor[2]);
            stroke(diffColor[0], diffColor[1], diffColor[2]);
        }
        strokeWeight(strokeW);
    }

    public void draw(){
    }

    public void drawLegend(List<String> labels, int x, int y, int size) {
        PFont f;
        f = createFont("Arial",size,true);
        textFont(f);
        int i = 0;// STEP 3 Specify font to be used
        for(; i < labels.size(); i++) {
            int[] c = picker.getColor(i);
            fill(c[0], c[1], c[2]);
            text(labels.get(i), x, 100+y+2*i*size);
        }
        fill(diffColor[0], diffColor[1], diffColor[2]);
        text("differences", x, 100+y+2*i*size);
    }

    int diffColor[] = {0xE3,0x26,0x36};
}
