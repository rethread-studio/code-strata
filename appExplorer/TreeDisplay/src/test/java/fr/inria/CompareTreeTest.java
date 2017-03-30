package fr.inria;

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
    public void testCompareTree1() throws Exception {
        CharTree a1 = new CharTree('a');
        CharTree b1 = new CharTree('b');
        CharTree c1 = new CharTree('c');
        CharTree d1 = new CharTree('d');
        CharTree e1 = new CharTree('e');
        CharTree f1 = new CharTree('f');
        a1.children.add(b1);
        a1.children.add(c1);
        a1.children.add(d1);
        c1.children.add(e1);
        c1.children.add(f1);
        assertTrue(a1.print().equals("A(B()C(E()F())D())"));


        CharTree a2 = new CharTree('a');
        CharTree b2 = new CharTree('b');
        CharTree c2 = new CharTree('c');
        CharTree d2 = new CharTree('d');
        CharTree e2 = new CharTree('e');
        CharTree f2 = new CharTree('f');
        a2.children.add(b2);
        a2.children.add(d2);
        assertTrue(a2.print().equals("A(B()D())"));
        List<Map.Entry<OrderedTree, OrderedTree>> l1 = TreeCallUtils.commonChildren(a1,a2);
        for(Map.Entry<OrderedTree, OrderedTree> e : l1) {
            if((e.getKey() != null) && (e.getValue() != null)) assertTrue(e.getKey().equals(e.getValue()));
        }
        assertTrue(l1.get(0).getKey().equals('B'));
        assertTrue(l1.get(1).getKey().equals('C'));
        assertTrue(l1.get(2).getKey().equals('D'));
    }

    class CharTree implements OrderedTree<Character> {
        public Character el;

        public List<CharTree> children = new ArrayList<>();

        public CharTree (char c) {
            el = new Character(c);
        }

        @Override
        public Character getEl() {
            return el;
        }

        @Override
        public List<? extends OrderedTree<Character>> getChildren() {
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
