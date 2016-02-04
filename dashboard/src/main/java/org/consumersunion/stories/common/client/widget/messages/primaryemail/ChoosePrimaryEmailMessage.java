package org.consumersunion.stories.common.client.widget.messages.primaryemail;

import java.util.List;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.api.AccountService;
import org.consumersunion.stories.common.client.api.RestCallbackAdapter;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.client.widget.RadioButtonGroup;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.client.widget.messages.ProgressMessage;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.dispatch.rest.shared.RestAction;

import static com.google.gwt.query.client.GQuery.$;

public class ChoosePrimaryEmailMessage extends ProgressMessage
        implements InteractiveMessageContent, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, ChoosePrimaryEmailMessage> {
    }

    @UiField(provided = true)
    final RadioButtonGroup<Contact> contactRadioButtonGroup;
    @UiField
    SpanElement addEmail;
    @UiField
    Button save;

    private final RestDispatch restDispatch;
    private final AccountService accountService;
    private final MessageFactory messageFactory;

    @Inject
    ChoosePrimaryEmailMessage(
            Binder binder,
            RestDispatch restDispatch,
            AccountService accountService,
            final MessageFactory messageFactory,
            final EmailUtil emailUtil,
            Provider<ChoosePrimaryEmailMinimizedMessage> minimizedMessagesProvider,
            @Assisted List<Contact> contacts) {
        super(binder, minimizedMessagesProvider, messageFactory);

        this.restDispatch = restDispatch;
        this.accountService = accountService;
        this.messageFactory = messageFactory;

        contactRadioButtonGroup = new RadioButtonGroup<Contact>(new AbstractRenderer<Contact>() {
            @Override
            public String render(Contact contact) {
                return emailUtil.hideEmail(contact.getValue());
            }
        }, "primary-email");

        if (!contacts.isEmpty()) {
            contactRadioButtonGroup.add(contacts);
            contactRadioButtonGroup.setValue(contacts.get(0));
        }

        createAndBindUi();

        if (contacts.isEmpty()) {
            save.removeFromParent();
        }

        updateProgress(1, 2);
        asWidget().addAttachHandler(this);
        $(addEmail).click(new Function() {
            @Override
            public void f() {
                AddPrimaryEmailMessage addPrimaryEmail = messageFactory.createAddPrimaryEmail();
                addPrimaryEmail.setContainer(container);
                Widget message = addPrimaryEmail.asWidget();

                replaceWith(message);
            }
        });
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            $("#messages").height($(this).height() + 50);
        }
    }

    @UiHandler("save")
    void onSaveClicked(ClickEvent event) {
        save.setEnabled(false);

        final Contact contact = contactRadioButtonGroup.getValue();
        RestAction<Void> action = accountService.setPrimaryEmail(contact);
        restDispatch.execute(action, new RestCallbackAdapter<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                messageDispatcher.displayMessage(MessageStyle.ERROR, caught.getMessage());
            }

            @Override
            public void setResponse(Response response) {
                save.setEnabled(true);
            }

            @Override
            public void onSuccess(Void result) {
                Widget message = messageFactory.createPrimaryEmailChosen(contact).asWidget();

                replaceWith(message);
            }
        });
    }
}
