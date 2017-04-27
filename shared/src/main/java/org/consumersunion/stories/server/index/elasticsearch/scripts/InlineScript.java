package org.consumersunion.stories.server.index.elasticsearch.scripts;

import java.util.Map;

import org.consumersunion.stories.server.index.Script;

public abstract class InlineScript implements Script {
    private final String inline;
    private final Map<String, Object> params;

    protected InlineScript(String inline) {
        this(inline, null);
    }

    protected InlineScript(String inline, Map<String, Object> params) {
        this.inline = inline;
        this.params = params;
    }

    public String getInline() {
        return inline;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
