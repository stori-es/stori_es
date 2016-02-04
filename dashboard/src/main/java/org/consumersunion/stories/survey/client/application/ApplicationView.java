package org.consumersunion.stories.survey.client.application;

import org.consumersunion.stories.survey.client.gin.Embedded;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    private final String surveyDivId;

    @Inject
    ApplicationView(
            Binder uiBinder,
            @Embedded String surveyDivId) {
        this.surveyDivId = surveyDivId;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (content != null) {
            if (slot == ApplicationPresenter.TYPE_SetMainContent) {
                RootPanel container = RootPanel.get(surveyDivId);
                container.clear();

                if (content != null) {
                    container.add(content);
                }
            }
        }
    }
}
