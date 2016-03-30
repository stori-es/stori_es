package org.consumersunion.stories.common.shared.model;

import java.io.Serializable;
import java.util.Date;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The SystemEntity defines the basic concept of an "independent thing". The
 * idea is aesthetic, but also useful for technical reason. The authorization
 * keys off SystemEntities, as does the audit system. It's also a convenient
 * place to track version information because, by definition, all objects that
 * support CRUD directly are SystemEntities. This is in contrasts with the
 * {@link org.consumersunion.stories.common.shared.model.entity.Entity} class, which has a more specific meaning.
 * <p/>
 * Note that the 'created' and 'lastModified' fields found in the DB are not
 * included in this class definition. The reason is that this data is not
 * normally needed when an Entity is serialized and is more generally retrieved
 * separately as part of an audit process or some similar activity.
 * <p/>
 * Note that a SystemEntity is primarily a technical distinction: a SystemEntity
 * is a first class domain object which is persisted directly. This in contrast
 * to an {@link org.consumersunion.stories.common.shared.model.entity.Entity}
 * which encapsulates a human concept. The following distinction may be helpful:
 * a SystemEntity is what the system refers to, and an Entity is what a user
 * refers to.
 */
public class SystemEntity implements Serializable, IsSerializable, HasId {
    protected static int DEFAULT_ID = -1;
    protected static int DEFAULT_VERSION = 1;

    /**
     * @see #getId()
     */
    private int id;
    /**
     * @see #getVersion()
     */
    private int version;
    /**
     * @see #getIsPublic();
     */
    private boolean isPublic;
    /**
     * @see #getCreated()
     */
    private Date created;
    /**
     * @see #getUpdated()
     */
    private Date updated;

    private long createdTimeStamp;

    /**
     * @see #getOwner()
     */
    private Integer owner;

    private Integer creator;

    /**
     * Constructor for new SystemEntities and serialization
     */
    protected SystemEntity() {
        this.id = DEFAULT_ID;
        this.version = DEFAULT_VERSION;
    }

    /**
     * Constructor for existing SystemEntities.
     *
     * @param id
     */
    public SystemEntity(int id, int version) {
        this.id = id;
        this.version = version;
    }

    /**
     * The ID is the canonical, immutable ID of the object. An ID of -1 means
     * the Entity has been constructed, but not yet saved. Whimsically, we could
     * say it has been conceived but not yet been made real.
     *
     * @return
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Setting the ID should only ever be done after a new record is created in
     * the database.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * The version of the object. Every atomic set of changes results in an
     * incremented version number which is used to guard against concurrent
     * edits / stale writes.
     */
    public int getVersion() {
        return version;
    }

    /**
     * Setting the version should only ever be done after a database update.
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * @see #getIsPublic()
     * @see #setIsPublic(boolean)
     */
    public void setIsPublic(boolean isPublic) {
        setPublic(isPublic);
    }

    /**
     * GWT doesn't deal with getters and setters 'getPublic()' and 'isPublic()' because the field can't be named
     * 'public'. So, the 'g/setIsPublic()' methods are provided for the GWT compiler.
     *
     * @see #isPublic()
     */
    public boolean getIsPublic() {
        return isPublic();
    }

    /**
     * Indicates whether the entity data is public.
     */
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
        if (creator == null) {
            creator = owner;
        }
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * Returns the date of the last modification. If there is no explicit last
     * modification date, we return the value of {@link #getCreated()} as a fall
     * back.
     */
    public Date getUpdated() {
        if (updated == null) {
            return getCreated();
        } else {
            return updated;
        }
    }

    /**
     * see #getUpdated()
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * Test to see if we're dealing with a newly constructed object (as opposed
     * to a retrieved object).
     */
    public boolean isNew() {
        return this.id == -1 && this.version == 1;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SystemEntity other = (SystemEntity) obj;
        return Objects.equal(this.id, other.id) && Objects.equal(this.version, other.version);
    }
}
