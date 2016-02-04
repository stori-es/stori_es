package org.consumersunion.stories.dashboard.client.application.questionnaire.ui;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;

public class DegradedAnswerSetRenderer extends Composite {
    private static final CommonI18nLabels LABELS = GWT.create(CommonI18nLabels.class);

    private final FlexTable table;

    public DegradedAnswerSetRenderer() {
        table = new FlexTable();
        initWidget(table);
    }

    public void setAnswerSet(final AnswerSet answerSet) {
        table.clear();
        table.setWidget(0, 0, new HTMLPanel(LABELS.label()));
        table.setWidget(0, 1, new HTMLPanel(LABELS.values()));
        table.getRowFormatter().addStyleName(0, "-degraded-answer-set-renderer-table-header");

        int row = 1;
        for (Answer answer : answerSet.getAnswers()) {
            table.setWidget(row, 0, new HTMLPanel(SafeHtmlUtils.fromString(answer.getLabel())));
            String reportValue = "";
            for (String value : answer.getReportValues()) {
                if (reportValue.length() > 0) {
                    reportValue += ", ";
                }
                reportValue += value;
            }
            table.setWidget(row, 1, new HTMLPanel(SafeHtmlUtils.fromString(reportValue)));
            table.getCellFormatter().addStyleName(row, 0, "-degraded-answer-set-renderer-label-cell");
            table.getCellFormatter().addStyleName(row, 1, "-degraded-answer-set-renderer-value-cell");
            row += 1;
        }
    }
}
