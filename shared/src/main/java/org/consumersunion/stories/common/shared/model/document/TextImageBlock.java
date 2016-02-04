package org.consumersunion.stories.common.shared.model.document;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;

@JsonTypeName("textimage")
@org.codehaus.jackson.annotate.JsonTypeName("textimage")
public class TextImageBlock extends ContentBase {
    private String text;
    private TextImage image;

    public TextImageBlock() {
        this("");
    }

    public TextImageBlock(String text) {
        super(BlockType.TEXT_IMAGE);
        this.text = text;
    }

    public TextImage getImage() {
        return image;
    }

    public void setImage(TextImage image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Can't name this method {@code hasImage} because {@link org.codehaus.jackson.annotate.JsonIgnore} is not
     * supported by our current version of Resty-GWT
     */
    public boolean containsImage() {
        return image != null;
    }

    @Override
    public Object clone() {
        TextImageBlock clone = new TextImageBlock();

        clone.setFormType(getFormType());
        clone.setStandardMeaning(getStandardMeaning());
        clone.setKey(getKey());
        if (image != null) {
            clone.image = (TextImage) image.clone();
        }
        clone.text = text;

        return clone;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .add("image", image)
                .toString();
    }
}
