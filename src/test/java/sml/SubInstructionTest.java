package sml;

import org.junit.Test;
import sml.AddInstruction;
import sml.LinInstruction;
import sml.Machine;
import sml.SubInstruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by liliya on 04/02/2015.
 */
public class SubInstructionTest {

    @Test
    public void testSubInstruction(){
        SubInstruction sub = new SubInstruction("sub", 5, 6, 5);
        assertNotNull(sub);
        assertEquals(5, sub.getResult());
        assertEquals(6, sub.getOp1());
        assertEquals(5, sub.getOp2());
    }

    @Test
    public void testExecute(){
        Machine m = new Machine();
        m.execute();

        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("l2", 6, 11);
        lin2.execute(m);


        SubInstruction sub = new SubInstruction("s1", 10, 6, 5);
        sub.execute(m);

        assertEquals(11, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));
        assertEquals(1, m.getRegisters().getRegister(10));

    }
}
