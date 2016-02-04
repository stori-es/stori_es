package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.shared.model.questionnaire.Option;

import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import gwtquery.plugins.draggable.client.events.BeforeDragStartEvent;
import gwtquery.plugins.draggable.client.events.DragStopEvent;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;

import static com.google.gwt.query.client.GQuery.$;
import static gwtquery.plugins.draggable.client.events.BeforeDragStartEvent.BeforeDragStartEventHandler;
import static gwtquery.plugins.draggable.client.events.DragStopEvent.DragStopEventHandler;

public class OptionEditor extends DraggableWidget<Widget> implements LeafValueEditor<Option> {
    interface Binder extends UiBinder<HTMLPanel, OptionEditor> {
    }

    public interface Handler {
        void onDelete();
    }

    private class DraggablePositionHandler implements BeforeDragStartEventHandler, DragStopEventHandler {
        public void onBeforeDragStart(BeforeDragStartEvent event) {
            Element draggable = event.getDraggable();
            $(draggable).width($(draggable).parent().innerWidth());
            $(draggable).css("position", "absolute");
        }

        public void onDragStop(DragStopEvent event) {
            $(event.getDraggable()).css("position", "relative").css("top", null).css("left", null);
        }
    }

    @UiField
    TextBox value;
    @UiField
    Label move;
    @UiField
    Label delete;

    private final DraggablePositionHandler DRAG_HANDLER = new DraggablePositionHandler();

    private Handler handler;

    @Inject
    OptionEditor(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        setup();
    }

    public void allowEdit(boolean allowEdit) {
        move.setVisible(allowEdit);
        delete.setVisible(allowEdit);
        value.setEnabled(allowEdit);
    }

    @Override
    public void setValue(Option option) {
        value.setText(option.getReportValue());
    }

    @Override
    public Option getValue() {
        Option option = new Option();
        option.setReportValue(value.getText());
        option.setDisplayValue(value.getText());

        return option;
    }

    public void addDeleteHandler(Handler handler) {
        this.handler = handler;
    }

    @UiHandler("delete")
    void onDeleteClicked(ClickEvent event) {
        handler.onDelete();
    }

    private void setup() {
        setDraggingOpacity(new Float(0.8));
        setDraggingZIndex(1000);
        addBeforeDragHandler(DRAG_HANDLER);
        addDragStopHandler(DRAG_HANDLER);
        setHandle("." + move.getStyleName());
    }
}
