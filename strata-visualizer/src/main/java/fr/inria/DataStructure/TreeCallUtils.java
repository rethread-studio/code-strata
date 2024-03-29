package fr.inria.DataStructure;

import fr.inria.DataStructure.CallTree;
import jdk.nashorn.internal.codegen.CompilerConstants;
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
            if(!str.equals("") && str.startsWith(s))
                return true;
        }
        return false;
    }

    public static void label (CallTree t, Map<Integer,Set<String>> l, int defaultLevel) {
        for(CallTree c : t.getChildren()) if(c.name.compareTo("Self time") == 0) t.children.remove(c);
        //if(t.name.compareTo("Self time") == 0) t.name = t.parent.name;

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

    public static void highlight(CallTree t, String target, int l, boolean h) {
        boolean paint = h;
        if(t.name.startsWith(target)) paint = true;
        for(CallTree c : t.getChildren()) {
            highlight(c,target,l,paint);
        }
        if(paint) t.level = l;
    }
    public static void highlight(CallTree t, String target, int l) {
        highlight(t,target,l,false);
    }

    public static CallTree from(CallTree t, String start) {
        if(t.name.startsWith(start)) return t;
        else {
            for(CallTree c : t.getChildren()) {
                CallTree tr = from(c, start);
                if(tr != null) return tr;
            }
            return null;
        }
    }

    public static CallTree trim (CallTree t, Set<String> toRemove) {
        if(isIn(t.name, toRemove)) {
            if(t.children.isEmpty()) return null;
            else {
                for(CallTree c : t.getChildren()) {
                    CallTree tr = trim(c,toRemove);
                    if(tr != null) return tr;
                }
                return null;
            }
        }


        for(CallTree c : t.getChildren())  {
            if(isIn(c.name, toRemove))
                t.children.remove(c);
            trim(c,toRemove);
        }

        return t;
    }

    public static CallTree trimWrapper (CallTree t, Set<String> toRemove) {
        CallTree r = null;
        List<CallTree> lr = trim2(t,toRemove);

        if(!lr.isEmpty()) {
            r = lr.get(0);
            correctDepth(r);
        }
        return r;
    }

    public static List<CallTree> trim2 (CallTree t, Set<String> toRemove) {
        List<CallTree> tmp = new ArrayList<>();
        for(CallTree c : t.getChildren())
            tmp.addAll(trim2(c,toRemove));
        if(isIn(t.name, toRemove)) {
            return tmp;
        } else {

            t.children = new ArrayList<CallTree>();
            t.children.addAll(tmp);
            List<CallTree> res = new ArrayList<>();
            res.add(t);
            return res;
        }
    }

    public static CallTree cut(CallTree t, Set<String> toRemove) {
        for(CallTree c : t.getChildren()) {
            if(isIn(c.name, toRemove)) t.children.remove(c);
            else cut(c,toRemove);
        }
        return t;
    }

    public static void removeLibsLeaf(CallTree t) {
        for(CallTree c : t.getChildren()) {
            if(c.level > 2 && c.children.isEmpty()) t.children.remove(c);
            else removeLibsLeaf(c);
        }
    }

    public static int correctDepth(CallTree t) {
        int d = 1;
        for(CallTree c : t.children) {
            d = Math.max(d, correctDepth(c) + 1);
        }
        t.depth = d;
        return d;
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
