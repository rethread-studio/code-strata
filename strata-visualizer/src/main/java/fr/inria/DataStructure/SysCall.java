package fr.inria.DataStructure;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by nharrand on 10/03/17.
 */
public class SysCall {
    public int pid;
    public String name;
    public int type;

    public static Set<String> types = new HashSet<>();

    public SysCall(String l, int form) throws Exception {
        if(form == 0) {
            String rawPid = l.split("  ")[0];
            pid = Integer.parseInt(rawPid);
            name = l.split("  ")[1].split("\\(")[0];
        } else if (form ==1) { //[pid \d+]
            String nl = l.replace("[pid ", "").replaceFirst("]", "");
            String rawPid = nl.split(" ")[0];
            pid = Integer.parseInt(rawPid);
            name = nl.split(" ")[1].split("\\(")[0];
        } else {
            pid = -1;
            name = l.split("\\(")[0];
        }
        if(!types.contains(name)) types.add(name);
    }
}
