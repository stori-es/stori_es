package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

public enum ContentKind {
    STORY("Story", "icon-comment-alt", "icon-comment-alt"),
    CONTENT("Content", "icon-file-text-alt", "icon-file-text"),
    NOTE("Note", "icon-edit", "icon-edit"),
    ATTACHMENT("Attachment", "icon-link", "icon-link"),
    RESPONSE("Response", "icon-list-ol", "icon-list-ol"),
    QUESTIONNAIRE("Questionnaire", "icon-list-alt", "icon-list-alt"),
    COLLECTION("Collection", "icon-folder-close", "icon-folder-close");

    private final String text;
    private final String icon;
    private final String alternateIcon;

    ContentKind(String text, String icon, String alternateIcon) {
        this.text = text;
        this.icon = icon;
        this.alternateIcon = alternateIcon;
    }

    public String getIcon() {
        return icon;
    }

    public String getAlternateIcon() {
        return alternateIcon;
    }

    public static ContentKind fromRelation(SystemEntityRelation entityRelation) {
        switch (entityRelation) {
            case BODY:
            case CONTENT:
                return CONTENT;
            case NOTE:
                return NOTE;
            case ANSWER_SET:
                return RESPONSE;
            case ATTACHMENT:
                return ATTACHMENT;
        }

        return ContentKind.STORY;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean hasLocale() {
        return equals(CONTENT) || equals(COLLECTION) || equals(QUESTIONNAIRE);
    }
}
