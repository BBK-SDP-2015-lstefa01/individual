package sml;

import org.junit.Test;
import static org.junit.Assert.*;
import sml.LinInstruction;
import sml.Machine;

import static org.hamcrest.Matcher.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by liliya on 04/02/2015.
 */
public class LinInstructionTest {


    @Test
    public void testLinInstructionCreation(){
        LinInstruction lin = new LinInstruction("lin", 5, 10);
        assertEquals(5, lin.getRegister());
    }

    @Test
    public void testExecute(){
        Machine m = new Machine();
        m.execute();

        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("l2", 6, 10);
        lin2.execute(m);


        assertEquals(10, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));

    }

}
