package fr.inria.Inputs;

import fr.inria.DataStructure.CallTree;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by nharrand on 09/03/17.
 */
public class XMLReaderTest extends TestCase {
    public void testRead() throws Exception {

    }

    public void testReadFromFile() throws Exception {
        XMLReader r = new XMLReader();
        CallTree t = r.readFromFile(new File(SimpleReaderTest.class.getClassLoader().getResource("test1.xml").getFile()));

    }

}