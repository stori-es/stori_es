package org.consumersunion.stories.login.application.forgotpassword;

import org.consumersunion.stories.login.application.i18n.LoginMessages;
import org.consumersunion.stories.login.application.ui.TitleWidget;
import org.consumersunion.stories.login.resource.LoginResources;

import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class ForgotPasswordView extends ViewWithUiHandlers<ForgotPasswordUiHandlers>
        implements ForgotPasswordPresenter.MyView {
    interface Binder extends UiBinder<Widget, ForgotPasswordView> {
    }

    @UiField
    FormElement form;
    @UiField
    InputElement submit;
    @UiField
    InputElement username;
    @UiField
    TitleWidget title;
    @UiField
    LoginResources res;

    private final LoginMessages loginMessages;

    @Inject
    ForgotPasswordView(
            Binder uiBinder,
            LoginMessages loginMessages) {
        this.loginMessages = loginMessages;

        initWidget(uiBinder.createAndBindUi(this));

        $(form).submit(new Function() {
            @Override
            public boolean f(Event e) {
                onSubmit();
                return false;
            }
        });
    }

    @Override
    public void setError(String error) {
        submit.setDisabled(false);
        title.setError(error);
        $(username).addClass(res.loginStyle().inputError());
    }

    @Override
    public void clearError() {
        submit.setDisabled(false);
        title.clearError();
        $(username).removeClass(res.loginStyle().inputError());
    }

    private void onSubmit() {
        clearError();

        String username = this.username.getValue();
        if (username.isEmpty()) {
            setError(loginMessages.incorrectUsernameOrEmail());
        } else {
            submit.setDisabled(true);
            getUiHandlers().resetPassword(username);
        }
    }
}
