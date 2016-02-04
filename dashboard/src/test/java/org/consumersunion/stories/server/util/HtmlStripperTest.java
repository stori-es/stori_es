package org.consumersunion.stories.server.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class HtmlStripperTest {
    private static final String BODY = "<body style=\"background-color: #fff; font-size: small; font-family: 'Droid " +
            "Sans', sans-serif\"><font size=\"2\">Testing the watcher email for html markup. &nbsp;<b>Bold,</b> " +
            "<i>italics. </i>&nbsp;<u>Underline</u>. &nbsp;<strike>Strikethrough.</strike></font><div " +
            "style=\"font-size: small;\"><ol><li>This text is behind a bullet.</li></ol></div><div style=\"font-size:" +
            " small;\"><ul><li>Also this one.</li></ul></div><div style=\"font-size: small;\">Here is a <a " +
            "href=\"http://google.com\">link.</a></div><div style=\"font-size: small;\"><br></div><div><font " +
            "size=\"7\">This text is large.</font></div><div><font size=\"1\">This text is small.</font></div></body>";

    @Test
    public void testStriptHtml() throws Exception {
        HtmlStripper htmlStripper = new HtmlStripper();

        String cleanedHtml = htmlStripper.striptHtml(BODY);

        assertFalse(cleanedHtml.contains("&nbsp;"));
    }
}
