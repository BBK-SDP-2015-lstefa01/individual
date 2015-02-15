package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    private final static Logger LOGGER = Logger.getLogger(BnzInstruction.class.getName());

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
        if (line.equals("")) { return null;}
        String ins = scan();                       //get the opcode for the instruction
        return getInstructionObject(ins, label);  //generate and return new instruction object of the required class
    }

    /*
     * Return the first word of line and remove it from line. If there is no
     * word, return ""
     */
    private String scan() {
        line = line.trim();
        if (line.length() == 0) {
            return "";
        }
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
     * @param ins   the opcode of the instruction
     * @param label the label of the instruction line
     * @return the new Instruction object
     */
    Instruction getInstructionObject(String ins, String label) {

        Class<?> cls;               //the instruction class
        Constructor cons = null;   //constructor for the instruction class
        Object[] consArgs;        //array of arguments to pass to the instruction subtype constructor
        Class[] paramTypes;      //list of parameter types that a constructor has

        try {
            cls = getInstructionTypeClass(ins);     //Get the class
            Constructor[] constructs = cls.getConstructors();
            for (Constructor c : constructs) {  //check all constructors the class has
                paramTypes = c.getParameterTypes();
                for (Class param_cl : paramTypes) {
                    /* this check indicates that the constructor takes an int, i.e. a register, indicating it is a
                       suitable constructor to create the correct instruction type with*/
                    if (param_cl.getName().equals("int")) {
                        break;
                    }
                }
                cons = c;           //Get the constructor
            }
            if (cons == null) {
                throw new RuntimeException("No suitable constructor for this instruction type exists!");
            }
            paramTypes = cons.getParameterTypes();            //get the parameter types
            consArgs = new Object[paramTypes.length];        //get the actual constructor arguments
            for (int i = 1; i < paramTypes.length; i++) {
                if (paramTypes[i].getTypeName().equals("int")) {
                    consArgs[i] = scanInt();
                } else if (paramTypes[i].getTypeName().equals("java.lang.String")) {
                    consArgs[i] = scan();
                }
            }
            Instruction instruction = (Instruction) cons.newInstance(consArgs);
            instruction.label = label;  //set label which is an inherited field
            instruction.opcode = ins;   //set opcode which is an inherited field
            return instruction;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            LOGGER.severe("Something went wrong while trying to create the correct instruction object");
        }
        return null;

    }

    /*
     * Helper method
     * returns the class object representing the instruction type required for the given operation
     * @param opcode the opcode for the specified transaction
     * @return class object for the specific instruction or null if there is no such class object
     */
    private Class<?> getInstructionTypeClass(String opcode) {
        StringBuilder opcd = new StringBuilder();
        opcd.append(opcode.substring(0, 1).toUpperCase()).append(opcode.substring(1));
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

    /*
     * Helper method
     * Looks through the java class path for the project to find all roots
     * @return a list of all root files/dirs
     */
    private static List<File> getRoots() {
        List<File> roots = new ArrayList<>();
        String classpath = System.getProperty("java.class.path");
        String[] locations = classpath.split(System.getProperty("path.separator"));
        for (String loc : locations) {
            roots.add(new File(loc));
        }
        return roots;
    }

    /*
     * Helper method
     * Using java class path roots, this method discovers all classes which inherit from Instruction and returns a list of them
     * This includes both classes within and outside of jar files
     * @param roots the list of roots on the class path
     * @return a list of classes which are subclasses of Instruction
     */
    private List<Class<?>> buildInstructionClassList(List<File> roots) {
        List<File> classFilesNotInJars = new ArrayList<>();
        for (File f : roots) {
            if (f.getName().contains(".jar")) {
                continue;
            }
            Translator.findAllNonJarClasses(f, classFilesNotInJars);
        }
        return convertFilesToClasses(classFilesNotInJars).stream()
                                                         .filter(Instruction.class::isAssignableFrom)
                                                         .collect(Collectors.toList());
    }

    /*
     * Helper method
     * Recursively finds all entries outside of jar files which have extension .class
     * @param fileOrFolder to search in, e.g. target
     * @param stor         the list to save results in as part of the recursive call
     */
    private static void findAllNonJarClasses(File fileOrFolder, List<File> stor) {
        if (!fileOrFolder.exists()) {
            return;
        }
        stor.add(fileOrFolder);
        if (fileOrFolder.isFile()) {
            return;
        }
        if (fileOrFolder.listFiles() != null) {
            for (File f : fileOrFolder.listFiles()) {
                stor.add(f);
                if (f.isDirectory()) {
                    findAllNonJarClasses(f, stor);
                }
            }
        }

    }

    /*
     * Helper method
     * Takes in a list of files generated from the class path search and converts them to class names
     * which can be used to create objects via reflection
     * @param files list of files
     * @return list of Classes
     */
    private static List<Class<?>> convertFilesToClasses(List<File> files) {
        List<Class<?>> classes = new ArrayList<>();
        String formattedPath = "";
        for (File f : files) {
            if (f.getName().endsWith(".class")) {
                try {
                    for (File rootFile : getRoots()) {
                        if (f.getAbsolutePath().contains(rootFile.getAbsolutePath())) {
                            int rootPathLength = rootFile.getAbsolutePath().length();
                            formattedPath = f.getAbsolutePath()
                                             .substring(rootPathLength + 1, f.getAbsolutePath().length() - 6)
                                             .replace("/", ".");
                        }
                    }
                    classes.add(Class.forName(formattedPath));
                } catch (ClassNotFoundException e) {
                    LOGGER.severe("Could not identify path to load class from");
                }
            }
        }
        return classes;
    }

}