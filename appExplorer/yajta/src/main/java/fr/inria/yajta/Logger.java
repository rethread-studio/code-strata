package fr.inria.yajta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by nharrand on 19/04/17.
 */
public class Logger {
    File log;
    BufferedWriter bufferedWriter;
    static Logger instance = new Logger();

    public static Logger getInstance() {
        return instance;
    }

    private Logger() {
        int i = (int) Math.floor(Math.random() * (double) Integer.MAX_VALUE);
        log = new File("log" + i + ".json");
        try {
            if(log.exists()) log.delete();
            log.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(log, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String str) {
        try {
            bufferedWriter.append(str);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init() {}

}
