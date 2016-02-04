package org.consumersunion.stories.dashboard.client.application.story.widget;

import java.util.List;

import org.consumersunion.stories.common.client.util.WidgetIds;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.application.story.ui.AttachmentCellFactory;
import org.consumersunion.stories.dashboard.client.application.ui.ConfirmationModal;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

/**
 * See {@link AttachmentsPresenter} for information on the widget usage and placement.
 */
public class AttachmentsView extends ViewWithUiHandlers<AttachmentsUiHandlers>
        implements AttachmentsPresenter.MyView {
    interface Binder extends UiBinder<Widget, AttachmentsView> {
    }

    @UiField(provided = true)
    final CellList<Document> attachementsList;

    @UiField
    DivElement addAttachmentWrapper;
    @UiField
    Label addAttachment;

    private final ListDataProvider<Document> dataProvider;

    @Inject
    AttachmentsView(
            Binder uiBinder,
            CommonI18nLabels labels,
            ListDataProvider<Document> dataProvider,
            AttachmentCellFactory attachmentCellFactory) {
        this.dataProvider = dataProvider;
        this.attachementsList = new CellList<Document>(attachmentCellFactory.create(setupRemoveAction()));

        initWidget(uiBinder.createAndBindUi(this));
        dataProvider.addDataDisplay(attachementsList);
        addAttachmentWrapper.setAttribute("data-tooltip", labels.clickToAdd());

        addAttachment.getElement().setId(WidgetIds.ADD_ATTACHMENT);
    }

    @Override
    public void setData(List<Document> data) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(data);
        dataProvider.refresh();
    }

    @UiHandler("addAttachment")
    void onAddAttachementClicked(ClickEvent event) {
        getUiHandlers().addAttachement();
    }

    private ActionCell.Delegate<Document> setupRemoveAction() {
        return new ActionCell.Delegate<Document>() {
            @Override
            public void execute(Document documentText) {
                removeAttachment(documentText);
            }
        };
    }

    private void removeAttachment(final Document documentText) {
        ConfirmationModal confirmationModal = new ConfirmationModal("Confirm Deletion",
                "Are you sure you would like to remove this attachment?") {
            @Override
            protected void handleConfirm() {
                getUiHandlers().deleteAttachment(documentText);
            }
        };
        confirmationModal.center();
    }
}
