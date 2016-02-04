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

    public Question createStandardQuestion(BlockType standardMeaning, BlockType formType) {
        Question question = new Question();
        question.setRequired(BlockType.isRequired(standardMeaning));
        question.setStandardMeaning(standardMeaning);
        question.setFormType(formType);

        if (standardMeaning == BlockType.FIRST_NAME) {
            question.setLabel(labels.standQuestFirstName());
            question.setText(labels.standQuestFirstName());
            question.setDataType(DataType.DATA_NAME.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning == BlockType.LAST_NAME) {
            question.setLabel(labels.standQuestLastName());
            question.setText(labels.standQuestLastName());
            question.setDataType(DataType.DATA_NAME.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning == BlockType.STREET_ADDRESS_1) {
            question.setLabel(labels.standQuestStreet());
            question.setText(labels.standQuestStreet());
            question.setDataType(DataType.DATA_STRING.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning == BlockType.CITY) {
            question.setLabel(labels.standQuestCity());
            question.setText(labels.standQuestCity());
            question.setDataType(DataType.DATA_NAME.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning == BlockType.STATE) {
            question.setLabel(labels.standQuestState());
            question.setText(labels.standQuestState());
            question.setFormType(BlockType.SELECT);
            for (State state : State.values()) {
                question.addOption(state.name(), state.name());
            }
        } else if (standardMeaning == BlockType.ZIP_CODE) {
            question.setLabel(labels.standQuestZip());
            question.setText(labels.standQuestZip());
            question.setDataType(DataType.DATA_ZIP.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning == BlockType.MAILING_OPT_IN) {
            question.setLabel(labels.standOptIn());
            question.setText(labels.standOptIn());
            question.setFormType(BlockType.CHECKBOX);
        } else if (standardMeaning == BlockType.PREFERRED_EMAIL_FORMAT) {
            question.setLabel(labels.standPreferredEmailFormat());
            question.setText(labels.standPreferredEmailFormat());
            question.setFormType(BlockType.SELECT);
            question.addOption("Plain Text", "Plain Text");
            question.addOption("HTML", "HTML");
        } else if (standardMeaning == BlockType.STORY_ASK) {
            question.setLabel(labels.tellYourStory());
            question.setText(labels.tellYourStory());
        } else if (standardMeaning == BlockType.UPDATES_OPT_IN) {
            question.setLabel(labels.updatesOptIn());
            question.setText(labels.updatesOptIn());
            question.setFormType(BlockType.CHECKBOX);
            question.addOption(labels.optInDetails(), labels.optInDetails());
        } else if (standardMeaning == BlockType.STORY_TITLE) {
            question.setFormType(BlockType.TEXT_INPUT);
            question.setLabel(labels.giveYourStoryATitle());
            question.setText(labels.giveYourStoryATitle());
            question.setHelpText("");
        }

        return question;
    }
}
