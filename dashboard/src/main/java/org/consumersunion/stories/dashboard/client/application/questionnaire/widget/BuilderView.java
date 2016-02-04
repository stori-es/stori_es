package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import java.util.Arrays;

import org.consumersunion.stories.dashboard.client.application.ui.BuilderTab;
import org.consumersunion.stories.dashboard.client.application.ui.EnumCell;
import org.consumersunion.stories.dashboard.client.resource.TabsStyle;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class BuilderView extends ViewWithUiHandlers<BuilderUiHandlers>
        implements BuilderPresenter.MyView {
    interface Binder extends UiBinder<Widget, BuilderView> {
    }

    @UiField(provided = true)
    final ValuePicker<BuilderTab> questionnaireTabs;

    @UiField
    SimplePanel tabContent;

    private final CellList<BuilderTab> builderTabCellList;

    @Inject
    BuilderView(
            Binder uiBinder,
            TabsStyle tabsStyle) {
        builderTabCellList = new CellList<BuilderTab>(new EnumCell<BuilderTab>(), tabsStyle);
        questionnaireTabs = new ValuePicker<BuilderTab>(builderTabCellList);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setTabs(BuilderTab... tabs) {
        builderTabCellList.setRowCount(0);
        questionnaireTabs.setAcceptableValues(Arrays.asList(tabs));
        questionnaireTabs.setValue(tabs[0]);
        builderTabCellList.redraw();
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == BuilderPresenter.SLOT_TAB_CONTENT) {
            tabContent.setWidget(content);
        }
    }

    @UiHandler("questionnaireTabs")
    public void onQuestionnaireTabsChanged(ValueChangeEvent<BuilderTab> event) {
        getUiHandlers().onTabChanged(event.getValue());
    }
}
