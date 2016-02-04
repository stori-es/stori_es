package org.consumersunion.stories.dashboard.client;

import org.consumersunion.stories.common.client.util.CookiesHelper;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Matchers;

import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public abstract class TestBase {
    @Before
    public void setUp(CookiesHelper cookiesHelper) {
        when(cookiesHelper.getCookie(Matchers.anyString())).thenReturn("SESSION");
    }
}
