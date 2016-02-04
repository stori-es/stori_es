package org.consumersunion.stories.common.shared.dto.post;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PermissionPost extends AbstractEntityData {
	private String title;
	@JsonProperty("organization_id")
	private int organizationId;
    @JsonProperty("entity_ids")
    private List<Integer> entityIds = new ArrayList<Integer>();

    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() {
    	return title;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public int getOrganizationId() {
    	return organizationId;
    }
    
    public void setOrganizationId(int organizationId) {
    	this.organizationId = organizationId;
    }

    public List<Integer> getEntityIds() {
        return entityIds;
    }

    public void setEntityIds(List<Integer> entityIds) {
        clearAndAdd(this.entityIds, entityIds);
    }

    public static class Builder extends AbstractEntityDataBuilder<Builder> {
        private String title;
        private int organizationId;
        private List<Integer> entityIds;

        private Builder() {
        }

        public Builder withTitle(String title) {
        	this.title = title;
        	return this;
        }
        
        public Builder withOrganizationId(int organizationId) {
        	this.organizationId = organizationId;
        	return this;
        }

        public Builder withEntityIds(List<Integer> entityIds) {
            this.entityIds = entityIds;
            return this;
        }

        public PermissionPost build() {
            PermissionPost permissionPost = buildBase();
            permissionPost.setTitle(title);
            permissionPost.setOrganizationId(organizationId);
            permissionPost.setEntityIds(entityIds);

            return permissionPost;
        }

        @Override
        protected PermissionPost createEmpty() {
            return new PermissionPost();
        }
    }
}
