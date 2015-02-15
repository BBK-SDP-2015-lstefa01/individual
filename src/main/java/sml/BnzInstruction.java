package sml;

import lombok.Getter;

import java.util.logging.Logger;

/**
 * Branch instruction type: jumps to execute the instruction with the label specified
 * Unless the register specified in the branch instruction is blank-a message is logged to the console in this case
 * Created by liliya on 17/01/2015.
 */
@Getter
public class BnzInstruction extends Instruction {

    private int op1;
    private String newLabel;

    private final static Logger LOGGER = Logger.getLogger(BnzInstruction.class.getName());

    public BnzInstruction(String label, String opcode) {
        super(label, opcode);
    }

    public BnzInstruction(String label, int op1, String newLabel) {
        this(label, "bnz");
        this.op1 = op1;
        this.newLabel = newLabel;
    }

    @Override
    public void execute(Machine m) {
        if (m.getRegisters().getRegister(op1) != 0) {   //check register is not 0
            m.getProg().stream().forEach(i-> {if (i.label.equals(newLabel))    //change the PC of the program to the new instruction register
                                                    {m.setPc(m.getProg().indexOf(i));}}); }
         else  LOGGER.info("\"Jump to " + newLabel + " not executed as register " + op1 + " is empty\"");

    }

    @Override
    public String toString() {
        return super.toString() + " jump to instruction " + newLabel + " next";
    }
}


