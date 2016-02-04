package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.client.util.StoriesJsonEncoderDecoder;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.dashboard.client.resource.BlockConfiguratorStyle;
import org.consumersunion.stories.dashboard.client.resource.BuilderStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

import static com.google.gwt.query.client.GQuery.$;

public abstract class AbstractConfigurator<T extends Block> extends Composite implements BlockConfigurator<T> {
    @Inject
    protected static Resources resources;
    @Inject
    protected static CommonResources commonResources;
    @Inject
    protected static FormI18nMessages messages;

    @Inject
    private static StoriesJsonEncoderDecoder jsonEncoderDecoder;

    protected final SimpleBeanEditorDriver<T, ? extends AbstractConfigurator<T>> driver;

    protected Callback<T> doneCallback;

    private T editedValue;
    private Label[] errorLabels;

    protected AbstractConfigurator(
            SimpleBeanEditorDriver<T, ? extends AbstractConfigurator<T>> driver,
            T editedValue) {
        this.driver = driver;
        this.editedValue = editedValue;
    }

    @Override
    public void setDoneCallback(Callback<T> doneCallback) {
        this.doneCallback = doneCallback;
    }

    @Override
    public final T getEditedValue() {
        return editedValue;
    }

    protected void init() {
        if (driver != null) {
            T editedValue = getEditedValue();
            driver.edit(jsonEncoderDecoder.clone((Class<T>) editedValue.getClass(), editedValue));
        }

        resetErrors();
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        deferTwice(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (isAttached()) {
                    BlockConfiguratorStyle style = resources.blockConfigurators();
                    BuilderStyle builderStyle = resources.builderStyleCss();

                    GQuery headerSelector = $("." + builderStyle.blockHeader(),
                            $(asWidget()).parents("." + builderStyle.blockWrapper()).widget());

                    $("." + style.fullRow() + ", ." + style.shortRow())
                            .width(headerSelector.width());
                }
            }
        });
    }

    protected void setErrorLabels(Label... errorLabels) {
        this.errorLabels = errorLabels;
    }

    protected void resetErrors() {
        setErrors("");
    }

    protected void setEditedValue(T editedValue) {
        this.editedValue = editedValue;
    }

    protected abstract void onDone();

    @UiHandler("done")
    final void onDoneClicked(ClickEvent event) {
        onDone();
    }

    @UiHandler("cancel")
    final void onCancelClicked(ClickEvent event) {
        if (tryCancel()) {
            doneCallback.onCancel();
            init();
        }
    }

    private boolean tryCancel() {
        boolean valid = !isNew();

        if (!valid) {
            setErrors(messages.requiredField());
        }

        return valid;
    }

    private void setErrors(String message) {
        if (errorLabels != null) {
            for (Label errorLabel : errorLabels) {
                errorLabel.setText(message);
            }
        }
    }

    private void deferTwice(final Scheduler.ScheduledCommand scheduledCommand) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                Scheduler.get().scheduleDeferred(scheduledCommand);
            }
        });
    }
}
