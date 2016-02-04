package org.consumersunion.stories.server.api.rest.converters;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.OrganizationResourceLinks;
import org.consumersunion.stories.common.shared.dto.OrganizationResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.ProfileService;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.PROFILES;
import static org.consumersunion.stories.common.shared.api.EndPoints.THEMES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;

@Component
public class OrganizationConverter extends Converter<Organization, OrganizationResponse> {
    private final ResourceLinksHelper resourceLinksHelper;
    private final ContactService contactService;
    private final ProfileService profileService;
    private final ContactConverter contactConverter;
    private final AddressConverter addressConverter;

    @Inject
    OrganizationConverter(
            ResourceLinksHelper resourceLinksHelper,
            ContactService contactService,
            ProfileService profileService,
            ContactConverter contactConverter,
            AddressConverter addressConverter) {
        this.resourceLinksHelper = resourceLinksHelper;
        this.contactService = contactService;
        this.profileService = profileService;
        this.contactConverter = contactConverter;
        this.addressConverter = addressConverter;
    }

    @Override
    protected OrganizationResponse doForward(Organization organization) {
        return OrganizationResponse.builder()
                .withId(organization.getId())
                .withCreatedOn(organization.getCreated())
                .withUpdatedOn(organization.getUpdated())
                .withName(organization.getName())
                .withShortName(organization.getShortName())
                .withContacts(createContacts(organization))
                .withLinks(createLinks(organization))
                .build();
    }

    @Override
    protected Organization doBackward(OrganizationResponse organizationResponse) {
        throw new UnsupportedOperationException("OrganizationPost should be used to build Organization from a request");
    }

    private List<ApiContact> createContacts(Organization organization) {
        List<Contact> contacts = contactService.getAllContacts(organization);
        List<Address> addresses = contactService.getAddresses(organization);

        List<ApiContact> results = Lists.newArrayList();
        results.addAll(contactConverter.convertAll(contacts).asList());
        results.addAll(addressConverter.convertAll(addresses).asList());

        return results;
    }

    private OrganizationResourceLinks createLinks(Organization organization) {
        OrganizationResourceLinks links = new OrganizationResourceLinks();

        // TODO: Fill in missing links
        links.setDefaultTheme(resourceLinksHelper.replaceId(endsWithId(THEMES), organization.getDefaultTheme()));
        links.setOwner(resourceLinksHelper.replaceId(endsWithId(ORGANIZATIONS), organization.getOwner()));
        links.setProfiles(resourceLinksHelper.replaceIntIds(endsWithId(PROFILES),
                profileService.getOrganizationProfiles(organization.getId())));

        return links;
    }
}
