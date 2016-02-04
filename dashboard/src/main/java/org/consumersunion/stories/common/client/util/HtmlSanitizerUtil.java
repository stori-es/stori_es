/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * The original file was modified to allow child classes to override the tag 
 * whitelist.
 */

package org.consumersunion.stories.common.client.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.google.common.base.Strings;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.HtmlSanitizer;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class HtmlSanitizerUtil implements HtmlSanitizer {
    private static final HtmlSanitizerUtil INSTANCE = new HtmlSanitizerUtil();

    protected Set<String> TAG_WHITELIST = new HashSet<String>(Arrays.asList("a", "abbr", "acronym",
            "address", "area", "b", "big", "blockquote", "br", "br /", "button", "caption", "center", "cite", "code",
            "col", "colgroup", "dd", "del", "dfn", "dir", "div", "dl", "dt", "em", "fieldset", "font", "form", "hr",
            "i", "ins", "kbd", "label", "legend", "li", "map", "menu", "ol", "optgroup", "option", "p", "pre", "q",
            "s", "samp", "select", "small", "span", "strike", "strong", "style", "sub", "sup", "table", "tbody",
            "td", "textarea", "tfoot", "th", "thead", "tr", "tt", "u", "ul", "var"));

    private static final Set<String> ATTRIBUTES_WHITELIST =
            new HashSet<String>(Arrays.asList("href", "size", "style", "face"));

    private static final RegExp tagWithoutAttrs = RegExp.compile("(\\w*).*?");

    /**
     * Return a singleton SimpleHtmlSanitizer instance.
     *
     * @return the instance
     */
    public static HtmlSanitizerUtil getInstance() {
        return INSTANCE;
    }

    /**
     * HTML-sanitizes a string.
     * <p/>
     * <p/>
     * The input string is processed as described above. The result of sanitizing
     * the string is guaranteed to be safe to use (with respect to XSS
     * vulnerabilities) in HTML contexts, and is returned as an instance of the
     * {@link com.google.gwt.safehtml.shared.SafeHtml} type.
     *
     * @param html the input String
     * @return a sanitized SafeHtml instance
     */
    public SafeHtml sanitize(String html) {
        if (html == null) {
            throw new NullPointerException("html is null");
        }
        return SafeHtmlUtils.fromTrustedString(simpleSanitize(html.replaceAll("\\n", "<br/>")));
    }

    /*
    * Sanitize a string containing simple HTML markup as defined above. The
    * approach is as follows: We split the string at each occurence of '<'. Each
    * segment thus obtained is inspected to determine if the leading '<' was
    * indeed the start of a whitelisted tag or not. If so, the tag is emitted
    * unescaped, and the remainder of the segment (which cannot contain any
    * additional tags) is emitted in escaped form. Otherwise, the entire segment
    * is emitted in escaped form.
    *
    * In either case, EscapeUtils.htmlEscapeAllowEntities is used to escape,
    * which escapes HTML but does not double escape existing syntactially valid
    * HTML entities.
    */
    private String simpleSanitize(String text) {
        StringBuilder sanitized = new StringBuilder();
        RegExp attributesRegex = RegExp.compile("([a-zA-Z]*)=\"([^\"]*)\"", "g");
        // Strip position and (min-|max-) height and width directives from 'style' attributes.
        RegExp styleCleaner = RegExp.compile("(position\\s*:\\s|(min-|max-)?(width|height)\\s*:\\s*\\d+)[^ ;]*;?",
                "gi");

        boolean firstSegment = true;
        for (String segment : text.split("<", -1)) {
            if (firstSegment) {
                /*
                *  the first segment is never part of a valid tag; note that if the
                *  input string starts with a tag, we will get an empty segment at the
                *  beginning.
                */
                firstSegment = false;
                sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(segment));
                continue;
            }

            /*
            *  determine if the current segment is the start of an attribute-free tag
            *  or end-tag in our whitelist
            */
            int tagStart = 0; // will be 1 if this turns out to be an end tag.
            int tagEnd = segment.indexOf('>');
            int segmentEnd = tagEnd;
            String tag;
            boolean isValidTag = false;
            if (tagEnd > 0) {
                tag = segment.substring(tagStart, tagEnd);
                if (segment.charAt(0) == '/') {
                    tagStart = 1;
                }

                if (tag.contains(" ")) {
                    tagEnd = tag.indexOf(" ");
                    MatchResult attributesResult = attributesRegex.exec(tag);
                    while (attributesResult != null) {
                        String attribute = attributesResult.getGroup(1);
                        if (!ATTRIBUTES_WHITELIST.contains(attribute)) {
                            String attributeToRemove = attributesResult.getGroup(0);
                            segment = segment.replace(attributeToRemove, "");
                            segmentEnd -= attributeToRemove.length();
                        } else {
                            String attributeToEscape = attributesResult.getGroup(0);
                            String attributeValue = attributesResult.getGroup(2);
                            String escapedAttribute = attributeToEscape.replace(attributeValue,
                                    SafeHtmlUtils.fromString(attributeValue).asString());
                            if ("style".equals(attribute)) {
                                escapedAttribute = styleCleaner.replace(escapedAttribute, "");
                            }
                            segment = segment.replace(attributeToEscape, escapedAttribute);
                            segmentEnd += (escapedAttribute.length() - attributeToEscape.length());
                        }

                        attributesResult = attributesRegex.exec(tag);
                    }
                }

                tag = tag.substring(tagStart, tagEnd);
                if (tag.endsWith("/")) {
                    isValidTag = TAG_WHITELIST.contains(tag.substring(0, tag.length() - 1));
                } else {
                    isValidTag = TAG_WHITELIST.contains(tag);
                }
            }

            if (isValidTag) {
                if (tagStart == 0) {
                    sanitized.append('<');
                } else {
                    // we had seen an end-tag
                    sanitized.append("<");
                }
                sanitized.append(segment.substring(0, segmentEnd).trim()).append(">");

                // append the rest of the segment, escaping it
                sanitized.append(SafeHtmlUtils.htmlEscapeAllowEntities(segment.substring(segmentEnd + 1)));
            } else {
                sanitized.append(segment.substring(segmentEnd + 1).trim());
            }
        }
        String cleaned = sanitized.toString();
        if (cleaned.contains("&#39;")) {
            cleaned = cleaned.replaceAll("&#39;", "'");
        }
        cleaned = cleaned.replaceAll("style=\"[^\"]*?&[^\"]*?\"", "");
        cleaned = cleaned.replaceAll("style=\"[^\"]*?java[^\"]*?\"", "");

        if (!Strings.isNullOrEmpty(cleaned)) {
            cleaned = makeValidHtml(cleaned);
        }

        return cleaned;
    }

    private static String makeValidHtml(String cleaned) {
        RegExp tagRegex = RegExp.compile("<([^>]*)>([^<]*)", "g");
        StringBuilder html = new StringBuilder();
        MatchResult result = tagRegex.exec(cleaned);
        Stack<String> tags = new Stack<String>();

        if (result != null && result.getIndex() > 0) {
            html.append(cleaned.substring(0, result.getIndex()));
        } else if (result == null) {
            html.append(cleaned);
        }
        while (result != null) {
            String tag = result.getGroup(1).trim();
            String content = result.getGroup(2);

            if (tag.startsWith("/")) {
                if (!tags.empty()) {
                    String openingTag = tags.pop();
                    if (tagsMatches(openingTag, tag)) {
                        appendTag(html, tag, content);
                        result = tagRegex.exec(cleaned);
                    } else {
                        appendClosingTag(html, openingTag);
                    }
                } else if (tags.empty() && tagRegex.getLastIndex() > 0) {
                    html.append(content);
                    break;
                }
            } else {
                if (isNotSimpleOrSelfClosingTag(tag)) {
                    tags.push(tag);
                }

                appendTag(html, tag, content);
                result = tagRegex.exec(cleaned);
            }
        }

        // close the remaining tags
        while (!tags.empty()) {
            String tag = tags.pop();
            appendClosingTag(html, tag);
        }

        return html.toString();
    }

    private static void appendClosingTag(StringBuilder html, String tag) {
        html.append("</").append(tag).append(">");
    }

    private static void appendTag(StringBuilder html, String tag, String content) {
        html.append("<").append(tag).append(">").append(content);
    }

    private static boolean tagsMatches(String openingTag, String closingTag) {
        return getTagWithoutAttributes(openingTag).equals(closingTag.substring(1));
    }

    private static String getTagWithoutAttributes(String tag) {
        MatchResult result = tagWithoutAttrs.exec(tag);
        if (result == null) {
            return tag;
        } else {
            return result.getGroup(1);
        }
    }

    private static boolean isNotSimpleOrSelfClosingTag(String lastTag) {
        return !(lastTag.endsWith("/") || lastTag.equals("br"));
    }

    HtmlSanitizerUtil() {
    }
}
