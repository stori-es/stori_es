package org.consumersunion.stories.dashboard.client.application.account;

import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.User;

import com.gwtplatform.mvp.client.UiHandlers;

public interface AccountUiHandlers extends UiHandlers {
    void changeAccountPassword(String currentPassword, String newPassword);

    void saveAccountSettings(User editedUser);

    void saveProfile(Profile profile);

    void onSortChanged(StorySortField item);

    void search(String searchText);

    void switchToList();

    void switchToMap();
}
