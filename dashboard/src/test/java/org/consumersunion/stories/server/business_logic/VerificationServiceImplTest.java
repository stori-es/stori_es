package org.consumersunion.stories.server.business_logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.email.ThankYouEmailGenerator;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.VerificationPersister;
import org.consumersunion.stories.server.util.UniqueIdGenerator;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;

import com.google.common.eventbus.EventBus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class VerificationServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(EventBus.class);
            forceMock(VerificationPersister.class);
            forceMock(ContactPersister.class);
            forceMock(OrganizationPersister.class);
            forceMock(ThankYouEmailGenerator.class);
        }
    }

    private static final int PROFILE_ID = 2;
    private static final int ORG_ID = 3;

    @Inject
    private VerificationServiceImpl verificationService;
    @Inject
    private EmailService emailService;
    @Inject
    private VerificationPersister verificationPersister;
    @Inject
    private ContactPersister contactPersister;
    @Inject
    private OrganizationPersister organizationPersister;
    @Inject
    private ThankYouEmailGenerator thankYouEmailGenerator;
    @Inject
    private UniqueIdGenerator uniqueIdGenerator;
    @Inject
    private EventBus eventBus;

    @Test
    public void newInstance_bindsEventBus() {
        verify(eventBus).register(same(verificationService));
    }

    @Test
    public void create() throws Exception {
        int profileId = 9;
        String email = "email@email.com";
        String nonce = "nonce";
        given(uniqueIdGenerator.get()).willReturn(nonce);
        Verification expected = new Verification(profileId, email, nonce);
        given(verificationPersister.create(argThat(new VerificationArgumentMatcher(email, profileId, nonce))))
                .willReturn(expected);
        Contact contact = new Contact(profileId, Contact.MediumType.EMAIL.name(), "Home", email);

        Verification result = verificationService.create(contact);

        assertThat(result).isSameAs(expected);
    }

    @Test
    public void get() throws Exception {
        String code = "code";
        Verification expected = mock(Verification.class);
        given(verificationPersister.get(code)).willReturn(expected);

        Verification result = verificationService.get(code);

        assertThat(result).isSameAs(expected);
    }

    @Test
    public void delete() throws Exception {
        String code = "code";

        verificationService.delete(code);

        verify(verificationPersister).delete(same(code));
    }

    @Test
    public void onStoryAdded() throws Exception {
        Profile profile = new Profile(PROFILE_ID, ORG_ID, 1);
        List<Story> stories = new ArrayList<Story>();
        String email = "email@email.com";
        SystemEntity systemEntity = mock(SystemEntity.class);
        given(systemEntity.getOwner()).willReturn(ORG_ID);
        StoriesAddedEvent event = new StoriesAddedEvent(systemEntity, stories, profile, email);
        Contact contact = new Contact();
        Organization organization = new Organization();
        given(contactPersister.retrieveEmailContact(PROFILE_ID, email)).willReturn(contact);
        given(organizationPersister.get(ORG_ID)).willReturn(organization);
        EmailData emailData = mock(EmailData.class);
        given(thankYouEmailGenerator.generate(same(event), same(contact), same(organization), any(Verification.class)))
                .willReturn(emailData);

        verificationService.onStoryAdded(event);

        verify(emailService).sendEmail(same(emailData));
    }

    private static class VerificationArgumentMatcher extends ArgumentMatcher<Verification> {
        private final String email;
        private final int profileId;
        private final String nonce;

        VerificationArgumentMatcher(String email, int profileId, String nonce) {
            this.email = email;
            this.profileId = profileId;
            this.nonce = nonce;
        }

        @Override
        public boolean matches(Object argument) {
            Verification other = (Verification) argument;
            return other.getEmail().equals(email) && other.getEntityId() == profileId && other.getNonce().equals(nonce);
        }
    }
}
