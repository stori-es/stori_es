package org.consumersunion.stories.login.application.resetpassword.emailsent;

import org.consumersunion.stories.login.application.ui.TitleWidget;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class PasswordChangedEmailSentView extends ViewWithUiHandlers<PasswordChangedEmailSentUiHandler>
        implements PasswordChangedEmailSentPresenter.MyView {
    interface Binder extends UiBinder<Widget, PasswordChangedEmailSentView> {
    }

    @UiField
    InputElement signin;
    @UiField
    TitleWidget title;

    @Inject
    PasswordChangedEmailSentView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        $(signin).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().signIn();
            }
        });

        $(title).children().last().remove();
    }
}
