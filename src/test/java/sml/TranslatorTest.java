package sml;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by liliya on 01/02/2015.
 */
public class TranslatorTest {

    //TODO test is currently failing

    String fileLoc = "src/dummy.txt";  //dummy file loc
    Translator t = new Translator(fileLoc);

    @Test
    public void testGetInstructionEmptyLine(){
        t.line = "";
        assertNull(t.getInstruction("lin"));
    }

    @Test
    public void testGetInstruction(){
        t.line = "s0 lin 5 100";
        assertEquals("sml.LinInstruction", t.getInstruction("s0").getClass().getName());
    }

    @Test
    public void testGetInstructionObject(){
        assertEquals("sml.LinInstruction", t.getInstructionObject("lin", "l1").getClass().getName());
        assertEquals("a1", t.getInstructionObject("add", "a1").label);
        assertEquals("add", t.getInstructionObject("add", "a1").opcode);
    }


}
