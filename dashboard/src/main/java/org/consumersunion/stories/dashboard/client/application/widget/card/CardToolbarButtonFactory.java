package org.consumersunion.stories.dashboard.client.application.widget.card;

public interface CardToolbarButtonFactory {
    CardToolbarButton createCopyButton(ToolbarButtonActionHandler actionHandler);

    CardToolbarButton createExportButton(ToolbarButtonActionHandler actionHandler);

    CardToolbarButton createDeleteButton(ToolbarButtonActionHandler actionHandler);

    CardToolbarButton createEditButton(ToolbarButtonActionHandler actionHandler);
}
