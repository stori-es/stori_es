package org.consumersunion.stories.dashboard.client.application.ui.addto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class AddToPopup extends Composite {
    interface Binder extends UiBinder<Widget, AddToPopup> {
    }

    private final AddToMenuHandler addToMenuHandler;

    @Inject
    AddToPopup(
            Binder uiBinder,
            @Assisted AddToMenuHandler addToMenuHandler) {
        initWidget(uiBinder.createAndBindUi(this));

        this.addToMenuHandler = addToMenuHandler;
    }

    @UiHandler("addToCollections")
    void onAddToCollectionsClicked(ClickEvent clickEvent) {
        addToMenuHandler.onAddToCollections();
    }

    @UiHandler("addTags")
    void onAddTagsClicked(ClickEvent clickEvent) {
        addToMenuHandler.onAddTags();
    }

    @UiHandler("addNote")
    void onAddNoteClicked(ClickEvent clickEvent) {
        addToMenuHandler.onAddNote();
    }
}
