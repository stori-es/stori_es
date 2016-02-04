package org.consumersunion.stories.dashboard.client.application.ui.addto;

public interface AddToWidgetFactory {
    AddToMenuWidget createAddToWidget(AddToMenuHandler addToMenuHandler);

    AddToPopup createAddToPopup(AddToMenuHandler addToMenuHandler);
}
