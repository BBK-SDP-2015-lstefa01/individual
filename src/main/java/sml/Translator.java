package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

    // word + line is the part of the current line that's not yet processed
    // word has no whitespace
    // If word and line are not empty, line begins with whitespace
    private String line = "";
    private Labels labels; // The labels of the program being translated
    private ArrayList<Instruction> program; // The program to be created
    private String fileName; // source file of SML code

    private static final String SRC = "src";

    public Translator(String fileName) {
        this.fileName = SRC + "/" + fileName;
    }

    // translate the small program in the file into lab (the labels) and
    // prog (the program)
    // return "no errors were detected"
    public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

        try (Scanner sc = new Scanner(new File(fileName))) {
            // Scanner attached to the file chosen by the user
            labels = lab;
            labels.reset();
            program = prog;
            program.clear();

            try {
                line = sc.nextLine();
            } catch (NoSuchElementException ioE) {
                return false;
            }

            // Each iteration processes line and reads the next line into line
            while (line != null) {
                // Store the label in label
                String label = scan();
                //check if the label is duplicate

                if (label.length() > 0) {
                    Instruction ins = getInstruction(label);
                    if (ins != null) {
                        labels.addLabel(label);
                        program.add(ins);
                    }
                }

                try {
                    line = sc.nextLine();
                } catch (NoSuchElementException ioE) {
                    return false;
                }
            }
        } catch (IOException ioE) {
            System.out.println("File: IO error " + ioE.getMessage());
            return false;
        }
        return true;
    }

    // line should consist of an MML instruction, with its label already
    // removed. Translate line into an instruction with label label
    // and return the instruction
    public Instruction getInstruction(String label) {
        int s1; // Possible operands of the instruction
        int s2;
        int r;
        int x;
        String l;
        if (line.equals(""))
            return null;
        String ins = scan();
        Class<?> cls = null;
        Constructor cons = null;
       try {
            switch (ins) {
                case "add":
                    r = scanInt();
                    s1 = scanInt();
                    s2 = scanInt();
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class, int.class, int.class);
                    return (Instruction) cons.newInstance(label,r, s1, s2);
                case "lin":
                    r = scanInt();
                    s1 = scanInt();
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class, int.class);
                    return (Instruction) cons.newInstance(label, r, s1);
                case "sub":
                    r = scanInt();
                    s1 = scanInt();
                    s2 = scanInt();
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class, int.class, int.class);
                    return (Instruction) cons.newInstance(label, r, s1, s2);
                case "mul":
                    r = scanInt();
                    s1 = scanInt();
                    s2 = scanInt();
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class, int.class, int.class);
                    return (Instruction) cons.newInstance(label, r, s1, s2);
                case "div":
                    r = scanInt();
                    s1 = scanInt();
                    s2 = scanInt();
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class, int.class, int.class);
                    return (Instruction) cons.newInstance(label, r, s1, s2);
                case "out":
                    r = scanInt();
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class);
                    return (Instruction) cons.newInstance(label, r);
                case "bnz":
                    r = scanInt();    //register
                    l = scan();        //new label
                    cls = getClassesForInstructionTypes(ins);
                    cons = cls.getConstructor(String.class, int.class, String.class);
                    return (Instruction) cons.newInstance(label, r, l);
            }
        }catch(NoSuchMethodException| InvocationTargetException|InstantiationException|IllegalAccessException ex){
            ex.printStackTrace();
       }
       // You will have to write code here for the other instructions.

        return null;
    }

    /*
     * Return the first word of line and remove it from line. If there is no
     * word, return ""
     */
    private String scan() {
        line = line.trim();
        if (line.length() == 0)
            return "";
        int i = 0;
        while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
            i = i + 1;
        }
        String word = line.substring(0, i);
        line = line.substring(i);
        return word;
    }

    // Return the first word of line as an integer. If there is
    // any error, return the maximum int
    private int scanInt() {
        String word = scan();
        if (word.length() == 0) {
            return Integer.MAX_VALUE;
        }
        try {
            return Integer.parseInt(word);
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    private Class<?> getClassesForInstructionTypes(String opcode){
        StringBuilder opcd = new StringBuilder();
        opcd.append(opcode.substring(0,1).toUpperCase()).append(opcode.substring(1));
        for(Class<?> c:findAllClassesInProgram()){
            if(c.getName().contains(opcd)){
                return c;
            }
        }
        return null;
    }

    private List<Class<?>> findAllClassesInProgram() {

        List<File> roots = getRoots();  //find all the class roots

        return buildClassList(roots);
    }

    private static List<File> getRoots(){
        List<File> roots = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] locations = classpath.split(System.getProperty("path.separator"));
        for (int i = 0; i < locations.length; i++) {
          //    System.out.println("Location on the classpath: "+locations[i]);
            roots.add(new File(locations[i]));
        }
        return roots;
    }

    private List<Class<?>> buildClassList(List<File> roots){
        List<File> classFilesNotInJars = new ArrayList<>();
        List<File> classFilesInJars = new ArrayList<>();
        List<Class<?>> instructionChildren = new ArrayList<>();
            for (File f : roots) {
                if (f.getName().contains(".jar")) {
                    classFilesInJars.add(f);
                }
                Translator.findAllNonJarClasses(f, classFilesNotInJars);
            }

        List<File> commonListClassFiles = new ArrayList<>();
        commonListClassFiles.addAll(classFilesInJars);
        commonListClassFiles.addAll(classFilesNotInJars);


        for(Class<?> c:convertFilesToClasses(commonListClassFiles)){
            if(Instruction.class.isAssignableFrom(c)){
                instructionChildren.add(c);
            }
        }
        return instructionChildren;
    }


    //TODO need to refactor this code and change variable names
    private List<String> findAllClassesInAJar(String path){
        List<String> classNamesInJar = new ArrayList<>();
        JarFile jar = null;
        try {
            jar = new JarFile(path);
            Enumeration e = jar.entries();
            URL [] urls = {new URL("jar:file:" + path + "!/")};
            URLClassLoader cls = URLClassLoader.newInstance(urls);

            while(e.hasMoreElements()){
                JarEntry je = (JarEntry) e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                //-6 to account for the class extension
                String className = je.getName().substring(0, je.getName().length()-6);
                classNamesInJar.add(className);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return classNamesInJar;

    }

    private static void findAllNonJarClasses(File fileOrFolder, List<File> stor){
        stor.add(fileOrFolder);
        if(fileOrFolder.isFile()){
            return;
        }
        try {
            for (File f : fileOrFolder.listFiles()) {
                stor.add(f);
                if (f.isDirectory()) {
                    findAllNonJarClasses(f, stor);
                }
            }
        }catch(NullPointerException ex){
            ex.printStackTrace();
            System.out.println("Empty folder!");
        }
    }

    private static List<Class<?>> convertFilesToClasses(List<File> files) {
        List<Class<?>> clazzes = new ArrayList<>();
        String formattedPath = "";
        for (File f : files) {
            if (f.getName().endsWith(".class")) {
                try {
                    for(File rootFile:getRoots()){
                        if(f.getAbsolutePath().contains(rootFile.getAbsolutePath())){
                            int rootPathLength = rootFile.getAbsolutePath().length();
                            formattedPath = f.getAbsolutePath().substring(rootPathLength+1, f.getAbsolutePath().length()-6);
                            formattedPath = formattedPath.replace("/", ".");
                        }
                    }
                    clazzes.add(Class.forName(formattedPath));
                } catch (ClassNotFoundException e) {
                    System.out.println("Could not identify path to load class from");
                    e.printStackTrace();
                }
            }
        }
        return clazzes;
    }



    public static void main(String [] args){
        Translator t = new Translator("testAddInstruction");
        t.findAllClassesInProgram();
    }
}