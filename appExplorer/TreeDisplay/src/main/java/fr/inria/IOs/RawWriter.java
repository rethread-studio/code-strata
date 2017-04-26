package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by nharrand on 25/04/17.
 */
public class RawWriter {

    public void writeInFile(CallTree t, File f, boolean end) {

        String str = write(t, end);

        try {
            PrintWriter w = new PrintWriter(f);
            w.print(str.toString());
            w.close();
        } catch (Exception ex) {
            System.err.println("Problem writing log");
            ex.printStackTrace();
        }
    }

    private String write(CallTree t, boolean end) {
        String str = t.name;
        if(end) str += " {";
        str += "\n";
        for(CallTree c : t.children) {
            str += write(c,end);
        }
        if(end) str += t.name + " }" + "\n";
        return str;
    }
}
