package org.consumersunion.stories.common.client.ui.block;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;

import com.google.gwt.user.client.ui.IsWidget;

import static org.consumersunion.stories.common.shared.model.document.BlockType.AUDIO;
import static org.consumersunion.stories.common.shared.model.document.BlockType.COLLECTION;
import static org.consumersunion.stories.common.shared.model.document.BlockType.CONTENT;
import static org.consumersunion.stories.common.shared.model.document.BlockType.CUSTOM_PERMISSIONS;
import static org.consumersunion.stories.common.shared.model.document.BlockType.DOCUMENT;
import static org.consumersunion.stories.common.shared.model.document.BlockType.IMAGE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.STORY;
import static org.consumersunion.stories.common.shared.model.document.BlockType.SUBHEADER;
import static org.consumersunion.stories.common.shared.model.document.BlockType.SUBMIT;
import static org.consumersunion.stories.common.shared.model.document.BlockType.TEXT_IMAGE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.VIDEO;

public class ElementFactoryImpl {
    private final ElementFactory elementFactory;

    @Inject
    public ElementFactoryImpl(ElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    public IsWidget create(Block block) {
        BlockType renderType = block.getBlockType().getRenderType();

        if (SUBHEADER.equals(renderType)) {
            return elementFactory.createHeader((Content) block);
        } else if (TEXT_IMAGE.equals(renderType)) {
            return elementFactory.createTextImage((TextImageBlock) block);
        } else if (CONTENT.equals(renderType)) {
            return elementFactory.createText((Content) block);
        } else if (CONTENT.equals(renderType)) {
            return elementFactory.createText((Content) block);
        } else if (IMAGE.equals(renderType)) {
            return elementFactory.createImage((ImageBlock) block);
        } else if (VIDEO.equals(renderType)) {
            return elementFactory.createVideo((MediaBlock) block);
        } else if (AUDIO.equals(renderType)) {
            return elementFactory.createAudio((MediaBlock) block);
        } else if (DOCUMENT.equals(renderType)) {
            return elementFactory.createDocument((DocumentBlock) block);
        } else if (COLLECTION.equals(renderType)) {
            return elementFactory.createCollection((Content) block);
        } else if (STORY.equals(renderType)) {
            return elementFactory.createStory((Content) block);
        } else if (SUBMIT.equals(renderType)) {
            return elementFactory.createSubmitButton((SubmitBlock) block);
        }

        return null;
    }
}
