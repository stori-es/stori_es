package org.consumersunion.stories.dashboard.client.application.widget;

import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.dashboard.client.event.AddNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static com.google.gwt.query.client.GQuery.$;

public class AddDocumentPopup extends Composite {
    public interface Binder extends UiBinder<Widget, AddDocumentPopup> {
    }

    @UiField
    protected Label addContent;

    private final EventBus eventBus;
    private final GeneralStyle generalStyle;

    private boolean addContentEnabled = true;

    @Inject
    protected AddDocumentPopup(
            Binder uiBinder,
            EventBus eventBus,
            Resources resources) {
        initWidget(uiBinder.createAndBindUi(this));

        this.eventBus = eventBus;
        generalStyle = resources.generalStyleCss();
    }

    public void setAddContentEnabled(boolean enabled) {
        addContentEnabled = enabled;
        $(addContent).toggleClass(generalStyle.disabledLink(), !enabled);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (event instanceof AddNewDocumentEvent) {
            eventBus.fireEventFromSource(event, this);
        } else {
            super.fireEvent(event);
        }
    }

    protected SystemEntityRelation getContentEntityRelation() {
        return SystemEntityRelation.BODY;
    }

    @UiHandler("addContent")
    void onAddContentClicked(ClickEvent clickEvent) {
        if (addContentEnabled) {
            AddNewDocumentEvent.fire(this, getContentEntityRelation());
        }
    }

    @UiHandler("addAttachment")
    void onAddAttachmentClicked(ClickEvent clickEvent) {
        AddNewDocumentEvent.fire(this, SystemEntityRelation.ATTACHMENT);
    }

    @UiHandler("addNote")
    void onAddNoteClicked(ClickEvent clickEvent) {
        AddNewDocumentEvent.fire(this, SystemEntityRelation.NOTE);
    }
}
