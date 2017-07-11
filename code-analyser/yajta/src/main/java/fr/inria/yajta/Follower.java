package fr.inria.yajta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nharrand on 11/07/17.
 */
public class Follower implements Tracking {

    Map<String, TreeNode> threadLogs = new HashMap<>();
    Map<String, Boolean> threadOfftrack = new HashMap<>();

    public synchronized void stepIn(String thread, String method) {
        if(!threadOfftrack.containsKey(thread) || threadOfftrack.get(thread)) return;
        //System.err.println("[" + thread + "] " + method + "{");
        TreeNode cur = threadLogs.get(thread);
        if(cur == null) offTrack(thread,method);
        else {
            if(cur.hasNext()) {
                cur = cur.next();
                if(cur.method.compareTo(method) != 0) {
                    offTrack(thread, method);
                } else {
                    threadLogs.put(thread,cur);
                }
            } else offTrack(thread, method);
        }
    }

    public synchronized void stepOut(String thread) {
        //System.err.println("[" + thread + "] }");
    }

    public void load(File trace) {
        JSONObject o = readFromFile(trace);

        try {
            JSONArray threads = o.getJSONArray("children");
            for(int i = 0; i < threads.length(); i++) {
                TreeNode t = new TreeNode(threads.getJSONObject(i));
                threadLogs.put(t.method,t);
                threadOfftrack.put(t.method,false);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject readFromFile(File f) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    public void offTrack(String thread, String method) {
        System.err.println("[" + thread + "] " + method + " >>>> OffTrack <<<< ");
        threadOfftrack.put(thread,true);

    }

    public void flush() {

    }
}
