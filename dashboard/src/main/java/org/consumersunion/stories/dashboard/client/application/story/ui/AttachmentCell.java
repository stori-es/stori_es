package org.consumersunion.stories.dashboard.client.application.story.ui;

import java.util.Date;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.util.DateParser;
import org.consumersunion.stories.common.client.util.URLUtils;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.resource.GeneralStyle;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
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

/**
 * Cell rending for entity attachments used by {@link
 * org.consumersunion.stories.dashboard.client.application.story.widget.AttachmentsPresenter} and friends.
 */
public class AttachmentCell extends AbstractCell<Document> {
    interface Template extends SafeHtmlTemplates {
        @Template("<div>"
                + "<div class=\"attachedCollections\"><a href=\"{0}\" target=\"_blank\">{1}</a></div>"
                + "<div class=\"{3} {4}\"/></div><div class=\"attachmentAuthor\">{2}</div>")
        SafeHtml attachementTemplate(String url, SafeHtml text, SafeHtml name, String deleteCollection,
                String deleteClass);
    }

    private final Template template;
    private final DateParser dateParser;
    private final StoryTellerDashboardI18nLabels labels;
    private final Delegate<Document> delegateRemove;
    private final GeneralStyle generalStyle;

    @Inject
    AttachmentCell(
            Template template,
            DateParser dateParser,
            StoryTellerDashboardI18nLabels labels,
            Resources resources,
            @Assisted("remove") Delegate<Document> delegateRemove) {
        super(BrowserEvents.CLICK);

        this.template = template;
        this.dateParser = dateParser;
        this.labels = labels;
        this.delegateRemove = delegateRemove;
        generalStyle = resources.generalStyleCss();
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Document value, NativeEvent event,
            ValueUpdater<Document> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if (BrowserEvents.CLICK.equals(event.getType())) {
            EventTarget eventTarget = event.getEventTarget();
            if (!Element.is(eventTarget)) {
                return;
            }

            if ($(Element.as(eventTarget)).hasClass(generalStyle.deleteCollection())) {
                delegateRemove.execute(value);
            }
        }
    }

    @Override
    public void render(Context context, Document value, SafeHtmlBuilder safeHtmlBuilder) {
        SafeHtml safeTitle = SafeHtmlUtils.fromString(getTitle(value));
        SafeHtml safeName = SafeHtmlUtils.fromString(getDateCreated(value.getCreated(), getAuthorName(value)));
        String permalink = URLUtils.appendDefaultProtocol(value.getPermalink());

        SafeHtml tmp = template.attachementTemplate(permalink, safeTitle, safeName, generalStyle.deleteCollection(),
                generalStyle.delete());
        safeHtmlBuilder.append(tmp);
    }

    private String getAuthorName(Document value) {
        return Strings.nullToEmpty(value.getPrimaryAuthorFirstName())
                + " " + Strings.nullToEmpty(value.getPrimaryAuthorLastName());
    }

    private String getDateCreated(Date created, String authorName) {
        return labels.attachmentBy(dateParser.parseDate(created), authorName);
    }

    private String getTitle(Document value) {
        if (Strings.isNullOrEmpty(value.getTitle())) {
            return value.getPermalink();
        } else {
            return value.getTitle();
        }
    }
}
