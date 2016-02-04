package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.model.questionnaire.HasCode;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

public class TextImage implements Serializable {
    public enum Position implements HasCode {
        LEFT("LEFT"),
        RIGHT("RIGHT");

        public static final Position DEFAULT = LEFT;

        private final String code;

        Position(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        public static Position fromCode(String code) {
            if (Strings.isNullOrEmpty(code)) {
                return null;
            }

            for (Position position : Position.values()) {
                if (code.equals(position.code)) {
                    return position;
                }
            }

            return null;
        }
    }

    public enum Size implements HasCode {
        SMALL("SMALL"),
        MEDIUM("MEDIUM"),
        LARGE("LARGE");

        public static final Size DEFAULT = MEDIUM;

        private final String code;

        Size(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        public static Size fromCode(String code) {
            if (Strings.isNullOrEmpty(code)) {
                return null;
            }

            for (Size size : Size.values()) {
                if (code.equals(size.code)) {
                    return size;
                }
            }

            return null;
        }
    }

    private String url;
    private String caption;
    private String altText;
    private Position position;
    private Size size;

    public TextImage() {
        url = "";
        caption = "";
        altText = "";
        position = Position.DEFAULT;
        size = Size.DEFAULT;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Object clone() {
        TextImage textImage = new TextImage();

        textImage.url = this.url;
        textImage.caption = this.caption;
        textImage.altText = this.altText;
        textImage.size = this.size;
        textImage.position = this.position;

        return textImage;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("caption", caption)
                .add("altText", altText)
                .add("position", position)
                .add("size", size)
                .toString();
    }
}
