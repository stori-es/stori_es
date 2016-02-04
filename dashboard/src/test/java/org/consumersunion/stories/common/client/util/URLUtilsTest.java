package org.consumersunion.stories.common.client.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class URLUtilsTest {
    @Test
    public void verifyNoProtocolOverride() {
        String[] urls = {"http://google.com", "https://google.com", "ftp://googlel.com"};

        for (String url : urls) {
            assertEquals("Unexpected change to full URL.", url, URLUtils.appendDefaultProtocol(url));
        }
    }

    @Test
    public void verifyDefaultProtocolAdded() {
        String[] urls = {"google.com", "www.google.com"};

        for (String url : urls) {
            String newUrl = URLUtils.appendDefaultProtocol(url);
            assertTrue("Did not find default URL.", newUrl.startsWith("http://"));
            assertEquals("Did not find expected default value.", "http://" + url, newUrl);
        }
    }
}
