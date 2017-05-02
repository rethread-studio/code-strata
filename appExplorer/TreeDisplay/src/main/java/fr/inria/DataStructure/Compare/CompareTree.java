package fr.inria.DataStructure.Compare;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.OrderedTree;
import fr.inria.DataStructure.TreeCallUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nharrand on 25/04/17.
 */
public class CompareTree<T> {
    public OrderedTree<T> t1;
    public OrderedTree<T> t2;

    public String name;
    public int depth;
    public int level = 0;

    public boolean areNodeEquals;

    public CompareTree parent = null;

    public List<CompareTree> children = new ArrayList<>();

    public CompareTree(OrderedTree t1, OrderedTree t2) {
        this.t1 = t1;
        this.t2 = t2;
        depth = 1;

        if((t1 != null) && (t2 != null)) {
            areNodeEquals = t1.getEl().equals(t2.getEl());
            name = t1.getEl().toString();
            buildChildren(t1, t2);

        } else if(t1 != null) {
            name = t1.getEl().toString();
            areNodeEquals = false;
            for(Object c : t1.getChildren()) {
                CompareTree cc = new CompareTree((OrderedTree) c, null);
                addChild(cc);
            }
        } else if(t2 != null) {
            name = t2.getEl().toString();
            areNodeEquals = false;
            for(Object c : t2.getChildren()) {
                CompareTree cc = new CompareTree(null, (OrderedTree) c);
                addChild(cc);
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
        for(CompareTree t : children) t.computeWidth(res, i + 1);
    }

    public void addChild(CompareTree c) {
        int d = c.depth +1;
        if(d > depth) depth = d;
        children.add(c);
        c.parent = this;
    }

    public List<CompareTree> getChildren() {
        return new ArrayList<CompareTree>(children);
    }

    public CallTree childGetter(List<CallTree> l, int i) {
        if(i < l.size()) return l.get(i);
        else return null;
    }



    public double distance(int[] data) {
        if(data == null) data = new int[4];
        //0 nodes
        //1 common nodes
        //2 arcs
        //3 common arcs

        data[0]++;
        if(areNodeEquals) data[1]++;
        for(CompareTree c : children) {
            data[2]++;
            if(areNodeEquals || c.areNodeEquals) data[3]++;
            c.distance(data);
        }


        return (data[2]);
    }

    public void buildChildren(OrderedTree t1, OrderedTree t2) {
        List<String> original = new ArrayList<>();
        for(Object c : t1.getChildren()) {
            OrderedTree child = (OrderedTree) c;
            original.add(((OrderedTree) c).getEl().toString());
        }
        List<String> revised = new ArrayList<>();
        for(Object c : t2.getChildren()) {
            OrderedTree child = (OrderedTree) c;
            revised.add(((OrderedTree) c).getEl().toString());
        }

        Patch patch = DiffUtils.diff(original, revised);
        int i = 0, j = 0;
        for (Delta delta: patch.getDeltas()) {
            int imax = delta.getOriginal().getPosition();
            int jmax = delta.getRevised().getPosition();
            while(i < imax && j < jmax) {
                CompareTree cc = new CompareTree((OrderedTree) t1.getChildren().get(i), (OrderedTree) t2.getChildren().get(j));
                addChild(cc);
                i++;j++;
            }


            switch (delta.getType()) {
                case CHANGE:
                    for(Object str : delta.getOriginal().getLines()) {
                        CompareTree cc = new CompareTree((OrderedTree) t1.getChildren().get(i), null);
                        addChild(cc);
                        i++;
                    }
                    for(Object str : delta.getRevised().getLines()) {
                        CompareTree cc = new CompareTree(null, (OrderedTree) t2.getChildren().get(j));
                        addChild(cc);
                        j++;
                    }
                    break;
                case DELETE:
                    for(Object str : delta.getOriginal().getLines()) {
                        CompareTree cc = new CompareTree((OrderedTree) t1.getChildren().get(i), null);
                        addChild(cc);
                        i++;
                    }
                    break;
                case INSERT:
                    for(Object str : delta.getRevised().getLines()) {
                        CompareTree cc = new CompareTree(null, (OrderedTree) t2.getChildren().get(j));
                        addChild(cc);
                        j++;
                    }
                    break;
            }
        }
        int imax = original.size();
        int jmax = revised.size();
        while(i < imax && j < jmax) {
            CompareTree cc = new CompareTree((OrderedTree) t1.getChildren().get(i), (OrderedTree) t2.getChildren().get(j));
            addChild(cc);
            i++;j++;
        }
    }

}