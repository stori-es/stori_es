package org.consumersunion.stories.dashboard.client.application.profile.widget;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.dashboard.client.event.ShowPersonProfileEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class NotesManagerPresenter extends PresenterWidget<NotesManagerPresenter.MyView>
        implements NotesManagerUiHandlers, ShowPersonProfileEvent.ShowPersonProfileHandler {
    interface MyView extends View, HasUiHandlers<NotesManagerUiHandlers> {
        void setData(List<Document> data, Organization currentObo);

        void clear();
    }

    private final RpcDocumentServiceAsync documentService;

    private SystemEntity currentEntity;

    @Inject
    NotesManagerPresenter(
            EventBus eventBus,
            MyView view,
            RpcDocumentServiceAsync documentService) {
        super(eventBus, view);

        this.documentService = documentService;

        getView().setUiHandlers(this);
    }

    public void initPresenter(SystemEntity currentEntity) {
        this.currentEntity = currentEntity;

        loadNotes();
    }

    @Override
    public void onShowPersonProfile(ShowPersonProfileEvent event) {
        currentEntity = event.getProfile();
        loadNotes();
    }

    @Override
    public void saveNote(String noteContent) {
        Document note = new Document();
        note.setEntity(currentEntity.getId());
        note.addBlock(new Content(BlockType.CONTENT, noteContent, TextType.PLAIN));
        note.setSystemEntityRelation(SystemEntityRelation.NOTE);

        documentService.createUntitledDocument(note, new ResponseHandlerLoader<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                loadNotes();
            }
        });
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(ShowPersonProfileEvent.TYPE, this);
    }

    private void loadNotes() {
        documentService.getByEntityAndRelation(currentEntity.getId(), SystemEntityRelation.NOTE,
                new ResponseHandlerLoader<DataResponse<Document>>() {
                    @Override
                    public void handleSuccess(DataResponse<Document> result) {
                        List<Document> documentList = new ArrayList<Document>();

                        if (result.getGlobalErrorMessages().size() == 0) {
                            for (Document document : result.getData()) {
                                documentList.add(document);
                            }
                            showNotes(documentList);
                        }
                    }
                });
    }

    private void showNotes(final List<Document> documents) {
        getView().setData(documents, null);
    }
}
