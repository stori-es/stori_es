package org.consumersunion.stories.server.index.elasticsearch.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Functions;

import static com.google.common.collect.Collections2.transform;

public class Ids {
    private String type;
    private List<String> values;

    public static Ids fromInts(Collection<Integer> ids) {
        return new Ids(transform(ids, Functions.toStringFunction()));
    }

    public static Ids fromInt(int input) {
        return new Ids(String.valueOf(input));
    }

    public Ids(String type, String[] ids) {
        this.type = type;
        this.values = Arrays.asList(ids);
    }

    public Ids(String... ids) {
        this.values = Arrays.asList(ids);
    }

    public Ids(Collection<String> ids) {
        this.values = new ArrayList<String>(ids);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
