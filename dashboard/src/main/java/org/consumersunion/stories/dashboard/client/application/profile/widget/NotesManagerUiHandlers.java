package org.consumersunion.stories.dashboard.client.application.profile.widget;

import com.gwtplatform.mvp.client.UiHandlers;

public interface NotesManagerUiHandlers extends UiHandlers {
    void saveNote(String noteContent);
}
