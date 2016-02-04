package org.consumersunion.stories.server.util;

import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

import static java.util.regex.Pattern.MULTILINE;

@Component
public class HtmlStripper {
    public String striptHtml(String content) {
        if (!Strings.isNullOrEmpty(content)) {
            String cleanedHtml = Jsoup.clean(content, "", Whitelist.none().addTags("br", "p", "li", "ol", "ul"),
                    new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));

            cleanedHtml = Jsoup.clean(cleanedHtml, "", Whitelist.none(),
                    new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml).prettyPrint(false))
                    .replaceAll("\n", "\r\n");

            return Pattern.compile("^\\s*", MULTILINE).matcher(cleanedHtml).replaceAll("");
        } else {
            return "";
        }
    }
}
