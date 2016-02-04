package org.consumersunion.stories.server.api.rest.util;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.model.SystemEntity;

public interface ResourceLinksHelper {
    List<ResourceLink> replaceIds(String baseUrl, Iterable<? extends SystemEntity> entities);

    List<ResourceLink> replaceIntIds(String baseUrl, Iterable<Integer> ids);

    List<ResourceLink> replaceStringIds(String baseUrl, Iterable<String> ids);

    ResourceLink replaceId(String baseUrl, Integer id);
}
