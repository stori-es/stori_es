package org.consumersunion.stories.dashboard.client.application.profile.ui;

import org.consumersunion.stories.common.client.util.DateParser;
import org.consumersunion.stories.common.client.util.PrimaryTextSanitizerUtil;
import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;

public class NoteCell extends AbstractCell<Document> {
    interface Template extends SafeHtmlTemplates {
        @Template("<div class=\"noteWrapper\">" + "<div class=\"noteStyle\">{0}</div>"
                + "<div class=\"authorStyle\">{2} by {1}</div></div>")
        SafeHtml noteTemplate(SafeHtml note, SafeHtml author, SafeHtml date);
    }

    final String LINK_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private final Template template;
    private final DateParser dateParser;
    private final PrimaryTextSanitizerUtil textSanitizerUtil;

    @Inject
    NoteCell(
            Template template,
            DateParser dateParser,
            PrimaryTextSanitizerUtil textSanitizerUtil) {
        super(BrowserEvents.CLICK);

        this.template = template;
        this.dateParser = dateParser;
        this.textSanitizerUtil = textSanitizerUtil;
    }

    public void render(Context context, Document value, SafeHtmlBuilder sb) {
        SafeHtml formatedDate = SafeHtmlUtils.fromString(dateParser.parseDate(value.getCreated()));
        SafeHtml author = SafeHtmlUtils.fromString(value.getFullPrimaryAuthorName());
        if (Strings.isNullOrEmpty(value.getFullPrimaryAuthorName())) {
            author = SafeHtmlUtils.fromString("anonymous");
        }

        String note = value.getOnlyContent();
        int linkStart = note.indexOf("http");
        if (linkStart > 0 && note.charAt(linkStart - 1) != ' ') {
            String firstPart = note.substring(0, linkStart);
            String lastPart = note.substring(linkStart, note.length());
            note = firstPart + " " + lastPart;
        }

        String[] words = note.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (isValidLink(word)) {
                builder.append(prepareLink(word));
            } else {
                word = textSanitizerUtil.sanitize(word).asString();
                builder.append(word);
            }

            builder.append(" ");
        }

        SafeHtml noteText = SafeHtmlUtils.fromTrustedString(builder.toString());
        sb.append(template.noteTemplate(noteText, author, formatedDate));
    }

    private String prepareLink(String link) {
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append("<a target=\"_blank\" class=\"noteLink\" href=\"");
        linkBuilder.append(link);
        linkBuilder.append("\">");
        linkBuilder.append(link.length() > 40 ? link.substring(0, 40) + "..." : link);
        linkBuilder.append("</a>");

        return linkBuilder.toString();
    }

    private Boolean isValidLink(String link) {
        try {
            RegExp linkExp = RegExp.compile(LINK_REGEX);
            return linkExp.test(link.trim());
        } catch (RuntimeException e) {
            return false;
        }
    }
}
