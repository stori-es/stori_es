package org.consumersunion.stories.dashboard.client.application.questionnaire.common;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.dashboard.client.application.ui.block.StandardQuestionFactory;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(JukitoRunner.class)
public class StandardQuestionFactoryTest {
    @Inject
    StandardQuestionFactory standardQuestionFactory;
    @Inject
    CommonI18nLabels labels;

    @Test
    public void createStandardQuestion_helpTextWillBeEmpty_forStoryTitle() {
        // Given
        BlockType anyFormType = null;

        // When
        Question standardQuestion =
                standardQuestionFactory.createStandardQuestion(BlockType.STORY_TITLE, anyFormType);

        // Then
        assertThat(standardQuestion.getHelpText()).isEmpty();
    }

    @Test
    public void createStandardQuestion_correctText_forStoryTitle() {
        // Given
        BlockType anyFormType = null;
        given(labels.giveYourStoryATitle()).willReturn("some string");

        // When
        Question standardQuestion =
                standardQuestionFactory.createStandardQuestion(BlockType.STORY_TITLE, anyFormType);

        // Then
        assertThat(standardQuestion.getText()).isEqualTo("some string");
    }
}
