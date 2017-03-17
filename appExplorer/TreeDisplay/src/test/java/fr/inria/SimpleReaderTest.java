package fr.inria;

import fr.inria.DataStructure.CallTree;
import fr.inria.Inputs.SimpleReader;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

/**
 * Created by nharrand on 08/03/17.
 */
public class SimpleReaderTest extends TestCase {

    @Test
    public void testRead() {
        SimpleReader r = new SimpleReader();
        CallTree t = r.readFromFile(new File(SimpleReaderTest.class.getClassLoader().getResource("test1.json").getFile()));
        assertTrue(t.getDepth() == 1);
        checkCallTree(t);

        t = r.readFromFile(new File(SimpleReaderTest.class.getClassLoader().getResource("test2.json").getFile()));
        assertTrue(t.getDepth() == 4);
        checkCallTree(t);
    }

    public void checkCallTree(CallTree c) {
        assertTrue(c.name != null);
        for(CallTree child : c.getChildren()) {
            checkCallTree(child);
        }
    }
}