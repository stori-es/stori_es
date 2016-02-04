package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;

public class TagsTextConverterTest {
    private TagsTextConverter tagsTextConverter;

    @Before
    public void setUp() {
        tagsTextConverter = new TagsTextConverter();
    }

    @Test
    public void toText_joinsStringsWithComma() {
        ArrayList<String> tags = Lists.<String>newArrayList("a", "b");

        String result = tagsTextConverter.toText(tags);

        assertThat(result).isEqualTo("a, b");
    }

    @Test
    public void formatText_joinsOnComma() throws Exception {
        String tags = "a, b";

        List<String> result = tagsTextConverter.formatText(tags);

        assertThat(result).containsExactly("a", "b");
    }
}
