package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import org.consumersunion.stories.dashboard.client.application.ui.BuilderTab;

import com.gwtplatform.mvp.client.UiHandlers;

public interface BuilderUiHandlers extends UiHandlers {
    void onTabChanged(BuilderTab tab);
}
