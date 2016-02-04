package org.consumersunion.stories.dashboard.client.application.profile;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.StorySortField;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ProfileUiHandlers extends UiHandlers {
    void savePerson(Profile profile);

    void onSortChanged(StorySortField item);

    void search(String searchText);
}
