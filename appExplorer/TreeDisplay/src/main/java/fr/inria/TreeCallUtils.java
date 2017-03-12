package fr.inria;

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
}
