package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.dashboard.client.application.questionnaire.ui.BlockBuilderPanel;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Predicate;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import gwtquery.plugins.draggable.client.events.BeforeDragStartEvent;
import gwtquery.plugins.draggable.client.events.BeforeDragStartEvent.BeforeDragStartEventHandler;
import gwtquery.plugins.draggable.client.events.DragStopEvent;
import gwtquery.plugins.draggable.client.events.DragStopEvent.DragStopEventHandler;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;

import static com.google.gwt.query.client.GQuery.$;

public abstract class BlockBuilder extends DraggableWidget<Widget> {
    interface Binder extends UiBinder<Widget, BlockBuilder> {
    }

    public enum BlockMoveAction {
        TOP,
        UP,
        DOWN,
        BOTTOM
    }

    public interface Handler {
        void setPanel(BlockBuilderPanel blockBuilderPanel);

        void onBeforeEdit(BlockBuilder blockBuilder);

        void onBlockEdited(BlockBuilder blockBuilder);

        void onBlockChanged(BlockBuilder blockBuilder);

        void onBlockRemoved(BlockBuilder blockBuilder);

        void onBlockDuplicated(BlockBuilder blockBuilder);

        void onBlockMove(BlockBuilder blockBuilder, BlockMoveAction action);

        void onBlockAdded(BlockBuilder blockBuilder);
    }

    private class DraggablePositionHandler implements BeforeDragStartEventHandler, DragStopEventHandler {
        public void onBeforeDragStart(BeforeDragStartEvent event) {
            Element draggable = event.getDraggable();
            $(draggable).width($(draggable).parent().width());
            $(draggable).css("position", "absolute");
        }

        public void onDragStop(DragStopEvent event) {
            $(event.getDraggable())
                    .css("position", "relative")
                    .css("top", null)
                    .css("left", null)
                    .css("width", null)
                    .css("z-index", "auto");
            handler.onBlockChanged(BlockBuilder.this);
        }
    }

    protected static final Integer PREVIEW = 0;
    protected static final Integer EDITION = 1;

    @UiField
    HTMLPanel block;
    @UiField
    SimplePanel switcher;
    @UiField
    DivElement toolbar;
    @UiField
    SpanElement duplicate;
    @UiField
    SpanElement remove;
    @UiField
    HeadingElement questionType;
    @UiField
    Resources resources;
    @UiField
    SpanElement edit;
    @UiField
    Element moveToTop;
    @UiField
    Element moveUp;
    @UiField
    Element moveDown;
    @UiField
    Element moveToBottom;

    protected Handler handler;

    private final HasValidation editView;
    private final DraggablePositionHandler HANDLER = new DraggablePositionHandler();

    private IsWidget previewView;
    private boolean showDuplicate;

    protected BlockBuilder(
            Binder uiBinder,
            IsWidget previewView,
            HasValidation editView,
            boolean editMode,
            boolean readOnly) {
        this.previewView = previewView;
        this.editView = editView;

        initWidget(uiBinder.createAndBindUi(this));

        setDisabledDrag(readOnly);
        setup();

        if (!readOnly) {
            switchTo(editMode ? EDITION : PREVIEW);
            block.setStyleName(resources.builderStyleCss().blockWrapper());
        } else {
            switchTo(PREVIEW);
            block.setStyleName(resources.builderStyleCss().blockWrapperReadOnly());
        }

        // Delays the init after the parent class is fully created
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                questionType.setInnerText(getValue().getBlockType().label());
            }
        });

        bind();
    }

    public void edit() {
        switchTo(EDITION);
    }

    public void allowDownOnly() {
        $(Lists.newArrayList(moveToTop, moveUp)).addClass(resources.builderStyleCss().disabled());
        $(Lists.newArrayList(moveToBottom, moveDown)).removeClass(resources.builderStyleCss().disabled());
    }

    public void allowUpOnly() {
        $(Lists.newArrayList(moveToBottom, moveDown)).addClass(resources.builderStyleCss().disabled());
        $(Lists.newArrayList(moveToTop, moveUp)).removeClass(resources.builderStyleCss().disabled());
    }

    public void allowAll() {
        $(Lists.newArrayList(moveToTop, moveUp, moveToBottom, moveDown))
                .removeClass(resources.builderStyleCss().disabled());
    }

    public void disableMove() {
        $(Lists.newArrayList(moveToTop, moveUp, moveToBottom, moveDown))
                .addClass(resources.builderStyleCss().disabled());
    }

    public abstract Block getValue();

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Boolean isOnEditMode() {
        return switcher.getWidget() == editView;
    }

    public void resetBlockBuilder() {
        switchTo(PREVIEW);
    }

    protected void switchTo(final Integer mode) {
        if (EDITION.equals(mode)) {
            switcher.setWidget(editView);
            setDisabledDrag(false);

            if (handler != null) {
                handler.onBeforeEdit(this);
            }

            $(questionType).css("display", "inline-block");
            showOrHideElement(duplicate, !editView.isNew() && showDuplicate);
        } else if (PREVIEW.equals(mode)) {
            switcher.setWidget(previewView);
            setDisabledDrag(true);

            showOrHideElement(duplicate, false);
            $(questionType).css("display", "none");
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                $(block).toggleClass(resources.builderStyleCss().blockWrapperEdit(), EDITION.equals(mode));
                $(questionType).parent().width("calc(100% - " + getToolbarWidth() + "px)");
            }
        });
    }

    protected void setShowDuplicate(boolean showDuplicate) {
        this.showDuplicate = showDuplicate;
    }

    protected void showRemove(boolean showRemove) {
        showOrHideElement(remove, showRemove);
    }

    protected void setPreviewView(IsWidget previewView) {
        this.previewView = previewView;
    }

    private int getToolbarWidth() {
        return ($(toolbar).children().filter(new Predicate() {
            @Override
            public boolean f(Element e, int index) {
                return !e.getStyle().getDisplay().equalsIgnoreCase(Style.Display.NONE.getCssName());
            }
        }).length() - 1) * 28;
    }

    private void showOrHideElement(Element element, boolean show) {
        if (show) {
            $(element).show();
        } else {
            $(element).hide();
        }
    }

    private void bind() {
        block.sinkEvents(Event.ONCLICK);
        block.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (canEdit($(block)) && isValidClickTarget($(event.getNativeEvent().getEventTarget()))) {
                    switchTo(EDITION);
                }
            }
        }, ClickEvent.getType());

        $(duplicate).click(new Function() {
            @Override
            public void f() {
                handler.onBlockDuplicated(BlockBuilder.this);
            }
        });

        $(remove).click(new Function() {
            @Override
            public void f() {
                handler.onBlockRemoved(BlockBuilder.this);
            }
        });

        bindMove(moveToTop, BlockMoveAction.TOP);
        bindMove(moveUp, BlockMoveAction.UP);
        bindMove(moveDown, BlockMoveAction.DOWN);
        bindMove(moveToBottom, BlockMoveAction.BOTTOM);
    }

    private boolean canEdit(GQuery block) {
        return !block.hasClass(resources.builderStyleCss().blockWrapperEdit())
                && !block.hasClass(resources.builderStyleCss().blockWrapperReadOnly());
    }

    private boolean isValidClickTarget(GQuery gQuery) {
        return !gQuery.is("input", "textarea", "button", "select", "div[role=button]", "[class*=\"icon-star\"");
    }

    private void bindMove(Element element, final BlockMoveAction action) {
        $(element).click(new Function() {
            @Override
            public void f() {
                if (!$(getElement()).hasClass(resources.builderStyleCss().disabled())) {
                    handler.onBlockMove(BlockBuilder.this, action);
                }
            }
        });
    }

    private void setup() {
        setDraggingOpacity(new Float(0.8));
        setDraggingZIndex(1000);
        addBeforeDragHandler(HANDLER);
        addDragStopHandler(HANDLER);
        setHandle("." + resources.builderStyleCss().blockHeader());
    }
}
