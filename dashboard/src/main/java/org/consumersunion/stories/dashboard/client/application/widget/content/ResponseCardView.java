package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;
import org.consumersunion.stories.dashboard.client.application.ui.TimeMetadataWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardContentEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardLocaleEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardSummaryWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardTitleWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarWidget;

import com.google.inject.Inject;

public class ResponseCardView extends DocumentCardView<AnswerSetSummary, DocumentCardUiHandlers>
        implements ResponseCardPresenter.MyView {
    @Inject
    ResponseCardView(
            DocumentCardView.Binder uiBinder,
            TimeMetadataWidget timeMetadataWidget,
            CardToolbarWidget toolbar,
            CardSummaryWidget cardSummary,
            CardTitleWidget cardTitle,
            CardContentEditWidget editContent,
            CardLocaleEditWidget editLocale,
            CommonI18nLabels commonI18nLabels,
            StoryTellerDashboardI18nLabels labels) {
        super(uiBinder, timeMetadataWidget, toolbar, cardSummary, cardTitle, editContent, editLocale, commonI18nLabels,
                labels);
    }

    @Override
    public void save(boolean done) {
        if (editLocale.validate()) {
            getUiHandlers().updateLocale(editLocale.getValue());
            getUiHandlers().save(done);
        } else {
            toolbar.onSaveFailed();
        }
    }

    @Override
    protected boolean canBeOpened() {
        return true;
    }

    @Override
    protected void initEditSection(AnswerSetSummary document) {
        super.initEditSection(document);

        editContent.removeFromParent();
    }
}
