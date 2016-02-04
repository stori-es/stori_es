package org.consumersunion.stories.common.client.ui.block;

import java.util.List;

import org.consumersunion.stories.common.shared.model.questionnaire.Answer;

import com.google.gwt.user.client.ui.IsWidget;

public interface QuestionElement<T> extends IsWidget {
    void display(T question);

    Answer get();

    String getKey();

    void setAnswer(List<String> reportedValues, Boolean enable);
}
