package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.StoriesApiResponse;
import org.consumersunion.stories.common.shared.dto.post.StoryPost;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.api.rest.converters.StoryPostConverter;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ANSWER_SET;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ATTACHMENT;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.NOTE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class StoriesResourceTest {

    private static final int STORY_ID = 32;

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(StoryPostConverter.class);
        }
    }

    @Inject
    StoriesResource resource;
    @Inject
    StoryService storyService;
    @Inject
    StoryPostConverter storyPostConverter;

    @Test
    public void createStory_willReturn201_whenBodyIsEmpty() {
        // Given
        given(storyPostConverter.convert(any(StoryPost.class))).willReturn(mock(Story.class));
        given(storyService.createStory(any(Story.class))).willReturn(mock(Story.class));
        given(storyService.getStorySummary(anyInt())).willReturn(mock(StorySummary.class, RETURNS_DEEP_STUBS));

        // When
        Response response = resource.createStory(null);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void createStory_willPersistEmptyStory_whenBodyIsEmpty() {
        // Given
        given(storyPostConverter.convert(any(StoryPost.class))).willReturn(mock(Story.class));
        given(storyService.createStory(any(Story.class))).willReturn(mock(Story.class));
        given(storyService.getStorySummary(anyInt())).willReturn(mock(StorySummary.class, RETURNS_DEEP_STUBS));
        ArgumentCaptor<StoryPost> argumentCaptor = ArgumentCaptor.forClass(StoryPost.class);

        // When
        resource.createStory(null);

        // Then
        verify(storyPostConverter).convert(argumentCaptor.capture());
        StoryPost value = argumentCaptor.getValue();

        assertNull(value.getAttachmentIds());
        assertNull(value.getContentIds());
        assertNull(value.getDefaultContentId());
        assertNull(value.getNoteIds());
        assertNull(value.getPermissionIds());
        assertNull(value.getResponseIds());
        assertThat(value.getTags()).isNull();
    }

    @Test
    public void getStory() {
        given(storyService.getStorySummary(STORY_ID)).willReturn(createStorySummary());

        Response response = resource.getStory(STORY_ID);

        StoriesApiResponse apiResponse = (StoriesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(STORY_ID);
    }

    @Test
    public void updateStory() {
        StoryPost storyPost = StoryPost.builder().build();
        Story story = mock(Story.class);
        given(storyService.getStory(STORY_ID)).willReturn(story);
        given(storyService.getStorySummary(STORY_ID)).willReturn(createStorySummary());

        resource.updateStory(STORY_ID, storyPost);

        InOrder inOrder = Mockito.inOrder(story, storyService);
        inOrder.verify(story).setDocuments(eq(ATTACHMENT), same(storyPost.getAttachmentIds()));
        inOrder.verify(story).setDocuments(eq(BODY), same(storyPost.getAttachmentIds()));
        inOrder.verify(story).setDocuments(eq(NOTE), same(storyPost.getAttachmentIds()));
        inOrder.verify(story).setDocuments(eq(ANSWER_SET), same(storyPost.getAttachmentIds()));
        inOrder.verify(story).setDefaultContent(storyPost.getDefaultContentId());
        inOrder.verify(storyService).updateStory(same(story));
    }

    @Test
    public void deleteStory() {
        resource.deleteStory(STORY_ID);

        verify(storyService).deleteStory(STORY_ID);
    }

    private StorySummary createStorySummary() {
        return new StorySummary(new Story(STORY_ID, 1), null, null, null, null, null, null, null, null);
    }
}
