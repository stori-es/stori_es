package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.type.DataType;
import org.consumersunion.stories.common.shared.model.type.State;

import com.google.inject.Inject;

public class StandardQuestionFactory {
    private final CommonI18nLabels labels;

    @Inject
    StandardQuestionFactory(CommonI18nLabels labels) {
        this.labels = labels;
    }

    public Question createStandardQuestion(BlockType blockType) {
        Question question = new Question();
        question.setRequired(blockType.isRequired());
        question.setBlockType(blockType);

        if (blockType == BlockType.FIRST_NAME) {
            question.setLabel(labels.standQuestFirstName());
            question.setText(labels.standQuestFirstName());
            question.setDataType(DataType.DATA_NAME.code());
        } else if (blockType == BlockType.LAST_NAME) {
            question.setLabel(labels.standQuestLastName());
            question.setText(labels.standQuestLastName());
            question.setDataType(DataType.DATA_NAME.code());
        } else if (blockType == BlockType.STREET_ADDRESS_1) {
            question.setLabel(labels.standQuestStreet());
            question.setText(labels.standQuestStreet());
            question.setDataType(DataType.DATA_STRING.code());
        } else if (blockType == BlockType.CITY) {
            question.setLabel(labels.standQuestCity());
            question.setText(labels.standQuestCity());
            question.setDataType(DataType.DATA_NAME.code());
        } else if (blockType == BlockType.STATE) {
            question.setLabel(labels.standQuestState());
            question.setText(labels.standQuestState());
            for (State state : State.values()) {
                question.addOption(state.name(), state.name());
            }
        } else if (blockType == BlockType.ZIP_CODE) {
            question.setLabel(labels.standQuestZip());
            question.setText(labels.standQuestZip());
            question.setDataType(DataType.DATA_ZIP.code());
        } else if (blockType == BlockType.MAILING_OPT_IN) {
            question.setLabel(labels.standOptIn());
            question.setText(labels.standOptIn());
        } else if (blockType == BlockType.PREFERRED_EMAIL_FORMAT) {
            question.setLabel(labels.standPreferredEmailFormat());
            question.setText(labels.standPreferredEmailFormat());
            question.addOption("Plain Text", "Plain Text");
            question.addOption("HTML", "HTML");
        } else if (blockType == BlockType.STORY_ASK_RICH
        		|| blockType == BlockType.STORY_ASK_PLAIN) {
            question.setLabel(labels.tellYourStory());
            question.setText(labels.tellYourStory());
        } else if (blockType == BlockType.UPDATES_OPT_IN) {
            question.setLabel(labels.updatesOptIn());
            question.setText(labels.updatesOptIn());
            question.addOption(labels.optInDetails(), labels.optInDetails());
        } else if (blockType == BlockType.STORY_TITLE) {
            question.setLabel(labels.giveYourStoryATitle());
            question.setText(labels.giveYourStoryATitle());
            question.setHelpText("");
        }

        return question;
    }
}
