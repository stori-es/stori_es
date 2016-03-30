package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import org.consumersunion.stories.common.client.ui.CollectionSuggestionOracle;
import org.consumersunion.stories.common.client.widget.CollectionListItemFactory;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class CollectionsTokenViewImpl
        extends AbstractCollectionsTokenView<CollectionSummary, Collection, CollectionData> {
    interface Binder extends UiBinder<Widget, CollectionsTokenViewImpl> {
    }

    private final CollectionListItemFactory collectionListItemFactory;

    @Inject
    CollectionsTokenViewImpl(
            final CollectionSuggestionOracle collectionSuggestionOracle,
            CollectionListItemFactory collectionListItemFactory) {
        super(collectionSuggestionOracle);

        this.collectionListItemFactory = collectionListItemFactory;

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                collectionSuggestionOracle.setFilter(getUiHandlers());
            }
        });

        $(questionnaireIcon).hide();
    }

    @Override
    public int compare(CollectionSummary o1, CollectionSummary o2) {
        if (o1.isQuestionnaire() && !o2.isQuestionnaire()) {
            return -1;
        } else if (o2.isQuestionnaire() && !o1.isQuestionnaire()) {
            return 1;
        }

        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
    }

    @Override
    protected IsWidget createItem(CollectionSummary collectionSummary, boolean canRemove) {
        return collectionListItemFactory.create(collectionSummary, this, canRemove);
    }
}
