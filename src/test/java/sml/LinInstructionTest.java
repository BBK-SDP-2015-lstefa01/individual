package sml;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by liliya on 04/02/2015.
 */
public class LinInstructionTest {

    Machine m = new Machine();

    public void buildUp() {
        m.execute();
    }

    @Test
    public void testLinInstructionCreation() {
        LinInstruction lin = new LinInstruction("lin", 5, 10);
        assertEquals(5, lin.getRegister());
    }

    @Test
    public void testExecute() {
        buildUp();
        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("l2", 6, 15);
        lin2.execute(m);

        assertEquals(15, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));

    }

    @Test
    public void testToString() {
        buildUp();
        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);

        String expectedString = "l1: lin register 5 value is 10".trim();

        assertEquals(expectedString, lin1.toString());
    }

}
