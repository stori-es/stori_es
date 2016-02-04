package org.consumersunion.stories.server.email;

public class EmailData {
    private final String subject;
    private final String body;
    private final String email;
    private final String personal;

    public EmailData(
            String subject,
            String body,
            String email,
            String personal) {
        this.subject = subject;
        this.body = body;
        this.email = email;
        this.personal = personal;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonal() {
        return personal;
    }
}
