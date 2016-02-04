package org.consumersunion.stories.common.client.widget;

public enum MessageStyle {
    INTERACTIVE("message-interactive", "icon-info-sign"),
    ERROR("message-error", "icon-exclamation-sign"),
    SUCCESS("message-success", "icon-ok-sign"),
    WARNING("message-warning", "icon-exclamation-sign"),
    INFORMATION("message-info", "icon-ok-sign");

    private final String style;
    private final String icon;

    MessageStyle(String style, String icon) {
        this.style = style;
        this.icon = icon;
    }

    public String getStyle() {
        return style;
    }

    public String getIcon() {
        return icon;
    }
}
