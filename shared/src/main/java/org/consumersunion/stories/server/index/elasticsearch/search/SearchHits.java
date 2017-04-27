package org.consumersunion.stories.server.index.elasticsearch.search;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class SearchHits {
    private long total;
    private double max_score;
    private List<SearchHit> hits;

    public long getTotal() {
        return total;
    }

    public double getMaxScore() {
        return max_score;
    }

    public List<SearchHit> getHits() {
        return hits;
    }
}
