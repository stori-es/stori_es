package org.consumersunion.stories.common.shared.dto;

import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.QuestionnaireMask;

public class RetrievePagedCollectionsParams extends AuthParam<RetrievePagedCollectionsParams> {
    public static class Builder {
        private int accessMode;
        private int questionnaireMask;
        private int permissionMask;
        private int start;
        private int length;
        private SortField sortField;
        private boolean ascending;
        private Integer effectiveId;
        private Integer storyId;
        private boolean includeStoryCount;
        private boolean includeLinkedCollections;

        public Builder() {
        }

        /**
         * @param accessMode as defined in the developer reference
         */
        public Builder withAccessMode(int accessMode) {
            this.accessMode = accessMode;
            return this;
        }

        /**
         * @param questionnaireMask if true, then Questionnaires are included and excluded if false
         */
        public Builder withQuestionnaireMask(int questionnaireMask) {
            this.questionnaireMask = questionnaireMask;
            return this;
        }

        public Builder withPermissionMask(int permissionMask) {
            this.permissionMask = permissionMask;
            return this;
        }

        /**
         * @param start the offset for the paged results
         */
        public Builder withStart(int start) {
            this.start = start;
            return this;
        }

        /**
         * @param length the number of items to return in the paged set
         */
        public Builder withLength(int length) {
            this.length = length;
            return this;
        }

        /**
         * @param sortField the field to sort on
         */
        public Builder withSortField(SortField sortField) {
            this.sortField = sortField;
            return this;
        }

        /**
         * @param ascending whether to sort ascending or descending
         */
        public Builder withAscending(boolean ascending) {
            this.ascending = ascending;
            return this;
        }

        public Builder withEffectiveId(Integer effectiveId) {
            this.effectiveId = effectiveId;
            return this;
        }

        public Builder withStoryId(Integer storyId) {
            this.storyId = storyId;
            return this;
        }

        public Builder withStoryCount(boolean includeStoryCount) {
            this.includeStoryCount = includeStoryCount;
            return this;
        }

        public Builder withLinkedCollections(boolean includeLinkedCollections) {
            this.includeLinkedCollections = includeLinkedCollections;
            return this;
        }

        /**
         * @param searchText the search text to search, which may be null
         */
        public RetrievePagedCollectionsParams build(String searchText) {
            return new RetrievePagedCollectionsParams(start, length, sortField, ascending, searchText,
                    questionnaireMask, accessMode, permissionMask, effectiveId, includeStoryCount,
                    includeLinkedCollections);
        }

        public RetrievePagedCollectionsParams build() {
            return new RetrievePagedCollectionsParams(start, length, sortField, ascending, storyId, accessMode,
                    permissionMask, effectiveId, includeStoryCount, includeLinkedCollections);
        }
    }

    private String searchText;
    private Integer storyId;
    private int questionnaireMask;
    private int permissionMask;
    private boolean includeStoryCount;
    private boolean includeLinkedCollections;

    private RetrievePagedCollectionsParams(int start,
            int length,
            SortField sortField,
            boolean ascending,
            String searchText,
            int questionnaireMask,
            int relation,
            int permissionMask,
            Integer effectiveId,
            boolean includeStoryCount,
            boolean includeLinkedCollections) {
        super(start, length, sortField, ascending, relation, effectiveId);

        this.searchText = searchText;
        this.questionnaireMask = questionnaireMask;
        this.permissionMask = permissionMask;
        this.includeStoryCount = includeStoryCount;
        this.includeLinkedCollections = includeLinkedCollections;
    }

    private RetrievePagedCollectionsParams(int start,
            int length,
            SortField sortField,
            boolean ascending,
            int storyId,
            int relation,
            int permissionMask,
            Integer effectiveId,
            boolean includeStoryCount,
            boolean includeLinkedCollections) {
        super(start, length, sortField, ascending, relation, effectiveId);

        this.storyId = storyId;
        this.includeStoryCount = includeStoryCount;
        this.includeLinkedCollections = includeLinkedCollections;
        this.questionnaireMask = QuestionnaireMask.QUESTIONNAIRE_MASK_ALL;
        this.permissionMask = permissionMask;
    }

    // For serialization
    public RetrievePagedCollectionsParams() {
    }

    public String getSearchText() {
        return searchText;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public int getQuestionnaireMask() {
        return questionnaireMask;
    }

    public int getPermissionMask() {
        return permissionMask;
    }

    public boolean isIncludeStoryCount() {
        return includeStoryCount;
    }

    public boolean isIncludeLinkedCollections() {
        return includeLinkedCollections;
    }

    @Override
    public RetrievePagedCollectionsParams noLimit() {
        if (storyId == null) {
            return new RetrievePagedCollectionsParams(0, 0, getSortField(), isAscending(), getSearchText(),
                    getQuestionnaireMask(), getAuthRelation(), permissionMask, getEffectiveId(), isIncludeStoryCount(),
                    isIncludeLinkedCollections());
        } else {
            return new RetrievePagedCollectionsParams(0, 0, getSortField(), isAscending(), getStoryId(),
                    getAuthRelation(), permissionMask, getEffectiveId(), isIncludeStoryCount(),
                    isIncludeLinkedCollections());
        }
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public void setQuestionnaireMask(int questionnaireMask) {
        this.questionnaireMask = questionnaireMask;
    }

    public void setPermissionMask(int permissionMask) {
        this.permissionMask = permissionMask;
    }

    public void setIncludeStoryCount(boolean includeStoryCount) {
        this.includeStoryCount = includeStoryCount;
    }

    public void setIncludeLinkedCollections(boolean includeLinkedCollections) {
        this.includeLinkedCollections = includeLinkedCollections;
    }
}
