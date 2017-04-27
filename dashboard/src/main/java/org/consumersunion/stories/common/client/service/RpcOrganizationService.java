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
import org.consumersunion.stories.server.business_logic.OrganizationService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Organization services.
 */
@RemoteServiceRelativePath("service/organization")
public interface RpcOrganizationService extends RemoteService {
    /**
     * @see OrganizationService#get(int)
     */
    DatumResponse<Organization> retrieveOrganization(int id);

    /**
     * Retrieves an organization by permalink. Requires the user have read
     * access through any standard means.
     */
    DatumResponse<Organization> retrieveOrganizationByPermalink(String permalink);

    /**
     * Updates an Organization. Requires the effective role be acting OBO or
     * have WRITE over the organization.
     */
    DatumResponse<OrganizationSummary> updateOrganization(Organization organization, List<Contact> contacts,
            List<Address> addresses, List<Integer> themes, List<String> adminUsers);

    /**
     * Retrieves set of {@link Organization}s over which the logged in user has
     * Admin privileges. This method ignores any OBO settings and is always
     * resolved with regard to the logged in user directly.
     */
    PagedDataResponse<OrganizationSummary> getAdminOrganizations(int start, int length, SortField sortField,
            boolean ascending);

    /**
     * Requires System Admin privs.
     */
    ActionResponse checkOrganizationAvailability(final String name);

    /**
     * @see OrganizationService#create(Organization, List, List, List, List)
     */
    ActionResponse createOrganization(Organization organization, List<Contact> contacts, List<Address> addresses,
            List<String> adminUsers, List<Integer> themes);
}
