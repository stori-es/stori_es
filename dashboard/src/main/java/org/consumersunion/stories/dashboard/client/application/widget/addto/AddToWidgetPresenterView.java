package org.consumersunion.stories.dashboard.client.application.widget.addto;

import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class AddToWidgetPresenterView extends ViewWithUiHandlers<AddToWidgetUiHandlers>
        implements AddToWidgetPresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, AddToWidgetPresenterView> {
    }

    private static final int ANIMATION_LENGTH = 350;

    @UiField
    SimplePanel addToCollections;
    @UiField
    SimplePanel addTags;
    @UiField
    SimplePanel addNote;

    private SimplePanel currentPanel;

    @Inject
    AddToWidgetPresenterView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void hideSlot(Object slot) {
        SimplePanel panel = getPanelForSlot(slot);
        $(panel).slideUp(ANIMATION_LENGTH);
        currentPanel = null;
    }

    @Override
    public void showSlot(Object slot) {
        SimplePanel panel = getPanelForSlot(slot);
        toggle(panel);
    }

    @Override
    public void reset() {
        hideWidgetNow(Widget.asWidgetOrNull(addTags));
        hideWidgetNow(Widget.asWidgetOrNull(addToCollections));
        hideWidgetNow(Widget.asWidgetOrNull(addNote));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        SimplePanel panel = getPanelForSlot(slot);

        if (panel != null) {
            panel.setWidget(content);
        }
    }

    private SimplePanel getPanelForSlot(Object slot) {
        if (slot == AddToWidgetPresenter.SLOT_ADD_TO_COLLECTIONS) {
            return addToCollections;
        } else if (slot == AddToWidgetPresenter.SLOT_ADD_TAGS) {
            return addTags;
        } else if (slot == AddToWidgetPresenter.SLOT_ADD_NOTE) {
            return addNote;
        }

        return null;
    }

    private void toggle(final SimplePanel panel) {
        boolean isToggle = currentPanel != panel;

        if (currentPanel != null) {
            $(currentPanel).slideUp(ANIMATION_LENGTH, new Function() {
                @Override
                public void f() {
                    $(panel).slideDown(ANIMATION_LENGTH);
                    getUiHandlers().onDisplay();
                }
            });
        } else {
            $(panel).slideDown(ANIMATION_LENGTH);
            getUiHandlers().onDisplay();
        }

        if (isToggle) {
            currentPanel = panel;
        } else {
            currentPanel = null;
        }
    }

    private void hideWidgetNow(Widget widget) {
        $(widget).slideUp(0);
    }
}
