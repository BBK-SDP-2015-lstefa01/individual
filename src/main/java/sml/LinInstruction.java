package sml;

import lombok.Getter;

/**
 * This class ....
 *
 * @author someone
 */
@Getter
public class LinInstruction extends Instruction {
    private int register;
    private int value;

    public LinInstruction(String label, String opcode) {
        super(label, opcode);
    }

    public LinInstruction(String label, int register, int value) {
        this(label, "lin");
        this.register = register;
        this.value = value;

    }

    @Override
    public void execute(Machine m) {
        m.getRegisters().setRegister(register, value);
    }

    @Override
    public String toString() {
        return super.toString() + " register " + register + " value is " + value;
    }
}
