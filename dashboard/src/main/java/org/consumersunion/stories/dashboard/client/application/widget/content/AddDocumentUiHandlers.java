package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.shared.model.Locale;

import com.gwtplatform.mvp.client.UiHandlers;

public interface AddDocumentUiHandlers extends UiHandlers {
    void create(String title, String summary, Locale locale);

    void cancel();
}
