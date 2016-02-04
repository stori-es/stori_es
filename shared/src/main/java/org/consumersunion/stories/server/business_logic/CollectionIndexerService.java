package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.shared.model.Collection;

public interface CollectionIndexerService {
    void index(Collection collection);
}
