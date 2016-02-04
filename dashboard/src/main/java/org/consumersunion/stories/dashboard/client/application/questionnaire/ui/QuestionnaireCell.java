package org.consumersunion.stories.dashboard.client.application.questionnaire.ui;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class QuestionnaireCell extends AbstractCell<QuestionnaireI15d> {
    interface Template extends SafeHtmlTemplates {
        @Template("<div class=\"questionnaire\">" +
                "<span class=\"questionnaireTitle\">{0} ({1})</span> " +
                "<div style=\"display: inline-block\">" +
                "<div class=\"{3} {4} simptip-position-top simptip-fade\" data-tooltip=\"{2}\"/>" +
                "</div></div></div>")
        SafeHtml questionnaireTemplate(SafeHtml title, SafeHtml responses, String toolTipMessage,
                String deleteCollection, String deleteClass);
    }

    private final Template template;
    private final CommonI18nLabels labels;
    private final GeneralStyle generalStyle;
    private final Delegate<QuestionnaireI15d> detailDelegate;
    private final Delegate<QuestionnaireI15d> deleteDelegate;

    @Inject
    QuestionnaireCell(
            Template template,
            CommonI18nLabels labels,
            Resources resources,
            @Assisted("detail") Delegate<QuestionnaireI15d> detailDelegate,
            @Assisted("delete") Delegate<QuestionnaireI15d> deleteDelegate) {
        super(BrowserEvents.CLICK);

        this.template = template;
        this.labels = labels;
        this.detailDelegate = detailDelegate;
        this.deleteDelegate = deleteDelegate;
        generalStyle = resources.generalStyleCss();
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, QuestionnaireI15d value, NativeEvent event,
            ValueUpdater<QuestionnaireI15d> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if (BrowserEvents.CLICK.equals(event.getType())) {
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }

            Element clickedElement = Element.as(eventTarget);
            if ($(clickedElement).hasClass("questionnaireTitle")) {
                detailDelegate.execute(value);
            } else if ($(clickedElement).hasClass(generalStyle.deleteCollection())) {
                deleteDelegate.execute(value);
            }
        }
    }

    @Override
    public void render(Context context, QuestionnaireI15d value, SafeHtmlBuilder sb) {
        SafeHtml safeTitle = SafeHtmlUtils.fromString(value.getTitle());
        SafeHtml safeNumberResponses = SafeHtmlUtils.fromString(String.valueOf(value.getNumberOfReponses()));

        sb.append(template.questionnaireTemplate(safeTitle, safeNumberResponses, labels.clickToRemove(),
                generalStyle.deleteCollection(), generalStyle.delete()));
    }
}
