package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.ProfilesApiResponse;
import org.consumersunion.stories.common.shared.dto.post.ProfilePost;
import org.consumersunion.stories.common.shared.dto.post.ProfilePut;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.server.api.rest.converters.ProfileConverter;
import org.consumersunion.stories.server.api.rest.converters.ProfilePostConverter;
import org.consumersunion.stories.server.api.rest.merger.ProfilePutMerger;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.ProfileService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ProfilesResourceTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(ProfilePostConverter.class);
            forceMock(ProfilePutMerger.class);
        }
    }

    private static final int PROFILE_ID = 300;

    @Inject
    private ProfilesResource profilesResource;
    @Inject
    private ProfileService profileService;
    @Inject
    private ProfileConverter profileConverter;
    @Inject
    private ProfilePostConverter profilePostConverter;
    @Inject
    private ContactService contactService;
    @Inject
    private ProfilePutMerger putMerger;

    @Test
    public void postProfile() throws Exception {
        ProfilePost profilePost = new ProfilePost();
        ProfileSummary profileSummary = new ProfileSummary(new Profile(), "org", false);
        ProfileSummary savedProfileSummary = new ProfileSummary(new Profile(PROFILE_ID, 1, 1), "org", false);
        given(profilePostConverter.convert(profilePost)).willReturn(profileSummary);
        given(profileService.createProfile(same(profileSummary.getProfile()))).willReturn(savedProfileSummary);

        Response response = profilesResource.postProfile(profilePost);

        ProfilesApiResponse apiResponse = (ProfilesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(PROFILE_ID);
    }

    @Test
    public void putProfile() throws Exception {
        ProfilePut profilePut = new ProfilePut();
        ProfileSummary profileSummary = new ProfileSummary(new Profile(), "org", false);
        given(profileService.get(PROFILE_ID)).willReturn(profileSummary);
        Profile updatedProfile = new Profile(PROFILE_ID, 1, 1);
        given(profileService.updateProfile(same(profileSummary.getProfile()))).willReturn(updatedProfile);

        Response response = profilesResource.putProfile(PROFILE_ID, profilePut);

        InOrder inOrder = Mockito.inOrder(putMerger, profileService);
        inOrder.verify(putMerger).merge(same(profileSummary), same(profilePut));
        inOrder.verify(profileService).updateProfile(same(profileSummary.getProfile()));
        ProfilesApiResponse apiResponse = (ProfilesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(PROFILE_ID);
    }

    @Test
    public void getProfile() throws Exception {
        Profile profile = new Profile(PROFILE_ID, 1, 1);
        given(profileService.getProfile(PROFILE_ID)).willReturn(profile);

        Response response = profilesResource.getProfile(PROFILE_ID);

        ProfilesApiResponse apiResponse = (ProfilesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(PROFILE_ID);
    }

    @Test
    public void deleteProfile() throws Exception {
        profilesResource.deleteProfile(PROFILE_ID);

        verify(profileService).delete(PROFILE_ID);
    }
}
