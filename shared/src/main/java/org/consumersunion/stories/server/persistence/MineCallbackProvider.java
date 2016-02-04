package org.consumersunion.stories.server.persistence;

import org.consumersunion.stories.common.shared.dto.AuthParam;
import org.consumersunion.stories.common.shared.model.entity.SortField;

public interface MineCallbackProvider {
    String DEFAULT_SORT_CLAUSE = "";
    String DEFAULT_MINE_CLAUSE = null;
    String DEFAULT_JOIN_CLAUSE = "";

    String getMineWhereClause(AuthParam<?> authParam);

    String getMineJoinClause(AuthParam<?> authParam);

    boolean hasMineClause();

    String getSortClause(SortField sortField, boolean ascending);
}
