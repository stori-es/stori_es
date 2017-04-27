package org.consumersunion.stories.server.index.elasticsearch;

import org.consumersunion.stories.server.index.Script;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;

public class UpdateByQuery {
    private final Query query;
    private final Script script;

    public UpdateByQuery(Query query, Script script) {
        this.query = query;
        this.script = script;
    }

    public Query getQuery() {
        return query;
    }

    public Script getScript() {
        return script;
    }
}
