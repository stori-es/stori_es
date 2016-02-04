package org.consumersunion.stories.dashboard.client.application.story.ui;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;

public class StoryByAuthorCell extends AbstractCell<StorySummary> {
    interface Template extends SafeHtmlTemplates {
        @Template("<div class=\"{1}\">{0}</div>")
        SafeHtml titleTemplate(SafeHtml safeTitle, String safeClass);
    }

    private final Template template;
    private final Resources resources;

    @Inject
    StoryByAuthorCell(
            Template template,
            Resources resources) {
        super(BrowserEvents.CLICK);

        this.template = template;
        this.resources = resources;
    }

    @Override
    public void render(Context context, StorySummary story, SafeHtmlBuilder safeHtmlBuilder) {
        SafeHtml safeTitle = SafeHtmlUtils.fromString(Strings.nullToEmpty(story.getTitle()));
        safeHtmlBuilder.append(template.titleTemplate(safeTitle, resources.generalStyleCss().storyByAuthorCell()));
    }
}
