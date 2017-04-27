package org.consumersunion.stories.server.index.elasticsearch.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class SearchResult {
    private long took;
    private boolean timed_out;
    private SearchHits hits;

    public long getTook() {
        return took;
    }

    public boolean isTimedOut() {
        return timed_out;
    }

    public SearchHits getHits() {
        return hits;
    }
}
