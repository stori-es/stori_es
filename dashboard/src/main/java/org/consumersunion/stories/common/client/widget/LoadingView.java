package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.ui.LoadingIndicator;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewImpl;

import static com.google.gwt.query.client.GQuery.$;

public class LoadingView extends PopupViewImpl implements LoadingPresenter.MyView {
    interface Binder extends UiBinder<PopupPanel, LoadingView> {
    }

    @UiField(provided = true)
    final LoadingIndicator loading;

    private final String highZIndexClass;

    private HandlerRegistration fullScreenHandlerRegistration;

    @Inject
    LoadingView(
            EventBus eventBus,
            Binder uiBinder,
            LoadingIndicator loadingIndicator,
            CommonResources resources) {
        super(eventBus);

        this.loading = loadingIndicator;

        initWidget(uiBinder.createAndBindUi(this));

        highZIndexClass = resources.generalStyleCss().maxZIndex();
    }

    @Override
    public void setFullScreen(final boolean fullScreen) {
        if (fullScreenHandlerRegistration == null) {
            fullScreenHandlerRegistration = asWidget().addAttachHandler(new AttachEvent.Handler() {
                @Override
                public void onAttachOrDetach(AttachEvent event) {
                    if (event.isAttached()) {
                        $(".gwt-PopupPanelGlass").toggleClass(highZIndexClass, fullScreen);
                        $(".gwt-PopupPanel").toggleClass(highZIndexClass, fullScreen);
                        GQuery.$(asWidget()).toggleClass(highZIndexClass, fullScreen);
                    } else {
                        fullScreenHandlerRegistration.removeHandler();
                        fullScreenHandlerRegistration = null;
                    }
                }
            });
        }
    }
}
