package org.consumersunion.stories.server.api.rest.converters;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.DocumentType;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.dto.post.DocumentPost;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;

@Component
public class DocumentPostConverter extends Converter<DocumentPost, Document> {
    private final ApiBlockConverter apiBlockConverter;
    private final DocumentTypeConverter documentTypeConverter;

    @Inject
    DocumentPostConverter(
            ApiBlockConverter apiBlockConverter,
            DocumentTypeConverter documentTypeConverter) {
        this.apiBlockConverter = apiBlockConverter;
        this.documentTypeConverter = documentTypeConverter;
    }

    @Override
    protected DocumentPost doBackward(Document document) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Document doForward(DocumentPost documentPost) {
        Document document = new Document();

        List<ApiBlock> blocks = documentPost.getBlocks();
        if (blocks != null) {
            document.setBlocks(Lists.newArrayList(apiBlockConverter.convertAll(blocks)));
        }
        document.setEntity(documentPost.getEntityId());
        document.setTitle(documentPost.getTitle());
        document.setSystemEntityRelation(documentTypeConverter.convert(documentPost.getDocumentType()));
        document.setLocale(Locale.ENGLISH);

        ResourceLink source = documentPost.getSource();
        if (source != null) {
            document.setPermalink(source.getHref());
        }

        if (DocumentType.ATTACHMENT.equals(documentPost.getDocumentType())) {
            document.addBlock(new Content(BlockType.CONTENT, documentPost.getTitle(), Content.TextType.PLAIN));
        }

        return document;
    }
}
