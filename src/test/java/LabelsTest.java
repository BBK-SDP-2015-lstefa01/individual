import org.junit.Test;
import sml.Labels;

import static org.junit.Assert.*;

/**
 * Created by liliya on 01/02/2015.
 */
public class LabelsTest {

    @Test
    public void testCreateLabels(){

        assertNotNull(new Labels());
    }

    @Test
    public void testAddLabel(){

        Labels l = new Labels();
        l.addLabel("s1");
        l.addLabel("s2");
        assertEquals(2, l.addLabel("s3"));
    }

    @Test
    public void testAddLabelEmpty(){
        Labels l = new Labels();
        assertEquals(0, l.addLabel("a1"));
    }

    @Test
    public void testIndexOf(){
        Labels l = new Labels();
        l.addLabel("s1");
        l.addLabel("s2");
        assertEquals(1, l.indexOf("s2"));
        assertEquals(-1, l.indexOf("s4"));
    }

    @Test
    public void testReset(){
        Labels l = new Labels();
        l.addLabel("s1");
        l.addLabel("s2");
        l.reset();
        l.addLabel("s3");
        //if the list is empty a newly added element will have index 0
        assertEquals(0, l.indexOf("s3"));
    }

    @Test
    public void toStringTest(){
        Labels l = new Labels();
        l.addLabel("s1");
        l.addLabel("s2");
        String s = "(s1, s2)".trim();
        assertEquals(s, l.toString().trim());
    }
}
