package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.Execution;
import fr.inria.DataStructure.TreeCallUtils;
import fr.inria.EquiDistantColors;
import fr.inria.IOs.JSONReader;
import fr.inria.IOs.SimpleReader;
import processing.core.PApplet;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nharrand on 28/06/17.
 */
public class CallTreeGifView  extends PApplet {
    public static ColorPicker picker;

    static int strokeLight = 255;

    public static Execution e;
    public static int maxWeight;

    public void settings(){

        e = Context.currentExec;

        size(e.screenSize, e.screenSize);

    }

    public void setup() {
        //JSONReader r = new VisualvmReader();
        //JSONReader r = new AgentReader();
        JSONReader r = new SimpleReader();

        CallTree t = r.readFromFile(e.trace);
        int h = e.screenSize / t.depth;

        TreeCallUtils.label(t, e.packages, e.defaultLevel);



        t = TreeCallUtils.trimWrapper(t, e.excludes);

        picker = new EquiDistantColors(127+64+32,75+12,0, e.nbLevel, 100);
        background(255);

        drawTree(t, e.outputDir + "/img/" + e.name + "_a_calltree.png");

        //exit();
    }




    public TreeNode root;
    public void drawTree(CallTree t, String name) {
        int[] width = t.getWidthArray();
        int[] pop = new int[t.depth];
        maxWeight = TreeCallUtils.maxWeight(t);

        noStroke();
        root = drawNode(t, e.screenSize/(3*t.depth), 0, width, pop);
        int i = 0;
        TreeNode cur = root;
        while (cur.hasNext()) {
            cur.draw();
            save("img_" + String.format("%03d", i++));
            cur = cur.next();
        }
        cur.draw();
    }

    public TreeNode drawNode(CallTree t, int w, int d, int[] width, int[] pop) {
        float h = (float)e.screenSize / (float)width[d];
        int x = d * w * 3;
        int y = Math.round((int)((float) pop[d] * h));
        pop[d]++;
        TreeNode treeNode = new TreeNode((int) (x+w/2), (int) (y+h/2),t.level);

        for (CallTree c : t.children) {
            float tmpH = (float)e.screenSize / (float)(width[d + 1]);
            setColors(c.level);
            float lx1 = (x + w/2);
            float ly1 = (y + (h / 2));
            float lx2 = (x + 3 * w + w/2);
            float ly2 = (tmpH * pop[d + 1] + (tmpH / 2));
            TreeNode childNode = drawNode(c, w, d + 1, width, pop);
            childNode.hasLine = true;
            childNode.lx1 = lx1;
            childNode.ly1 = ly1;
            childNode.lx2 = lx2;
            childNode.ly2 = ly2;
            treeNode.addChild(childNode);
        }
        return treeNode;
    }

    static int strokeW = 2;

    public void setColors(int level) {
        int[] c = picker.getColor(level);
        fill(c[0], c[1], c[2]);
        stroke(c[0], c[1], c[2]);
        strokeWeight(strokeW);
    }

    public void draw(){
    }

    public class TreeNode {
        public boolean hasLine;
        public float lx1;
        public float ly1;
        public float lx2;
        public float ly2;
        public int x;
        public int y;
        public int level;
        public TreeNode parent = null;

        public TreeNode(
                int x,
                int y,
                int level) {

            this.hasLine = false;
            this.lx1 = 0;
            this.ly1 = 0;
            this.lx2 = 0;
            this.ly2 = 0;
            this.x = x;
            this.y = y;
            this.level = level;
        }

        public void addChild(TreeNode t) {
            children.add(t);
            t.parent = this;
        }

        public boolean hasNext() {
            return (parent != null && parent.hasNext()) || (c < children.size());
        }

        public TreeNode next() {
            if(hasNext()) {
                if(c < children.size()) {
                    return children.get(c++);
                } else {
                    return parent.next();
                }
            } else {
                return null;
            }
        }

        int c = 0;

        public void reset() {
            c = 0;
            for(TreeNode n : children) n.reset();
        }

        public List<TreeNode> children = new LinkedList<>();

        public void draw() {
            setColors(level);
            if(hasLine) line(parent.x+4,parent.y,x-4,y+2);
            ellipse(x,y,8,8);
        }
    }

}