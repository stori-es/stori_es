package org.consumersunion.stories.dashboard.client.application.widget;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.dashboard.shared.MenuItem;

import com.gwtplatform.mvp.client.UiHandlers;

public interface HeaderUiHandlers extends UiHandlers {
    void userAccount();

    void logout();

    void specificProfile(ProfileSummary profile);

    void gotoOrganizationDetail();

    void allStories();

    void allCollections();

    void administration(MenuItem menuItem);

    void authorizeMenu(MenuItem item);
}
