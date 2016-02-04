package org.consumersunion.stories.server.api.rest.merger;

import java.util.ArrayList;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.ApiLocale;
import org.consumersunion.stories.common.shared.dto.DocumentType;
import org.consumersunion.stories.common.shared.dto.post.DocumentPut;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.server.api.rest.converters.ApiBlockConverter;
import org.consumersunion.stories.server.api.rest.converters.ApiLocaleConverter;
import org.consumersunion.stories.server.api.rest.converters.DocumentTypeConverter;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.dto.ApiLocale.ES_US;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class DocumentPutMergerTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(ApiBlockConverter.class);
        }
    }

    private static final String A_NEW_TITLE = "A_NEW_TITLE";

    @Inject
    private DocumentPutMerger merger;
    @Inject
    private ApiBlockConverter blockConverter;
    @Inject
    private ApiLocaleConverter localeConverter;
    @Inject
    private DocumentTypeConverter documentTypeConverter;

    @Test
    public void merge_mergesData() throws Exception {
        Document document = new Document();
        ArrayList<ApiBlock> apiBlocks = Lists.newArrayList(new ApiBlock());
        DocumentPut dto = DocumentPut.builder()
                .withBlocks(apiBlocks)
                .withDocumentType(DocumentType.ATTACHMENT)
                .withLocale(ES_US)
                .withTitle(A_NEW_TITLE)
                .build();
        ArrayList<Block> blocks = Lists.newArrayList(new Block());
        given(blockConverter.convertAll(same(apiBlocks))).willReturn(blocks);

        merger.merge(document, dto);

        assertThat(document.getBlocks()).isEqualTo(blocks);
        assertThat(document.getSystemEntityRelation()).isEqualTo(ATTACHMENT);
        assertThat(document.getLocale()).isEqualTo(Locale.SPANISH);
        assertThat(document.getTitle()).isEqualTo(A_NEW_TITLE);
    }

    @Test
    public void merge_dataNotProvided_doesNotUpdate() throws Exception {
        Document document = new Document();
        document = spy(document);
        DocumentPut dto = DocumentPut.builder()
                .withDocumentType(DocumentType.ATTACHMENT)
                .withLocale(ApiLocale.ES_US)
                .build();

        merger.merge(document, dto);

        verify(document, never()).setBlocks(anyList());
        verify(document, never()).setTitle(anyString());
        assertThat(document.getSystemEntityRelation()).isEqualTo(ATTACHMENT);
        assertThat(document.getLocale()).isEqualTo(Locale.SPANISH);
    }
}
