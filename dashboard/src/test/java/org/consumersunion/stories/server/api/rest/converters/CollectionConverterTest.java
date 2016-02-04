package org.consumersunion.stories.server.api.rest.converters;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.consumersunion.stories.common.shared.dto.post.CollectionPost;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionConverterTest {
    CollectionConverter converter = new CollectionConverter();

    @Test
    public void convert_willConvert_whenAllFieldsAreNull() {
        // Given
        CollectionPost collectionPost = new CollectionPost();

        // When
        Collection converted = converter.convert(collectionPost);

        // Then
        assertThat(converted).isNotNull();
    }

    @Test
    public void convert_willApplyStoryIds_whenStoryIdsAreProvided() {
        // Given
        CollectionPost collectionPost = new CollectionPost();
        collectionPost.setStoryIds(Lists.newArrayList(1, 2, 3));

        // When
        Collection converted = converter.convert(collectionPost);

        // Then
        Set<StoryLink> stories = converted.getStories();
        assertThat(Assertions.extractProperty("story", Integer.class).from(stories)).containsExactly(1, 2, 3);
    }

    @Test
    public void convert_willQuestionnaireIds_whenQuestionnaireIdsAreProvided() {
        // Given
        CollectionPost collectionPost = new CollectionPost();
        collectionPost.setQuestionnaireIds(Lists.newArrayList(1, 2, 3));

        // When
        Collection converted = converter.convert(collectionPost);

        // Then
        Set<Integer> questionnaires = converted.getCollectionSources();
        assertThat(questionnaires).containsExactly(1, 2, 3);
    }
}
