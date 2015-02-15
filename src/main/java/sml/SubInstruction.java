package sml;

import lombok.Getter;

/**
 * Class SubInstruction used to subtract values held in two pre-specified registers and store the result
 * in a third or one of existing two registers as specified by the instruction
 * Subtraction will be done out of the first of the specified operand registers
 * Created by liliya on 17/01/2015.
 */
@Getter
public class SubInstruction extends Instruction {

    private int result;
    private int op1;
    private int op2;

    public SubInstruction(String label, String op) {
        super(label, op);

    }

    public SubInstruction(String label, int result, int op1, int op2) {
        this(label, "sub");
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public void execute(Machine m) {
        int value1 = m.getRegisters().getRegister(op1);
        int value2 = m.getRegisters().getRegister(op2);
        m.getRegisters().setRegister(result, value1 - value2);
    }

    @Override
    public String toString() {
        return super.toString() + " " + op1 + " - " + op2 + " to " + result;
    }
}
