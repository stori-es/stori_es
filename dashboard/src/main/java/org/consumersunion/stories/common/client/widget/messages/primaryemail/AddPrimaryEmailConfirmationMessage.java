package org.consumersunion.stories.common.client.widget.messages.primaryemail;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.client.widget.messages.ProgressMessage;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class AddPrimaryEmailConfirmationMessage extends ProgressMessage
        implements InteractiveMessageContent {
    interface Binder extends UiBinder<Widget, AddPrimaryEmailConfirmationMessage> {
    }

    @UiField
    SpanElement email;
    @UiField
    ButtonElement close;

    @Inject
    AddPrimaryEmailConfirmationMessage(
            Binder binder,
            MessageFactory messageFactory,
            EmailUtil emailUtil,
            Provider<ChoosePrimaryEmailMinimizedMessage> minimizedMessagesProvider,
            @Assisted Contact contact) {
        super(binder, minimizedMessagesProvider, messageFactory);

        createAndBindUi();

        updateProgress(1, 1);
        setToDone();

        email.setInnerText(emailUtil.hideEmail(contact.getValue()));

        GQuery.$(close).click(new Function() {
            @Override
            public void f() {
                remove();
            }
        });
    }
}
