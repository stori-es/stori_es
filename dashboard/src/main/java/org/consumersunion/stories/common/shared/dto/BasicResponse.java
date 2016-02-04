package org.consumersunion.stories.common.shared.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BasicResponse<T extends ResourceLinks> {
    private Integer id;
    @JsonProperty("created_on")
    private Date createdOn;
    @JsonProperty("updated_on")
    private Date updatedOn;
    private boolean isArchived;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract T getLinks();

    public abstract void setLinks(T links);

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    @JsonProperty("is_archived")
    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    protected abstract static class AbstractBuilder<T extends AbstractBuilder<T, U, L>, U extends BasicResponse<L>,
            L extends ResourceLinks> {
        private Integer id;
        private L links;
        private Date createdOn;
        private Date updatedOn;
        private boolean isArchived;

        protected AbstractBuilder() {
        }

        public T withId(Integer id) {
            this.id = id;
            return self();
        }

        public T withLinks(L links) {
            this.links = links;
            return self();
        }

        public T withCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
            return self();
        }

        public T withUpdatedOn(Date updatedOn) {
            this.updatedOn = updatedOn;
            return self();
        }

        public T isArchived(boolean isArchived) {
            this.isArchived = isArchived;
            return self();
        }

        public U build() {
            U basicResponse = createBasicResponse();
            basicResponse.setId(id);
            basicResponse.setLinks(links);
            basicResponse.setCreatedOn(createdOn);
            basicResponse.setUpdatedOn(updatedOn);
            basicResponse.setArchived(isArchived);
            return basicResponse;
        }

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        protected abstract U createBasicResponse();
    }
}
