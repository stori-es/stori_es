package org.consumersunion.stories.login.application.resetpassword;

import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.login.application.i18n.LoginMessages;
import org.consumersunion.stories.login.application.ui.TitleWidget;
import org.consumersunion.stories.login.resource.LoginResources;

import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ResetPasswordView extends ViewWithUiHandlers<ResetPasswordUiHandlers>
        implements ResetPasswordPresenter.MyView {
    interface Binder extends UiBinder<Widget, ResetPasswordView> {
    }

    @UiField
    FormElement form;
    @UiField
    InputElement password;
    @UiField
    InputElement confirmPassword;
    @UiField
    TitleWidget title;
    @UiField
    LoginResources res;
    @UiField
    InputElement submit;

    private final LoginMessages loginMessages;

    @Inject
    ResetPasswordView(
            Binder uiBinder,
            LoginMessages loginMessages) {
        this.loginMessages = loginMessages;

        initWidget(uiBinder.createAndBindUi(this));

        GQuery.$(form).submit(new Function() {
            @Override
            public boolean f(Event e) {
                onSubmit();
                return false;
            }
        });
    }

    @Override
    public void clearError() {
        submit.setDisabled(false);
        title.clearError();
        GQuery.$(password, confirmPassword).removeClass(res.loginStyle().inputError());
    }

    @Override
    public void setError(String error) {
        submit.setDisabled(false);
        title.setError(error);
        GQuery.$(password, confirmPassword).addClass(res.loginStyle().inputError());
    }

    private void onSubmit() {
        clearError();

        String password = this.password.getValue();
        String confirmPassword = this.confirmPassword.getValue();
        if (password.length() < ResetPasswordRequest.MIN_LENGTH) {
            setError(loginMessages.invalidPasswordLength());
        } else if (!password.equals(confirmPassword)) {
            setError(loginMessages.passwordsNotMatching());
        } else {
            submit.setDisabled(true);
            getUiHandlers().resetPassword(password, confirmPassword);
        }
    }
}
