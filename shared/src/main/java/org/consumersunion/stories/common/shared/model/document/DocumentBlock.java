package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("document")
@org.codehaus.jackson.annotate.JsonTypeName("document")
public class DocumentBlock extends ContentBase implements Serializable {
    private String title;
    private String url;

    public DocumentBlock() {
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

    @Override
    public Object clone() {
        DocumentBlock documentBlock = new DocumentBlock();
        documentBlock.setTitle(getTitle());
        documentBlock.setKey(getKey());
        documentBlock.setUrl(getUrl());
        documentBlock.setDocument(getDocument());
        documentBlock.setBlockType(getBlockType());

        return documentBlock;
    }
}
