package org.consumersunion.stories.dashboard.client.application.organization;

import org.consumersunion.stories.common.shared.model.Organization;

import com.gwtplatform.mvp.client.UiHandlers;

public interface OrganizationUiHandlers extends UiHandlers {
    void checkOrganizationDisponibility(String organizationName);

    void saveOrganization(Organization editedorg);

    void saveOrganizationPermissions(String permissions);
}
