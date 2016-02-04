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
        BlockType formType = block.getFormType();
        BlockType standardMeaning = block.getStandardMeaning();

        if (SUBHEADER.equals(formType)) {
            return elementFactory.createHeader((Content) block);
        } else if (TEXT_IMAGE.equals(formType)) {
            return elementFactory.createTextImage((TextImageBlock) block);
        } else if (CONTENT.equals(formType)) {
            return elementFactory.createText((Content) block);
        } else if (CUSTOM_PERMISSIONS.equals(standardMeaning)) {
            return elementFactory.createText((Content) block);
        } else if (IMAGE.equals(formType)) {
            return elementFactory.createImage((ImageBlock) block);
        } else if (VIDEO.equals(formType)) {
            return elementFactory.createVideo((MediaBlock) block);
        } else if (AUDIO.equals(formType)) {
            return elementFactory.createAudio((MediaBlock) block);
        } else if (DOCUMENT.equals(formType)) {
            return elementFactory.createDocument((DocumentBlock) block);
        } else if (COLLECTION.equals(formType)) {
            return elementFactory.createCollection((Content) block);
        } else if (STORY.equals(formType)) {
            return elementFactory.createStory((Content) block);
        } else if (SUBMIT.equals(formType)) {
            return elementFactory.createSubmitButton((SubmitBlock) block);
        }

        return null;
    }
}
