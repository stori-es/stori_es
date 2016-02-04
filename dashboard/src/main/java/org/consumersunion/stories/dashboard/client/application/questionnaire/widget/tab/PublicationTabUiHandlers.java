package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import com.gwtplatform.mvp.client.UiHandlers;

public interface PublicationTabUiHandlers extends UiHandlers {
    void updatePermalink(String link);

    void checkIfLinkExists(String collectionLink);

    void toggleState();
}
