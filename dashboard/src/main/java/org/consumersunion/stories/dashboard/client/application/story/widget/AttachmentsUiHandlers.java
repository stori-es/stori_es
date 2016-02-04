package org.consumersunion.stories.dashboard.client.application.story.widget;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.gwtplatform.mvp.client.UiHandlers;

public interface AttachmentsUiHandlers extends UiHandlers {
    void addAttachement();

    void deleteAttachment(Document documentText);
}
