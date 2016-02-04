package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.dashboard.client.application.ui.BuilderTab;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ResponseBuilderUiHandlers extends UiHandlers {
    void onTabChanged(BuilderTab tab);
}
