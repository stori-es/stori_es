package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.QuestionnairePut;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireBase;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class QuestionnairePutMerger extends AbstractMerger<QuestionnaireBase, QuestionnairePut> {
    @Override
    public void merge(QuestionnaireBase entity, QuestionnairePut dto) {
        maybeUpdateBodyDocument(dto, entity);

        if (dto.getPublished() != null) {
            entity.setPublished(dto.getPublished());
        }
    }

    private void maybeUpdateBodyDocument(QuestionnairePut questionnairePut, QuestionnaireBase questionnaire) {
        if (!Strings.isNullOrEmpty(questionnairePut.getTitle())) {
            questionnaire.getBodyDocument().setTitle(questionnairePut.getTitle());
        }
        if (!Strings.isNullOrEmpty(questionnairePut.getSummary())) {
            questionnaire.getBodyDocument().setSummary(questionnairePut.getSummary());
        }
    }
}
