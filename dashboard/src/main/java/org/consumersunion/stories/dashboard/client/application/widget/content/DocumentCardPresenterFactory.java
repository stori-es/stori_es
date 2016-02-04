package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;

interface DocumentCardPresenterFactory {
    AddDocumentPresenter createAddContent(SystemEntity systemEntity,
            SystemEntityRelation entityRelation);

    DocumentTextCardPresenter createDocumentCard(Document documentText, ContentKind contentKind);

    ResponseCardPresenter createResponseCard(AnswerSetSummary answerSetSummary);
}
