package org.consumersunion.stories.common.shared.model.entity;

import org.consumersunion.stories.common.shared.model.SystemEntity;

/**
 * A (human) referenciable thing. An Entity is a distinct, definable thing. In
 * SYS, the primary purpose of Entities is as a reference point within a Story
 * Document. So, if you type, "Consumer's Union is awesome", "Consumer's Union"
 * is recognized as a potential Entity reference. If confirmed, the reference is
 * then associated to the Consumer's Union Organization (a type of Entity)
 * within the SYS system.
 * <p/>
 * Note this in contrast to a {@link SystemEntity} which is a technical / system
 * entity, as opposed to the more human oriented, human oriented conceptual
 * nature of the Entity. While it's possible and sometimes useful to retrieve
 * and reference Entities directly, it is more common to retrieve final types.
 */
public class Entity extends SystemEntity {
    private Integer profile;
    private String permalink;

    /**
     * New Entity constructor.
     */
    public Entity() {
        super();
    }

    /**
     * Existing Entity constructor.
     */
    public Entity(int id, int version) {
        super(id, version);
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
}
