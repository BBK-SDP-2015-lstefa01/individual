package sml;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liliya on 17/01/2015.
 */
@Getter
@Setter
public class BnzInstruction extends Instruction {

    private int op1;
    private String newLabel;

    public BnzInstruction(String label, String opcode){
        super(label, opcode);
    }

    public BnzInstruction(String label, int op1, String newLabel){
        this(label, "bnz");
        this.op1 = op1;
        this.newLabel = newLabel;
    }

    @Override
    public void execute(Machine m) {
        if(m.getRegisters().getRegister(op1)!=0){   //check register is not 0
            for(Instruction i: m.getProg()){
                if(i.label.equals(newLabel)){       //find the required instruction
                    int index = m.getProg().indexOf(i);
                    m.setPc(m.getProg().indexOf(i));
                }

            }

        }

    }

    @Override
    public String toString(){
        return super.toString() + " jump to instruction " + newLabel +" next";
    }
}
