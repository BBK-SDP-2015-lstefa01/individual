package sml;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by liliya on 01/02/2015.
 */
public class MachineTest {

    Machine m = new Machine();

    public Machine buildUp(){

        Instruction testLin = new LinInstruction("l14", 5, 15);
        ArrayList<Instruction> testProg = new ArrayList<>();
        testProg.add(testLin);
        m.setProg(testProg);

        Labels labels = new Labels();
        labels.addLabel("l14");
        return m;

    }

    @Test
    public void createNewMachineTest(){
        assertNotNull(new Machine());
    }

    @Test
    public void toStringTest(){

        Machine m = buildUp();
        String s="l14: lin register 5 value is 15".trim();
        assertEquals(s, m.toString().trim());

    }

    @Test
    public void testExecute(){
        buildUp();
        m.execute();

        assertEquals(15, m.getRegisters().getRegister(5));
    }

}
