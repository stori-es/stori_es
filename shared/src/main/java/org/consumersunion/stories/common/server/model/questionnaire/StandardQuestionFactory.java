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

    public Question createStandardQuestion(BlockType standardMeaning, BlockType formType) {
        return createStandardQuestion(standardMeaning, formType, 0);
    }

    public Question createStandardQuestion(BlockType standardMeaning, BlockType formType, int position) {
        Question question = new Question();
        question.setRequired(true);
        question.setStandardMeaning(standardMeaning);

        if (formType != null) {
            question.setFormType(formType);
        }

        String text = setTypeSettings(question);

        question.setLabel(text + position);
        question.setText(text);
        return question;
    }

    private String setTypeSettings(Question question) {
        BlockType standardMeaning = question.getStandardMeaning();
        String text = "";

        if (standardMeaning.equals(BlockType.FIRST_NAME)) {
            text = labels.standQuestFirstName();
            question.setDataType(DataType.DATA_NAME.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning.equals(BlockType.LAST_NAME)) {
            text = labels.standQuestLastName();
            question.setDataType(DataType.DATA_NAME.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning.equals(BlockType.STREET_ADDRESS_1)) {
            text = labels.standQuestStreet();
            question.setDataType(DataType.DATA_STRING.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning.equals(BlockType.CITY)) {
            text = labels.standQuestCity();
            question.setDataType(DataType.DATA_NAME.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning.equals(BlockType.STATE)) {
            text = labels.standQuestState();
            String[] states = StatesUtil.STATE_OPTIONS.concat(", " + StatesUtil.usTerritories)
                    .concat(", " + StatesUtil.armedForces).split(",\\s*");
            for (int i = 0; i < states.length; i++) {
                question.addOption(states[i], states[i]);
            }
            question.setFormType(BlockType.SELECT);
        } else if (standardMeaning.equals(BlockType.ZIP_CODE)) {
            text = labels.standQuestZip();
            question.setDataType(DataType.DATA_ZIP.code());
            question.setFormType(BlockType.TEXT_INPUT);
        } else if (standardMeaning.equals(BlockType.PHONE)) {
            text = labels.standQuestPhone();
            question.setDataType(DataType.DATA_PHONE_NUMBER.code());
            question.setFormType(BlockType.CONTACT);
        } else if (standardMeaning.equals(BlockType.MAILING_OPT_IN)) {
            text = "Opt into Mailing List";
            question.setFormType(BlockType.CHECKBOX);
        } else if (standardMeaning.equals(BlockType.PREFERRED_EMAIL_FORMAT)) {
            text = "Preferred Email Format";
            question.setFormType(BlockType.SELECT);
            question.addOption("Plain Text", "Plain Text");
            question.addOption("HTML", "HTML");
        } else if (standardMeaning.equals(BlockType.STORY_ASK)) {
            text = labels.tellYourStory();
        } else if (standardMeaning.equals(BlockType.UPDATES_OPT_IN)) {
            text = labels.updatesOptIn();
            question.setFormType(BlockType.CHECKBOX);
            question.addOption(labels.optInDetails(), labels.optInDetails());
        }
        return text;
    }
}
