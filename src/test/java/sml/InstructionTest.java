package sml;

import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionTest {

    @Test
    public void testNewInstruction(){

        Instruction ins = new AddInstruction("s1", "add");
        assertNotNull(ins);

    }

}
