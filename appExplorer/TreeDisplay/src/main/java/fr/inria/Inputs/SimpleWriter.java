package fr.inria.Inputs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nharrand on 08/03/17.
 */
public class SimpleWriter extends JSONWriter {
    @Override
    public JSONObject write(CallTree c) {
        JSONObject res = new JSONObject();
        try {
            res.put("name", c.name);
            res.put("weight", c.weight);
            JSONArray children = new JSONArray();
            for(CallTree child : c.children) {
                children.put(write(child));
            }
            res.put("children", children);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
