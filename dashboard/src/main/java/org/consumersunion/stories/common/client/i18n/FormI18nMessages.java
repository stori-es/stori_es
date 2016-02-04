package org.consumersunion.stories.common.client.i18n;

import com.google.gwt.i18n.client.Messages;

public interface FormI18nMessages extends Messages {
    @DefaultMessage("Month")
    String month();

    @DefaultMessage("Field is required.")
    String requiredField();

    @DefaultMessage("Value must be at least {0} characters long.")
    String valueAtLeast(int number);

    @DefaultMessage("Value must be no more than {0} long.")
    String valueMoreThan(int number);

    @DefaultMessage("Must provide decimal value.")
    String decimalRequired();

    @DefaultMessage("Unexpected account format. String should contain only digits and separators.")
    String accountFormat();

    @DefaultMessage("Unexpected name format. String should contain only letters, spaces, commas, periods, and hyphens.")
    String nameFormat();

    @DefaultMessage("Zip code must match 5 or 5 + 4 digit pattern.")
    String zipFormat();

    @DefaultMessage("Could not recognize email address.")
    String emailFormat();

    @DefaultMessage("Could not recognize phone number.")
    String phoneFormat();

    @DefaultMessage("Date must be after {0}")
    String dateInvalid(String date);

    @DefaultMessage("Please use date format: mm/dd/yyyy")
    String dateFormatInvalid();

    @DefaultMessage("Error: Invalid image URL")
    String invalidImageUrl();

    @DefaultMessage("Error: the URL is incorrect or the resource is unavailable.")
    String invalidDocumentUrl();

    @DefaultMessage("Enter a SoundClound track URL here")
    String enterSoundCloudUrl();

    @DefaultMessage("Enter a YouTube video URL here")
    String enterYoutubeUrl();

    @DefaultMessage("(Please choose a JPG, GIF, or PNG image)")
    String pleaseChooseImage();

    @DefaultMessage("(Maximum size: 4MB)")
    String maximumFileSize();

    @DefaultMessage("File:")
    String file();

    @DefaultMessage("Choose an image to upload")
    String chooseImageToUpload();
}
