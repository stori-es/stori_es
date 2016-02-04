package org.consumersunion.stories.login.application.ui;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.login.resource.LoginResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class TitleWidget extends Widget {
    interface Binder extends UiBinder<DivElement, TitleWidget> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    HeadingElement title;
    @UiField
    DivElement help;
    @UiField
    DivElement errorWrapper;
    @UiField
    SpanElement errorText;
    @UiField
    LoginResources res;
    @UiField
    CommonResources commonRes;

    public TitleWidget() {
        setElement(binder.createAndBindUi(this));
    }

    public void setTitle(String title) {
        this.title.setInnerText(title);
    }

    public void setHelp(String help) {
        this.help.setInnerText(help);
    }

    public void clearError() {
        errorText.setInnerText("");
        errorWrapper.removeClassName(commonRes.generalStyleCss().visible());
    }

    public void setError(String error) {
        errorText.setInnerText(error);
        errorWrapper.addClassName(commonRes.generalStyleCss().visible());
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}
