package org.consumersunion.stories.common.client.ui;

public class DefaultTextPagerHandler implements TextPager.Handler {
    @Override
    public void onGoToFirstPage() {
        onPageChanged();
    }

    @Override
    public void onGoToPreviousPage() {
        onPageChanged();
    }

    @Override
    public void onGoToNextPage() {
        onPageChanged();
    }

    @Override
    public void onGoToLastPage() {
        onPageChanged();
    }

    @Override
    public void onPageChanged() {
    }
}
