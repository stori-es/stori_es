package org.consumersunion.stories.common.client.ui.block.content;

import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.shared.model.document.TextImage;
import org.consumersunion.stories.common.shared.model.document.TextImage.Position;
import org.consumersunion.stories.common.shared.model.document.TextImage.Size;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;
import static org.consumersunion.stories.common.shared.model.document.TextImage.Position.LEFT;
import static org.consumersunion.stories.common.shared.model.document.TextImage.Position.RIGHT;
import static org.consumersunion.stories.common.shared.model.document.TextImage.Size.LARGE;
import static org.consumersunion.stories.common.shared.model.document.TextImage.Size.MEDIUM;
import static org.consumersunion.stories.common.shared.model.document.TextImage.Size.SMALL;

public class TextImageContent extends Composite implements ContentElement<TextImageBlock> {
    interface Binder extends UiBinder<Widget, TextImageContent> {
    }

    interface Style extends CssResource {
        String imageContainer();

        String small();

        String medium();

        String large();

        String left();

        String right();

        String clearfix();
    }

    @UiField
    ParagraphElement textContent;
    @UiField
    Image img;
    @UiField
    ParagraphElement caption;
    @UiField
    DivElement imageContainer;
    @UiField
    Style style;

    private final Map<Position, String> positionClass;
    private final Map<Size, String> sizeClass;

    @Inject
    TextImageContent(
            Binder binder,
            @Assisted TextImageBlock content) {
        initWidget(binder.createAndBindUi(this));

        positionClass = ImmutableMap.<Position, String>builder()
                .put(LEFT, style.left())
                .put(RIGHT, style.right())
                .build();

        sizeClass = ImmutableMap.<Size, String>builder()
                .put(SMALL, style.small())
                .put(MEDIUM, style.medium())
                .put(LARGE, style.large())
                .build();
        display(content);
    }

    @Override
    public void display(TextImageBlock content) {
        textContent.setInnerHTML(SafeHtmlUtils.fromTrustedString(Strings.nullToEmpty(content.getText())).asString());
        if (content.containsImage()) {
            $(imageContainer).show();
            img.setUrl(content.getImage().getUrl());
            img.setAltText(content.getImage().getAltText());
            caption.setInnerText(SafeHtmlUtils.htmlEscape(content.getImage().getCaption()));
            String classNames = buildContainerClass(content.getImage());
            imageContainer.setClassName(classNames);
        } else {
            $(imageContainer).hide();
        }
    }

    private String buildContainerClass(TextImage image) {
        Iterable<String> classNames = Lists.newArrayList(
                style.imageContainer(),
                positionClass.get(image.getPosition()),
                sizeClass.get(image.getSize()));

        return Joiner.on(" ").skipNulls().join(classNames);
    }
}
