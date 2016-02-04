package org.consumersunion.stories.common.client.widget.messages.primaryemail;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.api.AccountService;
import org.consumersunion.stories.common.client.api.RestCallbackAdapter;
import org.consumersunion.stories.common.client.ui.form.validators.EmailValidator;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.client.widget.messages.ProgressMessage;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rest.client.RestDispatch;
import com.gwtplatform.dispatch.rest.shared.RestAction;

import static com.google.gwt.query.client.GQuery.$;

public class AddPrimaryEmailMessage extends ProgressMessage
        implements InteractiveMessageContent, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, AddPrimaryEmailMessage> {
    }

    @UiField
    InputElement primaryEmail;
    @UiField
    ButtonElement confirm;
    @UiField
    DivElement errorWrapper;
    @UiField
    SpanElement errorText;

    private final RestDispatch restDispatch;
    private final AccountService accountService;
    private final CommonI18nLabels labels;
    private final EmailValidator emailValidator;
    private final MessageFactory messageFactory;

    @Inject
    AddPrimaryEmailMessage(
            Binder binder,
            RestDispatch restDispatch,
            AccountService accountService,
            CommonI18nLabels labels,
            EmailValidator emailValidator,
            MessageFactory messageFactory,
            Provider<ChoosePrimaryEmailMinimizedMessage> minimizedMessagesProvider) {
        super(binder, minimizedMessagesProvider, messageFactory);

        this.restDispatch = restDispatch;
        this.accountService = accountService;
        this.labels = labels;
        this.emailValidator = emailValidator;
        this.messageFactory = messageFactory;

        createAndBindUi();

        primaryEmail.setAttribute("placeholder", labels.enterPrimaryEmail());
        updateProgress(1, 2);

        asWidget().addAttachHandler(this);
        $(confirm).click(new Function() {
            @Override
            public void f() {
                onConfirmClicked();
            }
        });
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            $("#messages").height($(this).height() + 16);
        }
    }

    private void onConfirmClicked() {
        if (!validateEmail()) {
            setError(labels.couldNotRecognizeEmail());
            return;
        }

        confirm.setDisabled(true);

        final Contact contact = new Contact();
        contact.setValue(primaryEmail.getValue());

        RestAction<Void> action = accountService.setPrimaryEmail(contact);
        restDispatch.execute(action, new RestCallbackAdapter<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                messageDispatcher.displayMessage(MessageStyle.ERROR, caught.getMessage());
            }

            @Override
            public void setResponse(Response response) {
                confirm.setDisabled(false);
            }

            @Override
            public void onSuccess(Void result) {
                Widget message = messageFactory.createAddPrimaryEmailConfirmation(contact).asWidget();

                replaceWith(message);
            }
        });
    }

    private boolean validateEmail() {
        clearError();

        return emailValidator.isEmail(primaryEmail.getValue());
    }

    private void clearError() {
        errorText.setInnerText("");
        errorWrapper.removeClassName(resources.generalStyleCss().visible());
    }

    private void setError(String error) {
        errorText.setInnerText(error);
        errorWrapper.addClassName(resources.generalStyleCss().visible());
    }
}
