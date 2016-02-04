package org.consumersunion.stories.server.api.rest.merger;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.post.DocumentPut;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.server.api.rest.converters.ApiBlockConverter;
import org.consumersunion.stories.server.api.rest.converters.ApiLocaleConverter;
import org.consumersunion.stories.server.api.rest.converters.DocumentTypeConverter;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Component
public class DocumentPutMerger extends AbstractMerger<Document, DocumentPut> {
    private final ApiBlockConverter blockConverter;
    private final ApiLocaleConverter apiLocaleConverter;
    private final DocumentTypeConverter documentTypeConverter;

    @Inject
    DocumentPutMerger(
            ApiBlockConverter blockConverter,
            ApiLocaleConverter apiLocaleConverter,
            DocumentTypeConverter documentTypeConverter) {
        this.blockConverter = blockConverter;
        this.apiLocaleConverter = apiLocaleConverter;
        this.documentTypeConverter = documentTypeConverter;
    }

    @Override
    public void merge(Document entity, DocumentPut dto) {
        if (dto.getBlocks() != null) {
            Iterable<Block> blocks = blockConverter.convertAll(dto.getBlocks());
            entity.setBlocks(Lists.newArrayList(blocks));
        }

        entity.setSystemEntityRelation(documentTypeConverter.convert(dto.getDocumentType()));
        entity.setLocale(apiLocaleConverter.convert(dto.getLocale()));

        if (!Strings.isNullOrEmpty(dto.getTitle())) {
            entity.setTitle(dto.getTitle());
        }
    }
}
