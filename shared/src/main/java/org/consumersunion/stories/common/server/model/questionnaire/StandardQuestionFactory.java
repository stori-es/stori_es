package org.consumersunion.stories.common.server.model.questionnaire;

import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.type.DataType;
import org.consumersunion.stories.common.shared.util.StatesUtil;
import org.consumersunion.stories.i18n.CommonI18nLabels;

public class StandardQuestionFactory {
    private CommonI18nLabels labels;

    public StandardQuestionFactory(CommonI18nLabels labels) {
        this.labels = labels;
    }

    public Question createStandardQuestion(BlockType blockType) {
        return createStandardQuestion(blockType, 0);
    }

    public Question createStandardQuestion(BlockType blockType, int position) {
        Question question = new Question();
        question.setRequired(true);
        question.setBlockType(blockType);

        String text = setTypeSettings(question);

        question.setLabel(text + position);
        question.setText(text);
        return question;
    }

    private String setTypeSettings(Question question) {
        BlockType blockType = question.getBlockType();
        String text = "";

        if (blockType.equals(BlockType.FIRST_NAME)) {
            text = labels.standQuestFirstName();
            question.setDataType(DataType.DATA_NAME.code());
        } else if (blockType.equals(BlockType.LAST_NAME)) {
            text = labels.standQuestLastName();
            question.setDataType(DataType.DATA_NAME.code());
        } else if (blockType.equals(BlockType.STREET_ADDRESS_1)) {
            text = labels.standQuestStreet();
            question.setDataType(DataType.DATA_STRING.code());
        } else if (blockType.equals(BlockType.CITY)) {
            text = labels.standQuestCity();
            question.setDataType(DataType.DATA_NAME.code());
        } else if (blockType.equals(BlockType.STATE)) {
            text = labels.standQuestState();
            String[] states = StatesUtil.STATE_OPTIONS.concat(", " + StatesUtil.usTerritories)
                    .concat(", " + StatesUtil.armedForces).split(",\\s*");
            for (int i = 0; i < states.length; i++) {
                question.addOption(states[i], states[i]);
            }
        } else if (blockType.equals(BlockType.ZIP_CODE)) {
            text = labels.standQuestZip();
            question.setDataType(DataType.DATA_ZIP.code());
        } else if (blockType.equals(BlockType.PHONE)) {
            text = labels.standQuestPhone();
            question.setDataType(DataType.DATA_PHONE_NUMBER.code());
        } else if (blockType.equals(BlockType.MAILING_OPT_IN)) {
            text = "Opt into Mailing List";
        } else if (blockType.equals(BlockType.PREFERRED_EMAIL_FORMAT)) {
            text = "Preferred Email Format";
            question.addOption("Plain Text", "Plain Text");
            question.addOption("HTML", "HTML");
        } else if (blockType.equals(BlockType.STORY_ASK_RICH) || blockType.equals(BlockType.STORY_ASK_PLAIN)) {
            text = labels.tellYourStory();
        } else if (blockType.equals(BlockType.UPDATES_OPT_IN)) {
            text = labels.updatesOptIn();
            question.addOption(labels.optInDetails(), labels.optInDetails());
        }
        return text;
    }
}
