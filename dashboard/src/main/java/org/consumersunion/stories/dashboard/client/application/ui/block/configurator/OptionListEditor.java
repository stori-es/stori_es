package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import java.util.List;

import org.consumersunion.stories.common.shared.model.questionnaire.Option;
import org.consumersunion.stories.dashboard.client.application.ui.SortableDragAndDropHandler;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import gwtquery.plugins.draggable.client.DraggableOptions;
import gwtquery.plugins.droppable.client.DroppableOptions;
import gwtquery.plugins.droppable.client.events.DragAndDropContext;
import gwtquery.plugins.droppable.client.gwt.DroppableWidget;

public class OptionListEditor extends DroppableWidget<Widget>
        implements IsEditor<ListEditor<Option, OptionEditor>>, LeafValueEditor<List<Option>> {
    interface Binder extends UiBinder<Widget, OptionListEditor> {
    }

    private class OptionEditorSource extends EditorSource<OptionEditor> {
        @Override
        public OptionEditor create(final int index) {
            final OptionEditor subEditor = optionEditorFactory.createOptionEditor();
            subEditor.setAxis(DraggableOptions.AxisOption.Y_AXIS);
            setAccept(new DroppableOptions.AcceptFunction() {
                @Override
                public boolean acceptDrop(DragAndDropContext context) {
                    return context.getDroppableWidget() == OptionListEditor.this;
                }
            });
            subEditor.addDeleteHandler(new OptionEditor.Handler() {
                @Override
                public void onDelete() {
                    remove(optionsPanel.getWidgetIndex(subEditor));
                }
            });
            optionsPanel.insert(subEditor, index);

            updateOptionsPanelVisibility();

            return subEditor;
        }

        @Override
        public void dispose(OptionEditor subEditor) {
            subEditor.removeFromParent();
            updateOptionsPanelVisibility();
        }

        @Override
        public void setIndex(OptionEditor editor, int index) {
            optionsPanel.insert(editor, index);
        }
    }

    @UiField
    FlowPanel optionsPanel;
    @UiField
    Button addOption;

    private final OptionEditorFactory optionEditorFactory;
    private final ListEditor<Option, OptionEditor> editor;

    @Inject
    OptionListEditor(
            Binder uiBinder,
            OptionEditorFactory optionEditorFactory) {
        this.optionEditorFactory = optionEditorFactory;
        this.editor = ListEditor.of(new OptionEditorSource());

        initWidget(uiBinder.createAndBindUi(this));

        SortableDragAndDropHandler sortableHandler = new SortableDragAndDropHandler(optionsPanel);
        addDropHandler(sortableHandler);
        addOutDroppableHandler(sortableHandler);
        addOverDroppableHandler(sortableHandler);
    }

    public void allowEdit(boolean allowEdit) {
        setDisabled(!allowEdit);
        addOption.setVisible(allowEdit);

        for (int i = 0; i < optionsPanel.getWidgetCount(); i++) {
            OptionEditor editor = (OptionEditor) optionsPanel.getWidget(i);
            editor.allowEdit(allowEdit);
        }
    }

    @Override
    public ListEditor<Option, OptionEditor> asEditor() {
        return editor;
    }

    @Override
    public void setValue(List<Option> value) {
        if (value.isEmpty()) {
            value.add(new Option());
        }
        editor.setValue(value);
    }

    @Override
    public List<Option> getValue() {
        List<Option> options = Lists.newArrayList();
        for (int i = 0; i < optionsPanel.getWidgetCount(); i++) {
            OptionEditor editor = (OptionEditor) optionsPanel.getWidget(i);
            options.add(editor.getValue());
        }

        return options;
    }

    @UiHandler("addOption")
    void onAddOptionClicked(ClickEvent event) {
        editor.getList().add(new Option());
    }

    private void remove(final int index) {
        editor.getList().remove(index);
    }

    private void updateOptionsPanelVisibility() {
        optionsPanel.setVisible(optionsPanel.getWidgetCount() > 0);
    }
}
