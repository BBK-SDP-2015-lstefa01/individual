import org.junit.*;
import static org.junit.Assert.*;

import sml.*;

/**
 * Created by liliya on 04/02/2015.
 */
public class BnzInstructionTest {

    @Test
    public void testInstructionCreation(){

        BnzInstruction bnz = new BnzInstruction("b1", 5,"b5");
        assertEquals(5, bnz.getOp1());
        assertEquals("b5", bnz.getNewLabel());
    }

    @Test
    public void testExecute(){
        Machine m = new Machine();
        m.execute();

        LinInstruction lin1 = new LinInstruction("b1", 5, 23);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("b2", 6, 7);
        lin2.execute(m);
        BnzInstruction bnz = new BnzInstruction("b3", 5,"b5");
        bnz.execute(m);
        AddInstruction add1 = new AddInstruction("b4", 5, 5, 6);
        add1.execute(m);
        SubInstruction sub1 = new SubInstruction("b5", 10, 5, 6);
        sub1.execute(m);

        assertNotEquals(30, m.getRegisters().getRegister(5));


    }

    @Test
    public void testExecuteWithEmptyRegister(){

    }

}
