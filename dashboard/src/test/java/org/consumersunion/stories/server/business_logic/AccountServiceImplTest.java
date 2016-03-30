package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.validator.routines.EmailValidator;
import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.ResetPassword;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.email.ConfirmEmailAddressEmailGenerator;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.email.PasswordChangedEmailGenerator;
import org.consumersunion.stories.server.email.ResetPasswordEmailGenerator;
import org.consumersunion.stories.server.exception.BadRequestException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.ResetPasswordPersister;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.model.entity.ContactStatus.UNVERIFIED;
import static org.consumersunion.stories.common.shared.model.entity.ContactStatus.VERIFIED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class AccountServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(EmailValidator.class);
            forceMock(CredentialedUserPersister.class);
            forceMock(ResetPasswordPersister.class);
            forceMock(ProfilePersister.class);
            forceMock(ResetPasswordEmailGenerator.class);
            forceMock(PasswordChangedEmailGenerator.class);
            forceMock(ConfirmEmailAddressEmailGenerator.class);
        }
    }

    private static final String A_VALID_EMAIL = "avalid@email.com";
    private static final String A_VALID_USERNAME = "A VALID USERNAME";
    private static final String A_NAME = "John Smith";
    private static final String A_CODE = "code";
    private static final String VALID_PASSWORD = "abcdefg";
    private static final String INVALID_PASSWORD = "a";
    private static final String A_HANDLE = "A_HANDLE";
    private static final int DEFAULT_PROFILE = 32;
    private static final String FULL_NAME = "Full Name";
    private static final int USER_ID = 99;

    @Inject
    AccountServiceImpl accountService;

    @Inject
    EmailValidator emailValidator;
    @Inject
    CredentialedUserPersister credentialedUserPersister;
    @Inject
    ProfilePersister profilePersister;
    @Inject
    ResetPasswordPersister resetPasswordPersister;
    @Inject
    ContactService contactService;
    @Inject
    EmailService emailService;
    @Inject
    UserService userService;
    @Inject
    VerificationService verificationService;
    @Inject
    ConfirmEmailAddressEmailGenerator confirmEmailAddressEmailGenerator;

    @Test(expected = NotFoundException.class)
    public void generate_withInexistentEmail_throws() throws Exception {
        // given
        given(emailValidator.isValid(anyString())).willReturn(true);

        given(credentialedUserPersister.getByEmail(anyString())).willReturn(null);

        // when
        accountService.generateResetPassword(A_VALID_EMAIL);
    }

    @Test(expected = NotFoundException.class)
    public void generate_withInexistentUsername_throws() throws Exception {
        // given
        given(emailValidator.isValid(anyString())).willReturn(false);

        given(credentialedUserPersister.getByHandle(anyString())).willReturn(null);

        // when
        accountService.generateResetPassword(A_VALID_EMAIL);
    }

    @Test
    public void generate_withExistingEmail_willSendEmail(
            ResetPasswordEmailGenerator resetPasswordEmailGenerator) throws Exception {
        // given
        given(emailValidator.isValid(A_VALID_EMAIL)).willReturn(true);
        EmailData emailData = mockResetEmailGenerator(resetPasswordEmailGenerator);

        CredentialedUser credentialedUser = mockCredentialedUser();
        given(credentialedUserPersister.getByEmail(same(A_VALID_EMAIL))).willReturn(credentialedUser);

        mockGetPrimaryEmail(credentialedUser);

        ResetPassword resetPassword = mockCreateResetPassword();

        // when
        accountService.generateResetPassword(A_VALID_EMAIL);

        // then
        verifyInOrderGenerateResetPassword(resetPasswordEmailGenerator, emailData, resetPassword);
    }

    @Test
    public void generate_withExistingUsername_willSendEmail(
            ResetPasswordEmailGenerator resetPasswordEmailGenerator) throws Exception {
        // given
        given(emailValidator.isValid(A_VALID_USERNAME)).willReturn(false);

        EmailData emailData = mockResetEmailGenerator(resetPasswordEmailGenerator);

        CredentialedUser credentialedUser = Mockito.mock(CredentialedUser.class, Mockito.RETURNS_DEEP_STUBS);
        given(credentialedUserPersister.getByHandle(same(A_VALID_USERNAME))).willReturn(credentialedUser);

        mockGetPrimaryEmail(credentialedUser);

        ResetPassword resetPassword = mockCreateResetPassword();

        // when
        accountService.generateResetPassword(A_VALID_USERNAME);

        // then
        verifyInOrderGenerateResetPassword(resetPasswordEmailGenerator, emailData, resetPassword);
    }

    @Test
    public void resetPassword_withValidCodeAndPassword_resetsPassword(
            PersistenceService persistenceService,
            PasswordChangedEmailGenerator passwordChangedEmailGenerator) throws Exception {
        // given
        Connection connection = Mockito.mock(Connection.class);
        given(persistenceService.getConnection()).willReturn(connection);
        given(resetPasswordPersister.getByCode(A_CODE)).willReturn(new ResetPassword(A_HANDLE, A_CODE, new Date()));

        CredentialedUser credentialedUser = mockCredentialedUser();
        given(credentialedUserPersister.getByHandle(A_HANDLE)).willReturn(credentialedUser);
        mockGetPrimaryEmail(credentialedUser, connection);
        EmailData emailData = mockResetEmailGenerator(passwordChangedEmailGenerator);

        // when
        accountService.resetPassword(new ResetPasswordRequest(A_CODE, VALID_PASSWORD, VALID_PASSWORD));

        // then
        InOrder inOrder = inOrder(credentialedUserPersister, resetPasswordPersister,
                passwordChangedEmailGenerator, emailService);
        inOrder.verify(credentialedUserPersister).update(eq(credentialedUser), eq(connection));
        inOrder.verify(resetPasswordPersister).delete(eq(A_HANDLE), eq(connection));
        inOrder.verify(passwordChangedEmailGenerator).generate(eq(A_VALID_EMAIL), eq(A_NAME));
        inOrder.verify(emailService).sendEmail(emailData);

        Mockito.verify(connection, Mockito.never()).rollback();
        Mockito.verify(connection).close();
    }

    @Test(expected = BadRequestException.class)
    public void resetPassword_withValidCodeAndInvalidPassword_badRequest() throws Exception {
        // given
        given(resetPasswordPersister.getByCode(A_CODE)).willReturn(new ResetPassword(A_HANDLE, A_CODE, new Date()));

        // when
        accountService.resetPassword(new ResetPasswordRequest(A_CODE, INVALID_PASSWORD, VALID_PASSWORD));
    }

    @Test(expected = GeneralException.class)
    public void resetPassword_onException_willRollback(PersistenceService persistenceService) throws Exception {
        // given
        Connection connection = Mockito.mock(Connection.class);
        given(persistenceService.getConnection()).willReturn(connection);
        given(resetPasswordPersister.getByCode(A_CODE)).willReturn(new ResetPassword(A_HANDLE, A_CODE, new Date()));

        CredentialedUser credentialedUser = mockCredentialedUser();
        given(credentialedUserPersister.getByHandle(A_HANDLE)).willReturn(credentialedUser);
        mockGetPrimaryEmail(credentialedUser, connection);
        doThrow(new GeneralException("")).when(credentialedUserPersister).update(
                eq(credentialedUser), eq(connection));

        // when
        try {
            accountService.resetPassword(new ResetPasswordRequest(A_CODE, VALID_PASSWORD, VALID_PASSWORD));
        } catch (Exception e) {
            Mockito.verify(connection).rollback();
            Mockito.verify(connection).close();
            throw e;
        }
    }

    @Test(expected = NotFoundException.class)
    public void resetPassword_withInvalidCode() throws Exception {
        // given
        given(resetPasswordPersister.getByCode(A_CODE)).willReturn(null);

        // when
        accountService.resetPassword(new ResetPasswordRequest(A_CODE, VALID_PASSWORD, VALID_PASSWORD));
    }

    @Test
    public void getEmail_delegatesToService() {
        User user = mock(User.class);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        given(userService.getLoggedInUser(true)).willReturn(user);
        given(contactService.retrieveEmails(same(user))).willReturn(contacts);

        List<Contact> result = accountService.getEmails();

        assertThat(result).isSameAs(contacts);
    }

    @Test
    public void getPrimaryEmail_delegatesToService() {
        User user = mock(User.class);
        String primaryEmail = "primary@email.com";
        given(userService.getLoggedInUser(true)).willReturn(user);
        given(contactService.getPrimaryEmail(same(user))).willReturn(primaryEmail);

        String result = accountService.getPrimaryEmail();

        assertThat(result).isSameAs(primaryEmail);
    }

    @Test
    public void setPrimaryEmail_withUnverifiedContact_sendsEmail() {
        User user = new User(USER_ID, 1);
        user.setDefaultProfile(DEFAULT_PROFILE);
        String email = "email@email.com";
        given(userService.getLoggedInUser(true)).willReturn(user);
        Contact primaryEmail = new Contact(USER_ID, Contact.MediumType.EMAIL.name(), "Home", email, UNVERIFIED);

        Verification verification = mock(Verification.class);
        given(verificationService.create(eq(primaryEmail))).willReturn(verification);
        Profile profile = mock(Profile.class);
        given(profile.getFullName()).willReturn(FULL_NAME);
        given(profilePersister.get(DEFAULT_PROFILE)).willReturn(profile);
        EmailData emailData = mock(EmailData.class);
        given(confirmEmailAddressEmailGenerator.generate(same(verification), eq(FULL_NAME))).willReturn(emailData);

        accountService.setPrimaryEmail(primaryEmail);

        verify(emailService).sendEmail(same(emailData));
    }

    @Test
    public void setPrimaryEmail_savesContact() {
        User user = new User(USER_ID, 1);
        String email = "email@email.com";
        given(userService.getLoggedInUser(true)).willReturn(user);
        Contact primaryEmail = new Contact(USER_ID, Contact.MediumType.EMAIL.name(), "Home", email, VERIFIED);

        accountService.setPrimaryEmail(primaryEmail);

        verify(contactService).saveContact(eq(USER_ID), isNull(String.class), eq(primaryEmail));
    }

    private CredentialedUser mockCredentialedUser() {
        CredentialedUser credentialedUser = Mockito.mock(CredentialedUser.class);
        User user = Mockito.mock(User.class);

        given(credentialedUser.getUser()).willReturn(user);
        given(user.getHandle()).willReturn(A_HANDLE);

        return credentialedUser;
    }

    private ResetPassword mockCreateResetPassword() {
        ResetPassword resetPassword = Mockito.mock(ResetPassword.class);

        given(resetPasswordPersister.create(any(ResetPassword.class))).willReturn(resetPassword);

        return resetPassword;
    }

    private EmailData mockResetEmailGenerator(PasswordChangedEmailGenerator passwordChangedEmailGenerator) {
        EmailData emailData = Mockito.mock(EmailData.class);

        given(passwordChangedEmailGenerator.generate(anyString(), eq(A_NAME))).willReturn(emailData);

        return emailData;
    }

    private EmailData mockResetEmailGenerator(ResetPasswordEmailGenerator resetPasswordEmailGenerator) {
        EmailData emailData = Mockito.mock(EmailData.class);

        given(resetPasswordEmailGenerator.generate(anyString(), any(ResetPassword.class), eq(A_NAME)))
                .willReturn(emailData);

        return emailData;
    }

    private void mockGetPrimaryEmail(CredentialedUser credentialedUser) {
        mockGetPrimaryEmail(credentialedUser, null);
    }

    private void mockGetPrimaryEmail(CredentialedUser credentialedUser, Connection connection) {
        Profile profile = mock(Profile.class);
        given(profile.getGivenName()).willReturn(A_NAME);
        given(profilePersister.get(anyInt())).willReturn(profile);
        given(profilePersister.get(anyInt(), eq(connection))).willReturn(profile);
        given(contactService.getPrimaryEmail(eq(credentialedUser.getUser()))).willReturn(A_VALID_EMAIL);
    }

    private void verifyInOrderGenerateResetPassword(ResetPasswordEmailGenerator resetPasswordEmailGenerator,
            EmailData emailData, ResetPassword resetPassword) {
        InOrder inOrder = inOrder(resetPasswordPersister, resetPasswordEmailGenerator, emailService);
        inOrder.verify(resetPasswordPersister).create(any(ResetPassword.class));
        inOrder.verify(resetPasswordEmailGenerator).generate(eq(A_VALID_EMAIL), eq(resetPassword), eq(A_NAME));
        inOrder.verify(emailService).sendEmail(eq(emailData));
    }
}
