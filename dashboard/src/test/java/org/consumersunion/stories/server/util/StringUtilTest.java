package org.consumersunion.stories.server.util;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
    public void testGenerateSlug() {
        // first straightforward
        assertEquals("Unexpected simple slug.", "foo-bar", StringUtil.generateSlug("Foo Bar"));
        assertEquals("Unexpected simple slug.", "foo-bar", StringUtil.generateSlug("Foo  Bar")); // extra
        // space
        assertEquals("Unexpected simple slug.", "foo-bar", StringUtil.generateSlug("Foo-Bar"));
        // remove special characters
        assertEquals("Unexpected simple slug.", "foos-bar", StringUtil.generateSlug("Foo's Bar"));
        assertEquals("Unexpected simple slug.", "foo-bar", StringUtil.generateSlug("Foo & Bar"));
    }
}
