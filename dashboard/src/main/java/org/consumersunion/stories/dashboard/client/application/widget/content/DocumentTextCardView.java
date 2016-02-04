package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.application.ui.TimeMetadataWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardContentEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardLocaleEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardSummaryWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardTitleWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarWidget;

import com.google.inject.Inject;

public class DocumentTextCardView extends DocumentCardView<Document, DocumentTextCardUiHandlers>
        implements DocumentTextCardPresenter.MyView {
    @Inject
    DocumentTextCardView(
            Binder uiBinder,
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
        boolean localeValid = true;
        if (getUiHandlers().hasLocale()) {
            localeValid = editLocale.validate();
            if (localeValid) {
                getUiHandlers().updateLocale(editLocale.getValue());
            }
        }

        boolean contentValid = editContent.validate();
        if (contentValid && localeValid) {
            getUiHandlers().updateEditedDocument(editContent.getTitle(), editContent.getContentText());
            getUiHandlers().save(done);
        } else {
            toolbar.onSaveFailed();
        }
    }

    @Override
    protected boolean canBeOpened() {
        return ContentKind.CONTENT.equals(contentKind);
    }

    @Override
    protected void initEditSection(Document document) {
        super.initEditSection(document);

        editContent.init(document, contentKind);
    }
}
