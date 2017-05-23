package fr.inria.IOs;


import fr.inria.DataStructure.SysCall;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nharrand on 10/03/17.
 */
public class SysCallReader {

    private static Pattern patternPid;
    private static Pattern patternPid2;
    private static Pattern pattern;
    private static Matcher matcherPid2;
    private static Matcher matcherPid;
    private static Matcher matcher;

    public List<SysCall> readFromFile(File f) {
        List<SysCall> sysCallList = new ArrayList<>();
        BufferedReader br = null;

        patternPid2 = Pattern.compile("\\[pid \\d+\\] [a-zA-Z]+\\(.*");
        patternPid = Pattern.compile("\\d+  [a-zA-Z].*\\(.*");
        pattern = Pattern.compile("[a-zA-Z].*\\(.*");

        try {
            br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            while (line != null) {
                matcher = pattern.matcher(line);
                matcherPid = patternPid.matcher(line);
                matcherPid2 = patternPid2.matcher(line);
                if(matcherPid.matches())
                    sysCallList.add(new SysCall(line, 0));
                else if (matcherPid2.matches())
                    sysCallList.add(new SysCall(line, 1));
                else if(matcher.matches())
                    sysCallList.add(new SysCall(line, 2));
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sysCallList;
    }
}
