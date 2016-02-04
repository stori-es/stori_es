package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.util.Set;

import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireBase;

public class QuestionnaireData extends CollectionData {
    private Set<String> autoTags;

    public QuestionnaireData(QuestionnaireBase collection, Set<String> tags, Set<String> autoTags, String owner) {
        super(collection, tags, owner);

        this.autoTags = autoTags;
    }

    public QuestionnaireData(QuestionnaireBase collection, Set<String> tags, Set<String> autoTags) {
        this(collection, tags, autoTags, null);
    }

    public QuestionnaireData(QuestionnaireBase questionnaire) {
        this(questionnaire, null, null);
    }

    public Set<String> getAutoTags() {
        return autoTags;
    }

    public void setAutoTags(Set<String> autoTags) {
        this.autoTags = autoTags;
    }

    @Override
    public QuestionnaireBase getCollection() {
        return (QuestionnaireBase) super.getCollection();
    }
}
