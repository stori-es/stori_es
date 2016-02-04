package org.consumersunion.stories.dashboard.client.application.collection.ui;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.common.base.Function;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.inject.assistedinject.Assisted;

public interface AttachedCollectionCellFactory {
    AttachedCollectionCell create(
            @Assisted("detail") Delegate<CollectionSummary> delegateDetail,
            @Assisted("remove") Delegate<CollectionSummary> delegateRemove,
            Function<CollectionSummary, Boolean> canDeleteDelegate);
}
