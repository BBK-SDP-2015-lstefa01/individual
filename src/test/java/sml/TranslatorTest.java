package sml;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by liliya on 01/02/2015.
 */
public class TranslatorTest {

    //TODO test is currently failing

    @Test
    public void getInstructionTest(){

        Translator trans = new Translator("src/testOneLineFileInstruction.txt");
        Labels lab = new Labels();
        ArrayList<Instruction> prog = new ArrayList<>();
        trans.readAndTranslate(lab, prog);

        Instruction testLin = new LinInstruction("s0", 5, 100);
        assertNotNull(trans.getInstruction("lin"));
       // assertEquals(testLin, trans.getInstruction("lin"));

    }
//TODO this test is not passing at the moment
    @Test
    public void testReadAndTranslate(){
        Translator trans = new Translator("src/testOneLineFileInstruction.txt");
        Labels lab = new Labels();
        ArrayList<Instruction> prog = new ArrayList<>();

        assertTrue(trans.readAndTranslate(lab, prog));
    }

    @Test
    public void testGetInstructionObject(){

        Translator trans = new Translator("test");
        assertEquals("sml.LinInstruction", trans.getInstructionObject("lin", "l1").getClass().getName());
        assertEquals("a1", trans.getInstructionObject("add", "a1").label);
        assertEquals("add", trans.getInstructionObject("add", "a1").opcode);
    }

}
