package fr.inria.DataStructure;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Created by nharrand on 21/03/17.
 */
public class Execution {

    public String name;
    public int defaultLevel;
    public Map<Integer,Set<String>> packages;
    public File trace;
    public Set<JarFile> jars;
    public File x86log;
    public File syscalls;
    public File outputDir;


    public int screenSize;
    public boolean save;
}
