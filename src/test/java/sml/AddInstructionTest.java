package sml;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Created by liliya on 04/02/2015.
 *
 *
 */
public class AddInstructionTest {

    Machine m = new Machine();

    public void buildUp(){

        m.execute();
        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("l2", 6, 10);
        lin2.execute(m);

    }

    @Test
    public void testAddInstruction(){
        AddInstruction add = new AddInstruction("add", 5, 6, 5);
        assertNotNull(add);
        assertEquals(5, add.getResult());
        assertEquals(6, add.getOp1());
        assertEquals(5, add.getOp2());
    }

    @Test
    public void testExecute(){

        buildUp();
        AddInstruction add = new AddInstruction("a1", 10, 6, 5);
        add.execute(m);

        assertEquals(10, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));
        assertEquals(20, m.getRegisters().getRegister(10));

    }

    @Test
    public void testToString(){

        buildUp();
        AddInstruction add = new AddInstruction("a1", 10, 6, 5);
        add.execute(m);

        String toStringExpected = "a1: add 6 + 5 to 10".trim();
        assertEquals(toStringExpected, add.toString().trim());

    }
}
