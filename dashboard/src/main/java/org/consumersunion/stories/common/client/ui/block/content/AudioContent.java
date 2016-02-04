package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class AudioContent extends Composite implements ContentElement<MediaBlock> {
    interface Binder extends UiBinder<Widget, AudioContent> {
    }

    private static final Binder UI_BINDER = GWT.create(Binder.class);

    @UiField
    Label title;
    @UiField
    IFrameElement audio;
    @UiField
    HTML description;

    private final CommonI18nMessages messages;

    private MediaBlock audioBlock;

    @Inject
    public AudioContent(
            CommonI18nMessages messages,
            @Assisted MediaBlock audioBlock) {
        this.messages = messages;

        initWidget(UI_BINDER.createAndBindUi(this));

        display(audioBlock);
    }

    @Override
    public void display(MediaBlock audioBlock) {
        this.audioBlock = audioBlock;

        title.setText(audioBlock.getTitle());
        description.setHTML(SafeHtmlUtils.fromTrustedString(Strings.nullToEmpty(audioBlock.getDescription())));

        updateVideoSource();
    }

    private void updateVideoSource() {
        String src = messages.soundcloudEmbedUrl(audioBlock.getUrl());

        audio.setSrc(src);
    }
}
