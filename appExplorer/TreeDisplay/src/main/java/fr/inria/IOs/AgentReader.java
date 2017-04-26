package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by nharrand on 20/04/17.
 */
public class AgentReader extends JSONReader {

    public String readLineFiltered(BufferedReader b) throws IOException {
        String line = b.readLine();
        if(line == null) return null;
        //else if (line.matches("\\[inria filter\\].*")) {
        else if (line.matches(".*\\[inria filter\\].*")) {
            return  line.split("\\[inria filter\\]")[1];
            //return  line.substring(14);
        }
        else return readLineFiltered(b);
    }

    @Override
    public CallTree readFromFile(File f) {
        CallTree res = null;
        JSONObject jsonObject = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            String line = readLineFiltered(br);
            String line2 = readLineFiltered(br);
            while (line2 != null) {
                if(line2.startsWith("]") && line.endsWith(",")) {
                    line = line.substring(0,line2.length()-1);
                }
                sb.append(line);
                line = line2;
                line2 = readLineFiltered(br);
            }

            String buf = "{ \"Name\": \"Thread\", \"node\": [\n" + sb.toString() + line.substring(0,line.length()-1) + "\n]}" + "\n]}";

            /*File of = new File("toJsonify.json");
            try {
                PrintWriter w = new PrintWriter(of);
                w.print(buf);
                w.close();
            } catch (Exception ex) {
                System.err.println("Problem writing log");
                ex.printStackTrace();
            }*/

            jsonObject = new JSONObject(buf);
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

    @Override
    public CallTree read(JSONObject o) {
        CallTree res = null;
        try {
            int w = 1;
            String name = o.getString("Name");
            JSONArray children = o.getJSONArray("node");

            res = new CallTree(name,w);

            for(int i = 0; i < children.length(); i++) {
                res.addChild(read(children.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
