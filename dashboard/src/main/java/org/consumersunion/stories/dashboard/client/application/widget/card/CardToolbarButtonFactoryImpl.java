package org.consumersunion.stories.dashboard.client.application.widget.card;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;

public class CardToolbarButtonFactoryImpl implements CardToolbarButtonFactory {
    private final CommonI18nLabels labels;

    @Inject
    CardToolbarButtonFactoryImpl(CommonI18nLabels labels) {
        this.labels = labels;
    }

    @Override
    public CardToolbarButton createCopyButton(final ToolbarButtonActionHandler clickHandler) {
        return new CardToolbarButton("icon-copy", labels.clickToCopy(), labels.create(), clickHandler);
    }

    @Override
    public CardToolbarButton createExportButton(final ToolbarButtonActionHandler clickHandler) {
        return new CardToolbarButton("icon-download-alt", labels.clickToExport(), labels.export(), clickHandler);
    }

    @Override
    public CardToolbarButton createDeleteButton(final ToolbarButtonActionHandler clickHandler) {
        return new CardToolbarButton("icon-trash", labels.clickToDelete(), labels.confirm(), clickHandler);
    }

    @Override
    public CardToolbarButton createEditButton(ToolbarButtonActionHandler actionHandler) {
        return new CardToolbarButton("icon-pencil", labels.clickToEdit(), labels.done(), actionHandler);
    }
}
