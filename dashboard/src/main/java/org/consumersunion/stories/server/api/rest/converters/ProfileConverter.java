package org.consumersunion.stories.server.api.rest.converters;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ProfileResourceLinks;
import org.consumersunion.stories.common.shared.dto.ProfileResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.STORIES;
import static org.consumersunion.stories.common.shared.api.EndPoints.USERS;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;

@Component
public class ProfileConverter extends AbstractConverter<Profile, ProfileResponse> {
    private final ContactService contactService;
    private final StoryService storyService;
    private final ContactConverter contactConverter;
    private final AddressConverter addressConverter;
    private final ResourceLinksHelper resourceLinksHelper;

    @Inject
    ProfileConverter(
            ContactService contactService,
            StoryService storyService,
            ContactConverter contactConverter,
            AddressConverter addressConverter,
            ResourceLinksHelper resourceLinksHelper) {
        this.contactService = contactService;
        this.storyService = storyService;
        this.contactConverter = contactConverter;
        this.addressConverter = addressConverter;
        this.resourceLinksHelper = resourceLinksHelper;
    }

    @Override
    public ProfileResponse convert(Profile profile) {
        return ProfileResponse.builder()
                .withId(profile.getId())
                .withCreatedOn(profile.getCreated())
                .withUpdatedOn(profile.getUpdated())
                .withGivenName(profile.getGivenName())
                .withSurname(profile.getSurname())
                .withContacts(createContacts(profile))
                .withLinks(createLinks(profile))
                .build();
    }

    private List<ApiContact> createContacts(Profile profile) {
        List<Contact> contacts = contactService.getAllContacts(profile);
        List<Address> addresses = contactService.getAddresses(profile);

        List<ApiContact> results = Lists.newArrayList();
        results.addAll(contactConverter.convertAll(contacts).asList());
        results.addAll(addressConverter.convertAll(addresses).asList());

        return results;
    }

    private ProfileResourceLinks createLinks(Profile profile) {
        ProfileResourceLinks links = new ProfileResourceLinks();

        // TODO: Fill in missing links
        links.setOwner(resourceLinksHelper.replaceId(endsWithId(USERS), profile.getOwner()));
        links.setUser(resourceLinksHelper.replaceId(endsWithId(USERS), profile.getUserId()));
        links.setOrganization(resourceLinksHelper.replaceId(endsWithId(ORGANIZATIONS), profile.getOrganizationId()));
        links.setStories(resourceLinksHelper.replaceIntIds(endsWithId(STORIES),
                storyService.getStoriesIds(profile.getId())));

        return links;
    }
}
