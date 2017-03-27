package org.consumersunion.stories.server.api.rest.converters;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.post.ProfilePost;
import org.consumersunion.stories.common.shared.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfilePostConverter extends AbstractConverter<ProfilePost, ProfileSummary> {
	private final ApiContactConverter apiContactConverter;
	
	@Inject
	ProfilePostConverter(ApiContactConverter apiContactConverter) {
		this.apiContactConverter = apiContactConverter;
	}
	
    @Override
    public ProfileSummary convert(ProfilePost profilePost) {
        final Profile profile = new Profile();
    	profile.setGivenName(profilePost.getGivenName());
    	profile.setSurname(profilePost.getSurname());
    	profile.setUserId(profilePost.getUserId());
    	profile.setOrganizationId(profilePost.getOrganizationId());

        final ProfileSummary profileSummary = new ProfileSummary(profile, null, false);
    	profileSummary.setContacts(apiContactConverter.convertAll(profilePost.getContacts()).asList());
    	
    	return profileSummary;
    }
}
