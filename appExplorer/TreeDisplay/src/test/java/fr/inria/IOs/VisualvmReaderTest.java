package fr.inria.IOs;

import fr.inria.DataStructure.CallTree;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by nharrand on 09/03/17.
 */
public class VisualvmReaderTest extends TestCase {
    public void testRead() throws Exception {

        VisualvmReader r = new VisualvmReader();
        CallTree t = r.readFromFile(new File("inputsFiles/simple-java-editor/trace.json"));
        assertTrue(t.depth == 44);
        System.out.print("c");
    }

}