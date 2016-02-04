package org.consumersunion.stories.i18n;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;

public interface CommonI18nMessages extends Messages {
    @DefaultMessage("Welcome {0}!")
    String welcome(String name);

    @DefaultMessage("Last sign in: {0,date,medium} {0,time,medium}")
    String lastLogin(Date timestamp);

    @DefaultMessage("Untitled")
    String untitled();

    @DefaultMessage("<p>EXAMPLE TEXT CONTENT BLOCK</p><br/><p>This is an example of a " +
            "Text Content Block. You can edit this Block to add a title and leading text describing your stori.es " +
            "content. For a Questionnaire, briefly describe the overall subject matter and what you''d like people to" +
            " do. For Collections, introduce the theme of the published Stories.</p>")
    String defaultTextBlock();

    @DefaultMessage("SUBMIT")
    String submit();

    @DefaultMessage("Confirmation")
    String confirmationTitle();

    @DefaultMessage("<font size=\"4\">Thank You!</font>" +
            "<div style=\"font-size: small;\"><br></div>" +
            "<div style=\"font-size: small;\">" +
            "<span style=\"font-size: 13px; line-height: 20px;\">Thank you for sharing your story with us.</span>" +
            "</div>")
    String confirmationText();

    @DefaultMessage("Redirect")
    String redirect();
}
