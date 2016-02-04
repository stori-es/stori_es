package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import java.util.List;

import org.consumersunion.stories.common.client.ui.TextPager;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.QuestionnaireCellFactory;
import org.consumersunion.stories.dashboard.client.application.ui.ConfirmationModal;
import org.consumersunion.stories.dashboard.client.resource.QuestionnaireListStyle;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ListQuestionnaireView extends ViewWithUiHandlers<ListQuestionnaireUiHandlers>
        implements ListQuestionnairePresenter.MyView {
    interface Binder extends UiBinder<Widget, ListQuestionnaireView> {
    }

    @UiField(provided = true)
    final CellList<QuestionnaireI15d> questionnaireList;
    @UiField(provided = true)
    final TextPager pager;

    private final ProvidesKey<QuestionnaireI15d> keyProvider;
    private final AsyncDataProvider<QuestionnaireI15d> dataProvider;

    @Inject
    ListQuestionnaireView(
            Binder uiBinder,
            QuestionnaireCellFactory questionnaireCellFactory,
            QuestionnaireListStyle listStyle,
            TextPager pager) {
        this.pager = pager;
        keyProvider = setupKeyProvider();
        dataProvider = setupDataProvider();
        questionnaireList = new CellList<QuestionnaireI15d>(
                questionnaireCellFactory.create(setupQuestionnaireDetailAction(), setupQuestionnaireDeleteAction()),
                listStyle);

        initWidget(uiBinder.createAndBindUi(this));

        pager.setDisplay(questionnaireList);
        pager.setPageSize(ListQuestionnairePresenter.PAGE_SIZE);
    }

    @Override
    public void setData(List<QuestionnaireI15d> data, int start, int totalCount) {
        questionnaireList.setRowData(start, data);
        questionnaireList.setRowCount(totalCount);
        pager.setVisible(pager.getPageCount() > 1);
    }

    @Override
    public void init() {
        if (!dataProvider.getDataDisplays().isEmpty()) {
            dataProvider.removeDataDisplay(questionnaireList);
        }

        dataProvider.addDataDisplay(questionnaireList);
    }

    @Override
    public void clear() {
        questionnaireList.setRowCount(0);
        pager.setVisible(false);
    }

    @Override
    public void refreshQuestionnaires() {
        fetchData(questionnaireList);
    }

    private ProvidesKey<QuestionnaireI15d> setupKeyProvider() {
        return new ProvidesKey<QuestionnaireI15d>() {
            @Override
            public Object getKey(QuestionnaireI15d questionnaire) {
                return (questionnaire == null) ? null : questionnaire.getId();
            }
        };
    }

    private AsyncDataProvider<QuestionnaireI15d> setupDataProvider() {
        return new AsyncDataProvider<QuestionnaireI15d>(keyProvider) {
            @Override
            protected void onRangeChanged(HasData<QuestionnaireI15d> display) {
                fetchData(display);
            }
        };
    }

    private ActionCell.Delegate<QuestionnaireI15d> setupQuestionnaireDetailAction() {
        return new ActionCell.Delegate<QuestionnaireI15d>() {
            @Override
            public void execute(QuestionnaireI15d questionnaire) {
                getUiHandlers().questionnaireDetails(questionnaire);
            }
        };
    }

    private ActionCell.Delegate<QuestionnaireI15d> setupQuestionnaireDeleteAction() {
        return new ActionCell.Delegate<QuestionnaireI15d>() {
            @Override
            public void execute(QuestionnaireI15d questionnaire) {
                removeSourceQuestionnaire(questionnaire);
            }
        };
    }

    private void fetchData(HasData<QuestionnaireI15d> display) {
        Range range = display.getVisibleRange();

        getUiHandlers().loadQuestionnaire(range.getStart(), range.getLength());
    }

    private void removeSourceQuestionnaire(final QuestionnaireI15d questionnaire) {
        ConfirmationModal confirmationModal = new ConfirmationModal("Confirm Deletion",
                "Are you sure you would like to remove the source questionnaire \""
                        + questionnaire.getTitle() + "\"?") {
            @Override
            protected void handleConfirm() {
                getUiHandlers().removeSourceQuestionnaire(questionnaire);
            }
        };
        confirmationModal.center();
    }
}
