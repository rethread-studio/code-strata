package fr.inria.yajta;

/**
 * Created by nharrand on 02/05/17.
 */


public class Args {
    public String[] INCLUDES, EXCLUDES;
    public boolean strictIncludes = false, printTree = true;
    //includes=|excludes=
    public void parseArgs(String args) {
        if(args == null || args.equals("")) {
            INCLUDES = new String[0];
            EXCLUDES = new String[1];
            EXCLUDES[0] = "fr.inria.yajta";
        } else {
            String ar[];
            if(args.contains("|")) {
                ar = args.split("\\|");
            } else {
                ar = new String[1];
                ar[0] = args;
            }
            EXCLUDES = new String[1];
            INCLUDES = new String[0];
            strictIncludes = false;
            printTree = true;
            for(String p : ar) {
                parseArg(p);
            }
        }
    }

    public void parseArg(String p) {
        //System.err.println("p: " + p);
        if(p.startsWith("strict-includes")) {
            parseStrictIncludes(p);
        } else if (p.startsWith("print=")) {
            parsePrint(p);
        } else if (p.startsWith("includes=")) {
            parseIncludes(p);
        } else if (p.startsWith("excludes=")) {
            parseExcludes(p);
        }
    }

    public void parseIncludes(String p) {
        INCLUDES = parseArray(p.split("includes=")[1]);
    }
    public void parseExcludes(String p) {
        EXCLUDES = parseArray(p.split("excludes=")[1]);
    }
    public void parseStrictIncludes(String p) {
        strictIncludes = true;
    }
    public void parsePrint(String p) {
        if(p.compareTo("print=list") == 0) {
            printTree = false;
        }
    }

    public String[] parseArray(String li) {
        if(li.contains(","))
            return li.split(",");
        else {
            String res[] = new String[1];
            res[0] = li;
            return res;
        }
    }

    public void printUsage(String args) {
        System.err.println("Incorrect agent arguments. Argument must belong to the following list (and be seperated by |)");
        System.err.println("\t\t- includes=org.package(,org.package2)* Default: Empty");
        System.err.println("\t\t- excludes=org.package(,org.package2)* Default: fr.inria.yajta");
        System.err.println("\t\t- print=(list,tree) Default tree");
        System.err.println("\t\t- strinct-includes Default false");
        System.err.println("Found: \"" + args + "\"");
    }
}
