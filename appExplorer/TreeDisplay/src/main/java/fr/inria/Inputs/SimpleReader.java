package fr.inria.Inputs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nharrand on 08/03/17.
 */
public class SimpleReader extends JSONReader {
    @Override
    public CallTree read(JSONObject o) {
        CallTree res = null;
        try {
            int w = o.getInt("weight");
            String name = o.getString("name");
            JSONArray children = o.getJSONArray("children");

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
