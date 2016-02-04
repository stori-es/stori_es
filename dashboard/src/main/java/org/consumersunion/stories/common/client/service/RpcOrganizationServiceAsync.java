package org.consumersunion.stories.common.client.service;

import java.util.List;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.OrganizationSummary;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of {@link RpcOrganizationService}.
 */
public interface RpcOrganizationServiceAsync {
    void retrieveOrganization(int id, AsyncCallback<DatumResponse<Organization>> callback);

    void retrieveOrganizationByPermalink(String permalink, AsyncCallback<DatumResponse<Organization>> callback);

    void updateOrganization(Organization organization, List<Contact> contacts, List<Address> addresses,
            List<Integer> themes, List<String> adminUsers, AsyncCallback<DatumResponse<OrganizationSummary>> callback);

    void checkOrganizationAvailability(final String name, AsyncCallback<ActionResponse> callback);

    void createOrganization(Organization organization, AsyncCallback<ActionResponse> callback);

    void createOrganization(Organization organization, List<Contact> contacts, List<Address> addresses,
            List<String> adminUsers, List<Integer> themes, AsyncCallback<ActionResponse> callback);

    void getAdminOrganizations(int start, int length, SortField sortField, boolean ascending,
            AsyncCallback<PagedDataResponse<OrganizationSummary>> callback);
}
