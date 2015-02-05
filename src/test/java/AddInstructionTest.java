import static org.junit.Assert.*;
import org.junit.*;
import sml.AddInstruction;
import sml.Instruction;
import sml.LinInstruction;
import sml.Machine;

/**
 * Created by liliya on 04/02/2015.
 */
public class AddInstructionTest {

    @Test
    public void testAddInstruction(){
        AddInstruction add = new AddInstruction("add", 5, 6, 5);
        assertNotNull(add);
        assertEquals(5, add.getResult());
        assertEquals(6, add.getOp1());
        assertEquals(5, add.getOp2());
    }

    @Test
    public void testExecute(){
        Machine m = new Machine();
        m.execute();

        LinInstruction lin1 = new LinInstruction("l1", 5, 10);
        lin1.execute(m);
        LinInstruction lin2 = new LinInstruction("l2", 6, 10);
        lin2.execute(m);


        AddInstruction add = new AddInstruction("a1", 10, 6, 5);
        add.execute(m);

        assertEquals(10, m.getRegisters().getRegister(6));
        assertEquals(10, m.getRegisters().getRegister(5));
        assertEquals(20, m.getRegisters().getRegister(10));

    }
}
