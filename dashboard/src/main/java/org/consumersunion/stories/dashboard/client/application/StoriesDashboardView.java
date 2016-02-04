package org.consumersunion.stories.dashboard.client.application;

import org.consumersunion.stories.dashboard.client.application.widget.FooterView;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import static com.google.gwt.query.client.GQuery.$;

public class StoriesDashboardView extends ViewImpl implements StoriesDashboardPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoriesDashboardView> {
    }

    @UiField(provided = true)
    final FooterView footer;

    @UiField
    SimplePanel headerContent;
    @UiField
    HTMLPanel mainContent;

    @Inject
    StoriesDashboardView(
            Binder uiBinder,
            FooterView footer) {
        this.footer = footer;
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == StoriesDashboardPresenter.SLOT_MAIN_CONTENT) {
            mainContent.clear();
            if (content != null) {
                content.asWidget().setVisible(true);
                mainContent.add(content);
            }
        } else if (slot == StoriesDashboardPresenter.TYPE_SetHeaderContent) {
            headerContent.setWidget(content);
        }
    }

    @Override
    public void addToSlot(Object slot, IsWidget content) {
        if (slot == StoriesDashboardPresenter.SLOT_MAIN_CONTENT) {
            for (Widget widget : $(mainContent).children().widgets()) {
                widget.setVisible(false);
            }

            content.asWidget().setVisible(true);
            mainContent.add(content);
        }
    }

    @Override
    public void removeFromSlot(Object slot, IsWidget content) {
        if (slot == StoriesDashboardPresenter.SLOT_MAIN_CONTENT) {
            Widget lastContent = $(mainContent).children().last().widget();
            if (lastContent == content.asWidget() && mainContent.getWidgetCount() > 1) {
                mainContent.remove(lastContent);
            }

            Widget widget = $(mainContent).children().last().widget();
            if (widget != null) {
                widget.setVisible(true);
            }
        }
    }
}
