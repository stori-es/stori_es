package org.consumersunion.stories.dashboard.client.application.widget.content;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.event.SavedDocumentEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ContentDetailPresenter extends PresenterWidget<ContentDetailPresenter.MyView>
        implements ContentDetailUiHandlers {
    interface MyView extends View, HasUiHandlers<ContentDetailUiHandlers> {
        void setContent(Document document);
    }

    private final RpcDocumentServiceAsync documentService;

    private Document document;

    @Inject
    ContentDetailPresenter(
            EventBus eventBus,
            MyView view,
            RpcDocumentServiceAsync documentService) {
        super(eventBus, view);
        this.documentService = documentService;

        getView().setUiHandlers(this);
    }

    public void setContent(Document document) {
        this.document = document;
        getView().setContent(document);
    }

    @Override
    public void save(List<Block> blocks) {
        document.setBlocks(blocks);
        documentService.saveDocument(document, new ResponseHandlerLoader<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                Document document = result.getDatum();
                setContent(document);

                SavedDocumentEvent.fire(this, document);
            }
        });
    }
}
