package org.consumersunion.stories.common.client.ui.block.question;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.form.RichTextToolbar;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class RichTextQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-richTextBox";

    private final RichTextArea richText;

    private Question question;

    @Inject
    RichTextQuestion(
            Binder uiBinder,
            RichTextToolbar richTextToolbar,
            @Assisted Question question) {
        super(uiBinder, question, true, STYLE);

        this.question = question;
        this.richText = new RichTextArea();

        questionPanel.add(richTextToolbar);
        questionPanel.add(richText);

        richText.getElement().getStyle().setWidth(99, Style.Unit.PCT);

        richTextToolbar.initialize(richText);
    }

    @Override
    public void display(Question question) {
        this.question = question;

        initQuestion(question, true, STYLE);
    }

    @Override
    public Answer get() {
        if (validate(getHtml())) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(question.getLabel());
            answer.setDisplayValue(question.getLabel());
            answer.setReportValues(Collections.singletonList(getHtml()));
            return answer;
        }

        return null;
    }

    @Override
    public String getKey() {
        return question.getLabel();
    }

    @Override
    public void setAnswer(List<String> reportedValues, Boolean enable) {
        if (!enable) {
            questionPanel.clear();

            HTML richText = new HTML(reportedValues.get(0));
            richText.setStyleName("gwt-TextBox");
            richText.getElement().setAttribute("disabled", "");

            questionPanel.add(richText);
        } else {
            richText.setHTML(reportedValues.get(0));
            richText.setEnabled(enable);
        }
    }

    private String getHtml() {
        String html = richText.getHTML();
        return isEmptyLine(html) ? "" : html;
    }

    private boolean isEmptyLine(String html) {
        return "<br>".equals(html) || "<br/>".equals(html);
    }
}
