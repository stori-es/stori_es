package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.dashboard.client.application.ui.block.HasValidation;

import com.google.gwt.editor.client.Editor;

/**
 * Interface for the various {@link org.consumersunion.stories.common.shared.model.questionnaire.Block} 'configurators'
 * used to specify parameters specific to each Block 'type' (e.g., text,
 * multi-select, etc.).
 */
public interface BlockConfigurator<T> extends Editor<T>, HasValidation<T> {
    void setDoneCallback(Callback<T> doneCallback);

    T getEditedValue();
}
