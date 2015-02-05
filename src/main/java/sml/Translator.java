package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

        try (Scanner sc = new Scanner(new File(fileName))) {    //relies on default encoding
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
                //TODO check if the label is duplicate

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
        if (line.equals(""))
            return null;
        String ins = scan();    //get the opcode for the instruction
        return getInstructionObject(ins, label);    //generate and return new instruction object
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

    /**
     * Takes in an instruction opcode and label and returns an Instruction object of the right type
     * @param ins the opcode of the instruction
     * @param label the label of the instruction line
     * @return the new Instruction object
     */
     Instruction getInstructionObject(String ins, String label) {

        Class<?> cls;       //the appropriate instruction class
        Constructor cons = null;   //constructor for the instruction class
        Object[] consArgs;  //array of arguments to pass to the instruction subtype constructor

        try {
            cls = getInstructionTypeClass(ins);
            Constructor[] constructs = cls.getConstructors();
            //TODO need to find a more robust way to select the correct constructor
            for(int i=0; i<constructs.length; i++){
                Class [] paramTypes = constructs[i].getParameterTypes();
                for(int j =0; j<paramTypes.length; j++){
                    if(!paramTypes[j].getName().equals("java.lang.String")){
                        break;
                    }
                }
                cons = constructs[i];
            }
            if(cons == null) {throw new RuntimeException("No constructor for this instruction exists!");}
            Class[] paramType = cons.getParameterTypes();
            consArgs = new Object[paramType.length];        //parameters of constructor selected

            for (int i = 1; i < paramType.length; i++) {
                if (paramType[i].getTypeName().equals("int")) {
                    consArgs[i] = scanInt();
                } else if (paramType[i].getTypeName().equals("java.lang.String")) {
                    consArgs[i] = scan();
                }
            }
            cons = cls.getConstructor(paramType);  //ensure the right constructor is being selected
            Instruction instruction = (Instruction) cons.newInstance(consArgs);
            instruction.label = label;  //set label which is an inherited field
            instruction.opcode = ins;   //set opcode which is an inherited field
            return instruction;

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    /**
     * returns the class object representing the instruction type required for the given operation
     * @param opcode the opcode for the specified transaction
     * @return class object for the specific instruction or null if there is no such class object
     */
    private Class<?> getInstructionTypeClass(String opcode) {
        StringBuilder opcd = new StringBuilder();
        opcd.append(opcode.substring(0, 1).toUpperCase()).append(opcode.substring(1));
        //return findAllClassesInProgram().stream().filter((Class<?>c)->c.getName().contains(opcd)).findFirst().get();
        for (Class<?> c : findAllClassesInProgram()) {
            if (c.getName().contains(opcd)) {
                return c;
            }
        }
        return null;
    }

    private List<Class<?>> findAllClassesInProgram() {

        List<File> roots = getRoots();  //find all the class roots

        return buildInstructionClassList(roots);
    }

    /**
     * Looks through the java class path for the project to find all roots
     * @return
     */
    private static List<File> getRoots() {
        List<File> roots = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] locations = classpath.split(System.getProperty("path.separator"));
        for (int i = 0; i < locations.length; i++) {
            roots.add(new File(locations[i]));
        }
        return roots;
    }

    /**
     * Using java class path roots, this method discovers all classes which inherit from Instruction and returns a list of them
     * This includes both classes within and outside of jar files
     * @param roots the list of roots on the class path
     * @return a list of classes which are subclasses of Instruction
     */
    private List<Class<?>> buildInstructionClassList(List<File> roots) {
        List<File> classFilesNotInJars = new ArrayList<>();
        List<Class<?>> instructionChildren = new ArrayList<>();
        for (File f : roots) {
            if (f.getName().contains(".jar")) {
                continue;
            }
            Translator.findAllNonJarClasses(f, classFilesNotInJars);
        }

        List<File> commonListClassFiles = new ArrayList<>();
        commonListClassFiles.addAll(classFilesNotInJars);


        for (Class<?> c : convertFilesToClasses(commonListClassFiles)) {
            if (Instruction.class.isAssignableFrom(c)) {
                instructionChildren.add(c);
            }
        }
        return instructionChildren;
    }


    /**
     * Helper class
     * Recursively finds all entries outside of jar files which have extension .class
     * @param fileOrFolder to search in, e.g. target
     * @param stor the list to save results in as part of the recursive call
     */
    private static void findAllNonJarClasses(File fileOrFolder, List<File> stor) {
        stor.add(fileOrFolder);
        if (fileOrFolder.isFile()) {
            return;
        }
            if(!fileOrFolder.exists()){ throw new RuntimeException("No classes have been found");}
            for (File f : fileOrFolder.listFiles()) {
                stor.add(f);
                if (f.isDirectory()) {
                    findAllNonJarClasses(f, stor);
                }
            }
        }

    /**
     * Helper method
     * Takes in a list of files generated from the class path search and converts them to class names
     * which can be used to create objects via reflection
     * @param files list of files
     * @return list of Classes
     */
    private static List<Class<?>> convertFilesToClasses(List<File> files) {
        List<Class<?>> clazzes = new ArrayList<>();
        String formattedPath = "";
        for (File f : files) {
            if (f.getName().endsWith(".class")) {
                try {
                    for (File rootFile : getRoots()) {
                        if (f.getAbsolutePath().contains(rootFile.getAbsolutePath())) {
                            int rootPathLength = rootFile.getAbsolutePath().length();
                            formattedPath = f.getAbsolutePath().substring(rootPathLength + 1, f.getAbsolutePath().length() - 6);
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

}