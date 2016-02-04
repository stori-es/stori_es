package org.consumersunion.stories.common.shared.model.questionnaire;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A localized question. This in contrast to the {@link QuestionI15d} class
 * which carries the full i18n data. A Question in contrast only contains the
 * data a single localization.
 */
@JsonTypeName("question")
@org.codehaus.jackson.annotate.JsonTypeName("question")
public class Question extends QuestionBase {
    private String text;
    private String helpText;
    private List<Option> options;

    public Question() {
        super();
        options = new ArrayList<Option>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public void addOption(String displayValue, String reportValue) {
        if (options == null) {
            options = new LinkedList<Option>();
        }
        options.add(new Option(displayValue, reportValue));
    }

    @Override
    public Object clone() {
        return clone(new Question());
    }

    protected Object clone(Question question) {
        question.setText(text);
        question.setHelpText(helpText);
        question.setDataType(getDataType());
        question.setLabel(getLabel());
        question.setRequired(isRequired());
        question.setMultiselect(isMultiselect());
        question.setMinLength(getMinLength());
        question.setMaxLength(getMaxLength());
        question.setStartDate(getStartDate());
        question.setDocument(getDocument());
        question.setFormType(getFormType());
        question.setStandardMeaning(getStandardMeaning());

        for (Option option : options) {
            question.addOption(option.getDisplayValue(), option.getReportValue());
        }

        return question;
    }
}
