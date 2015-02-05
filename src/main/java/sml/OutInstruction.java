package sml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.logging.Logger;

/**
 * Created by liliya on 17/01/2015.
 */
@Getter
@Setter
public class OutInstruction extends Instruction {

    private int op1;

    private final static Logger LOGGER = Logger.getLogger(BnzInstruction.class.getName());

    public OutInstruction(String label, String opcode) {
        super(label, opcode);
    }

    public OutInstruction(String label, int op1) {
        this(label, "out");
        this.op1 = op1;
    }

    @Override
    public void execute(Machine m) {
        System.out.println("Value of register "+ op1 +" is: "+ m.getRegisters().getRegister(op1));
        LOGGER.info("Value of register "+ op1 +" is: "+ m.getRegisters().getRegister(op1));

    }

    @Override
    public String toString() {
        return super.toString() + " " + "value of register " + op1 + " is printed on the console";
    }
}
