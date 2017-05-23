package fr.inria.yajta;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.*;

/**
 * Created by nharrand on 20/04/17.
 */
public class Cleaner {

    @Parameter(names = {"--help", "-h"}, help = true, description = "Display this message.")
    private boolean help;
    @Parameter(names = {"--input-file", "-i"}, description = "File containing the trace to be cleaned.")
    private String inputFileName;
    @Parameter(names = {"--output-file", "-o"}, description = "File containing the trace once cleaned. Default: input file")
    private String outputFileName;
    @Parameter(names = {"--main-not-traced", "-m"}, description = "If the main method is traced, an additional ']}' nedd to be added. Default: false")
    private boolean isMainTraced;
    @Parameter(names = {"--Shutdown-traced", "-s"}, description = "If the main method is traced, an additional ']}' nedd to be added. Default: true")
    private boolean isShutdownTraced;

    public static void printUsage(JCommander jcom) {
        jcom.usage();
    }

    public static void main( String ... args ) {
        Cleaner cleaner = new Cleaner();
        JCommander jcom = new JCommander(cleaner,args);

        if(cleaner.help || cleaner.inputFileName == null) {
            printUsage(jcom);
        } else {
            File in, out;
            in = new File(cleaner.inputFileName);
            if(cleaner.outputFileName != null) out = new File(cleaner.outputFileName);
            else out = new File(cleaner.inputFileName);

            correct(in,out, cleaner.isMainTraced, cleaner.isShutdownTraced);
        }


        /*if(args.length < 1) {
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

        }*/
    }

    public static String readLineFiltered(BufferedReader b) throws IOException {
        String line = b.readLine();
        if(line == null) return null;
            //else if (line.matches("\\[inria filter\\].*")) {
        else if (line.matches(".*\\[yalta filter\\].*")) {
            return  line.split("\\[yalta filter\\]")[1];
            //return  line.substring(14);
        }
        else return readLineFiltered(b);
    }


    public static void correct(File in, File out, boolean isMainTraced, boolean isShutdownTraced) {
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
                sb.append(line + "\n");
                line = line2;
                line2 = readLineFiltered(br);
            }

            String buf = "{ \"name\": \"Thread\", \"children\": [\n" + sb.toString() + line.substring(0,line.length()-1) + "\n]}";
            if(!isMainTraced) buf += "\n]}";
            if(isShutdownTraced) buf += "\n]}\n]}";

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