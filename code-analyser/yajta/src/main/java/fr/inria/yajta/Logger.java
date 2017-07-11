package fr.inria.yajta;

import sun.reflect.generics.tree.Tree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by nharrand on 19/04/17.
 */
public class Logger {
    File log;
    BufferedWriter bufferedWriter;
    static Logger instance = new Logger();

    Map<String, Map.Entry<TreeNode, TreeNode>> threadLogs = new HashMap<>();

    public static Logger getInstance() {
        return instance;
    }

    private Logger() {
    }

    public synchronized void log(String thread, String method) {
        Map.Entry<TreeNode, TreeNode> entry = threadLogs.get(thread);
        if(entry == null) {
            TreeNode cur = new TreeNode();
            cur.method = thread;
            entry = new HashMap.SimpleEntry<>(cur,cur.addChild(method));
            threadLogs.put(thread, entry);
        } else {
            entry.setValue(entry.getValue().addChild(method));
            threadLogs.put(thread,entry);
        }
    }

    public synchronized void done(String thread) {
        Map.Entry<TreeNode, TreeNode> entry = threadLogs.get(thread);
        if(entry != null) {
            if(entry.getValue() != null) entry.setValue(entry.getValue().parent);
        }
    }

    public void flush() {
        int i = (int) Math.floor(Math.random() * (double) Integer.MAX_VALUE);
        log = new File("log" + i + ".json");
        try {
            if(log.exists()) log.delete();
            log.createNewFile();
            bufferedWriter = new BufferedWriter(new FileWriter(log, true));
            bufferedWriter.append("{\"name\":\"Threads\", \"children\":[");
            for(Map.Entry<String, Map.Entry<TreeNode, TreeNode>> e: threadLogs.entrySet()) {
                e.getValue().getKey().print(bufferedWriter);
            }
            bufferedWriter.append("]}");
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class TreeNode {
        protected String method;
        protected List<TreeNode> children;
        protected TreeNode parent;

        public TreeNode addChild(String m) {
            TreeNode t = new TreeNode();
            t.method = m;
            t.parent = this;
            if(children == null) children = new ArrayList<>();
            children.add(t);
            return t;
        }

        public void print(BufferedWriter b) throws IOException {
            b.append("{\"name\":\"" + method + "\", \"children\":[");
            if(children != null) {
                boolean isFirst = true;
                for (TreeNode t : children) {
                    if (isFirst) isFirst = false;
                    else b.append(",");
                    t.print(b);
                }
            }
            b.append("]}");
        }
    }

}
