package org.consumersunion.stories.server.api.rest.converters;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.post.QuestionnairePost;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

@Component
public class QuestionnaireConverter {
    public QuestionnaireI15d convert(QuestionnairePost questionnairePost) {
        QuestionnaireI15d questionnaire = new QuestionnaireI15d();

        questionnaire.getBodyDocument().setTitle(questionnairePost.getTitle());
        questionnaire.getBodyDocument().setSummary(questionnairePost.getSummary());

        List<Integer> collectionIds = questionnairePost.getCollectionIds();
        if (collectionIds != null && !collectionIds.isEmpty()) {
            questionnaire.setTargetCollections(Sets.newHashSet(collectionIds));
        }

        if (questionnairePost.getTheme() != null) {
            questionnaire.setTheme(questionnairePost.getTheme());
        }

        return questionnaire;
    }
}
