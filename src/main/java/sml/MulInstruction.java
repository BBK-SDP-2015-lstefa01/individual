package sml;

import lombok.Getter;

/**
 * This class is used to create a MulInstruction object which calculates the product of the values
 * in two registers specified and stores that product in a new register or one of the two existing ones
 * Created by liliya on 17/01/2015.
 */
@Getter
public class MulInstruction extends Instruction {

    private int op1;
    private int op2;
    private int result;

    public MulInstruction(String label, String opcode) {
        super(label, opcode);
    }

    public MulInstruction(String label, int result, int op1, int op2) {
        this(label, "mul");
        this.op1 = op1;
        this.op2 = op2;
        this.result = result;
    }

    @Override
    public void execute(Machine m) {
        int value1 = m.getRegisters().getRegister(op1);
        int value2 = m.getRegisters().getRegister(op2);
        m.getRegisters().setRegister(result, value1 * value2);
    }

    @Override
    public String toString() {
        return super.toString() + " " + op1 + " * " + op2 + " to " + result;
    }
}
