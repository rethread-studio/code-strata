package fr.inria;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by nharrand on 10/03/17.
 */
public class TreeCallUtils {

    public static boolean isIn(String str, Set<String> set) {
        for(String s : set) {
            if(str.startsWith(s)) return true;
        }
        return false;
    }

    public static void trim (CallTree t, Set<String> appPackages, Set<String> libs) {
        if(t.name.compareTo("Self time") == 0) t.name = t.parent.name;
        if(isIn(t.name, appPackages)) t.level = 0;
        else if (!isIn(t.name, libs)) t.level = 1;
        else  t.level = 2;
        for(CallTree c : t.getChildren()) trim(c,appPackages, libs);
    }

    public static Map<String, Integer> frequencies(CallTree t) {
        Map<String, Integer> result = new HashMap<>();
        computeF(t,result);
        return result;
    }

    public static void computeF(CallTree t, Map<String, Integer> f) {
        Integer i = f.get(t.name);
        if(i == null) f.put(t.name, new Integer(1));
        else {
            i = new Integer(i.intValue() + 1);
            f.put(t.name, i);
        }
        for(CallTree c : t.children) computeF(c,f);
    }

    public static String mostFrequentMethod(Map<String, Integer> f) {
        int max = 0;
        String name = null;
        for(String m : f.keySet()) {
            if(max < f.get(m)) {
                max = f.get(m);
                name = m;
            }
        }
        System.out.println("max: " + max);
        return name;
    }

    public static void color(CallTree t, String str) {
        if(t.name.equals(str)) t.level = 2;
        for(CallTree c: t.children) color(c,str);
    }
}
