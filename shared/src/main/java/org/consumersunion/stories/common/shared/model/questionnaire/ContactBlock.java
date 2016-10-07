package org.consumersunion.stories.common.shared.model.questionnaire;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("contact")
@org.codehaus.jackson.annotate.JsonTypeName("contact")
public class ContactBlock extends Question {
    private String type;
    private String option;

    public ContactBlock() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public Object clone() {
        ContactBlock contactBlock = new ContactBlock();
        contactBlock.setLabel(getLabel());
        contactBlock.setText(getText());
        contactBlock.setOption(option);
        contactBlock.setType(type);
        contactBlock.setDataType(getDataType());
        contactBlock.setRequired(isRequired());
        contactBlock.setDocument(getDocument());
        contactBlock.setBlockType(getBlockType());

        return contactBlock;
    }
}
