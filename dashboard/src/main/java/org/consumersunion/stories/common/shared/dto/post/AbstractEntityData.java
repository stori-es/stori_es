package org.consumersunion.stories.common.shared.dto.post;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

public abstract class AbstractEntityData {
    private Set<String> tags;
    @NotNull(message = "title must not be null")
    private String title;
    private String summary;

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = ensureSet(this.tags);

        clearAndAdd(this.tags, tags);
    }

    public String getTitle() {
        return title;
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

    protected <T> void clearAndAdd(Collection<T> receiver, Collection<T> toAdd) {
        receiver.clear();
        if (toAdd != null) {
            receiver.addAll(toAdd);
        }
    }

    protected <T> Set<T> ensureSet(Set<T> set) {
        if (set == null) {
            set = new LinkedHashSet<T>();
        }

        return set;
    }

    protected <T> List<T> ensureList(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }

        return list;
    }

    public abstract static class AbstractEntityDataBuilder<T extends AbstractEntityDataBuilder<T>> {
        private Set<String> tags;
        private String title;
        private String summary;

        protected AbstractEntityDataBuilder() {
        }

        public T withTags(Set<String> tags) {
            this.tags = tags;
            return self();
        }

        public T withTitle(String title) {
            this.title = title;
            return self();
        }

        public T withSummary(String summary) {
            this.summary = summary;
            return self();
        }

        protected <R extends AbstractEntityData> R buildBase() {
            AbstractEntityData abstractEntityData = createEmpty();
            abstractEntityData.setTags(tags);
            abstractEntityData.setTitle(title);
            abstractEntityData.setSummary(summary);

            return (R) abstractEntityData;
        }

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        protected abstract <R extends AbstractEntityData> R createEmpty();
    }
}
