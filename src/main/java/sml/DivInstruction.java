package sml;

import lombok.Getter;

import java.util.logging.Logger;

/**
 * Class used to create DivInstruction object, which divides the values contained in two registers
 * and stores the result in a third(which may be one of the first 2) registers
 * Attempt to divide by 0 generates an ArithmeticExpression and the instruction being ignored with message logged to screen
 * No registers get updated as part of an attempted division by 0
 * Created by liliya on 17/01/2015.
 */
@Getter
public class DivInstruction extends Instruction {

    private int op1;
    private int op2;
    private int result;

    private final static Logger LOGGER = Logger.getLogger(BnzInstruction.class.getName());

    public DivInstruction(String label, String opcode) {
        super(label, opcode);
    }

    public DivInstruction(String label, int result, int op1, int op2) {
        this(label, "div");
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
    }

    @Override
    public void execute(Machine m) {
        try {
            int value1 = m.getRegisters().getRegister(op1);
            int value2 = m.getRegisters().getRegister(op2);
            if (value2 != 0) {
                m.getRegisters().setRegister(result, value1 / value2);
            } else {
                throw new ArithmeticException("Division by 0");
            }
        } catch (ArithmeticException ae) {
            LOGGER.warning("Instruction " + super.label + " attempted to perform division by 0." + "\n" +
                    "Operation will be skipped and no registers updated");
        }

    }

    @Override
    public String toString() {
        return super.toString() + " " + op1 + " / " + op2 + " to " + result;
    }

}
