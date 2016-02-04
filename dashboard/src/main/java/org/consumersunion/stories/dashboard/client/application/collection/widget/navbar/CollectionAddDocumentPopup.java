package org.consumersunion.stories.dashboard.client.application.collection.widget.navbar;

import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.dashboard.client.application.widget.AddDocumentPopup;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class CollectionAddDocumentPopup extends AddDocumentPopup {
    @Inject
    CollectionAddDocumentPopup(
            Binder uiBinder,
            EventBus eventBus,
            Resources resources) {
        super(uiBinder, eventBus, resources);

        addContent.removeFromParent();
    }

    @Override
    protected SystemEntityRelation getContentEntityRelation() {
        return SystemEntityRelation.CONTENT;
    }
}
