package sml;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Created by liliya on 01/02/2015.
 */
public class TranslatorTest {


    String fileLoc = "src/dummy.txt";  //dummy file loc
    Translator t = new Translator(fileLoc);

    //Can retain test as line is not public
//    @Test
//    public void testGetInstructionEmptyLine(){
//        t.line = "";
//        assertNull(t.getInstruction("lin"));
//    }


    @Test
    public void testGetInstructionObject(){
        assertEquals("sml.LinInstruction", t.getInstructionObject("lin", "l1").getClass().getName());
        assertEquals("a1", t.getInstructionObject("add", "a1").label);
        assertEquals("add", t.getInstructionObject("add", "a1").opcode);
    }


}
