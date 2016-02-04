package org.consumersunion.stories.common.shared.model;

import java.util.List;

import org.consumersunion.stories.common.shared.model.document.Block;

public interface HasBlocks {
    List<Block> getBlocks();

    void setBlocks(List<Block> blocks);
}
