package fr.inria;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import fr.inria.DataStructure.Compare.CompareTree;
import fr.inria.DataStructure.OrderedTree;
import fr.inria.DataStructure.TreeCallUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nharrand on 23/03/17.
 */
public class CompareTreeTest extends TestCase {

    @Test
    public void testALaCon() throws Exception {
        List<String> original = new ArrayList<>();
        List<String> revised = new ArrayList<>();

        original.add("A(");
        original.add("X(");
        original.add("B(");
        original.add("B)");
        original.add("C(");
        original.add("E(");
        original.add("E)");
        original.add("F(");
        original.add("F)");
        original.add("C)");
        original.add("D(");
        original.add("D)");
        original.add("X)");
        original.add("A)");


        revised.add("A(");
        revised.add("B(");
        revised.add("B)");
        revised.add("Z(");
        revised.add("E(");
        revised.add("E)");
        revised.add("F(");
        revised.add("F)");
        revised.add("Z)");
        revised.add("D(");
        revised.add("Y(");
        revised.add("Y)");
        revised.add("D)");
        revised.add("A)");

        // Compute diff. Get the Patch object. Patch is the container for computed deltas.

        Patch patch = DiffUtils.diff(original, revised);
        int i = 0, j = 0;
        for (Delta delta: patch.getDeltas()) {
            int imax = delta.getOriginal().getPosition();
            int jmax = delta.getRevised().getPosition();
            while(i < imax && j < jmax) { System.out.println(i + ": O: " + original.get(i) + " R: " + revised.get(j));i++;j++;}
            //while(i < imax) { System.out.println(i + ": O: " + original.get(i));i++;}
            //while(j < jmax) { System.out.println("         R: " + revised.get(j));j++;}


            switch (delta.getType()) {
                case CHANGE:
                    for(Object str : delta.getOriginal().getLines()) {
                        System.out.println(i + ": O: " + str);
                        i++;
                    }
                    for(Object str : delta.getRevised().getLines()) {
                        System.out.println("         R: " + str);
                        j++;
                    }
                    break;
                case DELETE:
                    for(Object str : delta.getOriginal().getLines()) {
                        System.out.println(i + ": O: " + str);
                        i++;
                    }
                    break;
                case INSERT:
                    for(Object str : delta.getRevised().getLines()) {
                        System.out.println("         R: " + str);
                        j++;
                    }
                    break;
            }
        }
        int imax = original.size();
        int jmax = revised.size();
        while(i < imax && j < jmax) { System.out.println(i + ": O: " + original.get(i) + " R: " + revised.get(j));i++;j++;}
        //while(i < imax) { System.out.println(i + ": O: " + original.get(i));i++;}
        //while(j < jmax) { System.out.println("         R: " + revised.get(j));j++;}
    }

    @Test
    public void testCompareTree1() throws Exception {
        CharTree a1 = new CharTree('A');
        CharTree b1 = new CharTree('B');
        CharTree c1 = new CharTree('C');
        CharTree d1 = new CharTree('D');
        CharTree e1 = new CharTree('E');
        CharTree f1 = new CharTree('F');
        a1.children.add(b1);
        a1.children.add(c1);
        a1.children.add(d1);
        c1.children.add(e1);
        c1.children.add(f1);
        System.out.println("a1: " + a1.print());


        CharTree a2 = new CharTree('A');
        CharTree b2 = new CharTree('B');
        CharTree c2 = new CharTree('C');
        CharTree d2 = new CharTree('D');
        CharTree e2 = new CharTree('E');
        CharTree f2 = new CharTree('F');
        a2.children.add(b2);
        a2.children.add(d2);

        System.out.println("a2: " + a2.print());


        CharTree a3 = new CharTree('A');
        CharTree b3 = new CharTree('B');
        CharTree c3 = new CharTree('Z');
        CharTree d3 = new CharTree('D');
        CharTree e3 = new CharTree('E');
        CharTree f3 = new CharTree('F');
        a3.children.add(b3);
        a3.children.add(c3);
        a3.children.add(d3);
        c3.children.add(e3);
        c3.children.add(f3);
        System.out.println("a3: " + a3.print());

        assertTrue(a1.print().equals("A(B()C(E()F())D())"));
        assertTrue(a2.print().equals("A(B()D())"));
        assertTrue(a3.print().equals("A(B()Z(E()F())D())"));

        CompareTree<Character> comp = new CompareTree<>(a1,a2);
        CompareTree<Character> comp2 = new CompareTree<>(a1,a3);
        int[] dist = new int[4];
        //0 nodes
        //1 common nodes
        //2 arcs
        //3 common arcs
        comp.distance(dist);
        assertTrue(dist[0] == 6);
        assertTrue(dist[1] == 3);
        assertTrue(dist[2] == 5);
        assertTrue(dist[3] == 3);

        int[] dist2 = new int[4];
        comp2.distance(dist2);
        /*assertTrue(dist2[0] == 6);
        assertTrue(dist2[1] == 3);
        assertTrue(dist2[2] == 5);
        assertTrue(dist2[3] == 2);*/
    }

    class CharTree implements OrderedTree<String> {
        public String el;

        public List<CharTree> children = new ArrayList<>();

        public CharTree (char c) {
            el = new String(c + "");
        }

        @Override
        public String getEl() {
            return el;
        }

        @Override
        public List<? extends OrderedTree<String>> getChildren() {
            return children;
        }

        public String print() {
            String res = "" + el + "(";
            for(CharTree c : children) res += c.print();
            res += ")";
            return res;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof CharTree)) return false;
            CharTree c = (CharTree)other;
            if(!c.el.equals(el)) return false;
            return true;
        }
    }
}
