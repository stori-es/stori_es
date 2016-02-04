package org.consumersunion.stories.dashboard.client.application.story.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.dashboard.client.application.story.popup.AddAttachmentPresenter;
import org.consumersunion.stories.dashboard.client.event.AttachmentChangedEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 * Used to present an 'attachments' management widget allowing the user to attach URL references to an entity. This is
 * specifically utilized in the {@link Story} and {@link Collection} detail views. The management widget lists all
 * current attachments as standard links (which will open the linked page in a new window/tab) with a 'x' icon to delete
 * the attachment, and a single '+' icon, which opens a modal input where the user can specify the title and URL of the
 * attachment.
 */
public class AttachmentsPresenter extends PresenterWidget<AttachmentsPresenter.MyView>
        implements AttachmentsUiHandlers, AttachmentChangedEvent.AttachmentChangedHandler {
    interface MyView extends View, HasUiHandlers<AttachmentsUiHandlers> {
        void setData(List<Document> data);
    }

    private final RpcDocumentServiceAsync documentService;
    private final AddAttachmentPresenter addAttachmentPresenter;

    private SystemEntity currentEntity;

    @Inject
    AttachmentsPresenter(
            EventBus eventBus,
            MyView view,
            RpcDocumentServiceAsync documentService,
            AddAttachmentPresenter addAttachmentPresenter) {
        super(eventBus, view);

        this.documentService = documentService;
        this.addAttachmentPresenter = addAttachmentPresenter;

        getView().setUiHandlers(this);
    }

    public void initPresenter(SystemEntity currentEntity) {
        this.currentEntity = currentEntity;

        loadAttachements();
    }

    @Override
    public void onAttachmentChanged(AttachmentChangedEvent event) {
        loadAttachements();
    }

    @Override
    public void addAttachement() {
        addAttachmentPresenter.setCurrentEntity(currentEntity);
        addToPopupSlot(addAttachmentPresenter);
    }

    @Override
    public void deleteAttachment(Document documentText) {
        documentService.deleteDocument(documentText.getId(), new ResponseHandler<ActionResponse>() {
            @Override
            public void handleSuccess(ActionResponse result) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Attachment removed successfully.");
                loadAttachements();
            }
        });
    }

    @Override
    protected void onBind() {
        addVisibleHandler(AttachmentChangedEvent.TYPE, this);
    }

    private void loadAttachements() {
        Integer entityId = currentEntity.getId();
        SystemEntityRelation type = SystemEntityRelation.ATTACHMENT;

        documentService.getByEntityAndRelation(entityId, type, new ResponseHandler<DataResponse<Document>>() {
            @Override
            public void handleSuccess(DataResponse<Document> result) {
                if (result.getGlobalErrorMessages().isEmpty()) {
                    List<Document> documentList = new ArrayList<Document>();
                    for (Document document : result.getData()) {
                        documentList.add(document);
                    }

                    getView().setData(documentList);
                }
            }
        });
    }
}
