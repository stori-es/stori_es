package org.consumersunion.stories.common.shared.model.questionnaire;

public class QuestionnaireI15d extends QuestionnaireBase {
    private int numberOfReponses;

    public QuestionnaireI15d() {
        super();
    }

    public QuestionnaireI15d(int id, int version) {
        super(id, version);
    }

    public int getNumberOfReponses() {
        return numberOfReponses;
    }

    public void setNumberOfReponses(int numberOfReponses) {
        this.numberOfReponses = numberOfReponses;
    }

    @Deprecated
    public Questionnaire toQuestionnaire() {
        Questionnaire questionnaire = new Questionnaire();

        questionnaire.setId(getId());
        questionnaire.setOwner(getOwner());
        questionnaire.setTheme(getTheme());
        questionnaire.setBodyDocument(getBodyDocument());
        questionnaire.setCreated(getCreated());
        questionnaire.setUpdated(getUpdated());
        questionnaire.setPublic(getIsPublic());
        questionnaire.setDeleted(getDeleted());
        questionnaire.setVersion(getVersion());
        questionnaire.setPermalink(getPermalink());
        questionnaire.setStories(getStories());
        questionnaire.setProfile(getProfile());

        questionnaire.setTargetCollections(getTargetCollections());
        questionnaire.setCollectionSources(getCollectionSources());

        questionnaire.setPublished(isPublished());
        questionnaire.setPublishedDate(getPublishedDate());
        questionnaire.setPreviewKey(getPreviewKey());

        questionnaire.setSurvey(getSurvey());
        questionnaire.setBodyDocument(getBodyDocument());
        questionnaire.setConfirmationDocument(getConfirmationDocument());

        return questionnaire;
    }
}
