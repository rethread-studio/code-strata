package fr.inria;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
//import xbn.lang.SimpleXbnObject;

/**
 * Created by nharrand on 12/03/17.
 */
public class JarParser {

    public Map<String,int[]> methodsByteCode = new HashMap<>();

    public boolean verbose = false;


    public void printMethodStubs(JarFile jarFile) throws Exception {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            String entryName = entry.getName();
            if (entryName.endsWith(".class")) {
                ClassNode classNode = new ClassNode();

                InputStream classFileInputStream = jarFile.getInputStream(entry);
                try {
                    ClassReader classReader = new ClassReader(classFileInputStream);
                    classReader.accept(classNode, 0);
                } finally {
                    classFileInputStream.close();
                }

                if(verbose) System.out.println(describeClass(classNode));
                else describeClass(classNode);
            }
        }
    }

    public String describeClass(ClassNode classNode) {
        StringBuilder classDescription = new StringBuilder();

        Type classType = Type.getObjectType(classNode.name);



        // The class signature (e.g. - "public class Foo")
        if ((classNode.access & Opcodes.ACC_PUBLIC) != 0) {
            classDescription.append("public ");
        }

        if ((classNode.access & Opcodes.ACC_PRIVATE) != 0) {
            classDescription.append("private ");
        }

        if ((classNode.access & Opcodes.ACC_PROTECTED) != 0) {
            classDescription.append("protected ");
        }

        if ((classNode.access & Opcodes.ACC_ABSTRACT) != 0) {
            classDescription.append("abstract ");
        }

        if ((classNode.access & Opcodes.ACC_INTERFACE) != 0) {
            classDescription.append("interface ");
        } else {
            classDescription.append("class ");
        }

        classDescription.append(classType.getClassName()).append("\n");
        classDescription.append("{\n");



        // The method signatures (e.g. - "public static void main(String[]) throws Exception")
        @SuppressWarnings("unchecked")
        List<MethodNode> methodNodes = classNode.methods;

        for (MethodNode methodNode : methodNodes) {
            String methodDescription = describeMethod(classType.getClassName(), methodNode);
            classDescription.append("\t").append(methodDescription).append("\n");
        }



        classDescription.append("}\n");

        return classDescription.toString();
    }

    public String describeMethod(String classQName, MethodNode methodNode) {

        StringBuilder methodDescription = new StringBuilder();

        Type returnType = Type.getReturnType(methodNode.desc);
        Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);

        @SuppressWarnings("unchecked")
        List<String> thrownInternalClassNames = methodNode.exceptions;

        /*if ((methodNode.access & Opcodes.ACC_PUBLIC) != 0) {
            methodDescription.append("public ");
        }

        if ((methodNode.access & Opcodes.ACC_PRIVATE) != 0) {
            methodDescription.append("private ");
        }

        if ((methodNode.access & Opcodes.ACC_PROTECTED) != 0) {
            methodDescription.append("protected ");
        }

        if ((methodNode.access & Opcodes.ACC_STATIC) != 0) {
            methodDescription.append("static ");
        }

        if ((methodNode.access & Opcodes.ACC_ABSTRACT) != 0) {
            methodDescription.append("abstract ");
        }

        if ((methodNode.access & Opcodes.ACC_SYNCHRONIZED) != 0) {
            methodDescription.append("synchronized ");
        }

        methodDescription.append(returnType.getClassName());
        methodDescription.append(" ");*/
        methodDescription.append(methodNode.name);

        methodDescription.append("(");
        for (int i = 0; i < argumentTypes.length; i++) {
            Type argumentType = argumentTypes[i];
            if (i > 0) {
                methodDescription.append(", ");
            }
            methodDescription.append(argumentType.getClassName());
        }
        methodDescription.append(")");

        /*if (!thrownInternalClassNames.isEmpty()) {
            methodDescription.append(" throws ");
            int i = 0;
            for (String thrownInternalClassName : thrownInternalClassNames) {
                if (i > 0) {
                    methodDescription.append(", ");
                }
                methodDescription.append(Type.getObjectType(thrownInternalClassName).getClassName());
                i++;
            }
        }*/
        //methodDescription.append("{");
        InsnList insnList = methodNode.instructions;
        int[] b = new int[insnList.size()];
        int[] b2 = new int[insnList.size()];
        for (int i = 0; i < insnList.size(); i++) {
            b[i] = insnList.get(i).getOpcode();
            b2[i] = insnList.get(i).getType();
            //methodDescription.append(", (" + b[i] + ", " + b2[i] + ")");
        }
        //methodDescription.append("}");
        methodsByteCode.put(classQName + "." + methodDescription.toString(), b);

        return methodDescription.toString();
    }
}
