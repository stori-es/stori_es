package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

import com.google.common.base.Preconditions;

import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;
import org.consumersunion.stories.common.shared.service.GeneralException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Object that references Block DB table
 */
@JsonSubTypes({
        @JsonSubTypes.Type(Question.class),
        @JsonSubTypes.Type(Content.class),
        @JsonSubTypes.Type(SubmitBlock.class),
        @JsonSubTypes.Type(RatingQuestion.class),
        @JsonSubTypes.Type(ContactBlock.class),
        @JsonSubTypes.Type(DocumentBlock.class),
        @JsonSubTypes.Type(ImageBlock.class),
        @JsonSubTypes.Type(TextImageBlock.class),
        @JsonSubTypes.Type(MediaBlock.class)})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type")
@org.codehaus.jackson.annotate.JsonSubTypes({
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(Question.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(Content.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(SubmitBlock.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(RatingQuestion.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(ContactBlock.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(DocumentBlock.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(ImageBlock.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(TextImageBlock.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(MediaBlock.class)})
@org.codehaus.jackson.annotate.JsonTypeInfo(
        use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.NAME,
        include = org.codehaus.jackson.annotate.JsonTypeInfo.As.PROPERTY,
        property = "@type")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block implements Serializable, Cloneable {

    /**
     * @see #getDocument()
     */
    private int document;

    /**
     * @see #getIdx()
     */
    private int idx;

    /**
     * @see #getVersion()
     */
    private int version;

    private BlockType blockType;

    private String key;

    public Block() {
    }

    public Block(BlockType blockType) {
        this.blockType = blockType;
    }

    /**
     * @return blockType
     */
    public BlockType getBlockType() {
        return blockType;
    }
    
    public BlockType getRenderType() {
    	return blockType.getRenderType();
    }

    /**
     * @param blockType
     */
    public void setBlockType(final BlockType blockType) {
        Preconditions.checkNotNull(blockType);
        
        this.blockType = blockType;
    }

    public int getDocument() {
        return document;
    }

    public void setDocument(final int questionnaire) {
        this.document = questionnaire;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(final int idx) {
        this.idx = idx;
    }

    /**
     * The version of the {@code Block}. {@code Block} versions track the document / system entity versions. With
     * {@code document} and {@code idx} identifies a particular {@code Block} instance.
     */
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object clone() {
        return null;
    }
}
