package sml;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by liliya on 01/02/2015.
 */
public class MachineTest {

    Machine m;

    public Machine buildUp(){

        Machine m = new Machine();
        Instruction testLin = new LinInstruction("l14", 5, 15);
       // Instruction testLin2 = new LinInstruction("l15", 4, 20);
      //  Instruction testAdd = new AddInstruction("a23", 10, 5, 4);
        ArrayList<Instruction> testProg = new ArrayList<>();
        testProg.add(testLin);
       // testProg.add(testLin2);
       // testProg.add(testAdd);
        m.setProg(testProg);

        Labels labels = new Labels();
        labels.addLabel("l14");
       // labels.addLabel("l15");
       // labels.addLabel("a23");
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

    public void tearDown(){

    }

}
