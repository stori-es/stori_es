package org.consumersunion.stories.dashboard.client.application.story.ui;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.inject.assistedinject.Assisted;

public interface AttachmentCellFactory {
    AttachmentCell create(@Assisted("remove") Delegate<Document> delegateRemove);
}
