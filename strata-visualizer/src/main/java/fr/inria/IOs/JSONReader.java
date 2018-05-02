package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by nharrand on 08/03/17.
 */
public abstract class JSONReader {
    public abstract CallTree read(JSONObject o);

    public CallTree readFromFile(File f) {
        CallTree res = null;
        JSONObject jsonObject = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }

            jsonObject = new JSONObject(sb.toString());
            res = read(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
