package fr.inria.View;

import fr.inria.ColorPicker;
import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.Execution;
import fr.inria.DataStructure.SysCall;
import fr.inria.IOs.SysCallReader;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static fr.inria.DataStructure.SysCall.types;

/**
 * Created by nharrand on 21/03/17.
 */
public class SysCallView extends PApplet {

    public static Execution e;

    public void settings(){

        e = Context.currentExec;
        //e = PropertiesReader.readProperties(new File("inputsFiles/simple-java-editor/simple-java-editor.properties"));
        size(e.screenSize, e.screenSize);

    }

    public void setup() {
        background(0);
        SysCallReader r = new SysCallReader();
        List<SysCall> sysCalls = r.readFromFile(e.syscalls);
        List<String> colors = new ArrayList<>(SysCall.types);

        int step = 360 / SysCall.types.size();

        ColorPicker colorPicker = new ColorPicker(255,100,0,types.size());
        int n = (int) Math.floor(Math.sqrt(sysCalls.size()));
        n++;
        int w = e.screenSize / n;

        int x = 0, y = 0;
        for(SysCall s : sysCalls) {
            if(x >= n) {
                x = 0;
                y++;
            }
            int[] c = colorPicker.getColor(colors.indexOf(s.name));
            fill(c[0], c[1], c[2]);
            //System.out.println("(" + x + ", " + y + ", " + w + ") - (" + colors.indexOf(s.name) * step + ")");
            rect(x*(w+1), y*(w+1), w+2, w+2);
            x++;
        }

        if(e.save) save(e.outputDir + "/img/" + e.name + "_syscall.png");
    }
}
