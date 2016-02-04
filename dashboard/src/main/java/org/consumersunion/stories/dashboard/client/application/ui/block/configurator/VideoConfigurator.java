package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.RpcResourceCheckerServiceAsync;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Configurator for {@link MediaBlock} elements with a form type of
 * {@link BlockType#VIDEO}.
 */
public class VideoConfigurator extends AbstractMediaConfigurator {
    private final CommonI18nErrorMessages errorMessages;
    private final RpcResourceCheckerServiceAsync resourceChecker;

    @Inject
    VideoConfigurator(
            Binder uiBinder,
            Driver driver,
            CommonI18nErrorMessages errorMessages,
            RpcResourceCheckerServiceAsync resourceChecker,
            @Assisted MediaBlock video) {
        super(uiBinder, driver, video);

        this.errorMessages = errorMessages;
        this.resourceChecker = resourceChecker;

        url.getElement().setAttribute("placeholder", messages.enterYoutubeUrl());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    protected void verifyMediaValidity(final MediaBlock video) {
        resourceChecker.checkValidVideo(video.getUrl(), new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean valid) {
                if (valid) {
                    resetErrors();
                    VideoConfigurator.this.onSuccess(video);
                    setEditedValue(video);
                } else {
                    urlError.setText(errorMessages.invalidVideoUrl());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(errorMessages.resourceNotFound());
            }
        });
    }
}
