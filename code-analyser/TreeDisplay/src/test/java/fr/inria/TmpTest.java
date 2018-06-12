package fr.inria;

import fr.inria.DataStructure.CallTree;
import fr.inria.IOs.JSONReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TmpTest {
    @Test
    public void testRepartition() throws JSONException {
    /*
        File in = new File("/home/nharrand/Téléchargements/tree.json");
        JSONObject o = readFromFile(in);
        int nbLevel = 44;
        int[] pop = new int[nbLevel];
        JSONArray array = o.getJSONArray("nodes");
        int total = array.length();
        for(int i = 0; i < array.length(); i++) {
            JSONObject node = array.getJSONObject(i);
            pop[node.getInt("z")]++;
        }
        int nbVoix = 13;
        int[] nodePerVoix = new int[nbVoix];
        int[] zPerVoix = new int[nbVoix];
        int average = (int)((total / nbVoix) * 0.8);
        int curVoix = 0;
        for(int i = 0; i < nbLevel; i++) {
            if(nodePerVoix[curVoix] >= average && curVoix < nbVoix) curVoix++;
            nodePerVoix[curVoix] += pop[i];
            zPerVoix[curVoix] = i;
        }
        for(int k = 0; k < nbLevel; k++) {
            System.out.println("pour z: " + k + " repartition: " + getVoixRep(k,zPerVoix) + " modulo: " + getVoixModulo(k, zPerVoix));
        }
        JSONObject output = new JSONObject();
        JSONArray outputNodes = new JSONArray();
        for(int i = 0; i < array.length(); i++) {
            JSONObject node = array.getJSONObject(i);
            //node.put("voix", getVoixRep(node.getInt("z"), zPerVoix));
            node.put("voix", getVoixModulo(node.getInt("z"), zPerVoix));
            outputNodes.put(node);
        }
        output.put("nodes", outputNodes);
        File out = new File("/home/nharrand/Téléchargements/tree-with-modulo.json");
        writeFile(output,out);
*/
    }

    public int getVoixRep(int i, int[] zPerVoix) {
        for(int j = 0; j < zPerVoix.length; j++) {
            if(i <= zPerVoix[j]) return j;
        }
        return zPerVoix.length;
    }

    public int getVoixModulo(int i, int[] zPerVoix) {
        return i % zPerVoix.length;
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
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void writeFile(JSONObject o, File f) {
        try {
            PrintWriter w = new PrintWriter(f);
            w.print(o.toString());
            w.close();
        } catch (Exception ex) {
            System.err.println("Problem writing log");
            ex.printStackTrace();
        }
    }
}
