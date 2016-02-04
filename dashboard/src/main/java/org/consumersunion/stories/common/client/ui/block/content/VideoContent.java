package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.common.base.Strings.nullToEmpty;
import static com.google.gwt.safehtml.shared.SafeHtmlUtils.fromTrustedString;

public class VideoContent extends Composite implements ContentElement<MediaBlock> {
    interface Binder extends UiBinder<Widget, VideoContent> {
    }

    private static final Binder UI_BINDER = GWT.create(Binder.class);

    private static final RegExp EXTRACT_YOUTUBE_ID_REG_EXP = RegExp.compile(
            "^.*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=|\\&v=)([^#\\&\\?]*).*");
    private static final int YOUTUBE_ID_LENGTH = 11;

    @UiField
    Label title;
    @UiField
    IFrameElement video;
    @UiField
    HTML description;
    @UiField
    Anchor videoAnchor;

    private final CommonI18nMessages messages;

    private MediaBlock videoBlock;

    @Inject
    public VideoContent(CommonI18nMessages messages,
            @Assisted MediaBlock videoBlock) {
        this.messages = messages;

        initWidget(UI_BINDER.createAndBindUi(this));

        display(videoBlock);
    }

    @Override
    public void display(MediaBlock videoBlock) {
        this.videoBlock = videoBlock;

        title.setText(videoBlock.getTitle());
        description.setHTML(fromTrustedString(nullToEmpty(videoBlock.getDescription())));

        updateVideoSource();
    }

    private void updateVideoSource() {
        String url = cleanupUrl();

        MatchResult result = EXTRACT_YOUTUBE_ID_REG_EXP.exec(url);

        if (result != null && result.getGroup(1).length() == YOUTUBE_ID_LENGTH) {
            revealVideo(result.getGroup(1));
        } else {
            revealVideoAnchor(url);
        }
    }

    private String cleanupUrl() {
        String url = Strings.nullToEmpty(videoBlock.getUrl()).replace("http:", "");
        if (!url.startsWith("//")) {
            url = "//" + url;
        }
        return url;
    }

    private void revealVideoAnchor(String url) {
        videoAnchor.setText("http:" + url);
        videoAnchor.setHref(url);

        videoAnchor.setVisible(true);
        video.getStyle().setDisplay(Display.NONE);
    }

    private void revealVideo(String id) {
        String src = messages.youtubeEmbedUrl(id);

        video.setSrc(src);
        videoAnchor.setVisible(false);
        video.getStyle().setDisplay(Display.BLOCK);
    }
}
