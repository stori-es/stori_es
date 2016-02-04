package org.consumersunion.stories.server.business_logic;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.helper.EmailProperties;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailServiceImpl.class.getName());

    private final EmailProperties emailProperties;

    @Inject
    EmailServiceImpl(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    private void sendEmail(String subject, String body, String toAddress, String toName) {
        Session session = createMailSession();
        Transport transport = createMailTransport(session);

        try {
            transport.connect(emailProperties.getSmtpUserName(), emailProperties.getSmtpPassword());
            MimeMessage message = buildConcreteMessage(session, subject, body, toAddress, toName);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            LOGGER.log(Level.WARNING, "Unable to send direct email.", e);
            throw new GeneralException(e);
        } finally {
            try {
                transport.close();
            } catch (MessagingException e) {
                throw new GeneralException(e);
            }
        }
    }

    @Override
    public void sendEmail(EmailData emailData) {
        sendEmail(emailData.getSubject(), emailData.getBody(), emailData.getEmail(), emailData.getPersonal());
    }

    private Session createMailSession() {
        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.transport.protocol", "smtp");
        sessionProperties.put("mail.smtp.port", emailProperties.getSmtpPort());
        sessionProperties.put("mail.smtp.host", emailProperties.getSmtpEndpoint());
        sessionProperties.put("mail.smtp.starttls.enable", "true");
        sessionProperties.put("mail.smtp.starttls.required", "true");

        return Session.getInstance(sessionProperties);
    }

    private Transport createMailTransport(Session session) {
        try {
            return session.getTransport();
        } catch (NoSuchProviderException e) {
            throw new GeneralException(e);
        }
    }

    private MimeMessage buildConcreteMessage(Session session, String subject, String body, String toAddresses,
            String toName) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(emailProperties.getSmtpFrom(), emailProperties.getSmtpFromName()));
        } catch (UnsupportedEncodingException ignored) {
            message.setFrom(new InternetAddress(emailProperties.getSmtpFrom()));
        }
        message.addRecipients(RecipientType.TO, new Address[]{buildRecipientsAddress(toAddresses, toName)});
        message.setSubject(subject);
        message.setText(body, emailProperties.getCharset(), emailProperties.getEmailType());

        return message;
    }

    public InternetAddress buildRecipientsAddress(String address, String name) throws AddressException {
        String overrideRecipient = emailProperties.getOverrideRecipient();

        if (Strings.isNullOrEmpty(overrideRecipient)) {
            try {
                return new InternetAddress(address, name);
            } catch (UnsupportedEncodingException ignored) {
                return new InternetAddress(address);
            }
        } else {
            return new InternetAddress(overrideRecipient);
        }
    }
}
