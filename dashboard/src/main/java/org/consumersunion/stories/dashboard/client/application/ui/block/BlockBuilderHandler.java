package org.consumersunion.stories.dashboard.client.application.ui.block;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.ContentBase;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.BlockBuilderPanel;
import org.consumersunion.stories.dashboard.client.resource.BuilderStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class BlockBuilderHandler implements BlockBuilder.Handler {
    public interface SaveHandler {
        void save(List<Block> blocks);
    }

    private final Resources resources;
    private final BlockLabelHelper blockLabelHelper;
    private final SaveHandler saveHandler;

    private boolean isQuestionnaire;
    private BlockBuilderPanel blockBuilderPanel;

    @Inject
    BlockBuilderHandler(
            Resources resources,
            BlockLabelHelper blockLabelHelper,
            @Assisted SaveHandler saveHandler) {
        this.resources = resources;
        this.blockLabelHelper = blockLabelHelper;
        this.saveHandler = saveHandler;
    }

    @Override
    public void setPanel(BlockBuilderPanel blockBuilderPanel) {
        this.blockBuilderPanel = blockBuilderPanel;
    }

    public void setIsQuestionnaire(boolean isQuestionnaire) {
        this.isQuestionnaire = isQuestionnaire;
    }

    @Override
    public void onBeforeEdit(BlockBuilder blockBuilder) {
        for (BlockBuilder builder : blockBuilderPanel.getBuilders()) {
            if (builder.isOnEditMode() && builder != blockBuilder) {
                builder.resetBlockBuilder();
                break;
            }
        }
    }

    @Override
    public void onBlockEdited(BlockBuilder blockBuilder) {
        if (blockBuilder instanceof ContactBlockBuilder) {
            blockBuilderPanel.updateElements();
        }

        saveOrUpdate(blockBuilder);
    }

    @Override
    public void onBlockChanged(BlockBuilder blockBuilder) {
        saveOrUpdate(blockBuilder);
    }

    @Override
    public void onBlockRemoved(final BlockBuilder blockBuilder) {
        blockBuilderPanel.removeBuilder(blockBuilder);
        saveOrUpdate();
        initQuestionnaireBuilder();
    }

    @Override
    public void onBlockDuplicated(BlockBuilder blockBuilder) {
        Block element = blockBuilder.getValue();
        Block duplicatedElement = null;

        if (element instanceof Question) {
            duplicatedElement = (Question) element.clone();
            ((Question) duplicatedElement).setLabel(blockLabelHelper.getUniqueLabel(blockBuilderPanel));
        } else if (element instanceof ContentBase) {
            duplicatedElement = (ContentBase) element.clone();
        }

        List<Block> blocks = blockBuilderPanel.getBlocks();
        blocks.add(duplicatedElement);
        initQuestionnaireBuilder(blocks);
        saveOrUpdate();
    }

    @Override
    public void onBlockMove(BlockBuilder blockBuilder, BlockBuilder.BlockMoveAction action) {
        switch (action) {
            case TOP:
                blockBuilderPanel.moveToTop(blockBuilder);
                break;
            case UP:
                blockBuilderPanel.moveUp(blockBuilder);
                break;
            case DOWN:
                blockBuilderPanel.moveDown(blockBuilder);
                break;
            case BOTTOM:
                blockBuilderPanel.moveToBottom(blockBuilder);
                break;
            default:
                assert false : "Missing case for " + action.name();
        }

        saveOrUpdate(blockBuilder);
    }

    @Override
    public void onBlockAdded(BlockBuilder blockBuilder) {
        Block block = blockBuilder.getValue();
        if (block.getBlockType().isStandard()) {
            saveOrUpdate(blockBuilder);
        } else {
            updateDrawers(blockBuilder);
        }

        blockBuilder.edit();
    }

    private void saveOrUpdate() {
        saveOrUpdate(null);
    }

    private void saveOrUpdate(BlockBuilder blockBuilder) {
        updateDrawers(blockBuilder);
        saveHandler.save(blockBuilderPanel.getBlocks());
    }

    private void updateDrawers(BlockBuilder blockBuilder) {
        BuilderStyle builderStyle = resources.builderStyleCss();
        String metaDrawerStyle = "." + builderStyle.metaDrawer();
        $(metaDrawerStyle + "+" + metaDrawerStyle).remove();

        if (blockBuilder != null) {
            blockBuilderPanel.ensureDrawerAround(blockBuilder);
        }
    }

    private void initQuestionnaireBuilder(List<Block> blocks) {
        blockBuilderPanel.init(blocks, !isQuestionnaire);
    }

    private void initQuestionnaireBuilder() {
        initQuestionnaireBuilder(blockBuilderPanel.getBlocks());
    }
}
