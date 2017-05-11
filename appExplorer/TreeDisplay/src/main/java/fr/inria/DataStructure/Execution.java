package fr.inria.DataStructure;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Created by nharrand on 21/03/17.
 */
public class Execution {

    public String name;
    public String entry;
    public int defaultLevel;
    public int nbLevel;
    public Map<Integer,Set<String>> packages;
    public File trace;
    public Set<JarFile> jars;
    public File x86log;
    public File syscalls;
    public File outputDir;
    public Set<String> excludes;
    public String from;


    public int screenSize;
    public boolean save;
    public boolean exit = false;

    public static Execution defaultJunitExecution() {
        Execution e = new Execution();
        e.name = "tree";
        e.defaultLevel = 3;
        e.nbLevel = 4;
        e.packages = new HashMap<>();
        HashSet p0 = new HashSet();
        p0.add("org.junit");
        e.packages.put(new Integer(0),p0);
        HashSet p1 = new HashSet();
        p1.add("java");
        p1.add("javax");
        e.packages.put(new Integer(1),p1);
        HashSet p2 = new HashSet();
        p2.add("sun");
        p2.add("org.sun");
        p2.add("sunw");
        e.packages.put(new Integer(2),p2);
        HashSet p3 = new HashSet();
        e.packages.put(new Integer(3),p3);

        e.from="org.junit.runners.ParentRunner.run";
        e.save=true;
        e.screenSize=1200;
        return e;
    }
}
