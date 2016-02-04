package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.HtmlSanitizerUtil;
import org.consumersunion.stories.common.shared.model.document.Content;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TextContent extends Composite implements ContentElement<Content> {
    private final HtmlSanitizerUtil htmlSanitizerUtil;

    interface Binder extends UiBinder<Widget, TextContent> {
    }

    @UiField
    HTML textContent;

    @Inject
    TextContent(
            Binder uiBinder,
            HtmlSanitizerUtil htmlSanitizerUtil,
            @Assisted Content content) {
        this.htmlSanitizerUtil = htmlSanitizerUtil;
        initWidget(uiBinder.createAndBindUi(this));
        display(content);
    }

    @Override
    public void display(Content content) {
        String textContent = content.getContent();
        if (textContent != null) {
            this.textContent.setHTML(htmlSanitizerUtil.sanitize(textContent));
        } else {
            this.textContent.setHTML("");
        }
    }
}
