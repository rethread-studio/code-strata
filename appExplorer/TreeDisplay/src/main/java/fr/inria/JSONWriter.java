package fr.inria;

import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by nharrand on 08/03/17.
 */
public abstract class JSONWriter {
    public abstract JSONObject write(CallTree c);

    public void writeInFile(CallTree c, File f) {

        JSONObject json = write(c);

        try {
            PrintWriter w = new PrintWriter(f);
            w.print(json.toString());
            w.close();
        } catch (Exception ex) {
            System.err.println("Problem writing log");
            ex.printStackTrace();
        }
    }
}
