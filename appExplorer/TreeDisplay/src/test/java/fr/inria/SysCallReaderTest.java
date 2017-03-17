package fr.inria;

import fr.inria.DataStructure.SysCall;
import fr.inria.Inputs.SysCallReader;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by nharrand on 10/03/17.
 */
public class SysCallReaderTest extends TestCase {

    @Test
    public void testReadFromFile() throws Exception {
        SysCallReader r = new SysCallReader();
        List<SysCall> l = r.readFromFile(new File(SimpleReaderTest.class.getClassLoader().getResource("test1.log").getFile()));

        assertTrue(l.size() == 40);
        assertTrue(SysCall.types.size() == 11);
    }

}