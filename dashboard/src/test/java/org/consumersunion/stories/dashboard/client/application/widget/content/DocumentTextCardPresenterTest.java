package org.consumersunion.stories.dashboard.client.application.widget.content;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.Random;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class DocumentTextCardPresenterTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            bind(Document.class).annotatedWith(Assisted.class).toInstance(mock(Document.class));
            bind(ContentKind.class).annotatedWith(Assisted.class).toInstance(ContentKind.CONTENT);
            forceMock(CachingService.class);
            forceMock(ContentDetailPresenter.class);
        }
    }

    @Inject
    DocumentTextCardPresenter presenter;
    @Inject
    ContentDetailPresenter contentDetailPresenter;

    @Test
    public void updateEditedDocument_willSetTextOnContentDetailPresenter_whenContentKindEqualsContent() {
        // Given
        Document document = mock(Document.class);
        List<Block> blocks = Lists.<Block>newArrayList(new Content(BlockType.CONTENT, "", TextType.PLAIN));
        when(document.getBlocks()).thenReturn(blocks);
        String content = Random.string();
        presenter.doEdit(document);

        // When
        presenter.updateEditedDocument(Random.string(), content);

        // Then
        verify(contentDetailPresenter).setContent(document);
    }
}
