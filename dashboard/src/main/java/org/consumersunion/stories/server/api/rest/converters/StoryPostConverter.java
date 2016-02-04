package org.consumersunion.stories.server.api.rest.converters;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.post.StoryPost;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.server.business_logic.UserService;
import org.springframework.stereotype.Component;

@Component
public class StoryPostConverter extends AbstractConverter<StoryPost, Story> {
    private final UserService userService;

    @Inject
    StoryPostConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Story convert(StoryPost storyPost) {
        Story story = new Story();

        story.setDefaultContent(storyPost.getDefaultContentId());
        story.setOwner(userService.getActiveProfileId());

        story.addDocuments(SystemEntityRelation.ATTACHMENT, storyPost.getAttachmentIds());
        story.addDocuments(SystemEntityRelation.BODY, storyPost.getContentIds());
        story.addDocuments(SystemEntityRelation.NOTE, storyPost.getNoteIds());
        story.addDocuments(SystemEntityRelation.ANSWER_SET, storyPost.getResponseIds());

        return story;
    }
}
