package fr.inria.tie;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import oracle.jrockit.jfr.StringConstantPool;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
* Created by nharrand on 04/05/17.
*/
public class Cleaner {

    @Parameter(names = {"--help", "-h"}, help = true, description = "Display this message.")
    private boolean help;
    @Parameter(names = {"--input-file", "-i"}, description = "File containing the trace to be cleaned.")
    private String inputFileName;
    @Parameter(names = {"--output-file", "-o"}, description = "File containing the dictionary. Default: tie-report.json")
    private String outputFileName = "tie-report.json";
    @Parameter(names = {"--list", "-l"}, description = " \"test\" (list methods called in each test) or \"method\" (list all test calling each method). Default: test")
    private String list = "test";

    boolean printTests = true;

    public static void printUsage(JCommander jcom) {
        jcom.usage();
    }

    public static void main( String ... args ) {
        Cleaner cleaner = new Cleaner();
        JCommander jcom = new JCommander(cleaner,args);

        if(cleaner.help || cleaner.inputFileName == null || cleaner.outputFileName == null) {
            printUsage(jcom);
        } else {
            File in, out;
            in = new File(cleaner.inputFileName);
            out = new File(cleaner.outputFileName);
            if(cleaner.list != null) {
                if(cleaner.list.equals("method")) cleaner.printTests = false;
                else if(cleaner.list.equals("test")) cleaner.printTests = true;
                else {
                    System.out.println("Incorrect parameter -l / --list, expect \"test\" or \"method\", found \"" + cleaner.list + "\".");
                    printUsage(jcom);
                    return;
                }
            }
            cleaner.correct(in,out);
        }
    }

    public static String readLineFiltered(BufferedReader b) throws IOException {
        String line = b.readLine();
        if(line == null) return null;
            //else if (line.matches("\\[inria filter\\].*")) {
        else if (line.matches(".*\\[yalta filter\\].*")) {
            return  line.split("\\[yalta filter\\]")[1];
            //return  line.substring(14);
        } else if (line.startsWith("Running ")) return line;
        else return readLineFiltered(b);
    }

    public String curTest;
    public Map<String,Set<String>> called = new HashMap<>();
    public Set<String> curCalled;

    public void correct(File in, File out) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(in));
            StringBuilder sb = new StringBuilder();
            String line = readLineFiltered(br);
            boolean first = true;
            while (line != null) {
                if(line.startsWith("Running ")) {
                    if(first) first = false;
                    else called.put(curTest, curCalled);
                    curTest = line.split("Running ")[1];
                    curCalled = new HashSet<>();
                } else {
                    curCalled.add(line);
                }
                line = readLineFiltered(br);
            }
            printResults(out);



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String printTestsMethods() {
        String buf = "{\"testList\": [\n";
        boolean first = true;
        for (Map.Entry<String, Set<String>> e : called.entrySet()) {
            if (first) first = false;
            else buf += ",";
            buf += "{\"test\": \"" + e.getKey() + "\", \"called\":[\n";

            boolean first2 = true;
            for (String c : e.getValue()) {
                if (first2) first2 = false;
                else buf += ",\n";
                buf += "\"" + c + "\"";
            }

            buf += "\n]}\n";
        }
        buf += "]}";
        return buf;
    }

    public String printMethodsTests() {
        Map<String, Set<String>> impact = new HashMap<>();
        for (Map.Entry<String, Set<String>> e : called.entrySet()) {
            for(String m : e.getValue()) {
                if(!impact.keySet().contains(m)) {
                    Set<String> tests = new HashSet<>();
                    tests.add(e.getKey());
                    impact.put(m,tests);
                } else {
                    impact.get(m).add(e.getKey());
                }
            }
        }



        String buf = "{\"methodList\": [\n";
        boolean first = true;
        for (Map.Entry<String, Set<String>> e : impact.entrySet()) {
            if (first) first = false;
            else buf += ",";
            buf += "{\"method\": \"" + e.getKey() + "\", \"called-in\":[\n";

            boolean first2 = true;
            for (String c : e.getValue()) {
                if (first2) first2 = false;
                else buf += ",\n";
                buf += "\"" + c + "\"";
            }

            buf += "\n]}\n";
        }
        buf += "]}";
        return buf;
    }

    public void printResults(File out) {
        String buf;
        if(printTests) buf = printTestsMethods();
        else buf = printMethodsTests();

        try {
            PrintWriter w = new PrintWriter(out);
            w.print(buf);
            w.close();
        } catch (Exception ex) {
            System.err.println("Problem writing log");
            ex.printStackTrace();
        }
    }

}
