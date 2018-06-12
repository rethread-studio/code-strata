package fr.inria.IOs;

import fr.inria.DataStructure.Execution;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.jar.JarFile;

/**
 * Created by nharrand on 21/03/17.
 */
public class PropertiesReader {
    public static Execution readProperties(File properties) {
        Execution e = new Execution();
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(properties));

            String sDefaultLevel, sNbLevel, trace, jars, x86log, syscalls, outputDir, sScreenSize, sSave, sBranch;
            e.name = p.getProperty("name", "project");

            sScreenSize = p.getProperty("screenSize", "1200");
            e.screenSize = Integer.parseInt(sScreenSize);

            sSave = p.getProperty("save", "false");
            e.save = Boolean.parseBoolean(sSave);

            sBranch = p.getProperty("branch", "false");
            e.branch = Boolean.parseBoolean(sBranch);


            sDefaultLevel = p.getProperty("defaultLevel", "0");
            sNbLevel = p.getProperty("nbLevel", "1");

            e.nbLevel = Integer.parseInt(sNbLevel);
            e.defaultLevel = Integer.parseInt(sDefaultLevel);

            e.packages = new HashMap<>();

            for(int i =0; i < e.nbLevel; i++) {
                String pack = p.getProperty("packages_" + i, "");
                if(pack != null) {
                    Set<String> packs = new HashSet<>();
                    for(String s : pack.replace(" ", "").split(",")) {
                        if(!s.equals("")) packs.add(s);
                    }
                    e.packages.put(new Integer(i), packs);
                }
            }
            jars = p.getProperty("jars");
            if(jars != null) {
                e.jars = new HashSet<>();
                for(String s : jars.replace(" ", "").split(",")) {
                    e.jars.add(new JarFile(s));
                }
            }

            String ex = p.getProperty("excludes", "");
            e.excludes = new HashSet<>();
            if(ex != null && !ex.equals("")) {

                for(String s : ex.replace(" ", "").split(",")) {
                    e.excludes.add(s);
                }
            }

            trace = p.getProperty("trace");
            if(trace != null) e.trace = new File(trace);
            x86log = p.getProperty("x86log");
            if(x86log != null) e.x86log = new File(x86log);
            syscalls = p.getProperty("syscalls");
            if(syscalls != null) e.syscalls = new File(syscalls);
            outputDir = p.getProperty("outputDir","./");
            if(outputDir != null) e.outputDir = new File(outputDir);
            e.from = p.getProperty("from");


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return e;
    }
}
