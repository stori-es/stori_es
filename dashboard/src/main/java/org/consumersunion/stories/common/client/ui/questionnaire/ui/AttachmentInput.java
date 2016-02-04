package org.consumersunion.stories.common.client.ui.questionnaire.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.RpcResourceCheckerServiceAsync;
import org.consumersunion.stories.common.client.util.EventBusWrapper;
import org.consumersunion.stories.common.client.util.URLUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * A component of {@link org.consumersunion.stories.common.client.ui.block.question.AttachmentQuestion};
 * specifically, the the 'input view'. Specifically, a small panel to add
 * new attachments. See {@link org.consumersunion.stories.common.client.ui.questionnaire.ui.AttachmentLink}.
 */
public class AttachmentInput extends Composite implements HasHandlers {
    interface Binder extends UiBinder<Widget, AttachmentInput> {
    }

    @UiField
    TextBox title;
    @UiField
    TextBox url;
    @UiField
    Button addLink;
    @UiField
    HTMLPanel formContainer;
    @UiField
    HTMLPanel linksContainer;
    @UiField
    Label errorMessage;

    private final List<String> links;
    private final Integer maxAttachments;
    private final RpcResourceCheckerServiceAsync resourceCheckerService;
    private final CommonI18nErrorMessages messages;
    private final EventBusWrapper eventBusWrapper;

    @Inject
    AttachmentInput(Binder binder,
            EventBusWrapper eventBusWrapper,
            RpcResourceCheckerServiceAsync resourceCheckerService,
            CommonI18nErrorMessages messages,
            @Assisted Integer maxAttachments) {
        initWidget(binder.createAndBindUi(this));

        this.resourceCheckerService = resourceCheckerService;
        this.messages = messages;
        this.links = new ArrayList<String>();
        this.maxAttachments = maxAttachments;
        this.eventBusWrapper = eventBusWrapper;
    }

    public List<String> getValues() {
        return links;
    }

    public void setValues(List<String> values) {
        clear();
        errorMessage.setVisible(false);

        for (String link : values) {
            addNewLink(link);
        }

        if (links.size() >= maxAttachments) {
            formContainer.setVisible(false);
        } else {
            formContainer.setVisible(true);
        }
    }

    public void clear() {
        links.clear();
        linksContainer.clear();
    }

    public void setEnabled(Boolean enabled) {
        title.setEnabled(enabled);
        url.setEnabled(enabled);
        addLink.setEnabled(enabled);
    }

    @UiHandler("addLink")
    void onAddCliked(ClickEvent event) {
        errorMessage.setVisible(false);

        if (!url.getText().isEmpty()) {
            addNewLink();
        } else {
            errorMessage.setText("You must provide a URL.");
            errorMessage.setVisible(true);
        }
    }

    private void addNewLink() {
        final String urlText = URLUtils.appendDefaultProtocol(url.getText());
        resourceCheckerService.checkURL(urlText, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                showResourceUnavailable();

                HideLoadingEvent.fire(eventBusWrapper);
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    String fullLink = title.getText() + "@" + urlText;
                    addNewLink(fullLink);
                } else {
                    showResourceUnavailable();
                }

                HideLoadingEvent.fire(eventBusWrapper);
            }
        });

        ShowLoadingEvent.fire(eventBusWrapper);
    }

    private void addNewLink(final String fullLink) {
        final org.consumersunion.stories.common.client.ui.questionnaire.ui.AttachmentLink newLink = new org
                .consumersunion.stories.common.client.ui.questionnaire.ui.AttachmentLink(
                fullLink);

        links.add(fullLink);
        linksContainer.add(newLink);

        title.setText("");
        url.setText("");

        if (links.size() == maxAttachments) {
            formContainer.setVisible(false);
        }

        newLink.addRemoveHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Integer index = linksContainer.getWidgetIndex(newLink);
                linksContainer.remove(index);
                links.remove(fullLink);

                if (links.size() < maxAttachments) {
                    formContainer.setVisible(true);
                }
            }
        });
    }

    private void showResourceUnavailable() {
        errorMessage.setText(messages.documentUnavailable());
        errorMessage.setVisible(true);
    }
}
