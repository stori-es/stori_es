package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;

public class CollectionListItem extends AbstractListItem<CollectionSummary> {
    @AssistedInject
    CollectionListItem(
            EventBus eventBus,
            @Assisted CollectionSummary collectionSummary,
            @Assisted ListItemHandler<CollectionSummary> handler,
            @Assisted boolean canRemove) {
        this(eventBus, collectionSummary, handler, canRemove, true);
    }

    @AssistedInject
    CollectionListItem(
            EventBus eventBus,
            @Assisted CollectionSummary collectionSummary,
            @Assisted ListItemHandler<CollectionSummary> handler,
            @Assisted("canRemove") boolean canRemove,
            @Assisted("withClickAction") boolean withClickAction) {
        super(eventBus, collectionSummary, handler, canRemove, withClickAction);
    }

    @Override
    protected ContentKind getContentKind(CollectionSummary content) {
        return content.isQuestionnaire() ? ContentKind.QUESTIONNAIRE : ContentKind.COLLECTION;
    }
}
