package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface CommonI18nErrorMessages extends Messages {
    @DefaultMessage("Not logged in.")
    String notLoggedIn();

    @DefaultMessage("Invalid parameters.")
    String invalidParameters();

    @DefaultMessage("Error: ")
    String error();

    @DefaultMessage("Bad request error: resource not found.")
    String resourceNotFound();

    @DefaultMessage("User not found")
    String userNotFound();

    @DefaultMessage("Please fill all the required fields.")
    String requiredFields();

    @DefaultMessage("Service error: ''{0}''.")
    String serviceError(String message);

    @DefaultMessage("Confirmation password does not match")
    String confirmationPassword();

    @DefaultMessage("The DIV element in your page where the questionnaire will be embedded must contain "
            + "an attribute survey holding the permlink of the questionnaire you want to load.")
    String noSurveyAttribute();

    @DefaultMessage("Please fix the errors before save the Contact Information.")
    String contactInfoError();

    @DefaultMessage("Please add a value to the first admin username")
    String onAdminRequired();

    @DefaultMessage("Please enter a valid page number.")
    String pageOutBounds();

    @DefaultMessage("Error: Invalid Video URL.")
    String invalidVideoUrl();

    @DefaultMessage("Error: Invalid Audio URL.")
    String invalidAudioUrl();

    @DefaultMessage("Error: the URL is incorrect or the resource is unavailable.")
    String documentUnavailable();

    @DefaultMessage("OOPS! The requested file is currently unavailable.")
    String documentInvalid();

    @DefaultMessage("The tag you entered is too long! Please try again.")
    String tagToLong();

    @DefaultMessage("The content you''ve requested is not currently published. Please contact the content " +
            "administrator.")
    SafeHtml contentNotPublished();
}
