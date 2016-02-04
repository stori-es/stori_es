package org.consumersunion.stories.survey.client.application.stories.ui;

import org.consumersunion.stories.common.client.util.HtmlSanitizerUtil;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;

public class StoryCell extends AbstractCell<StorySummary> {

    public interface Template extends SafeHtmlTemplates {
        @Template("<div class=\"cu-story-summary-view-main-container\">"
                + "<div class=\"cu-story-summary-view-title-label\">{0}</div>"
                + "<div class=\"cu-story-summary-view-summary-text-label\">{1}</div>"
                + "<div class=\"cu-story-summary-view-summary-submitter-label\">{2}</div>" + "</div>")
        SafeHtml storyTemplate(SafeHtml title, SafeHtml summary, SafeHtml submitter);
    }

    private final Template template;
    private final HtmlSanitizerUtil htmlSanitizerUtil;

    @Inject
    StoryCell(
            Template template,
            HtmlSanitizerUtil htmlSanitizerUtil) {
        super(BrowserEvents.CLICK);

        this.template = template;
        this.htmlSanitizerUtil = htmlSanitizerUtil;
    }

    public void render(Context context, StorySummary value, SafeHtmlBuilder sb) {
        SafeHtml safeTitle = SafeHtmlUtils.fromString(value.getTitle());
        SafeHtml safeSummary = htmlSanitizerUtil.sanitize(value.getSummary());
        SafeHtml safeSubmitter = SafeHtmlUtils.fromString(value.getAuthor());
        sb.append(template.storyTemplate(safeTitle, safeSummary, safeSubmitter));
    }
}
