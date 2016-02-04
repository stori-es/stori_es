package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Strings;

@JsonTypeName("image")
@org.codehaus.jackson.annotate.JsonTypeName("image")
public class ImageBlock extends ContentBase implements Serializable {

    public ImageBlock() {
        super();
    }

    private String url;
    private String caption;
    private String altText;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    @JsonIgnore
    public boolean hasImage() {
        return !Strings.isNullOrEmpty(url);
    }

    @Override
    public Object clone() {
        ImageBlock image = new ImageBlock();
        image.setUrl(this.getUrl());
        image.setCaption(this.getCaption());
        image.setAltText(this.getAltText());
        image.setDocument(getDocument());
        image.setFormType(getFormType());
        image.setKey(getKey());
        image.setStandardMeaning(getStandardMeaning());

        return image;
    }
}
