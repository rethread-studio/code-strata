package fr.inria.IOs;

import junit.framework.TestCase;

import java.io.File;

/**
 * Created by nharrand on 18/03/17.
 */
public class x86ParserTest extends TestCase {

    public void testReadFromFile() throws Exception {
        x86Parser j = new x86Parser();
        j.readFromFile(new File(x86ParserTest.class.getClassLoader().getResource("testx86.log").getFile()));
        //assertTrue(j.methodsByteCode.size() == 140);
        System.out.println("Done");
    }
}