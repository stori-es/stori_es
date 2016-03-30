package org.consumersunion.stories.server.api.rest.converters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.dto.UserResourceLinks;
import org.consumersunion.stories.common.shared.dto.UserResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.api.EndPoints.PROFILES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;

@Component
public class UserConverter extends Converter<User, UserResponse> {
    private final ResourceLinksHelper resourceLinksHelper;
    private final ContactService contactService;
    private final ContactConverter contactConverter;
    private final AddressConverter addressConverter;

    @Inject
    UserConverter(
            ResourceLinksHelper resourceLinksHelper,
            ContactService contactService,
            ContactConverter contactConverter,
            AddressConverter addressConverter) {
        this.resourceLinksHelper = resourceLinksHelper;
        this.contactService = contactService;
        this.contactConverter = contactConverter;
        this.addressConverter = addressConverter;
    }

    @Override
    protected User doBackward(UserResponse userData) {
        User user = new User();
        user.setId(userData.getId());
        user.setHandle(userData.getHandle());
        user.setActive(!userData.isArchived());

        return user;
    }

    @Override
    protected UserResponse doForward(User user) {
        return UserResponse.builder()
                .withId(user.getId())
                .withCreatedOn(user.getCreated())
                .withUpdatedOn(user.getUpdated())
                .withHandle(user.getHandle())
                .withContacts(createContacts(user))
                .withLinks(createLinks(user))
                .isArchived(!user.isActive())
                .build();
    }

    private List<ApiContact> createContacts(User user) {
        List<Contact> contacts = contactService.getAllContacts(user);
        List<Address> addresses = contactService.getAddresses(user);

        List<ApiContact> results = Lists.newArrayList();
        results.addAll(contactConverter.convertAll(contacts).asList());
        results.addAll(addressConverter.convertAll(addresses).asList());

        return results;
    }

    private UserResourceLinks createLinks(User user) {
        UserResourceLinks links = new UserResourceLinks();

        // TODO: Fill in missing links
        List<ResourceLink> profiles = new ArrayList<ResourceLink>();
        for (ProfileSummary profile : user.getProfiles()) {
            profiles.add(resourceLinksHelper.replaceId(endsWithId(PROFILES), profile.getProfile().getId()));
        }
        links.setProfiles(profiles);
        links.setDefaultProfile(resourceLinksHelper.replaceId(endsWithId(PROFILES), user.getDefaultProfile()));

        return links;
    }
}
