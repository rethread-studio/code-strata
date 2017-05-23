package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by nharrand on 08/03/17.
 */
public abstract class JSONWriter {
    public abstract JSONObject write(CallTree c);
    public abstract void write(CallTree c, JSONArray array);

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



    public void writeInFileArray(CallTree c, File f) {

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        write(c, array);

        try {
            json.put("nodes", array);
            PrintWriter w = new PrintWriter(f);
            w.print(json.toString());
            w.close();
        } catch (Exception ex) {
            System.err.println("Problem writing log");
            ex.printStackTrace();
        }
    }
}
