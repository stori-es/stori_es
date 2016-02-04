package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
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
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.ResetPasswordPersister;
import org.consumersunion.stories.server.util.UniqueIdGenerator;
import org.springframework.stereotype.Service;

import static org.consumersunion.stories.common.shared.model.entity.Contact.MediumType.EMAIL;
import static org.consumersunion.stories.common.shared.model.entity.Contact.TYPE_HOME;

@Service
public class AccountServiceImpl implements AccountService {
    private final EmailValidator emailValidator;
    private final CredentialedUserPersister credentialedUserPersister;
    private final ResetPasswordPersister resetPasswordPersister;
    private final ProfilePersister profilePersister;
    private final VerificationService verificationService;
    private final UserService userService;
    private final EmailService emailService;
    private final ContactService contactService;
    private final PersistenceService persistenceService;
    private final ConfirmEmailAddressEmailGenerator confirmEmailAddressEmailGenerator;
    private final ResetPasswordEmailGenerator resetPasswordEmailGenerator;
    private final PasswordChangedEmailGenerator passwordChangedEmailGenerator;
    private final UniqueIdGenerator uniqueIdGenerator;

    @Inject
    AccountServiceImpl(
            EmailValidator emailValidator,
            CredentialedUserPersister credentialedUserPersister,
            ResetPasswordPersister resetPasswordPersister,
            ProfilePersister profilePersister,
            VerificationService verificationService,
            UserService userService,
            EmailService emailService,
            ContactService contactService,
            PersistenceService persistenceService,
            ConfirmEmailAddressEmailGenerator confirmEmailAddressEmailGenerator,
            ResetPasswordEmailGenerator resetPasswordEmailGenerator,
            PasswordChangedEmailGenerator passwordChangedEmailGenerator,
            UniqueIdGenerator uniqueIdGenerator) {
        this.emailValidator = emailValidator;
        this.credentialedUserPersister = credentialedUserPersister;
        this.resetPasswordPersister = resetPasswordPersister;
        this.profilePersister = profilePersister;
        this.verificationService = verificationService;
        this.userService = userService;
        this.emailService = emailService;
        this.contactService = contactService;
        this.persistenceService = persistenceService;
        this.confirmEmailAddressEmailGenerator = confirmEmailAddressEmailGenerator;
        this.resetPasswordEmailGenerator = resetPasswordEmailGenerator;
        this.passwordChangedEmailGenerator = passwordChangedEmailGenerator;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    @Override
    public void generateResetPassword(String usernameOrEmail) {
        User user = getUser(usernameOrEmail);

        String nonce = uniqueIdGenerator.get();
        ResetPassword resetPassword = resetPasswordPersister.create(new ResetPassword(user.getHandle(), nonce));

        String primaryEmail = contactService.getPrimaryEmail(user);
        Profile profile = profilePersister.get(user.getDefaultProfile());

        EmailData emailData = resetPasswordEmailGenerator.generate(primaryEmail, resetPassword, profile.getGivenName());
        emailService.sendEmail(emailData);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ResetPassword resetPassword = resetPasswordPersister.getByCode(resetPasswordRequest.getCode());

        validateResetPasswordRequest(resetPasswordRequest, resetPassword);

        CredentialedUser credentialedUser = credentialedUserPersister.getByHandle(resetPassword.getHandle());
        credentialedUser.setPasswordClearText(resetPasswordRequest.getPassword());

        Connection conn = persistenceService.getConnection();
        try {
            credentialedUserPersister.update(credentialedUser, conn);

            User user = credentialedUser.getUser();
            resetPasswordPersister.delete(user.getHandle(), conn);

            String primaryEmail = contactService.getPrimaryEmail(user);
            Profile profile = profilePersister.get(user.getDefaultProfile(), conn);

            EmailData emailData = passwordChangedEmailGenerator.generate(primaryEmail, profile.getGivenName());
            emailService.sendEmail(emailData);
        } catch (RuntimeException e) {
            try {
                conn.rollback();
                throw e;
            } catch (SQLException e1) {
                throw new GeneralException(e1);
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public List<Contact> getEmails() throws NotLoggedInException {
        User user = userService.getLoggedInUser(true);

        return contactService.retrieveEmails(user);
    }

    @Override
    public String getPrimaryEmail() throws NotFoundException {
        User user = userService.getLoggedInUser(true);

        return contactService.getPrimaryEmail(user);
    }

    @Override
    public void setPrimaryEmail(Contact contact) {
        User user = userService.getLoggedInUser(true);

        Contact primaryEmail =
                new Contact(user.getId(), EMAIL.name(), TYPE_HOME, contact.getValue(), contact.getStatus());

        if (contact.getEntityId() == 0) {
            Verification verification = verificationService.create(primaryEmail);

            Profile profile = profilePersister.get(user.getDefaultProfile());

            EmailData emailData = confirmEmailAddressEmailGenerator.generate(verification, profile.getFullName());
            emailService.sendEmail(emailData);
        } else {
            contactService.saveContact(user.getId(), null, primaryEmail);
        }
    }

    private User getUser(String usernameOrEmail) {
        CredentialedUser credentialedUser;
        if (emailValidator.isValid(usernameOrEmail)) {
            credentialedUser = credentialedUserPersister.getByEmail(usernameOrEmail);
        } else {
            credentialedUser = credentialedUserPersister.getByHandle(usernameOrEmail);
        }

        if (credentialedUser == null) {
            throw new NotFoundException();
        }

        return credentialedUser.getUser();
    }

    private void validateResetPasswordRequest(ResetPasswordRequest resetPasswordRequest, ResetPassword resetPassword) {
        if (resetPassword == null) {
            throw new NotFoundException();
        }

        if (!validTime(resetPassword.getCreated())
                || !resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())
                || resetPasswordRequest.getPassword().length() < ResetPasswordRequest.MIN_LENGTH) {
            throw new BadRequestException();
        }
    }

    private boolean validTime(Date created) {
        Calendar createdCalendar = Calendar.getInstance();
        createdCalendar.setTime(created);

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.add(Calendar.HOUR, -ResetPassword.NB_HOURS);

        return nowCalendar.before(createdCalendar);
    }
}
