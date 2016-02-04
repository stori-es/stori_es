package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;

public abstract class ContentBase extends Block implements Serializable {
    public ContentBase() {
    }

    public ContentBase(BlockType blockType) {
        super(blockType);
    }

    @Override
    public abstract Object clone();
}
