package hudson.plugins.phing.console;

import hudson.MarkupText;
import hudson.console.ConsoleAnnotator;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PhingTargetNote
 * 
 * @author Seiji Sogabe
 */
public class PhingTargetNoteTest {

    private PhingTargetNote target;

    @Before
    public void setUp() {
        target = new PhingTargetNote();
    }

    /**
     * Test of annotate method, of class PhingTargetNote.
     */
    @Test
    public void testAnnotate() throws IOException {
        Object context = new Object();
        MarkupText text = new MarkupText("phing > build:");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("phing > <span class='phing-target'>build</span>:", text.toString(true));
    }

    /**
     * Test of annotate method, of class PhingTargetNote.
     */
    @Test
    public void testAnnotateNoTarget() throws IOException {
        Object context = new Object();
        MarkupText text = new MarkupText("phing ");

        ConsoleAnnotator<?> ca = target.annotate(context, text, 0);

        Assert.assertNull(ca);
        Assert.assertEquals("phing ", text.toString(true));
    }

}