package org.consumersunion.stories.service.client;

import org.consumersunion.stories.common.client.service.RpcOrganizationService;
import org.consumersunion.stories.common.client.service.RpcOrganizationServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;

import com.google.gwt.core.client.GWT;

public class OrganizationServiceGwtTest extends GWTTestCaseExposed {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    public void testOrganizationService() {
        updateOrganization();
    }

    private void updateOrganization() {
        final Organization organization = new Organization();
        organization.setName("my name");
        organization.setDefaultTheme(236166);
        organization.setPermalink("my permalink");
        organization.setId(60);

        final RpcOrganizationServiceAsync organizationService = GWT.create(RpcOrganizationService.class);

        userService.login("root", "password", new AsyncFail<DatumResponse<User>>(OrganizationServiceGwtTest.this,
                "updateOrganization:createOrganization") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                organizationService.updateOrganization(organization, null, null, null, null,
                        new AsyncFail<DatumResponse<OrganizationSummary>>(OrganizationServiceGwtTest.this,
                                "updateOrganization:updateOrganization") {
                            @Override
                            public void onSuccess(DatumResponse<OrganizationSummary> result) {
                                assertTrue("Unexpected success.", result.isError());
                                retrieveOrganization();
                            }
                        });
            }
        });
    }

    private void retrieveOrganization() {
        final Organization organization = new Organization();
        organization.setId(60);

        userService.login("root", "password", new AsyncFail<DatumResponse<User>>(OrganizationServiceGwtTest.this,
                "retrieveOrganization:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                final RpcOrganizationServiceAsync organizationService = GWT.create(RpcOrganizationService.class);
                organizationService.retrieveOrganization(60, new AsyncFail<DatumResponse<Organization>>(
                        OrganizationServiceGwtTest.this, "retrieveOrganization:retrieveOrganization") {
                    @Override
                    public void onSuccess(DatumResponse<Organization> result) {
                        assertFalse("Unexpected failrue.", result.isError());
                        finishTest();
                    }
                });
            }
        });
    }
}
