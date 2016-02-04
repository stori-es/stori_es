package org.consumersunion.stories.survey.client.application.error;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ErrorView extends ViewImpl implements ErrorPresenter.MyView {
    interface Binder extends UiBinder<Widget, ErrorView> {
    }

    @UiField
    HTML alert;

    @Inject
    ErrorView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void displayError(String message) {
        alert.setText(message);
        alert.setVisible(true);
    }

    @Override
    public void displayError(SafeHtml safeHtml) {
        alert.setHTML(safeHtml);
        alert.setVisible(true);
    }
}
