package org.consumersunion.stories.dashboard.client.application.administration;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AdministrationView extends ViewImpl implements AdministrationPresenter.MyView {
    interface Binder extends UiBinder<Widget, AdministrationView> {
    }

    @UiField
    SimplePanel mainContent;

    @Inject
    AdministrationView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (content != null) {
            mainContent.setWidget(content);
        }
    }
}
