package org.consumersunion.stories.server.api.rest.merger;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.post.ProfilePut;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.api.rest.converters.ApiContactConverter;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class ProfilePutMerger extends AbstractMerger<ProfileSummary, ProfilePut> {
    private final ApiContactConverter contactConverter;

    @Inject
    ProfilePutMerger(ApiContactConverter contactConverter) {
        this.contactConverter = contactConverter;
    }

    @Override
    public void merge(ProfileSummary entity, ProfilePut dto) {
        Profile profile = entity.getProfile();

        if (!Strings.isNullOrEmpty(dto.getGivenName())) {
            profile.setGivenName(dto.getGivenName());
        }
        if (!Strings.isNullOrEmpty(dto.getSurname())) {
            profile.setSurname(dto.getSurname());
        }
        if (dto.getContacts() != null && !dto.getContacts().isEmpty()) {
            entity.setContacts(contactConverter.convertAll(dto.getContacts()).asList());
        }
    }
}
