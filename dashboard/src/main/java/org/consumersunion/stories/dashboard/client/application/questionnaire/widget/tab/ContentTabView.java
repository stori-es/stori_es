package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.BlockBuilderPanel;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.BlockBuildersPanelFactory;
import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilderHandler;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ContentTabView extends ViewWithUiHandlers<ContentTabUiHandlers>
        implements ContentTabPresenter.MyView, BlockBuilderHandler.SaveHandler {
    interface Binder extends UiBinder<Widget, ContentTabView> {
    }

    private static final int INITIAL_VERSION = 1;

    @UiField(provided = true)
    final BlockBuilderPanel blockBuilderPanel;

    @UiField
    HTMLPanel themePanel;
    @UiField
    ListBox themeList;
    @UiField
    Button preview;
    @UiField
    InlineLabel selectedTheme;
    @UiField
    Label publishedState;
    @UiField
    Resources res;

    private final PermalinkUtil permalinkUtil;
    private final GeneralStyle generalStyle;
    private final StoryTellerDashboardI18nLabels storyLabels;
    private final BlockBuilderHandler blockBuilderHandler;

    private boolean readOnly;
    private String permalinkUrl;
    private boolean isQuestionnaire;

    @Inject
    ContentTabView(
            Binder uiBinder,
            Resources resources,
            PermalinkUtil permalinkUtil,
            StoryTellerDashboardI18nLabels storyLabels,
            BlockBuildersPanelFactory blockBuilderPanelFactory) {
        this.permalinkUtil = permalinkUtil;
        this.storyLabels = storyLabels;
        generalStyle = resources.generalStyleCss();

        blockBuilderHandler = blockBuilderPanelFactory.createHandler(this);
        blockBuilderPanel = blockBuilderPanelFactory.create(blockBuilderHandler);
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setContent(Collection collection, boolean readOnly) {
        this.readOnly = readOnly;
        themeList.setEnabled(true);
        blockBuilderPanel.setReadOnly(readOnly);

        refreshView(collection, readOnly);

        blockBuilderPanel.init(collection.getBlocks(), !isQuestionnaire);

        if (collection.getVersion() == INITIAL_VERSION) {
            blockBuilderPanel.openDefaultDrawer();
        }
    }

    @Override
    public void setThemeList(List<Theme> themes) {
        themeList.clear();
        if (themes != null) {
            for (Theme item : themes) {
                themeList.addItem(item.getName(), Integer.toString(item.getId()));
            }
        }
    }

    @Override
    public void save(List<Block> blocks) {
        getUiHandlers().save(blocks);
    }

    @Override
    public void setSelectedTheme(int themeId) {
        for (int i = 0; i < themeList.getItemCount(); i++) {
            int value = Integer.parseInt(themeList.getValue(i));
            if (themeId == value) {
                themeList.setSelectedIndex(i);
                break;
            }
        }

        selectedTheme.setText(themeList.getItemText(themeList.getSelectedIndex()));
    }

    @Override
    public void refreshView(Collection collection, boolean readOnly) {
        if (this.readOnly != readOnly) {
            setContent(collection, readOnly);
        }

        themeList.setVisible(!readOnly);
        selectedTheme.setVisible(readOnly);

        publishedState.setText(readOnly ? storyLabels.published() : storyLabels.draft());
        GQuery.$(publishedState).toggleClass(generalStyle.draftState(), !readOnly);

        isQuestionnaire = isQuestionnaire(collection);
        blockBuilderHandler.setIsQuestionnaire(isQuestionnaire);
        permalinkUrl = permalinkUtil.getPermalink(collection.getPermalink(), collection.getPreviewKey());
    }

    @UiHandler("themeList")
    void onThemeChanged(ChangeEvent event) {
        int selectedItem = themeList.getSelectedIndex();
        int themeId = Integer.parseInt(themeList.getValue(selectedItem));

        getUiHandlers().updateTheme(themeId);
    }

    @UiHandler("preview")
    void onPreviewClicked(ClickEvent event) {
        permalinkUtil.openPermalink(permalinkUrl, isQuestionnaire);
    }

    private boolean isQuestionnaire(Collection collection) {
        return collection.isQuestionnaire() && collection instanceof QuestionnaireI15d;
    }
}
