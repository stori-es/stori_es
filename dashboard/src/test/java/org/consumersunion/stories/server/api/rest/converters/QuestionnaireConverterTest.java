package org.consumersunion.stories.server.api.rest.converters;

import java.util.Set;

import org.consumersunion.stories.common.shared.dto.post.QuestionnairePost;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QuestionnaireConverterTest {
    QuestionnaireConverter converter = new QuestionnaireConverter();

    @Test
    public void convert_willConvert_whenAllPostFieldsAreNull() {
        // Given
        QuestionnairePost post = new QuestionnairePost();

        // When
        QuestionnaireI15d converted = converter.convert(post);

        // Then
        assertNotNull(converted);
    }

    @Test
    public void convert_willAssignCollectionIds_whenPostContainsCollectionIds() {
        // Given
        QuestionnairePost post = new QuestionnairePost();
        post.setCollectionIds(Lists.newArrayList(1, 2, 3));

        // When
        QuestionnaireI15d converted = converter.convert(post);

        // Then
        Set<Integer> targetCollections = converted.getTargetCollections();
        assertTrue(targetCollections.contains(1));
        assertTrue(targetCollections.contains(2));
        assertTrue(targetCollections.contains(3));
    }
}
