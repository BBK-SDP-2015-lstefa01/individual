package sml;

import org.junit.Test;
import sml.AddInstruction;
import sml.LinInstruction;
import sml.Machine;
import sml.MulInstruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by liliya on 04/02/2015.
 */
public class MulInstructionTest {

    @Test
    public void testMulInstruction(){
        MulInstruction mul = new MulInstruction("mul", 5, 6, 5);
        assertNotNull(mul);
        assertEquals(5, mul.getResult());
        assertEquals(6, mul.getOp1());
        assertEquals(5, mul.getOp2());
    }

    @Test
    public void testExecute(){
        Machine m = new Machine();
        m.execute();

        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("l2", 6, 10);
        lin2.execute(m);


        MulInstruction mul = new MulInstruction("m1", 10, 6, 5);
        mul.execute(m);

        assertEquals(10, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));
        assertEquals(100, m.getRegisters().getRegister(10));

    }
}
