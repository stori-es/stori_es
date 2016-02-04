package org.consumersunion.stories.dashboard.client.application.widget.content;

import java.util.Arrays;
import java.util.List;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.BlockBuilderPanel;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.BlockBuildersPanelFactory;
import org.consumersunion.stories.dashboard.client.application.ui.BuilderTab;
import org.consumersunion.stories.dashboard.client.application.ui.EnumCell;
import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilderHandler;
import org.consumersunion.stories.dashboard.client.resource.TabsStyle;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ValuePicker;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ContentDetailView extends ViewWithUiHandlers<ContentDetailUiHandlers>
        implements ContentDetailPresenter.MyView, BlockBuilderHandler.SaveHandler {
    interface Binder extends UiBinder<Widget, ContentDetailView> {
    }

    @UiField(provided = true)
    final ValuePicker<BuilderTab> tabs;

    @UiField
    SimplePanel content;

    private final CellList<BuilderTab> builderTabCellList;
    private final BlockBuilderPanel blockBuilderPanel;

    @Inject
    ContentDetailView(
            Binder uiBinder,
            TabsStyle tabsStyle,
            BlockBuildersPanelFactory factory) {
        builderTabCellList = new CellList<BuilderTab>(new EnumCell<BuilderTab>(), tabsStyle);
        tabs = new ValuePicker<BuilderTab>(builderTabCellList);

        initWidget(uiBinder.createAndBindUi(this));

        BlockBuilderHandler blockBuilderHandler = factory.createHandler(this);
        blockBuilderPanel = factory.create(blockBuilderHandler);
        content.setWidget(blockBuilderPanel);

        setTabs(BuilderTab.CONTENT);
    }

    @Override
    public void save(List<Block> blocks) {
        getUiHandlers().save(blocks);
    }

    @Override
    public void setContent(Document document) {
        blockBuilderPanel.init(document.getBlocks(), true);
    }

    private void setTabs(BuilderTab... tabs) {
        builderTabCellList.setRowCount(0);
        this.tabs.setAcceptableValues(Arrays.asList(tabs));
        this.tabs.setValue(tabs[0]);
        builderTabCellList.redraw();
    }
}
