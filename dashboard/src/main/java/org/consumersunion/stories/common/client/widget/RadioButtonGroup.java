package org.consumersunion.stories.common.client.widget;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

public class RadioButtonGroup<T> extends FlowPanel
        implements LeafValueEditor<T>, HasValueChangeHandlers<T> {
    private final Renderer<T> renderer;
    private final Map<RadioButton, T> values;
    private final String name;
    private final HandlerManager handlerManager;

    public RadioButtonGroup(Renderer<T> renderer, String name) {
        handlerManager = new HandlerManager(this);
        this.renderer = renderer;
        values = Maps.newHashMap();
        this.name = name;
    }

    @Override
    public void clear() {
        super.clear();

        values.clear();
    }

    public void add(Iterable<T> values) {
        for (T value : values) {
            add(value);
        }
    }

    public void add(T... values) {
        for (T value : values) {
            add(value);
        }
    }

    public void add(final T value) {
        final RadioButton radioButton = new RadioButton(name, renderer.render(value));
        radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue()) {
                    ValueChangeEvent.fire(RadioButtonGroup.this, value);
                }
            }
        });

        add(radioButton);
        this.values.put(radioButton, value);
    }

    @Override
    public void setValue(T value) {
        for (Map.Entry<RadioButton, T> entry : values.entrySet()) {
            boolean selected = entry.getValue() == value;

            entry.getKey().setValue(selected);
        }
    }

    @Override
    public T getValue() {
        for (Map.Entry<RadioButton, T> entry : values.entrySet()) {
            if (entry.getKey().getValue()) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        if (event instanceof ValueChangeEvent) {
            handlerManager.fireEvent(event);
        } else {
            super.fireEvent(event);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
    }

    public void setEnabled(boolean enable) {
        for (RadioButton radioButton : values.keySet()) {
            radioButton.setEnabled(enable);
        }
    }
}
