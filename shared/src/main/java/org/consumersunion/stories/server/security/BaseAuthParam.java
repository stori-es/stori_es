package org.consumersunion.stories.server.security;

import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.entity.SortField;

public class BaseAuthParam extends AuthParam<BaseAuthParam> {
    public BaseAuthParam(int start,
            int length,
            SortField sortField,
            boolean ascending,
            int authRelation,
            Integer effectiveId) {
        super(start, length, sortField, ascending, authRelation, effectiveId);
    }

    @Override
    public BaseAuthParam noLimit() {
        return new BaseAuthParam(0, 0, getSortField(), isAscending(), getAuthRelation(), getEffectiveId());
    }
}
