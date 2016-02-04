package org.consumersunion.stories.common.shared.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.entity.Entity;

import com.google.common.base.Preconditions;

/**
 * Represents a collection of stories.
 */
public class Collection extends Entity implements HasTheme, HasTitle, HasBlocks {

    private int theme;

    /**
     * @see #getProfile()
     */
    private Integer profile;

    /**
     * @see #deleted
     */
    private boolean deleted;

    /**
     * @see #getStories()
     */
    private Set<StoryLink> stories;

    private Set<Integer> collectionSources;

    private Set<Integer> targetCollections;

    /**
     * @see #isQuestionnaire()
     */
    private boolean questionnaire;

    private Date publishedDate;
    private boolean published;
    private String previewKey;
    private Document bodyDocument;

    public Collection() {
        this(DEFAULT_ID, DEFAULT_VERSION);
    }

    public Collection(int id, int version) {
        super(id, version);

        stories = new HashSet<StoryLink>();

        // A valid Collection MUST contain a valid BODY document. For retrieved Collections, the Document will be
        // overridden.
        bodyDocument = new Document();
        bodyDocument.setEntity(getId());
        bodyDocument.setSystemEntityRelation(SystemEntityRelation.BODY);
    }

    /**
     * The ID of the profile {@link Document}.
     */
    public Integer getProfile() {
        return profile;
    }

    /**
     * @see #getProfile()
     */
    public void setProfile(final Integer profile) {
        this.profile = profile;
    }

    /**
     * Set of {@link Story Stories} included in the Collection. Each link
     * indicates the Story and whether or not the story has been cleared for
     * public inclusion in the Collection. Non-cleared stories may be
     * manipulated by the Collection Administrator, but would not be visible to
     * public consumers.
     */
    public Set<StoryLink> getStories() {
        return stories;
    }

    /**
     * @see #getStories()
     */
    public void setStories(final Set<StoryLink> stories) {
        this.stories = stories;
    }

    public boolean addStory(final StoryLink storyLink) {
        return stories.add(storyLink);
    }

    public boolean isAssociatedToStory(final int storyId) {
        return stories.contains(new StoryLink(storyId));
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Integer> getCollectionSources() {
        if (collectionSources == null) {
            this.collectionSources = new HashSet<Integer>();
        }
        return collectionSources;
    }

    public void setCollectionSources(Set<Integer> collectionSources) {
        this.collectionSources = collectionSources;
    }

    public Set<Integer> getTargetCollections() {
        if (targetCollections == null) {
            targetCollections = new HashSet<Integer>();
        }
        return targetCollections;
    }

    public void setTargetCollections(Set<Integer> targetCollections) {
        this.targetCollections = targetCollections;
    }

    /**
     * A transient value determined in the Persister by checking whether the {@link Collection} has a relation to the
     * {@link Questionnaire} table.
     */
    public boolean isQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(boolean questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public List<Block> getBlocks() {
        return getBodyDocument().getBlocks();
    }

    @Override
    public void setBlocks(List<Block> blocks) {
        getBodyDocument().setBlocks(blocks);
    }

    /**
     * Provides the theme page name. If no explicit value is set, then the
     * default 'sys.jsp' is returned.
     */
    @Override
    public int getTheme() {
        return theme;
    }

    /**
     * @see #getTheme
     */
    @Override
    public void setTheme(final int theme) {
        this.theme = theme;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPreviewKey(String previewKey) {
        this.previewKey = previewKey;
    }

    public String getPreviewKey() {
        return previewKey;
    }

    public String getSummary() {
        Document bodyDocument = getBodyDocument();

        Preconditions.checkNotNull(bodyDocument);

        return bodyDocument.getSummary();
    }

    public int getPrimaryAuthor() {
        return bodyDocument.getPrimaryAuthor();
    }

    public Locale getLocale() {
        return bodyDocument.getLocale();
    }

    public Document getBodyDocument() {
        Preconditions.checkNotNull(bodyDocument);

        return bodyDocument;
    }

    public void setBodyDocument(Document bodyDocument) {
        this.bodyDocument = bodyDocument;
    }

    @Override
    public String getTitle() {
        return bodyDocument.getTitle();
    }
}
