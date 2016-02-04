package org.consumersunion.stories.login.application.login;

import org.consumersunion.stories.login.application.i18n.LoginMessages;
import org.consumersunion.stories.login.application.ui.TitleWidget;
import org.consumersunion.stories.login.resource.LoginResources;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.http.client.URL;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class LoginView extends ViewWithUiHandlers<LoginUiHandlers> implements LoginPresenter.MyView {
    interface Binder extends UiBinder<Widget, LoginView> {
    }

    @UiField
    ParagraphElement helpSection;
    @UiField
    AnchorElement needHelp;
    @UiField
    TitleWidget title;
    @UiField
    LoginResources res;

    private final LoginMessages loginMessages;
    private final GQuery form;

    @Inject
    LoginView(
            Binder uiBinder,
            LoginMessages loginMessages) {
        this.loginMessages = loginMessages;

        initWidget(uiBinder.createAndBindUi(this));

        form = $("#appLogin").submit(new Function() {
            @Override
            public boolean f(Event e) {
                onSubmit();
                return false;
            }
        });

        form.insertAfter($("div", asWidget()).first());
        form.show();

        $(helpSection).hide();
        $(needHelp).click(new Function() {
            @Override
            public void f() {
                $(this).parent().hide();
                $(helpSection).show();
            }
        });
    }

    private void onSubmit() {
        clearErrors();

        Ajax.Settings settings = createLoginRequest();
        GQuery.ajax(settings);
    }

    private void clearErrors() {
        title.clearError();
        $(form).find("input").removeClass(res.loginStyle().inputError());
    }

    private void setErrors() {
        title.setError(loginMessages.incorrectUsernameEmailOrPassword());
        $(form).find("input").addClass(res.loginStyle().inputError());
    }

    private Ajax.Settings createLoginRequest() {
        // TODO : Use REST-Dispatch when we switch to GWTP 1.5 (and when it's released)
        Ajax.Settings settings = Ajax.createSettings();
        settings.set("cache", false);
        settings.setType("POST");
        settings.setUrl("/j_spring_security_check");
        settings.set("crossDomain", false);
        settings.setHeaders(Properties.create());
        settings.getHeaders().set("x-ajax-call", "true");
        settings.getHeaders().set("renew", "true");

        Properties data = createFormData();
        settings.setData(data);
        settings.setSuccess(new Function() {
            @Override
            public void f() {
                String result = getArgument(0);
                if ("error=true".equals(result)) {
                    setErrors();
                } else {
                    Window.Location.assign(result);
                }
            }
        });

        return settings;
    }

    private Properties createFormData() {
        Properties data = Properties.create();
        GQuery username = form.children().first();
        data.set("j_username", URL.encodeQueryString(username.val()));
        data.set("j_password", URL.encodeQueryString(username.next().val()));

        return data;
    }
}
