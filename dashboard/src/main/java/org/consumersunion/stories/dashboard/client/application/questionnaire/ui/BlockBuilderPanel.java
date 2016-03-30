package org.consumersunion.stories.dashboard.client.application.questionnaire.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.HasBlocks;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.type.ContactType;
import org.consumersunion.stories.common.shared.model.type.DataType;
import org.consumersunion.stories.dashboard.client.application.ui.ScrollToAnimation;
import org.consumersunion.stories.dashboard.client.application.ui.SortableDragAndDropHandler;
import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilder;
import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilderFactory;
import org.consumersunion.stories.dashboard.client.application.ui.block.ContactBlockBuilder;
import org.consumersunion.stories.dashboard.client.application.ui.block.MetaBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.MetaBlockHandler;
import org.consumersunion.stories.dashboard.client.application.ui.builder.MetaBlockDrawer;
import org.consumersunion.stories.dashboard.client.application.ui.builder.MetaBlockFactory;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;

import gwtquery.plugins.droppable.client.DroppableOptions;
import gwtquery.plugins.droppable.client.events.DragAndDropContext;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

import static com.google.gwt.query.client.GQuery.$;

public class BlockBuilderPanel extends DroppableWidget<FlowPanel>
        implements MetaBlockHandler, HasBlocks {
    private static final int ANIMATION_DURATION = 400;

    private final Resources resources;
    private final BlockBuilderFactory blockBuilderFactory;
    private final MetaBlockFactory metaBlockFactory;
    private final BlockBuilder.Handler handler;

    private FlowPanel innerPanel;
    private boolean readOnly;
    private Multimap<BlockType, BlockBuilder> standardElements;
    private boolean contentOnly;
    private HandlerRegistration openDefaultDrawerHandler;
    private LinkedHashSet<ContactType> emailOptions;
    private LinkedHashSet<ContactType> phoneOptions;

    @Inject
    BlockBuilderPanel(
            Resources resources,
            BlockBuilderFactory blockBuilderFactory,
            MetaBlockFactory metaBlockFactory,
            @Assisted BlockBuilder.Handler handler) {
        this.resources = resources;
        this.blockBuilderFactory = blockBuilderFactory;
        this.metaBlockFactory = metaBlockFactory;
        this.handler = handler;

        init();
        initWidget(innerPanel);
        setupDrop();
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void init(List<Block> elements, boolean contentOnly) {
        this.contentOnly = contentOnly;

        standardElements = LinkedListMultimap.create();
        emailOptions = new LinkedHashSet<ContactType>();
        phoneOptions = new LinkedHashSet<ContactType>();
        innerPanel.clear();

        fillContactTypes(elements);

        for (Block element : elements) {
            maybeAddDrawer();
            BlockBuilder blockBuilder = createBlockBuilder(element);
            if (blockBuilder != null) {
                add(blockBuilder);
            }
        }

        maybeAddDrawer();

        updateBlocksMove();
    }

    @Override
    public List<Block> getBlocks() {
        List<BlockBuilder> builders = getBuilders();
        List<Block> values = new ArrayList<Block>();

        for (BlockBuilder builder : builders) {
            values.add(builder.getValue());
        }
        return values;
    }

    @Override
    public void setBlocks(List<Block> blocks) {
        // No-op
    }

    public List<BlockBuilder> getBuilders() {
        return $(innerPanel).children().widgets(BlockBuilder.class);
    }

    @Override
    public void replace(Widget toReplace, Widget newWidget) {
        doReplace(toReplace, newWidget);
    }

    @Override
    public void setupMetaBlock(MetaBlock metaBlock) {
        if (!contentOnly) {
            metaBlock.setupQuestionBlocks();
            metaBlock.setupQuestionBank(standardElements);
        }
    }

    @Override
    public void createBlockAndReplace(BlockType blockType, MetaBlock metaBlock) {
        BlockBuilder blockBuilder;
        if (blockType.isStandard()) {
            blockBuilder =
                    blockBuilderFactory.createNewStandardBlock(this, standardElements, blockType, readOnly);
        } else {
            blockBuilder =
                    blockBuilderFactory.createNewCustomBlock(this, blockType, readOnly);
        }

        Block block = blockBuilder.getValue();
        fillContactType(block);
        processBlockBuilder(block, blockBuilder);

        replace(metaBlock, blockBuilder);

        handler.onBlockAdded(blockBuilder);
        updateBlocksMove();
    }

    public void openDefaultDrawer() {
        if (innerPanel.getWidgetCount() > 2) {
            final Widget widget = innerPanel.getWidget(2);
            if (widget instanceof MetaBlockDrawer) {
                openDefaultDrawerHandler = widget.addAttachHandler(new AttachEvent.Handler() {
                    @Override
                    public void onAttachOrDetach(AttachEvent event) {
                        $(widget).click();
                    }
                });
            }
        }
    }

    public void ensureDrawerAround(BlockBuilder beforeBlock) {
        int index = innerPanel.getWidgetIndex(beforeBlock);
        if (index != -1) {
            if (index == 0) {
                if (!isDrawer(index + 1)) {
                    innerPanel.insert(createDrawer(), index + 1);
                }
                innerPanel.insert(createDrawer(), 0);
            } else if (index == innerPanel.getWidgetCount() - 1) {
                maybeAddDrawer();
                if (!isDrawer(index - 1)) {
                    innerPanel.insert(createDrawer(), index);
                }
            } else {
                if (!isDrawer(index - 1)) {
                    innerPanel.insert(createDrawer(), index);
                } else if (!isDrawer(index + 1)) {
                    innerPanel.insert(createDrawer(), index + 1);
                }
            }
        }
    }

    public void removeBuilder(BlockBuilder blockBuilder) {
        innerPanel.remove(blockBuilder);
    }

    public void clear() {
        innerPanel.clear();
    }

    public void moveToTop(BlockBuilder blockBuilder) {
        moveTo(blockBuilder, 1);
    }

    public void moveUp(BlockBuilder blockBuilder) {
        int index = innerPanel.getWidgetIndex(blockBuilder);
        moveTo(blockBuilder, index - 2);
    }

    public void moveDown(BlockBuilder blockBuilder) {
        int index = innerPanel.getWidgetIndex(blockBuilder);
        moveTo(blockBuilder, index + 2);
    }

    public void moveToBottom(BlockBuilder blockBuilder) {
        moveTo(blockBuilder, innerPanel.getWidgetCount() - 2);
    }

    public String getUniqueLabel() {
        return null;
    }

    public void updateElements() {
        standardElements = LinkedListMultimap.create();
        emailOptions = new LinkedHashSet<ContactType>();
        phoneOptions = new LinkedHashSet<ContactType>();

        fillContactTypes(getBlocks());
        for (BlockBuilder blockBuilder : getBuilders()) {
            updateElement(blockBuilder, blockBuilder.getValue());
        }
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        updateBlocksMove();

        if (openDefaultDrawerHandler != null) {
            openDefaultDrawerHandler.removeHandler();
        }
    }

    private BlockBuilder createBlockBuilder(Block element) {
        BlockBuilder blockBuilder = blockBuilderFactory.create(element, readOnly);

        processBlockBuilder(element, blockBuilder);

        return blockBuilder;
    }

    private void processBlockBuilder(Block element, BlockBuilder blockBuilder) {
        if (blockBuilder != null) {
            updateElement(blockBuilder, element);

            blockBuilder.setHandler(handler);
        }
    }

    private void updateElement(BlockBuilder blockBuilder, Block element) {
        BlockType blockType = element.getBlockType();
        standardElements.put(blockType, blockBuilder);

        if (blockBuilder instanceof ContactBlockBuilder) {
            ContactBlockBuilder contactBlockBuilder = (ContactBlockBuilder) blockBuilder;

            LinkedHashSet<ContactType> options;
            if (isEmail((ContactBlock) element)) {
                options = Sets.newLinkedHashSet(ContactType.getEmailValues());
                options.removeAll(emailOptions);
            } else {
                options = Sets.newLinkedHashSet(Arrays.asList(ContactType.values()));
                options.removeAll(phoneOptions);
            }

            options.add(ContactType.OTHER);
            contactBlockBuilder.setOptions(options);
        }
    }

    private void doReplace(Widget toReplace, IsWidget newWidget) {
        innerPanel.insert(newWidget, innerPanel.getWidgetIndex(toReplace));
        innerPanel.remove(toReplace);
    }

    private boolean isDrawer(int index) {
        Element element = $(innerPanel).children().get(index);
        return element != null && (element.hasClassName(resources.builderStyleCss().metaDrawer())
                || element.hasClassName(resources.builderStyleCss().metaBlock()));
    }

    private void init() {
        innerPanel = new FlowPanel();
        handler.setPanel(this);
    }

    private void setupDrop() {
        SortableDragAndDropHandler sortableHandler = new SortableDragAndDropHandler(innerPanel, 1);
        addDropHandler(sortableHandler);
        addDropHandler(new DropEvent.DropEventHandler() {
            @Override
            public void onDrop(DropEvent dropEvent) {
                updateBlocksMove();
            }
        });
        addOutDroppableHandler(sortableHandler);
        addOverDroppableHandler(sortableHandler);
        setAccept(new DroppableOptions.AcceptFunction() {
            @Override
            public boolean acceptDrop(DragAndDropContext context) {
                return context.getDraggableWidget() instanceof BlockBuilder;
            }
        });
    }

    private void moveTo(BlockBuilder blockBuilder, int index) {
        blockBuilder.removeFromParent();
        innerPanel.insert(blockBuilder, index);
        scrollTo(blockBuilder);

        updateBlocksMove();
    }

    private void updateBlocksMove() {
        List<BlockBuilder> widgets = $(innerPanel).children().widgets(BlockBuilder.class);

        if (!widgets.isEmpty()) {
            int maxIndex = widgets.size() - 1;

            if (maxIndex > 0) {
                widgets.get(0).allowDownOnly();
                widgets.get(maxIndex).allowUpOnly();
                for (int i = 1; i < maxIndex; i++) {
                    widgets.get(i).allowAll();
                }
            } else {
                widgets.get(0).disableMove();
            }
        }
    }

    private void scrollTo(final BlockBuilder blockBuilder) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                $("body").each(new Function() {
                    @Override
                    public void f(Element element) {
                        new ScrollToAnimation(element, blockBuilder).run(ANIMATION_DURATION);
                    }
                });
            }
        });
    }

    private void add(IsWidget widget) {
        innerPanel.add(widget);
    }

    private void maybeAddDrawer() {
        if (!readOnly) {
            add(createDrawer());
        }
    }

    private IsWidget createDrawer() {
        return metaBlockFactory.createDrawer(this);
    }

    private void fillContactTypes(List<Block> elements) {
        for (Block element : elements) {
            fillContactType(element);
        }
    }

    private void fillContactType(Block element) {
        if (element instanceof ContactBlock) {
            ContactBlock contactBlock = (ContactBlock) element;
            if (isEmail(contactBlock)) {
                emailOptions.add(ContactType.valueOfCode(contactBlock.getOption()));
            } else if (isPhone(contactBlock)) {
                phoneOptions.add(ContactType.valueOfCode(contactBlock.getOption()));
            }
        }
    }

    private boolean isPhone(ContactBlock contactBlock) {
        return DataType.DATA_PHONE_NUMBER.code().equals(contactBlock.getDataType());
    }

    private boolean isEmail(ContactBlock contactBlock) {
        return DataType.DATA_EMAIL.code().equals(contactBlock.getDataType());
    }
}
