package org.consumersunion.stories.common.shared.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

import com.google.common.collect.Sets;

/**
 * A set of {@link Document}s organized to communicate an experience or
 * narrative. The Story is the central concept of SYS.
 */
public class Story extends SystemEntity {
    /**
     * @see #getPermalink()
     */
    private String permalink;

    /**
     * @see #getDefaultContent()
     */
    private Integer defaultContent;

    /**
     * @see #getPublished()
     */
    private boolean published;

    /**
     * @see #getFirstPublished()
     */
    private Date firstPublished;
    private String byLine;
    private String storyTeller;
    private boolean readProfile;
    /**
     * @see #getDocument(SystemEntityRelation)
     */
    private Map<String, Set<Integer>> documents = new HashMap<String, Set<Integer>>();

    public Story() {
        super();
    }

    public Story(int id, int version) {
        super(id, version);
    }

    /**
     * Returns the permanent URL String for the Story.
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * @see #getPermalink()
     */
    public void setPermalink(final String permalink) {
        this.permalink = permalink;
    }

    /**
     * Returns the ID of the {@link Document} which best represents the Story.
     */
    public Integer getDefaultContent() {
        return defaultContent;
    }

    /**
     * @see #getDefaultContent()
     */
    public void setDefaultContent(Integer defaultContent) {
        this.defaultContent = defaultContent;
    }

    /**
     * Indicates whether the Story has been published.
     */
    public boolean getPublished() {
        return published;
    }

    /**
     * @see #getPublished()
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

    /**
     * Returns the date the Story was first published or null.
     */
    public Date getFirstPublished() {
        return firstPublished;
    }

    /**
     * @see #getFirstPublished
     */
    public void setFirstPublished(Date firstPublished) {
        this.firstPublished = firstPublished;
    }

    public String getByLine() {
        return byLine;
    }

    public void setByLine(String byLine) {
        this.byLine = byLine;
    }

    public String getStoryTeller() {
        return storyTeller;
    }

    public void setStoryTeller(String storyTeller) {
        this.storyTeller = storyTeller;
    }

    public boolean isReadProfile() {
        return readProfile;
    }

    public void setReadProfile(boolean readProfile) {
        this.readProfile = readProfile;
    }

    /**
     * Returns the ID of the {@link Document} with the given
     * {@link SystemEntityRelation} to the Story. At the DB level, we support
     * many Documents (of each type) to each one Story, and in SYS3.0, will
     * support that through the model and UI. At this point, we support a single
     * association of each type. See 'AnswerSets and Stories' in the Developer
     * Reference for more information.
     *
     * @param relation
     * @return
     */
    public Integer getOnlyDocument(SystemEntityRelation relation) {
        Set<Integer> documentIds = getDocuments(relation);
        if (documentIds.isEmpty()) {
            return null;
        } else if (documentIds.size() > 1) {
            throw new IllegalArgumentException("Story has more than one document of type " + relation.name());
        } else {
            return documentIds.iterator().next();
        }
    }

    public Set<Integer> getDocuments(SystemEntityRelation relation) {
        Set<Integer> documentIds = documents.get(relation.name());
        if (documentIds == null) {
            documentIds = Sets.newLinkedHashSet();
        }

        return documentIds;
    }

    public void addDocument(SystemEntityRelation relation, Integer documentId) {
        Set<Integer> relationDocuments = documents.get(relation.name());
        if (relationDocuments == null) {
            relationDocuments = Sets.newLinkedHashSet();
        }

        relationDocuments.add(documentId);
        documents.put(relation.name(), relationDocuments);
    }

    public void addDocuments(SystemEntityRelation relation, java.util.Collection<Integer> documentIds) {
        if (documentIds != null && !documentIds.isEmpty()) {
            Set<Integer> relationDocuments = documents.get(relation.name());
            if (relationDocuments == null) {
                relationDocuments = Sets.newLinkedHashSet();
            }

            relationDocuments.addAll(documentIds);
            documents.put(relation.name(), relationDocuments);
        }
    }

    public Map<String, Set<Integer>> getDocuments() {
        return documents;
    }

    public void setDocuments(SystemEntityRelation relation, Set<Integer> documentIds) {
        documents.put(relation.name(), documentIds);
    }

    public void setDocuments(Map<String, Set<Integer>> documents) {
        this.documents = documents;
    }
}
