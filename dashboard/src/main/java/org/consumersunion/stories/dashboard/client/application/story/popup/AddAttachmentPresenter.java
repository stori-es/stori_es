package org.consumersunion.stories.dashboard.client.application.story.popup;

import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.event.AttachmentChangedEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class AddAttachmentPresenter extends PresenterWidget<AddAttachmentPresenter.MyView>
        implements AddAttachmentUiHandlers {
    interface MyView extends PopupView, HasUiHandlers<AddAttachmentUiHandlers> {
        void clearForm();
    }

    private final RpcDocumentServiceAsync documentService;

    private SystemEntity currentEntity;

    @Inject
    AddAttachmentPresenter(
            EventBus eventBus,
            MyView view,
            RpcDocumentServiceAsync documentService) {
        super(eventBus, view);

        this.documentService = documentService;

        getView().setUiHandlers(this);
    }

    public void setCurrentEntity(SystemEntity currentEntity) {
        this.currentEntity = currentEntity;
    }

    @Override
    public void addAttachment(String text, String url) {
        Document newAttachment = new Document();
        newAttachment.setEntity(currentEntity.getId());
        newAttachment.addBlock(new Content(BlockType.CONTENT, text, TextType.PLAIN));
        newAttachment.setTitle(text);
        newAttachment.setPermalink(url);

        documentService.createAttachment(newAttachment, new ResponseHandler<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Attachment added successfully.");
                AttachmentChangedEvent.fire(this);

                getView().clearForm();
                getView().hide();
            }
        });
    }
}
