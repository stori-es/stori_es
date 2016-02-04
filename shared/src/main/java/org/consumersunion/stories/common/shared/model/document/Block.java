package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

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

    /**
     * @see #getFormType()
     */
    private BlockType formType;

    private BlockType standardMeaning;

    private String key;

    public Block() {
    }

    public Block(BlockType blockType) {
        this.formType = blockType;
    }

    /**
     * Defines the question form representation, e.g. text, select list, etc.
     */
    public BlockType getFormType() {
        return formType;
    }

    /**
     * @see #getFormType
     */
    public void setFormType(final BlockType formType) {
        if (formType != null) {
            boolean exists = false;
            for (BlockType blockType : BlockType.customElements()) {
                exists |= blockType.equals(formType);
            }

            if (!exists) {
                throw new GeneralException("Invalid form type: " + formType);
            }
        }
        this.formType = formType;
    }

    /**
     * @return standardMeaning
     */
    public BlockType getStandardMeaning() {
        return standardMeaning;
    }

    /**
     * @param standardMeaning
     */
    public void setStandardMeaning(final BlockType standardMeaning) {
        if (standardMeaning != null
                && !(BlockType.FIRST_NAME.equals(standardMeaning)
                || BlockType.LAST_NAME.equals(standardMeaning)
                || BlockType.SUBMIT.equals(standardMeaning)
                || BlockType.RATING.equals(standardMeaning)
                || BlockType.EMAIL.equals(standardMeaning)
                || BlockType.STREET_ADDRESS_1.equals(standardMeaning)
                || BlockType.CITY.equals(standardMeaning)
                || BlockType.STATE.equals(standardMeaning)
                || BlockType.ZIP_CODE.equals(standardMeaning)
                || BlockType.PHONE.equals(standardMeaning)
                || BlockType.MAILING_OPT_IN.equals(standardMeaning)
                || BlockType.PREFERRED_EMAIL_FORMAT.equals(standardMeaning)
                || BlockType.STORY_ASK.equals(standardMeaning)
                || BlockType.CUSTOM_PERMISSIONS.equals(standardMeaning)
                || BlockType.STORY_TITLE.equals(standardMeaning)
                || BlockType.UPDATES_OPT_IN.equals(standardMeaning)
                || BlockType.EMAIL_WORK.equals(standardMeaning)
                || BlockType.EMAIL_OTHER.equals(standardMeaning)
                || BlockType.PHONE_MOBILE.equals(standardMeaning)
                || BlockType.PHONE_WORK.equals(standardMeaning)
                || BlockType.PHONE_OTHER.equals(standardMeaning))) {
            throw new GeneralException("Invalid Standard Meaning type: " + standardMeaning);
        }
        this.standardMeaning = standardMeaning;
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
