package org.consumersunion.stories.dashboard.client.application.ui.builder;

import javax.inject.Inject;

import org.consumersunion.stories.dashboard.client.application.ui.block.MetaBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.MetaBlockHandler;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class MetaBlockDrawer extends Widget implements AttachEvent.Handler {
    interface Binder extends UiBinder<DivElement, MetaBlockDrawer> {
    }

    private final MetaBlockFactory metaBlockFactory;
    private final MetaBlockHandler handler;

    @UiField
    Resources res;

    private Function clickHandler;

    @Inject
    MetaBlockDrawer(
            Binder binder,
            MetaBlockFactory metaBlockFactory,
            @Assisted MetaBlockHandler handler) {
        this.metaBlockFactory = metaBlockFactory;
        this.handler = handler;

        setElement(binder.createAndBindUi(this));

        addAttachHandler(this);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            clickHandler = new Function() {
                @Override
                public void f() {
                    onClick();
                }
            };
            $(this).click(clickHandler);
        } else {
            $(this).unbind(BrowserEvents.CLICK, clickHandler);
        }
    }

    private void onClick() {
        MetaBlock metaBlock = $("." + res.builderStyleCss().metaBlock(), getParent()).widget();
        if (metaBlock != null) {
            metaBlock.removeAndReplace();
        }

        handler.replace(this, metaBlockFactory.createBlock(handler));
    }
}
