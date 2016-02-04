package org.consumersunion.stories.common.client.service.response;

import java.util.HashMap;
import java.util.Map;

import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;

public class QuestionnaireSurveyResponse extends Response {

    private String analyticsKey;
    private Questionnaire questionnaire;
    private Integer storyId;
    private Theme theme;
    private Map<String, Question> questions;
    private Map<String, Content> contents;
    private Map<String, ContactBlock> contacts;

    public QuestionnaireSurveyResponse() {
        questions = new HashMap<String, Question>();
        contents = new HashMap<String, Content>();
        contacts = new HashMap<String, ContactBlock>();
    }

    public QuestionnaireSurveyResponse(Response template) {
        super(template);
        questions = new HashMap<String, Question>();
        contents = new HashMap<String, Content>();
        contacts = new HashMap<String, ContactBlock>();
    }

    public void setupQuestionsAndContents() {
        for (Integer i = 0; i < questionnaire.getSurvey().getBlocks().size(); i++) {
            Block item = questionnaire.getSurvey().getBlocks().get(i);
            if (item instanceof Content) {
                contents.put(i.toString(), (Content) item);
            } else if (item instanceof ContactBlock) {
                contacts.put(i.toString(), (ContactBlock) item);
            } else if (item instanceof Question) {
                questions.put(i.toString(), (Question) item);
            }
        }
    }

    public String getAnalyticsKey() {
        return analyticsKey;
    }

    public void setAnalyticsKey(String analyticsKey) {
        this.analyticsKey = analyticsKey;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(final Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public void setStoryId(final Integer storyId) {
        this.storyId = storyId;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(final Theme theme) {
        this.theme = theme;
    }

    public Map<String, Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, Question> questions) {
        this.questions = questions;
    }

    public Map<String, Content> getContents() {
        return contents;
    }

    public void setContents(Map<String, Content> contents) {
        this.contents = contents;
    }

    public Map<String, ContactBlock> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, ContactBlock> contacts) {
        this.contacts = contacts;
    }
}
