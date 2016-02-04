package org.consumersunion.stories.dashboard.client.application.widget.content;

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

public class ResponseBuilderView extends ViewWithUiHandlers<ResponseBuilderUiHandlers>
        implements ResponseBuilderPresenter.MyView {
    interface Binder extends UiBinder<Widget, ResponseBuilderView> {
    }

    @UiField(provided = true)
    final ValuePicker<BuilderTab> tabs;

    @UiField
    SimplePanel tabContent;

    private final CellList<BuilderTab> builderTabCellList;

    @Inject
    ResponseBuilderView(
            Binder uiBinder,
            TabsStyle tabsStyle) {
        builderTabCellList = new CellList<BuilderTab>(new EnumCell<BuilderTab>(), tabsStyle);
        tabs = new ValuePicker<BuilderTab>(builderTabCellList);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setTabs(BuilderTab... tabs) {
        builderTabCellList.setRowCount(0);
        this.tabs.setAcceptableValues(Arrays.asList(tabs));
        this.tabs.setValue(tabs[0]);
        builderTabCellList.redraw();
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ResponseBuilderPresenter.SLOT_TAB_CONTENT) {
            tabContent.setWidget(content);
        }
    }

    @UiHandler("tabs")
    public void onTabsChanged(ValueChangeEvent<BuilderTab> event) {
        getUiHandlers().onTabChanged(event.getValue());
    }
}
