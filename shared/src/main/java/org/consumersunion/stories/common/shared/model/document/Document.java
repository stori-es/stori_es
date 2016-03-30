package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.HasBlocks;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.Strings;

/**
 * The document reference.
 *
 * @author Zane Rockenbaugh
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type",
        defaultImpl = AnswerSet.class)
@JsonSubTypes({
        @JsonSubTypes.Type(Document.class),
        @JsonSubTypes.Type(AnswerSet.class),
        @JsonSubTypes.Type(AnswerSetSummary.class)})
@org.codehaus.jackson.annotate.JsonTypeInfo(
        use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.NAME,
        include = org.codehaus.jackson.annotate.JsonTypeInfo.As.PROPERTY,
        property = "@type",
        defaultImpl = AnswerSet.class)
@org.codehaus.jackson.annotate.JsonSubTypes({
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(Document.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(AnswerSet.class),
        @org.codehaus.jackson.annotate.JsonSubTypes.Type(AnswerSetSummary.class)})
@JsonTypeName("document")
@org.codehaus.jackson.annotate.JsonTypeName("document")
public class Document extends SystemEntity implements HasBlocks {
    public enum DocumentContributorRole implements Serializable {
        AUTHOR
    }

    /**
     * @see #getStory()
     */
    private int entity;

    /**
     * @see #getStoryrelation()
     */
    private SystemEntityRelation systemEntityRelation;

    /**
     * @see #getPrimaryAuthor
     */
    private int primaryAuthor;

    private Address authorAddress;

    /**
     * @see #getprimaryAuthorFirstName
     */
    private String primaryAuthorFirstName;

    /**
     * @see #getprimaryAutorLastName
     */
    private String primaryAuthorLastName;

    /**
     * @see #getPermalink()
     */
    private String permalink;

    /**
     * @see #getContributors()
     */
    private List<DocumentContributor> contributors;
    /**
     * @see #getEntities()
     */
    private List<Integer> entityReferences;
    private List<Block> blocks = new LinkedList<Block>();

    private Locale locale;
    private String title;
    private String summary;

    /**
     * New document constructor.
     */
    public Document() {
        super();
    }

    /**
     * Existing document constructor.
     */
    public Document(int id, int version) {
        super(id, version);
    }

    public int getEntity() {
        return entity;
    }

    public void setEntity(int entity) {
        this.entity = entity;
    }

    /**
     * The {@link Entity} associated to the Document.
     */
    public int getSystemEntity() {
        return entity;
    }

    public SystemEntityRelation getSystemEntityRelation() {
        return systemEntityRelation;
    }

    public void setSystemEntityRelation(SystemEntityRelation systemEntityRelation) {
        this.systemEntityRelation = systemEntityRelation;
    }

    public int getPrimaryAuthor() {
        return primaryAuthor;
    }

    public void setPrimaryAuthor(int primaryAuthor) {
        this.primaryAuthor = primaryAuthor;
    }

    /**
     * The URL string for the permanent Document link.
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * @see #getPermalink()
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * A list of the Document contributors.
     */
    public List<DocumentContributor> getContributors() {
        return contributors;
    }

    public void addContributor(DocumentContributor contributor) {
        contributors.add(contributor);
    }

    /**
     * @see #getContributor()
     */
    public void setContributors(List<DocumentContributor> contributors) {
        this.contributors = contributors;
    }

    /**
     * A list of Entities associated to the document.
     */
    public List<Integer> getEntityReferences() {
        return entityReferences;
    }

    /**
     * @see #getEntityReferences()
     */
    public void setEntityReferences(List<Integer> entityReferences) {
        this.entityReferences = entityReferences;
    }

    public String getPrimaryAuthorFirstName() {
        return primaryAuthorFirstName;
    }

    public void setPrimaryAuthorFirstName(String primaryAuthorFirstName) {
        this.primaryAuthorFirstName = primaryAuthorFirstName;
    }

    public String getPrimaryAuthorLastName() {
        return primaryAuthorLastName;
    }

    public void setPrimaryAuthorLastName(String primaryAuthorLastName) {
        this.primaryAuthorLastName = primaryAuthorLastName;
    }

    public String getFullPrimaryAuthorName() {
        return (primaryAuthorFirstName == null ? "" : primaryAuthorFirstName + " ")
                + (primaryAuthorLastName == null ? "" : primaryAuthorLastName);
    }

    public void setAuthorAddress(Address address) {
        this.authorAddress = address;
    }

    public Address getAuthorAddress() {
        return authorAddress;
    }

    public Locale getLocale() {
        if (locale == null) {
            return Locale.ENGLISH;
        } else {
            return locale;
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return Strings.nullToEmpty(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean hasSummary() {
        return summary != null;
    }

    public String getFirstContent() {
        Block firstContentBlock = getFirstContentBlock();
        if (firstContentBlock instanceof Content && firstContentBlock.getBlockType().getRenderType().isText()) {
            return ((Content) firstContentBlock).getContent();
        } else if (firstContentBlock instanceof TextImageBlock) {
            return ((TextImageBlock) firstContentBlock).getText();
        }

        return null;
    }

    public Block getFirstContentBlock() {
        for (Block block : getBlocks()) {
            if (block instanceof Content && block.getBlockType().getRenderType().isText()
                    || block instanceof TextImageBlock) {
                return block;
            }
        }

        return null;
    }

    @JsonIgnore
    public String getOnlyContent() {
        if (getBlocks().size() == 1) {
            return getFirstContent();
        }

        throw new IllegalStateException(
                "The document type " + systemEntityRelation + " should only contain 1 block of type Content");
    }

    @JsonIgnore
    public void setOnlyContent(String contentText) {
        if (getBlocks().size() == 1) {
            for (Block block : getBlocks()) {
                if (block instanceof Content) {
                    ((Content) block).setContent(contentText);
                    return;
                } else if (block instanceof TextImageBlock) {
                    ((TextImageBlock) block).setText(contentText);
                    return;
                }
            }
        }

        throw new IllegalStateException(
                "The document type " + systemEntityRelation + " should only contain 1 block of type Content");
    }

    public Document cloneContent() {
        Document clone = new Document();

        clone.setTitle(getTitle());
        clone.setPrimaryAuthor(getPrimaryAuthor());
        clone.setLocale(getLocale());
        clone.setSummary(getSummary());
        clone.setSystemEntityRelation(getSystemEntityRelation());
        for (Block block : getBlocks()) {
            clone.addBlock((Block) block.clone());
        }

        return clone;
    }

    /**
     * The list of questions associated to the Questionnaire.
     */
    @Override
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * @see #getBlocks()
     */
    @Override
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }
}
