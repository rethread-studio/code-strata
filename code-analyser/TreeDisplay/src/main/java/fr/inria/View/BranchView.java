package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.Execution;
import fr.inria.DataStructure.TreeCallUtils;
import fr.inria.IOs.JSONReader;
import fr.inria.IOs.YajtaReader;
import fr.inria.SimpleColor;
import processing.core.PApplet;

public class BranchView  extends PApplet {
    public static ColorPicker picker;

    static int strokeLight = 255;

    public static Execution e;
    public static int maxWeight;

    public void settings(){

        e = Context.currentExec;

        size(e.screenSize, e.screenSize+10);

    }

    public void setup() {
        //JSONReader r = new VisualvmReader();
        //JSONReader r = new AgentReader();
        //JSONReader r = new SimpleReader();
        JSONReader r = new YajtaReader();

        CallTree t = r.readFromFile(e.trace);
        int h = e.screenSize / t.depth;

        TreeCallUtils.label(t, e.packages, e.defaultLevel);

        //t = TreeCallUtils.from(t, "org.apache.commons.codec.binary.BinaryCodecTest.testToAsciiBytes()");
        t = TreeCallUtils.from(t, "org.apache.commons.codec.binary.BinaryCodec.toAsciiBytes(byte[])");

        //t = TreeCallUtils.from(t, "org.apache.commons.collections4.map.CompositeMapTest.testAddComposited");
        //t = TreeCallUtils.from(t, "org.apache.commons.collections4.map.CompositeMapTest.testAddComposited");
        //t = TreeCallUtils.from(t, "org.apache.commons.collections4.map.CompositeMap.entrySet");
        //TreeCallUtils.highlight(t,"org.apache.commons.lang3.reflect.MethodUtils.getOverrideHierarchy(java.lang.reflect.Method, org.apache.commons.lang3.ClassUtils$Interfaces)", 2);
        //t = TreeCallUtils.from(t, "null.Thread-1");
        //t = TreeCallUtils.from(t, "null.Thread-3");
        //t = TreeCallUtils.from(t, "null.main");
        //t = TreeCallUtils.from(t, "org.apache.commons.codec.language.bm.Lang.loadFromResource");
        //TreeCallUtils.highlight(t,"org.apache.commons.codec.binary.BaseNCodec.isInAlphabet", 3);
        //t = TreeCallUtils.from(t, "org.apache.commons.codec.binary.Base32.decode");
        //t= TreeCallUtils.from(t, "fr.inria");
        //t= TreeCallUtils.from(t, "org.apache.commons.codec.language.DoubleMetaphone2Test.testDoubleMetaphonePrimary");
        //t= TreeCallUtils.from(t, "null.main");
        //t= TreeCallUtils.from(t, "java.lang.reflect.Method.invoke");
        //t = TreeCallUtils.from(t, "QuickSortTest.quickSort()");
        //t = TreeCallUtils.from(t, "QuickSort.sort(");



        t = TreeCallUtils.trimWrapper(t, e.excludes);
        //TreeCallUtils.removeLibsLeaf(t);

        //picker = new ColorPicker(255,100,0, e.nbLevel, 30);
        //background(0);
        //picker = new ColorPicker(127+64+32,75+12,0, e.nbLevel, 100);
        picker = new SimpleColor(127+64+32,75+12,0, e.nbLevel, 100);
        //picker = new EquiDistantColors(127+64+32,75+12,0, e.nbLevel, 100);
        background(255);
        //background(0);

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
                if(c.level == 1) {
                    dottedLine(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
                } else if(c.level == 2) {
                    dashedLine(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
                } else {
                    line(x + w/2, y + (h / 2), x + 3 * w + w/2, tmpH * pop[d + 1] + (tmpH / 2));
                }
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
            if(t.name.contains("@")) {
                rect(x+w/2-r/2, y+h/2-r/2, r, r);
            } else {
                ellipse(x+w/2, y+h/2, r, r);
            }
        }
    }

    static int strokeW = 2;

    public void setColors(int level) {
        if (level == 0) {
            fill(0x43, 0x43, 0x43);
            stroke(0x43, 0x43, 0x43);
        } else if (level == 1) {
            fill(0x6a, 0xa8, 0x4f);
            stroke(0x6a, 0xa8, 0x4f);
        } else if (level == 2) {

            fill(0xe6, 0x91, 0x38);
            stroke(0xe6, 0x91, 0x38);
        } else {
            fill(204, 102, 0);
            stroke(204, 102, 0);
        }
        /*if(false) {
        //if(level > 1) {
        //if(level != 0) {

            fill(0);
            stroke(0);
        } else {
            int[] c = picker.getColor(level);
            fill(c[0], c[1], c[2]);
            stroke(c[0], c[1], c[2]);
        }*/
        strokeWeight(strokeW);
    }

    public void draw(){
    }

    void dottedLine(float x1, float y1, float x2, float y2) {
        int distance = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) / 10;
        float r = 2.0f;
        for (int i = 0; i <= distance; i++) {
            float s = ((float)i) / distance;
            float x = lerp(x1, x2, s);
            float y = lerp(y1, y2, s);
            //point(x, y);
            ellipse(x-r/2, y-r/2, r, r);
        }
    }

    void dashedLine(float x1, float y1, float x2, float y2) {
        int distance = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) / 10;

        float inter[][] = new float[distance+1][2];

        for (int i = 0; i <= distance; i++) {
            float s = ((float)i) / distance;
            inter[i][0] = lerp(x1, x2, s);
            inter[i][1] = lerp(y1, y2, s);
        }
        for(int i = 0; i < distance; i += 2) {
            line(inter[i][0],inter[i][1],inter[i+1][0],inter[i+1][1]);
        }
    }
}