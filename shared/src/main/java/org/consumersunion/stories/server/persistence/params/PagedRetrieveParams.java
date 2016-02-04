package org.consumersunion.stories.server.persistence.params;

import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.server.security.BaseAuthParam;

public class PagedRetrieveParams extends BaseAuthParam {
    private final String searchText;

    public PagedRetrieveParams(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            int authRelation,
            Integer effectiveId,
            String searchText) {
        super(start, length, sortField, ascending, authRelation, effectiveId);

        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }
}
