package fr.inria.DataStructure;

import fr.inria.DataStructure.CallTree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

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

    public static void label (CallTree t, Map<Integer,Set<String>> l, int defaultLevel) {
        if(t.name.compareTo("Self time") == 0) t.name = t.parent.name;

        boolean marked = false;
        for(Integer i: l.keySet()) {
            if (isIn(t.name, l.get(i))) {
                t.level = i.intValue();
                marked = true;
            }
        }
        if(!marked) t.level = defaultLevel;
        for(CallTree c : t.getChildren()) label(c,l,defaultLevel);

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

    public static List<Map.Entry<OrderedTree,OrderedTree>> commonChildren(OrderedTree t1,OrderedTree t2) {
        List<Map.Entry<OrderedTree,OrderedTree>> res = new ArrayList<>();
        List<OrderedTree> children1 = t1.getChildren();
        List<OrderedTree> children2 = t2.getChildren();
        List<OrderedTree> childrenMax, childrenMin;
        boolean max1;
        if(children1.size() > children2.size()) {
            childrenMax = children1;
            childrenMin = children2;
            max1 = true;
        }
        else {
            childrenMax = children2;
            childrenMin = children1;
            max1 =false;
        }

        int i = 0, tmp =0;
        for(OrderedTree c1 : childrenMin) {
            for(int k = i; k < childrenMax.size(); k++) {
                if(c1.getEl().equals(childrenMax.get(k).getEl())) {
                    if(max1)
                        res.add(new HashMap.SimpleEntry<>(childrenMax.get(k), c1));
                    else
                        res.add(new HashMap.SimpleEntry<>(c1, childrenMax.get(k)));
                    tmp = k;
                    break;
                } else {
                    if (max1)
                        res.add(new HashMap.SimpleEntry<>(childrenMax.get(k), null));
                    else
                        res.add(new HashMap.SimpleEntry<>(null, childrenMax.get(k)));
                }
            }
            i = tmp+1;
        }
        return res;
    }

    public static void annotateD(CallTree t, int d) {
        t.d2 = d;
        for (CallTree c : t.children) {
            annotateD(c, d+1);
        }
    }

    public static int maxWeight(CallTree t) {
        int m = t.weight;
        for(CallTree c : t.children) {
            m = Math.max(m, maxWeight(c));
        }
        return m;
    }


}
