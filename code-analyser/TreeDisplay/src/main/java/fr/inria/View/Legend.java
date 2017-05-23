package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.Execution;
import fr.inria.SimpleColor;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by nharrand on 03/05/17.
 */
public class Legend extends PApplet {

    public static ColorPicker picker;

    public static Execution e;

    public void settings(){
        e = Context.currentExec;
        size(600, 600);

    }

    public void setup() {

        picker = new SimpleColor(127+64+32,75+12,0, e.nbLevel, 100);
        background(255);

        List<String> labels = new ArrayList<>();
        for(Set<String> s : e.packages.values()) {
            String str = "";
            boolean first = true;
            for(String ss : s) {
                if(first) first = false;
                else str += ", ";
                str += ss;
            }
            labels.add(str);
        }
        drawLegend(labels, 10, 10, 32);
        if(e.save) save(e.outputDir + "/img/" + e.name + "_legend.png");
    }

    public void drawLegend(List<String> labels, int x, int y, int size) {
        PFont f;
        f = createFont("Arial",size,true);
        textFont(f);                  // STEP 3 Specify font to be used
        for(int i = 0; i < labels.size(); i++) {
            int[] c = picker.getColor(i);
            fill(c[0], c[1], c[2]);
            text(labels.get(i), x, 30+y+2*i*size);
        }
    }
}
