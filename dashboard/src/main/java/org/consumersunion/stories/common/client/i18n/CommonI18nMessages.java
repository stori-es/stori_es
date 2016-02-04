package org.consumersunion.stories.common.client.i18n;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;

public interface CommonI18nMessages extends Messages {
    @DefaultMessage("Are you sure you would like to remove this phone number {0} ?")
    String deletePhoneConf(String number);

    @DefaultMessage("Are you sure you would like to remove this email address {0} ?")
    String deleteEmailConf(String email);

    @DefaultMessage("Are you sure you would like to remove this address {0} ?")
    String deleteAddressConf(String address);

    @DefaultMessage("Are you sure you would like to remove this social account ?")
    String deleteSocialAccount();

    @DefaultMessage("Account updated successfully")
    String accountUpdated();

    @DefaultMessage("Account disabled successfully")
    String accountDisabled();

    @DefaultMessage("Account enabled successfully")
    String accountEnabled();

    @DefaultMessage("Your session will expire in {0} minutes !")
    String timeoutWarning(String counter);

    @DefaultMessage("Stories added successfully.")
    String storiesAddedSuccessfully();

    @DefaultMessage("Tags added successfully.")
    String tagsAddedSuccessfully();

    @DefaultMessage("Note added successfully.")
    String noteAddedSuccessfully();

    @DefaultMessage("//www.youtube.com/embed/{0}?hl=en&persist_hl=1")
    String youtubeEmbedUrl(String videoId);

    @DefaultMessage("//w.soundcloud.com/player/?auto_play=false&url={0}")
    String soundcloudEmbedUrl(String videoId);

    @DefaultMessage("{0} ({1})")
    String collectionTitleWithCount(String title, Integer count);

    @DefaultMessage("Created {0,date, MMM dd, yyyy}")
    String createdDate(Date date);

    @DefaultMessage("Modified {0,date, MMM dd, yyyy}")
    String modifiedDate(Date date);

    @DefaultMessage("Modified {0,date, MMM dd, yyyy hh:mm:ss a z}")
    String modifiedDateTime(Date date);

    @DefaultMessage("Published {0,date, MMM dd, yyyy hh:mm:ss a z}")
    String publishedDateTime(Date date);

    @DefaultMessage("Created {0,date,MMM dd, yyyy hh:mm:ss a z}\nModified {1,date,MMM dd, yyyy hh:mm:ss a z}")
    String createModifiedTimestamp(Date createdDate, Date modifiedDate);

    @DefaultMessage("expires {0,date, MMM dd, yyyy}")
    String expiresDate(Date date);

    @DefaultMessage("Collection deleted successfully.")
    String collectionDeleted();

    @DefaultMessage("Questionnaire deleted successfully.")
    String questionnaireDeleted();

    @DefaultMessage("Showing {0} collections/{1} questionnaires")
    String showingNbCollectionsQuestionnaires(int nbCollections, int nbQuestionnaires);

    @DefaultMessage("Password updated successfully")
    String passwordSaved();

    @DefaultMessage("Personal information was saved successfully.")
    String personSaved();

    @DefaultMessage("Notifications for the selected content are now on.")
    String notificationsOn();

    @DefaultMessage("Notifications for the selected content are now off.")
    String notificationsOff();

    @DefaultMessage("Questionnaire copied successfully")
    String questionnaireCopied();

    @DefaultMessage("{0,date,MMM dd, yyyy}")
    String shortDate(Date date);

    @DefaultMessage("Adding stories to collections... ({0}%)")
    String addingStoriesPercent(int percent);

    @DefaultMessage("Adding stories to collections... done!")
    String addingStoriesDone();

    @DefaultMessage("Exporting... ({0}%)")
    String exportingPercent(int percent);

    @DefaultMessage("Exporting... done!")
    String exportingDone();
}
