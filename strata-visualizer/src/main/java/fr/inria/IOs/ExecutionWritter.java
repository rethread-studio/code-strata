package fr.inria.IOs;

import fr.inria.DataStructure.*;
import processing.data.JSONObject;

import java.io.File;

/**
 * Created by nharrand on 24/03/17.
 */
public class ExecutionWritter {

    public void toJSON() {
        JSONObject jsonObject = new JSONObject();
        Execution e = Context.currentExec;
        VisualvmReader r = new VisualvmReader();
        CallTree t = r.readFromFile(e.trace);
        TreeCallUtils.label(t, e.packages, e.defaultLevel);
        TreeCallUtils.annotateD(t, 0);
        /*JarParser j = new JarParser();
        for(JarFile jar :e.jars) {
            try {
                j.printMethodStubs(jar);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        x86Parser p = new x86Parser();
        p.readFromFile(e.x86log);*/
        //int[] x86 = p.instructions.getInstructions(method);
        //int[] byteCode = j.methodsByteCode.get(method);

        SimpleWriter w = new SimpleWriter();
        w.nbBrothers = t.getWidthArray();
        w.writeInFileArray(t, new File(e.outputDir + "/tree.json"));

        System.out.println("depth: " + t.depth);
        int total = 0;
        for(int i : w.nbBrothers) {
            total += i;
        }
        System.out.println("nb node: " + total);
    }


}
