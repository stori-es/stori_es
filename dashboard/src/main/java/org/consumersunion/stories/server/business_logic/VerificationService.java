package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.server.model.Verification;
import org.consumersunion.stories.common.shared.model.entity.Contact;

public interface VerificationService {
    Verification create(Contact contact);

    Verification get(String code);

    void delete(String code);
}
