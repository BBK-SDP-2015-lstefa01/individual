package sml;

/**
 * Created by liliya on 17/01/2015.
 */
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
        if(m.getRegisters().getRegister(op1)!=0){
            for(Instruction i: m.getProg()){
                if(i.label==newLabel){
                    int index = m.getProg().indexOf(i);
                    m.setPc(m.getProg().indexOf(i));
                }

            }

        }

    }
}
