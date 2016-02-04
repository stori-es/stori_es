package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("media")
@org.codehaus.jackson.annotate.JsonTypeName("media")
public class MediaBlock extends ContentBase implements Serializable {
    private String title;
    private String url;
    private String description;

    // For serialization
    public MediaBlock() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Object clone() {
        MediaBlock audio = new MediaBlock();
        audio.setDocument(getDocument());
        audio.setFormType(getFormType());
        audio.setKey(getKey());
        audio.setStandardMeaning(getStandardMeaning());
        audio.setUrl(getUrl());
        audio.setTitle(getTitle());
        audio.setDescription(getDescription());

        return audio;
    }
}
