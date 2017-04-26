package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;

/**
 * Created by nharrand on 08/03/17.
 */
public class SimpleWriterTest extends TestCase {

    @Test
    public void testWrite() throws Exception {
        SimpleWriter w = new SimpleWriter();
        SimpleReader r = new SimpleReader();
        CallTree t1 = r.readFromFile(new File(SimpleReaderTest.class.getClassLoader().getResource("test1.json").getFile()));
        CallTree t2 = r.read(w.write(t1));
        assertTrue(t1.equals(t2));

        t1 = r.readFromFile(new File(SimpleReaderTest.class.getClassLoader().getResource("test2.json").getFile()));
        t2 = r.read(w.write(t1));
        assertTrue(t1.equals(t2));
    }

}