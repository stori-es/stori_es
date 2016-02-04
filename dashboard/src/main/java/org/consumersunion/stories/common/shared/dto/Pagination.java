package org.consumersunion.stories.common.shared.dto;

public class Pagination {
    private int limit;
    private int offset;
    private Sort sort;
    private Navigation navigation;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit > 50) {
            this.limit = 50;
        } else {
            this.limit = limit;
        }
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public static class Builder {
        private int limit = 8;
        private int offset;
        private Sort sort;
        private Navigation navigation;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder withOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder withSort(Sort sort) {
            this.sort = sort;
            return this;
        }

        public Builder withNavigation(Navigation navigation) {
            this.navigation = navigation;
            return this;
        }

        public Pagination build() {
            Pagination pagination = new Pagination();
            pagination.setLimit(limit);
            pagination.setOffset(offset);
            pagination.setSort(sort);
            pagination.setNavigation(navigation);

            return pagination;
        }
    }
}
