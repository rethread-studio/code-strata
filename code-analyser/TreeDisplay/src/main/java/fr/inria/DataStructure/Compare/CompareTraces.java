package fr.inria.DataStructure.Compare;

import fr.inria.DataStructure.CallTree;
import fr.inria.DataStructure.TreeCallUtils;
import fr.inria.IOs.JSONReader;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nharrand on 10/05/17.
 */
public class CompareTraces {
    File traces1;
    File traces2;
    JSONReader reader;

    public CompareTraces(File traces1, File traces2, JSONReader reader) {
        new CompareTraces(traces1, traces2, reader, new HashSet<>(), null);
    }

    public CompareTraces(File traces1, File traces2, JSONReader reader, Set<String> toRemove, String from) {
        this.traces1 = traces1;
        this.traces2 = traces2;
        this.reader = reader;
        CallTree t1 = this.reader.readFromFile(traces1);
        if(from != null && !from.equals("")) t1 = TreeCallUtils.from(t1,from);
        TreeCallUtils.cut(t1,toRemove);
        CallTree t2 = this.reader.readFromFile(traces2);
        if(from != null && !from.equals("")) t2 = TreeCallUtils.from(t2,from);
        TreeCallUtils.cut(t2,toRemove);
        CompareTree<CallTree> t = new CompareTree(t1,t2);

        int d[] = new int[4];
        t.distance(d);

        //0 nodes
        //1 common nodes
        //2 arcs
        //3 common arcs

        System.out.println("{");
        System.out.println("\"nodes\": " + d[0]);
        System.out.println("\"common nodes\": " + d[1]);
        System.out.println("\"arcs\": " + d[2]);
        System.out.println("\"common arcs\": " + d[3]);
        System.out.println("}");
    }


}
