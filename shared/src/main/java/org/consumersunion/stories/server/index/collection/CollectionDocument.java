package org.consumersunion.stories.server.index.collection;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.consumersunion.stories.server.index.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@JsonAutoDetect(
        fieldVisibility = ANY,
        getterVisibility = NONE,
        setterVisibility = NONE)
public class CollectionDocument implements Document {
    @JsonProperty(access = WRITE_ONLY)
    private final String _type = "collections";

    @JsonIgnore
    private int id;
    @JsonProperty(access = WRITE_ONLY)
    private String _id;
    @JsonProperty(access = WRITE_ONLY)
    private String _index;
    private String title;
    private int ownerId;
    private boolean isPublic;
    private boolean deleted;
    private Date created;
    private Date modified;
    private Set<String> admins = new LinkedHashSet<String>();
    private Set<Integer> readAuths = new LinkedHashSet<Integer>();
    private Set<Integer> writeAuths = new LinkedHashSet<Integer>();
    private Set<Integer> adminAuths = new LinkedHashSet<Integer>();
    private Set<String> tags = new LinkedHashSet<String>();
    private boolean isQuestionnaire;

    private CollectionDocument() {
        tags = new LinkedHashSet<String>();
        readAuths = new LinkedHashSet<Integer>();
        writeAuths = new LinkedHashSet<Integer>();
        adminAuths = new LinkedHashSet<Integer>();
        admins = new LinkedHashSet<String>();
    }

    public CollectionDocument(
            org.consumersunion.stories.common.shared.model.Collection collection,
            Set<String> tags,
            Set<Integer> readAuths,
            Set<Integer> writeAuths,
            Set<Integer> adminAuths,
            Set<String> admins) {
        this.setId(collection.getId());
        this.title = collection.getTitle();
        this.ownerId = collection.getOwner();
        this.created = collection.getCreated();
        Date lastModified = collection.getUpdated();
        this.modified = lastModified == null ? created : lastModified;
        this.isPublic = collection.isPublic();
        this.deleted = collection.getDeleted();
        this.tags = tags;
        this.readAuths = readAuths;
        this.writeAuths = writeAuths;
        this.adminAuths = adminAuths;
        this.admins = admins;
        this.isQuestionnaire = collection.isQuestionnaire();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = Integer.valueOf(id);
        this._id = id;
    }

    public void setId(int id) {
        this.id = id;
        this._id = String.valueOf(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Set<String> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<String> admins) {
        this.admins.addAll(admins);
    }

    @Override
    public Set<Integer> getReadAuths() {
        return readAuths;
    }

    public void setReadAuths(Set<Integer> readAuths) {
        this.readAuths.addAll(readAuths);
    }

    public Set<Integer> getWriteAuths() {
        return writeAuths;
    }

    public void setWriteAuths(Set<Integer> writeAuths) {
        this.writeAuths.addAll(writeAuths);
    }

    public Set<Integer> getAdminAuths() {
        return adminAuths;
    }

    public void setAdminAuths(Set<Integer> adminAuths) {
        this.adminAuths.addAll(adminAuths);
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags.addAll(tags);
    }

    public String getIndex() {
        return _index;
    }

    public void setIndex(String index) {
        this._index = index;
    }

    @Override
    @JsonIgnore
    public String getType() {
        return _type;
    }
}
