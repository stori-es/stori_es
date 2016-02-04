package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("content")
@org.codehaus.jackson.annotate.JsonTypeName("content")
public class Content extends ContentBase {
    public enum TextType implements Serializable {
        PLAIN, CSS, HTML
    }

    private String content;
    private TextType textType;

    public Content() {
    }

    public Content(BlockType blockType, String content, TextType textType) {
        super(blockType);
        this.content = content;
        this.textType = textType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextType getTextType() {
        return textType;
    }

    public void setTextType(TextType textType) {
        this.textType = textType;
    }

    @Override
    public Object clone() {
        Content content = new Content();
        content.setContent(getContent());
        content.setTextType(getTextType());
        content.setDocument(getDocument());
        content.setFormType(getFormType());
        content.setStandardMeaning(getStandardMeaning());

        return content;
    }
}
