package org.consumersunion.stories.server.business_logic;

import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.TagsPersister;
import org.consumersunion.stories.server.persistence.TagsPersister.TagsParams;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

@Component
public class TagsServiceImpl implements TagsService {
    private final SystemEntityService systemEntityService;
    private final AuthorizationService authService;
    private final UserService userService;
    private final TagsPersister tagsPersister;

    @Inject
    TagsServiceImpl(
            SystemEntityService systemEntityService,
            AuthorizationService authService,
            UserService userService,
            TagsPersister tagsPersister) {
        this.systemEntityService = systemEntityService;
        this.authService = authService;
        this.userService = userService;
        this.tagsPersister = tagsPersister;
    }

    @Override
    public void addTags(SystemEntity entity, Set<String> tags) {
        SystemEntity targetObj = systemEntityService.getSystemEntity(entity.getId());

        if (authService.isUserAuthorized(AuthConstants.ROLE_CURATOR, targetObj)) {
            try {
                Set<String> tagsT = getTags(entity);
                Set<String> entityTags = Sets.newLinkedHashSet(tagsT);
                entityTags.addAll(tags);

                TagsParams params = new TagsParams(entity, entityTags);
                tagsPersister.updateTags(params);
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    @Override
    public void setTags(SystemEntity entity, Set<String> tags) {
        if (tags == null) {
            return;
        }

        SystemEntity targetObj = systemEntityService.getSystemEntity(entity.getId());

        if (authService.isUserAuthorized(AuthConstants.ROLE_CURATOR, targetObj)) {
            try {
                Set<String> entityTags = Sets.newLinkedHashSet(tags);

                TagsParams params = new TagsParams(entity, entityTags);
                tagsPersister.updateTags(params);
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    @Override
    public void setAutoTags(SystemEntity entity, Set<String> tags) {
        if (tags == null) {
            return;
        }

        userService.getLoggedInUser(true);

        SystemEntity targetObj = systemEntityService.getSystemEntity(entity.getId());

        if (authService.isUserAuthorized(AuthConstants.ROLE_CURATOR, targetObj)) {
            try {
                TagsParams params = new TagsParams(entity, tags);
                tagsPersister.setAutoTags(params);
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }
    }

    @Override
    public Set<String> getAllTags() {
        return tagsPersister.getAllTags();
    }

    @Override
    public Set<String> getTags(SystemEntity entity) {
        SystemEntity targetObj = systemEntityService.getSystemEntity(entity.getId());

        if (authService.isUserAuthorized(AuthConstants.ROLE_CURATOR, targetObj)) {
            try {
                TagsParams params = new TagsParams(entity, null);
                return tagsPersister.getTags(params);
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }

        return null;
    }

    @Override
    public Set<String> getAutoTags(SystemEntity entity) {
        // TODO : Add Authorization Check - REF SYSTHREE-991
        return tagsPersister.getAutoTags(new TagsParams(entity));
    }
}
