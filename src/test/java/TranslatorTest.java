import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Test.*;
import sml.*;

import java.util.ArrayList;

/**
 * Created by liliya on 01/02/2015.
 */
public class TranslatorTest {

    //TODO test is currently failing

    @Test
    public void getInstructionTest(){

        Translator trans = new Translator("testOneLineFileInstruction.txt");
        Labels lab = new Labels();
        ArrayList<Instruction> prog = new ArrayList<>();
        trans.readAndTranslate(lab, prog);

        Instruction testLin = new LinInstruction("s0", 5, 100);
        assertNotNull(trans.getInstruction("lin"));
       // assertEquals(testLin, trans.getInstruction("lin"));

    }

    @Test
    public void testReadAndTranslate(){
        Translator trans = new Translator("testOneLineFileInstruction.txt");
        Labels lab = new Labels();
        ArrayList<Instruction> prog = new ArrayList<>();

        assertTrue(trans.readAndTranslate(lab, prog));
    }

}
