package org.consumersunion.stories.server.business_logic;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.exception.NotFoundException;

public interface AccountService {
    void generateResetPassword(String username);

    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    List<Contact> getEmails();

    String getPrimaryEmail() throws NotFoundException;

    void setPrimaryEmail(Contact contact);
}
