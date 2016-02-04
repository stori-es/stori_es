package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.RpcResourceCheckerServiceAsync;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Configurator for {@link MediaBlock} elements with a form type of
 * {@link org.consumersunion.stories.common.shared.model.questionnaire.BlockType#AUDIO}.
 */
public class AudioConfigurator extends AbstractMediaConfigurator {
    private final CommonI18nErrorMessages errorMessages;
    private final RpcResourceCheckerServiceAsync resourceChecker;

    @Inject
    AudioConfigurator(
            Binder uiBinder,
            Driver driver,
            CommonI18nErrorMessages errorMessages,
            RpcResourceCheckerServiceAsync resourceChecker,
            @Assisted MediaBlock audio) {
        super(uiBinder, driver, audio);

        this.errorMessages = errorMessages;
        this.resourceChecker = resourceChecker;

        url.getElement().setAttribute("placeholder", messages.enterSoundCloudUrl());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    protected void verifyMediaValidity(final MediaBlock audio) {
        resourceChecker.checkValidAudio(audio.getUrl(), new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean valid) {
                if (valid) {
                    urlError.setText("");
                    AudioConfigurator.this.onSuccess(audio);
                    setEditedValue(audio);
                } else {
                    urlError.setText(errorMessages.invalidAudioUrl());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(errorMessages.resourceNotFound());
            }
        });
    }
}
