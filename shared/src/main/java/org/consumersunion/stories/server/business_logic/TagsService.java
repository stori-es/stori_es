package org.consumersunion.stories.server.business_logic;

import java.util.Set;

import org.consumersunion.stories.common.shared.model.SystemEntity;

public interface TagsService {
    void addTags(SystemEntity entity, Set<String> tags);

    void setTags(SystemEntity entity, Set<String> tags);
    
    void setAutoTags(SystemEntity entity, Set<String> tags);

    Set<String> getAllTags();

    Set<String> getTags(SystemEntity entity);

    Set<String> getAutoTags(SystemEntity entity);
}
