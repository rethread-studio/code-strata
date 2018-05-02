package fr.inria.IOs;

import fr.inria.DataStructure.JarParser;
import junit.framework.TestCase;

import java.util.jar.JarFile;

/**
 * Created by nharrand on 12/03/17.
 */
public class JarParserTest extends TestCase {
    public void testPrintMethodStubs() throws Exception {
        JarParser j = new JarParser();
        j.printMethodStubs(new JarFile(SimpleReaderTest.class.getClassLoader().getResource("SimpleTextEditor.jar").getFile()));
        assertTrue(j.methodsByteCode.size() == 146);
        System.out.println("Done");
    }

}