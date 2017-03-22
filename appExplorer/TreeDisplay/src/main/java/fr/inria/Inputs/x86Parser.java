package fr.inria.Inputs;

import fr.inria.DataStructure.Execution;
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
                        curMethod += readParameters(param);
                    }
                    curMethod += ")";
                    //System.out.println("Read method: " + curMethod);
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

    public static String[] readParameter(String parameters) throws InvalidCharacter {
        String res[] = new String[2];
        char first = parameters.charAt(0);
        switch (first) {
            case 'B':
                res[0] = "byte";
                res[1] = parameters.substring(1);
                break;
            case 'C':
                res[0] = "char";
                res[1] = parameters.substring(1);
                break;
            case 'D':
                res[0] = "double";
                res[1] = parameters.substring(1);
                break;
            case 'F':
                res[0] = "float";
                res[1] = parameters.substring(1);
                break;
            case 'I':
                res[0] = "int";
                res[1] = parameters.substring(1);
                break;
            case 'J':
                res[0] = "long";
                res[1] = parameters.substring(1);
                break;
            case 'S':
                res[0] = "short";
                res[1] = parameters.substring(1);
                break;
                /*case 'V':
                    result.add("void");
                    remain = remain.substring(1);
                    break;*/
            case 'Z':
                res[0] = "boolean";
                res[1] = parameters.substring(1);
                break;
            case '[':
                String[] tmp = readParameter(parameters.substring(1));
                res[0] = tmp[0] + "[]";
                res[1] = tmp[1];
                break;
            case 'L':
                res[0] = parameters.split(";")[0].substring(1);
                res[1] = parameters.substring(res[0].length() + 2);
                break;
            default:
                System.out.println("Invalid char: '" + first + "'");
                throw new InvalidCharacter();
        }
        return res;
    }

    public static String readParameters(String parameters) throws InvalidCharacter {
        /*TODO [Ljava.lang.Class -> Class
        [BII -> ?
        */
        String result = "";
        String remain = parameters;
        int i = 0;
        while(remain.length() > 0) {
            if(i != 0) result += ",";
            String[] tmp = readParameter(remain);
            result += tmp[0];
            remain = tmp[1];
            i++;
        }
        return result;
    }

    public static class InvalidCharacter extends Exception {}
}
