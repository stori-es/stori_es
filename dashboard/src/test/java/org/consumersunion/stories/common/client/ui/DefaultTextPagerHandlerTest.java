package org.consumersunion.stories.common.client.ui;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultTextPagerHandlerTest {
    private DefaultTextPagerHandler handler;

    @Before
    public void setUp() {
        handler = spy(new DefaultTextPagerHandler());
    }

    @Test
    public void onGoToFirstPage_triggersPageChanged() {
        // when
        handler.onGoToFirstPage();

        // then
        verify(handler).onPageChanged();
    }

    @Test
    public void onGoToPreviousPage_triggersPageChanged() {
        // when
        handler.onGoToPreviousPage();

        // then
        verify(handler).onPageChanged();
    }

    @Test
    public void onGoToNextPage_triggersPageChanged() {
        // when
        handler.onGoToNextPage();

        // then
        verify(handler).onPageChanged();
    }

    @Test
    public void onGoToLastPage_triggersPageChanged() {
        // when
        handler.onGoToLastPage();

        // then
        verify(handler).onPageChanged();
    }
}
