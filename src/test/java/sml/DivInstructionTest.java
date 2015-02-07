package sml;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by liliya on 04/02/2015.
 */
public class DivInstructionTest {

    Machine m = new Machine();

    public void buildUp() {
        m.execute();

        LinInstruction lin1 = new LinInstruction("lin", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("lin", 6, 10);
        lin2.execute(m);

    }

    @Test
    public void testDivInstruction() {
        DivInstruction div = new DivInstruction("div", 5, 6, 5);
        assertNotNull(div);
        assertEquals(5, div.getResult());
        assertEquals(6, div.getOp1());
        assertEquals(5, div.getOp2());
    }

    @Test
    public void testExecute() {

        buildUp();
        DivInstruction div = new DivInstruction("div", 10, 6, 5);
        div.execute(m);

        assertEquals(10, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));
        assertEquals(1, m.getRegisters().getRegister(10));

    }

    @Test
    public void testDivByZero() {
        buildUp();
        LinInstruction lin3 = new LinInstruction("l1", 7, 0);
        lin3.execute(m);

        DivInstruction div = new DivInstruction("d1", 10, 6, 7);
        div.execute(m);
        //register remains empty as operation is not executed
        assertEquals(0, m.getRegisters().getRegister(10));

    }

    @Test
    public void testToString() {
        buildUp();
        DivInstruction div = new DivInstruction("d2", 10, 6, 5);
        div.execute(m);
        String expectedString = "d2: div 6 / 5 to 10".trim();

        assertEquals(expectedString, div.toString());
    }
}
