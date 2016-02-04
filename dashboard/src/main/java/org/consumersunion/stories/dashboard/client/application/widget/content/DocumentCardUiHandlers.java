package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.shared.model.Locale;

import com.gwtplatform.mvp.client.UiHandlers;

public interface DocumentCardUiHandlers extends UiHandlers {
    void closeCard();

    void openCard(boolean editMode, boolean addContent);

    void save(boolean done);

    void setEditMode(boolean editMode);

    void cancelEdit();

    void edit();

    boolean hasLocale();

    void updateLocale(Locale locale);
}
