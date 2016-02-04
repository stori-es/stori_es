package org.consumersunion.stories.server.business_logic;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.Contact;

public interface OrganizationService {
    /**
     * Retrieves an organization by ID. Requires the user have read access through any standard means.
     */
    Organization get(int id);

    /**
     * Requires System Admin privs.
     */
    Organization create(
            Organization organization,
            List<Contact> contacts,
            List<Address> addresses,
            List<Integer> themeIds,
            List<String> adminUsers);

    void delete(int id);

    Organization update(Organization organization, List<Contact> contacts, List<Address> addresses,
            List<Integer> themes);
}
