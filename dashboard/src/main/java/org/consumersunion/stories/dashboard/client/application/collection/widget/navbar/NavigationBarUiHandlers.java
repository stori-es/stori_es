package org.consumersunion.stories.dashboard.client.application.collection.widget.navbar;

import org.consumersunion.stories.dashboard.client.util.NavigationBarElement;

import com.gwtplatform.mvp.client.UiHandlers;

public interface NavigationBarUiHandlers extends UiHandlers {
    void goTo(NavigationBarElement element);
}
