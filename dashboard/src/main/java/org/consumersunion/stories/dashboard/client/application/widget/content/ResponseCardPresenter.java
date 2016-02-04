package org.consumersunion.stories.dashboard.client.application.widget.content;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.ui.stories.Redrawable;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;

import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

public class ResponseCardPresenter extends DocumentCardPresenter<AnswerSetSummary, DocumentCardUiHandlers>
        implements DocumentCardUiHandlers, Redrawable {
    interface MyView extends DocumentCardPresenter.MyView<AnswerSetSummary, DocumentCardUiHandlers> {
    }

    private final ResponseBuilderPresenter responseBuilderPresenter;
    private final AnswerSetSummary answerSetSummary;

    @Inject
    ResponseCardPresenter(
            EventBus eventBus,
            MyView view,
            ResponseBuilderPresenter responseBuilderPresenter,
            @Assisted AnswerSetSummary answerSetSummary) {
        super(eventBus, view, answerSetSummary, ContentKind.RESPONSE);

        this.responseBuilderPresenter = responseBuilderPresenter;
        this.answerSetSummary = answerSetSummary;

        getView().setUiHandlers(this);
    }

    @Override
    public void closeCard() {
        getView().setForContentCard();
    }

    @Override
    public void openCard(boolean editMode, boolean addContent) {
        responseBuilderPresenter.initPresenter(answerSetSummary);

        setInSlot(SLOT_DETAILS, responseBuilderPresenter);

        getView().setForContentDetails();
    }

    @Override
    public boolean hasLocale() {
        return ContentKind.RESPONSE.equals(contentKind);
    }

    @Override
    protected Class<AnswerSetSummary> getDocumentClass() {
        return AnswerSetSummary.class;
    }

    @Override
    protected String getCardTitle() {
        return answerSetSummary.getQuestionnaireTitle();
    }
}
