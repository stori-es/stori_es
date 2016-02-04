package org.consumersunion.stories.dashboard.client.application.widget.content;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;

import com.gwtplatform.mvp.client.PresenterWidget;

public class DocumentCardFactory {
    private final DocumentCardPresenterFactory documentCardPresenterFactory;

    @Inject
    DocumentCardFactory(DocumentCardPresenterFactory documentCardPresenterFactory) {
        this.documentCardPresenterFactory = documentCardPresenterFactory;
    }

    public AddDocumentPresenter createAddContent(SystemEntity systemEntity, SystemEntityRelation entityRelation) {
        return documentCardPresenterFactory.createAddContent(systemEntity, entityRelation);
    }

    public PresenterWidget createContentCard(Document document, ContentKind contentKind) {
        switch (contentKind) {
            case STORY:
            case CONTENT:
            case NOTE:
            case ATTACHMENT:
                return documentCardPresenterFactory.createDocumentCard(document, contentKind);
            case RESPONSE:
                return documentCardPresenterFactory.createResponseCard((AnswerSetSummary) document);
        }

        return null;
    }
}
