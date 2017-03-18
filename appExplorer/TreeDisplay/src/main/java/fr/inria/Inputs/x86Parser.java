package fr.inria.Inputs;

import fr.inria.DataStructure.SysCall;
import fr.inria.DataStructure.x86Instructions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nharrand on 18/03/17.
 */
public class x86Parser {
    public x86Instructions instructions = new x86Instructions();



    private static Pattern pattern;
    private static Matcher matcher;

    public void readFromFile(File f) {
        BufferedReader br = null;

        /*
        Decoding compiled method 0x00007fcdc550edd0:
        Code:
        [Entry Point]
        [Constants]
          # {method} {0x00007fcdb746b418} '<init>' '(Ljava/lang/invoke/MethodType;Ljava/lang/invoke/LambdaForm;Ljava/lang/invoke/MemberName;ILjava/lang/invoke/DirectMethodHandle$1;)V' in 'java/lang/invoke/DirectMethodHandle$Accessor'
          # this:     rsi:rsi   = 'java/lang/invoke/DirectMethodHandle$Accessor'
          # parm0:    rdx:rdx   = 'java/lang/invoke/MethodType'
          # parm1:    rcx:rcx   = 'java/lang/invoke/LambdaForm'
          # parm2:    r8:r8     = 'java/lang/invoke/MemberName'
          # parm3:    r9        = int
          # parm4:    rdi:rdi   = 'java/lang/invoke/DirectMethodHandle$1'
          #           [sp+0xc0]  (sp of caller)
          0x00007fcdc550ef60: mov    0x8(%rsi),%r10d
          0x00007fcdc550ef64: shl    $0x3,%r10
          0x00007fcdc550ef68: cmp    %rax,%r10
        */

        //pattern = Pattern.compile("\\[pid \\d+\\]  [a-zA-Z].*\\(.*");
        pattern = Pattern.compile("\\d+  [a-zA-Z].*\\(.*");

        try {
            br = new BufferedReader(new FileReader(f));
            //StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            List<String> inst = new ArrayList<>();
            String curMethod = null, curClass = null;

            while (line != null) {
                if(line.startsWith("Decoding compiled method")) {
                    if((curMethod != null) && (curClass != null)) {
                        instructions.add(curClass,curMethod,inst);
                        inst = new ArrayList<>();
                    }

                }
                else if (line.startsWith("  # {method}")) {
                    curClass = line.split("'")[5].replace("/", ".");
                    String param = line.split("'")[3];
                    param = param.split("\\(")[1].split("\\)")[0];
                    param = param.replace("/", ".");
                    curMethod = line.split("'")[1] + "(";
                    if(!param.equals("")) {
                        int  i = 0;
                        for(String p : param.split(";")) {
                            if(i != 0) curMethod += ", ";
                            curMethod +=removeFirstCaps(p);
                            i++;
                        }
                    }
                    curMethod += ")";
                }
                else if (line.startsWith("  0x")) {//0x00007fcdc550ef68: cmp
                    String instruction = line.split("0x")[1].split(": ")[1];
                    if(instruction.matches("[a-z]*"))
                        inst.add(instruction);
                    else if(instruction.matches("[a-z]*[^a-z].*")) {
                        try {
                            inst.add(instruction.split("[^a-z]")[0]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //e.printStackTrace();
                        }
                    }
                }
                else {}
                line = br.readLine();
            }
            if((curMethod != null) && (curClass != null)) {
                instructions.add(curClass,curMethod,inst);
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

    public static String removeFirstCaps(String str) {
        while(str.matches("[A-Z].*")) str = str.substring(1);
        return str;
    }
}
