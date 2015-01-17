package sml;

import lombok.ToString;

/**
 * Created by liliya on 17/01/2015.
 */

public class OutInstruction extends Instruction {

    private int op1;
    private int regVal;

    public OutInstruction(String label, String opcode){
        super(label, opcode);
    }

    public OutInstruction(String label, int op1){
        this(label, "out");
        this.op1 = op1;
    }

    @Override
    public void execute(Machine m) {
        regVal = m.getRegisters().getRegister(op1);

        System.out.println("Value of register " + op1 +": " + regVal);

    }

    @Override
    public String toString(){
        return super.toString() + " " + "value of register " + op1 + ": " + regVal ;
    }
}
