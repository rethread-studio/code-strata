package fr.inria.Inputs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nharrand on 08/03/17.
 */
public class SimpleWriter extends JSONWriter {
    public int[] nbBrothers;
    @Override
    public JSONObject write(CallTree c) {
        JSONObject res = new JSONObject();
        try {
            res.put("name", c.name);
            res.put("weight", c.weight);
            res.put("nbChildren", c.children.size());
            res.put("nbBrothers", nbBrothers[c.d2]);
            res.put("depth", c.depth);
            if(c.d2 < 6) {
                JSONArray children = new JSONArray();
                for (CallTree child : c.children) {
                    children.put(write(child));
                }
                res.put("children", children);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public void write(CallTree c, JSONArray array) {
        JSONObject res = new JSONObject();
        try {
            res.put("name", c.name);
            res.put("weight", c.weight);
            res.put("nbChildren", c.children.size());
            res.put("nbBrothers", nbBrothers[c.d2]);
            res.put("depth", c.depth);
            res.put("z", c.d2);
            //if(c.d2 < 6) {
                JSONArray children = new JSONArray();
                for (CallTree child : c.children) {
                    write(child, array);
                }
                res.put("children", children);
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        array.put(res);
    }
}
