package fr.inria.Inputs;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nharrand on 09/03/17.
 */
public class VisualvmReader extends JSONReader {
    @Override
    public CallTree read(JSONObject o) {
        CallTree res = null;
        try {
            String time = o.getString("Time");
            int w ;
            try {
                w = Integer.parseInt(time);
            } catch (NumberFormatException e) {
                w = 0;
            }

            String name = o.getString("Name");
            JSONArray children = null;
            try {
                children = o.getJSONArray("node");
            } catch (JSONException e) {

            }

            res = new CallTree(name,w);
            if(children != null) {
                for(int i = 0; i < children.length(); i++) {
                    res.addChild(read(children.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
