package org.consumersunion.stories.dashboard.client.event;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadCollectionsEvent extends GwtEvent<ReloadCollectionsEvent.ReloadCollectionsHandler> {
    public interface ReloadCollectionsHandler extends EventHandler {
        void onReloadCollections(ReloadCollectionsEvent event);
    }

    public static final Type<ReloadCollectionsHandler> TYPE = new Type<ReloadCollectionsHandler>();

    private final Collection sourceCollection;
    private final List<Integer> relatedCollectionIds;

    private ReloadCollectionsEvent(Collection sourceCollection, List<Integer> relatedCollectionIds) {
        this.sourceCollection = sourceCollection;
        this.relatedCollectionIds = relatedCollectionIds;
    }

    public static void fire(HasHandlers source, Collection sourceCollection) {
        fire(source, sourceCollection, Lists.<Integer>newArrayList());
    }

    public static void fire(HasHandlers source, Collection sourceCollection, List<Integer> relatedCollectionIds) {
        source.fireEvent(new ReloadCollectionsEvent(sourceCollection, relatedCollectionIds));
    }

    public Collection getSourceCollection() {
        return sourceCollection;
    }

    public List<Integer> getRelatedCollectionIds() {
        return relatedCollectionIds;
    }

    @Override
    public Type<ReloadCollectionsHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ReloadCollectionsHandler handler) {
        handler.onReloadCollections(this);
    }
}
