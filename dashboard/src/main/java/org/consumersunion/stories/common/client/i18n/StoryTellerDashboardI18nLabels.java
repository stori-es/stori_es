package org.consumersunion.stories.common.client.i18n;

import java.util.Date;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface StoryTellerDashboardI18nLabels extends Messages {
    @DefaultMessage("View Stories")
    String myStories();

    @DefaultMessage("View Collections")
    String myCollections();

    @DefaultMessage("Admin")
    String admin();

    @DefaultMessage("Add to:")
    String addTo();

    @DefaultMessage("Sign out")
    String signOut();

    @DefaultMessage("search...")
    String search();

    @DefaultMessage("search stories...")
    String searchStories();

    @DefaultMessage("search collections...")
    String searchCollections();

    @DefaultMessage("search for collection by title...")
    String searchCollectionByTitle();

    @DefaultMessage("Search for story by title...")
    String searchStoryByTitle();

    @DefaultMessage("Stories")
    String stories();

    @DefaultMessage("Collections")
    String collections();

    @DefaultMessage("See all")
    String seeAll();

    @DefaultMessage("None")
    String none();

    @DefaultMessage("Create New")
    String createNew();

    @DefaultMessage("Preview")
    String preview();

    @DefaultMessage("Organizations")
    String organizations();

    @DefaultMessage("Questionnaire updated successfully.")
    String questionnaireSaved();

    @DefaultMessage("Collection updated successfully.")
    String collectionSaved();

    @DefaultMessage("Story updated successfully.")
    String storySaved();

    @DefaultMessage("Permissions saved successfully.")
    String permissionsSaved();

    @DefaultMessage("Organization saved successfully.")
    String organizationSaved();

    @DefaultMessage("Showing <span>{0}</span> Stories")
    SafeHtml storiesCount(String count);

    @DefaultMessage("Showing {0} Stories - {1} selected")
    String storiesCountWithSelection(String totalRowCount, String selectedRowCount);

    @DefaultMessage("{0} day ago")
    String dayAgo(Long day);

    @DefaultMessage("{0} days ago")
    String daysAgo(Long day);

    @DefaultMessage("{0}, {1}")
    String cityAndSate(String city, String state);

    @DefaultMessage("Created {0}")
    String createdDateAgo(String date);

    @DefaultMessage("Modified {0}")
    String updatedDateAgo(String date);

    @DefaultMessage("{0} by {1}")
    String attachmentBy(String date, String author);

    @DefaultMessage("New Collection")
    String newCollection();

    @DefaultMessage("New Questionnaire")
    String newQuestionnaire();

    @DefaultMessage("Add Source Questionnaire")
    String addSourceQuestionnaire();

    @DefaultMessage("Type new questionnaire title")
    String newQuestionnaireTitle();

    @DefaultMessage("Type new collection title")
    String newCollectionTitle();

    @DefaultMessage("Today")
    String today();

    @DefaultMessage("Title required.")
    String titleRequired();

    @DefaultMessage("Please add a title")
    String requiredCollectionTitle();

    @DefaultMessage("Click to delete")
    String clickToDelete();

    @DefaultMessage("Click to remove")
    String clickToRemove();

    @DefaultMessage("Click to search")
    String clickToSearch();

    @DefaultMessage("Click to open")
    String clickToOpen();

    @DefaultMessage("Set to draft")
    String setToDraft();

    @DefaultMessage("Publish")
    String publish();

    @DefaultMessage("Modified")
    String modified();

    @DefaultMessage("Published")
    String published();

    @DefaultMessage("Draft")
    String draft();

    @DefaultMessage("There are currently no stories in this collection")
    String noStoriesInCollection();

    @DefaultMessage("{0} ({1,date,MMM dd, yyyy})")
    String authorWithoutAddress(String authorGivenName, Date date);

    @DefaultMessage("{0} of {1}, {2} ({3,date,MMM dd, yyyy})")
    String authorWithAddress(String authorGivenName, String city, String state, Date date);

    @DefaultMessage("{0} of {1} ({2,date,MMM dd, yyyy})")
    String authorWithState(String authorGivenName, String state, Date created);

    @DefaultMessage("No content available!")
    String noContentAvailable();

    @DefaultMessage("Content created successfully")
    String contentCreated();

    @DefaultMessage("Note created successfully")
    String noteCreated();

    @DefaultMessage("Attachment created successfully")
    String attachmentCreated();

    @DefaultMessage("Content")
    String content();

    @DefaultMessage("Note")
    String note();

    @DefaultMessage("Attachment")
    String attachment();

    @DefaultMessage("URL <sup>*</sup>")
    String urlRequiredLabel();

    @DefaultMessage("{0} updated successfully")
    String elementSaved(String type);

    @DefaultMessage("Failed to deliver")
    String failedToDeliver();
}
