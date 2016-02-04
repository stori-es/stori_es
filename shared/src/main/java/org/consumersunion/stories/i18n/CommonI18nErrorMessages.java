package org.consumersunion.stories.i18n;

import com.google.gwt.i18n.client.Messages;

public interface CommonI18nErrorMessages extends Messages {
    @DefaultMessage("Could not parse ''{0}'' as integer.")
    String invalidInteger(String input);

    @DefaultMessage("Not logged in.")
    String notLoggedIn();

    @DefaultMessage("Invalid parameters.")
    String invalidParameters();

    @DefaultMessage("Invalid request.")
    String invalidRequest();

    @DefaultMessage("Invalid link.")
    String invalidLink();

    @DefaultMessage("Please change the questionnaire title to allow a valid permalink generation.")
    String permalinkUnavailable();

    @DefaultMessage("Error: ")
    String error();

    @DefaultMessage("User not found")
    String userNotFound();

    @DefaultMessage("Invalid password")
    String passwordInvalid();

    @DefaultMessage("There was an error trying to authenticate user on the system. ")
    String authenticationError();

    @DefaultMessage("Please fill all the required fields.")
    String requiredFields();

    @DefaultMessage("Network error. Try again later.")
    String networkError();

    @DefaultMessage("Service error: ''{0}''.")
    String serviceError(String message);

    @DefaultMessage("Error getting the user logged: ")
    String gettingUserLoggedError();

    @DefaultMessage("Confirmation password does not match")
    String confirmationPassword();

    @DefaultMessage("Invalid current password")
    String currentPasswordInvalid();

    @DefaultMessage("The user does not exist in the database. ")
    String userDoesnotInDataBase();

    @DefaultMessage("The requested questionnaire does not exist.")
    String questionnaireNotFound();

    @DefaultMessage("The requested content has not been published yet. ")
    String contentNotPublished();

    @DefaultMessage("User has not permissions to remove this record")
    String notPermissionsToRemove();

    @DefaultMessage("User has no administrative rights")
    String noAdministrativeRights();

    @DefaultMessage("Invalid Organization Name")
    String invalidOrganizationName();

    @DefaultMessage("No AnswerSet with ID {0} found.")
    String answerSetWithIdNotFound(String id);

    @DefaultMessage("No Collection with ID {0} found.")
    String collectionWithIdNotFound(String id);

    @DefaultMessage("No Collection with Questionnaire title {0} and permanent link {1} found.")
    String collectionWithQuestionnaireAndPermalinkNotFound(String questionnaireTitle, String permanentLink);

    @DefaultMessage("No CredentialedUser with ID {0} found.")
    String credentialedWithIdUserNotFound(String id);

    @DefaultMessage("No Document with ID {0,number,#} found.")
    String documentWithIdNotFound(int id);

    @DefaultMessage("No Entity with ID {0,number,#} found.")
    String entityWithIdNotFound(String id);

    @DefaultMessage("No Questionnaire with ID {0} / {1} found.")
    String questionnaireWithIdLocaleNotFound(int id, String locale);

    @DefaultMessage("No Story with ID {0,number,#} found.")
    String storyWithIdNotFound(int id);

    @DefaultMessage("No SystemEntity with ID {0} found.")
    String systemEntityWithIdNotFound(String id);

    @DefaultMessage("No User with ID {0} found.")
    String userWithIdNotFound(String id);

    @DefaultMessage("No SID with Sid {0} found.")
    String sidWithSidNotFound(String sid);

    @DefaultMessage("Can''t persist invalid questionnaire.")
    String cantPersistInvalidQuestionnaire();

    @DefaultMessage("Unable to link stories to collections : {0}")
    String unableToLinkStoriesToCollections(String collectionsList);
}
