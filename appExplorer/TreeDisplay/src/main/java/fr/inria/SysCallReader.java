package fr.inria;


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

    private static Pattern pattern;
    private static Matcher matcher;

    protected List<SysCall> readFromFile(File f) {
        List<SysCall> sysCallList = new ArrayList<>();
        BufferedReader br = null;

        //pattern = Pattern.compile("\\[pid \\d+\\]  [a-zA-Z].*\\(.*");
        pattern = Pattern.compile("\\d+  [a-zA-Z].*\\(.*");

        try {
            br = new BufferedReader(new FileReader(f));
            //StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                //sb.append(line);
                matcher = pattern.matcher(line);
                if(matcher.matches())
                    sysCallList.add(new SysCall(line));
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
