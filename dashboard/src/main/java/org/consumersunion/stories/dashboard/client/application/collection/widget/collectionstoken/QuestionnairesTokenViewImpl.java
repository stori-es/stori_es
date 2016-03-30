package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import org.consumersunion.stories.common.client.ui.QuestionnaireSuggestionOracle;
import org.consumersunion.stories.common.client.widget.QuestionnaireListItemFactory;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class QuestionnairesTokenViewImpl
        extends AbstractCollectionsTokenView<QuestionnaireI15d, QuestionnaireI15d, CollectionData> {
    interface Binder extends UiBinder<Widget, QuestionnairesTokenViewImpl> {
    }

    private final QuestionnaireListItemFactory questionnaireListItemFactory;

    @Inject
    QuestionnairesTokenViewImpl(
            final QuestionnaireSuggestionOracle questionnaireSuggestOracle,
            QuestionnaireListItemFactory questionnaireListItemFactory) {
        super(questionnaireSuggestOracle);

        this.questionnaireListItemFactory = questionnaireListItemFactory;
        title.setInnerText(LABELS.sourceQuestionnaires());

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                questionnaireSuggestOracle.setFilter(getUiHandlers());
            }
        });

        $(collectionIcon).hide();
    }

    @Override
    public int compare(QuestionnaireI15d o1, QuestionnaireI15d o2) {
        if (o1.isQuestionnaire() && !o2.isQuestionnaire()) {
            return -1;
        } else if (o2.isQuestionnaire() && !o1.isQuestionnaire()) {
            return 1;
        }

        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
    }

    @Override
    protected IsWidget createItem(QuestionnaireI15d questionnaire, boolean canRemove) {
        return questionnaireListItemFactory.create(questionnaire, this, canRemove);
    }
}
