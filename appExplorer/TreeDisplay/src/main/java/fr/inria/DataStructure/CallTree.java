package fr.inria.DataStructure;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by nharrand on 08/03/17.
 */
public class CallTree implements OrderedTree<String> {
    public List<CallTree> children;
    public CallTree parent = null;
    public int weight;
    public String name;
    public int depth;
    public int level = 0;
    public int d2;

    public String toString() {
        return name;
    }

    public CallTree() {}

    public CallTree(String name, int w) {
        children = new ArrayList<>();
        this.name = name;
        weight = w;
        depth = 1;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String getEl() {
        return name;
    }

    public List<CallTree> getChildren() {
        return new ArrayList<CallTree>(children);
    }

    public void addChild(CallTree c) {
        int d = c.depth +1;
        if(d > depth) depth = d;
        children.add(c);
        c.parent = this;
    }

    public void addChildren(Collection<CallTree> collection) {
        for(CallTree c : collection) {
            addChild(c);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof CallTree)) return false;
        CallTree c = (CallTree)other;
        if(!c.name.equals(name)) return false;
        List<CallTree> otherChildren = c.getChildren();
        if(otherChildren.size() != children.size()) return false;
        for(int i = 0; i < children.size(); i++) {
            if(!children.get(i).equals(otherChildren.get(i))) return false;
        }
        return true;
    }

    public int computeDepth() {
        if(children.isEmpty()) depth = 1;
        else {
            int d = 1;
            for(CallTree c : children) {
                d = Math.max(d, c.computeDepth() + 1);
            }
            depth = d;
        }
        return depth;
    }

    public int[] getWidthArray() {
        int[] result = new int[depth];
        computeWidth(result, 0);

        return result;
    }

    void computeWidth(int[] res, int i) {
        res[i]++;
        for(CallTree t : children) t.computeWidth(res, i + 1);
    }

}
