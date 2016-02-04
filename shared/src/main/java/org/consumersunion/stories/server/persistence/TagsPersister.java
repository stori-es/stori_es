package org.consumersunion.stories.server.persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

@Component
public class TagsPersister {
    private final PersistenceService persistenceService;
    private final TagsPersistenceHelper tagsPersistenceHelper;

    @Inject
    TagsPersister(
            PersistenceService persistenceService,
            TagsPersistenceHelper tagsPersistenceHelper) {
        this.persistenceService = persistenceService;
        this.tagsPersistenceHelper = tagsPersistenceHelper;
    }

    public Set<String> getAutoTags(TagsParams params) {
        return persistenceService.process(new GetAutoTagsFunc(params, tagsPersistenceHelper));
    }

    public void updateTags(TagsParams params) {
        persistenceService.process(new UpdateTagsFunc(params, tagsPersistenceHelper));
    }

    public void setAutoTags(TagsParams params) {
        persistenceService.process(new UpdateAutoTagsFunc(params, tagsPersistenceHelper));
    }

    public Set<String> getAllTags() {
        TagsParams params = new TagsParams(null, null);

        return persistenceService.process(new TagsPersister.GetTagsFunc(params, tagsPersistenceHelper));
    }

    public Set<String> getTags(TagsParams params) {
        return persistenceService.process(new GetTagsFunc(params, tagsPersistenceHelper));
    }

    public static class TagsParams {
        private final SystemEntity entity;
        private final java.util.Collection<String> tags;

        public TagsParams(SystemEntity entity, java.util.Collection<String> tags) {
            this.entity = entity;
            this.tags = tags;
        }

        public TagsParams(SystemEntity entity) {
            this(entity, null);
        }

        public SystemEntity getEntity() {
            return entity;
        }

        public java.util.Collection<String> getTags() {
            return tags;
        }
    }

    public static class UpdateTagsFunc extends ProcessFunc<TagsParams, Integer> {
        private final TagsPersistenceHelper tagsPersistenceHelper;

        UpdateTagsFunc(
                TagsParams input,
                TagsPersistenceHelper tagsPersistenceHelper) {
            super(input);

            this.tagsPersistenceHelper = tagsPersistenceHelper;
        }

        @Override
        public Integer process() {
            try {
                tagsPersistenceHelper.updateTags(input.getEntity(), input.getTags(), conn);

                PreparedStatement update = conn.prepareStatement("UPDATE systemEntity SET lastModified =? WHERE id=?");

                update.setTimestamp(1, new Timestamp(new Date().getTime()));
                update.setInt(2, input.getEntity().getId());
                update.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
            return 0;
        }
    }

    public static class UpdateAutoTagsFunc extends ProcessFunc<TagsParams, Integer> {
        private final TagsPersistenceHelper tagsPersistenceHelper;

        UpdateAutoTagsFunc(
                TagsParams input,
                TagsPersistenceHelper tagsPersistenceHelper) {
            super(input);

            this.tagsPersistenceHelper = tagsPersistenceHelper;
        }

        @Override
        public Integer process() {
            try {
                tagsPersistenceHelper.updateAutoTags(input.getEntity(), input.getTags(), conn);
                PreparedStatement update = conn.prepareStatement("UPDATE systemEntity SET lastModified =? WHERE id=?");

                update.setTimestamp(1, new Timestamp(new Date().getTime()));
                update.setInt(2, input.getEntity().getId());
                update.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
            return 0;
        }
    }

    public static class GetTagsFunc extends ProcessFunc<TagsParams, Set<String>> {
        private final TagsPersistenceHelper tagsPersistenceHelper;

        GetTagsFunc(
                TagsParams input,
                TagsPersistenceHelper tagsPersistenceHelper) {
            super(input);

            this.tagsPersistenceHelper = tagsPersistenceHelper;
        }

        @Override
        public Set<String> process() {
            try {
                if (input.getEntity() == null) {
                    return tagsPersistenceHelper.getTags(conn);
                } else {
                    return tagsPersistenceHelper.getTags(input.getEntity(), conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    public static class GetAutoTagsFunc extends ProcessFunc<TagsParams, Set<String>> {
        private final TagsPersistenceHelper tagsPersistenceHelper;

        GetAutoTagsFunc(
                TagsParams input,
                TagsPersistenceHelper tagsPersistenceHelper) {
            super(input);

            this.tagsPersistenceHelper = tagsPersistenceHelper;
        }

        @Override
        public Set<String> process() {
            try {
                if (input.getEntity() == null) {
                    return tagsPersistenceHelper.getTags(conn);
                } else {
                    return tagsPersistenceHelper.getAutoTags(input.getEntity(), conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }
}
