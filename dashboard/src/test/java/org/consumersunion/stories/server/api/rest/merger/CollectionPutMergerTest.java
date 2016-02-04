package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.CollectionPut;
import org.consumersunion.stories.common.shared.model.Collection;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CollectionPutMergerTest {
    private static final String A_NEW_TITLE = "A_NEW_TITLE";
    private static final String A_NEW_SUMMARY = "A_NEW_SUMMARY";

    private CollectionPutMerger merger;

    @Before
    public void setUp() {
        merger = new CollectionPutMerger();
    }

    @Test
    public void merge_mergesData() throws Exception {
        Collection collection = new Collection();
        collection.setPublished(false);
        CollectionPut dto = CollectionPut.builder()
                .isPublished(true)
                .withTitle(A_NEW_TITLE)
                .withSummary(A_NEW_SUMMARY)
                .build();

        merger.merge(collection, dto);

        assertThat(collection.isPublished()).isTrue();
        assertThat(collection.getBodyDocument().getTitle()).isEqualTo(A_NEW_TITLE);
        assertThat(collection.getBodyDocument().getSummary()).isEqualTo(A_NEW_SUMMARY);
    }

    @Test
    public void merge_dataNotProvided_doesNotUpdate() throws Exception {
        Collection collection = new Collection();
        collection.setPublished(false);
        collection = spy(collection);
        CollectionPut dto = CollectionPut.builder().build();

        merger.merge(collection, dto);

        verify(collection, never()).setPublished(anyBoolean());
        verify(collection, never()).getBodyDocument();
    }
}
