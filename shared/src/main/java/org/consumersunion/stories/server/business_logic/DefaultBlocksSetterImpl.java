package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.i18n.CommonI18nMessages;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;

@Component
public class DefaultBlocksSetterImpl implements DefaultBlocksSetter {
    @Override
    public void setFor(Collection collection) {
        TextImageBlock defaultTextContentBlock =
                new TextImageBlock(LocaleFactory.get(CommonI18nMessages.class).defaultTextBlock());
        defaultTextContentBlock.setDocument(collection.getId());

        collection.setBlocks(Lists.<Block>newArrayList(defaultTextContentBlock));
    }
}
