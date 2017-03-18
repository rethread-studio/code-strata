package fr.inria.DataStructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nharrand on 18/03/17.
 */
public class x86Instructions {
    public static Map<String, Integer> instructionSet = new HashMap<>();
    public static int getInstruction(String inst) {
        if(!instructionSet.containsKey(inst)) instructionSet.put(inst, instructionSet.keySet().size());
        return instructionSet.get(inst);
    }

    public Map<String, Map<String, int[]>> classMethodInstructions = new HashMap<>();

    public void add(String c, String m, List<String> insts) {
        Map<String, int[]> methods = classMethodInstructions.get(c);
        if (methods == null) methods = new HashMap<>();

        int[] arrayInst = new int[insts.size()];
        int i = 0;
        for(String inst : insts) {
            arrayInst[i] = getInstruction(inst);
            i++;
        }
        methods.put(m,arrayInst);

        classMethodInstructions.put(c,methods);
    }

    public int[] getInstructions(String method) {
        String[] raw = method.split("\\(");
        String[] raw2 = raw[0].split("\\.");
        String m = raw2[raw2.length-1] + "(" + raw[1];
        String c = raw2[0];
        for(int i = 1; i < raw2.length-1; i++) {
            c += "." + raw2[i];
        }
        if(classMethodInstructions.containsKey(c))
            return  classMethodInstructions.get(c).get(m);
        else
            return null;
    }
}
