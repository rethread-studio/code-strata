package fr.inria.DataStructure.Compare;

import fr.inria.DataStructure.CallTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nharrand on 22/03/17.
 */
public class CompareCallTree {
    public CallTree t1;
    public CallTree t2;

    public String name;
    public int depth;
    public int level = 0;

    public boolean areNodeEquals;

    public CompareCallTree parent = null;

    public List<CompareCallTree> children = new ArrayList<>();

    public CompareCallTree(CallTree t1, CallTree t2) {
        this.t1 = t1;
        this.t2 = t2;
        depth = 1;
        name = "*";


        if((t1 != null) && (t2 != null)) {
            areNodeEquals = t1.name.equals(t2.name);
            if(areNodeEquals) {
                name = t1.name;
                level = t1.level;
            } else {
                System.out.println("diff: " + t1.name + " != " + t2.name);
            }

            List<CallTree> children1 = t1.getChildren();
            List<CallTree> children2 = t2.getChildren();
            List<CallTree> childrenMax;
            if(children1.size() > children2.size()) childrenMax = children1;
            else  childrenMax = children2;

            int i = 0;
            for(CallTree c : childrenMax) {
                CompareCallTree cc = new CompareCallTree(childGetter(children1, i), childGetter(children2, i));
                addChild(cc);
                i++;
            }
        } else if(t1 != null) {
            name = t1.name;
            level = t1.level;
            areNodeEquals = false;
            int i = 0;
            for(CallTree c : t1.getChildren()) {
                CompareCallTree cc = new CompareCallTree(c, null);
                addChild(cc);
                i++;
            }
        } else if(t2 != null) {
            name = t2.name;
            level = t2.level;
            areNodeEquals = false;
            int i = 0;
            for(CallTree c : t2.getChildren()) {
                CompareCallTree cc = new CompareCallTree(null, c);
                addChild(cc);
                i++;
            }
        } else {
            areNodeEquals = false;
        }

    }

    public int[] getWidthArray() {
        int[] result = new int[depth];
        computeWidth(result, 0);

        return result;
    }

    void computeWidth(int[] res, int i) {
        res[i]++;
        for(CompareCallTree t : children) t.computeWidth(res, i + 1);
    }

    public void addChild(CompareCallTree c) {
        int d = c.depth +1;
        if(d > depth) depth = d;
        children.add(c);
        c.parent = this;
    }

    public List<CompareCallTree> getChildren() {
        return new ArrayList<CompareCallTree>(children);
    }

    public CallTree childGetter(List<CallTree> l, int i) {
        if(i < l.size()) return l.get(i);
        else return null;
    }

}
