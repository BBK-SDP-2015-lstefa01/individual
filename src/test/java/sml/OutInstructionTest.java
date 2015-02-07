package sml;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by liliya on 07/02/2015.
 */
public class OutInstructionTest {

    Machine m = new Machine();

    public void buildUp() {
        m.execute();
        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
    }

    @Test
    public void testOutInstructionCreation() {
        OutInstruction out = new OutInstruction("o1", 5);
        assertNotNull(out);
    }

    //TODO
    @Test
    public void testExecute() {


    }

    @Test
    public void testToString() {
        buildUp();
        OutInstruction out = new OutInstruction("o1", 5);
        out.execute(m);

        String expectedString = "o1: out value of register 5 is printed on the console".trim();

        assertEquals(expectedString, out.toString());
    }

}
