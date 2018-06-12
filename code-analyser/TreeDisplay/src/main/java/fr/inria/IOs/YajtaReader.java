package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class YajtaReader extends JSONReader {

    @Override
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
            res = readRoot(jsonObject);
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

    public CallTree readRoot(JSONObject o) {
        CallTree res = null;
        try {
            int w = 1;//o.getInt("weight");
            String name = o.getString("name");

            JSONArray children = o.getJSONArray("children");

            res = new CallTree(name,w);
            res.thread = name;

            for(int i = 0; i < children.length(); i++) {
                res.addChild(read(children.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }


    @Override
    public CallTree read(JSONObject o) {
        CallTree res = null;
        try {
            int w = 1;//o.getInt("weight");
            String name = o.getString("class") + "." + o.getString("method");

            JSONArray children = o.getJSONArray("children");

            res = new CallTree(name,w);

            try {
                String t = o.getString("thread");
                res.thread = t;
            } catch (Exception e) {}

            for(int i = 0; i < children.length(); i++) {
                res.addChild(read(children.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
