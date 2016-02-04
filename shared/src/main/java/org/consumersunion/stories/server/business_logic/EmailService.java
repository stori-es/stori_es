package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.server.email.EmailData;

public interface EmailService {
    void sendEmail(EmailData emailData);
}
