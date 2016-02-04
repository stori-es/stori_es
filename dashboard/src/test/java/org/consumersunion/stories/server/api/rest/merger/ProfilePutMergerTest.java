package org.consumersunion.stories.server.api.rest.merger;

import java.util.ArrayList;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.post.ProfilePut;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.server.api.rest.converters.AbstractConverter;
import org.consumersunion.stories.server.api.rest.converters.ApiContactConverter;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ProfilePutMergerTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(ApiContactConverter.class);
        }
    }

    private static final String A_GIVEN_NAME = "A_GIVEN_NAME";
    private static final String A_SURNAME = "A_SURNAME";

    @Inject
    private ProfilePutMerger merger;
    @Inject
    private ApiContactConverter contactConverter;

    @Test
    public void merge_mergesData() {
        ProfileSummary profileSummary = new ProfileSummary(new Profile(), "", false);
        ArrayList<ApiContact> apiContacts = Lists.newArrayList(new ApiContact());
        ProfilePut dto = ProfilePut.builder()
                .withGivenName(A_GIVEN_NAME)
                .withSurname(A_SURNAME)
                .withContacts(apiContacts)
                .build();
        ArrayList<Contact> contacts = Lists.<Contact>newArrayList();
        AbstractConverter.PendingConverter converter = mock(AbstractConverter.PendingConverter.class);
        given(converter.asList()).willReturn(contacts);
        given(contactConverter.convertAll(same(apiContacts))).willReturn(converter);

        merger.merge(profileSummary, dto);

        Profile profile = profileSummary.getProfile();
        assertThat(profile.getGivenName()).isEqualTo(A_GIVEN_NAME);
        assertThat(profile.getSurname()).isEqualTo(A_SURNAME);
        assertThat(profileSummary.getContacts()).isSameAs(contacts);
    }

    @Test
    public void merge_noDataProvided_doesNotUpdateData() {
        ProfileSummary profileSummary = new ProfileSummary(spy(new Profile()), "", false);
        profileSummary = spy(profileSummary);
        ProfilePut dto = ProfilePut.builder().build();

        merger.merge(profileSummary, dto);

        Profile profile = profileSummary.getProfile();
        verify(profile, never()).setGivenName(anyString());
        verify(profile, never()).setSurname(anyString());
        verify(profileSummary, never()).setContacts(anyList());
    }
}
