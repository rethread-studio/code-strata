package fr.inria.yajta;

import java.io.*;

/**
 * Created by nharrand on 20/04/17.
 */
public class Cleaner {

    public static void main( String ... args ) {
        if(args.length < 1) {
            System.out.println("usage fr.inria.yajta.Cleaner file.json\n or fr.inria.yajta.Cleaner in.json out.json");
        } else {
            File in, out;
            if(args.length == 1) {
                in = new File(args[0]);
                out = new File(args[0]);
            } else {
                in = new File(args[0]);
                out = new File(args[1]);
            }
            correct(in,out);
        }
    }

    public static String readLineFiltered(BufferedReader b) throws IOException {
        String line = b.readLine();
        if(line == null) return null;
            //else if (line.matches("\\[inria filter\\].*")) {
        else if (line.matches(".*\\[inria filter\\].*")) {
            return  line.split("\\[inria filter\\]")[1];
            //return  line.substring(14);
        }
        else return readLineFiltered(b);
    }


    public static void correct(File in, File out) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(in));
            StringBuilder sb = new StringBuilder();
            String line = readLineFiltered(br);
            String line2 = readLineFiltered(br);
            while (line2 != null) {
                if(line2.startsWith("]") && line.endsWith(",")) {
                    line = line.substring(0,line2.length()-1);
                }
                sb.append(line);
                line = line2;
                line2 = readLineFiltered(br);
            }

            String buf = "{ \"name\": \"Thread\", \"children\": [\n" + sb.toString() + line.substring(0,line.length()-1) + "\n]}" + "\n]}";

            try {
                PrintWriter w = new PrintWriter(out);
                w.print(buf);
                w.close();
            } catch (Exception ex) {
                System.err.println("Problem writing log");
                ex.printStackTrace();
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
    }

}