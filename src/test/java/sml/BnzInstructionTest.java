package sml;

import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by liliya on 04/02/2015.
 */
public class BnzInstructionTest {

    Machine m = new Machine();

    public void buildUp(){
        m.setProg(new ArrayList<>());
        LinInstruction lin1 = new LinInstruction("b1", 5, 23);
        m.getProg().add(lin1);
        LinInstruction lin2 = new LinInstruction("b2", 6, 7);
        m.getProg().add(lin2);
    }

    @Test
    public void testInstructionCreation(){

        BnzInstruction bnz = new BnzInstruction("b1", 5,"b5");
        assertEquals(5, bnz.getOp1());
        assertEquals("b5", bnz.getNewLabel());
    }

    @Test
    public void testExecute(){

        buildUp();
        BnzInstruction bnz = new BnzInstruction("b3", 5,"b5");
        m.getProg().add(bnz);
        AddInstruction add1 = new AddInstruction("b4", 5, 5, 6);
        m.getProg().add(add1);
        SubInstruction sub1 = new SubInstruction("b5", 10, 5, 6);
        m.getProg().add(sub1);

        m.execute();

        assertNotEquals(30, m.getRegisters().getRegister(5));

    }

    @Test
    public void testExecuteWithEmptyRegister(){
        buildUp();
        LinInstruction lin1 = new LinInstruction("b1", 7, 0);
        m.getProg().add(lin1);

        BnzInstruction bnz = new BnzInstruction("b3", 7,"b5");
        m.getProg().add(bnz);
        AddInstruction add1 = new AddInstruction("b4", 5, 5, 6);
        m.getProg().add(add1);
        SubInstruction sub1 = new SubInstruction("b5", 10, 5, 6);
        m.getProg().add(sub1);

        m.execute();
        //add instruction has not been skipped and the value of reg 5 is 30
        assertEquals(30, m.getRegisters().getRegister(5));

    }

    @Test
    public void testToString(){

        String expectedToString="b3: bnz jump to instruction b5 next".trim();
        buildUp();

        BnzInstruction bnz = new BnzInstruction("b3", 5,"b5");
        m.getProg().add(bnz);

        m.execute();

        assertEquals(expectedToString, bnz.toString());
    }

}
