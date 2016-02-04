package org.consumersunion.stories.server.api.rest.converters;

import java.util.List;

import org.consumersunion.stories.common.shared.dto.post.CollectionPost;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

@Component
public class CollectionConverter {
    public Collection convert(CollectionPost collectionPost) {
        Collection collection = new Collection();

        collection.getBodyDocument().setTitle(collectionPost.getTitle());
        collection.getBodyDocument().setSummary(collectionPost.getSummary());

        List<Integer> questionnaireIds = collectionPost.getQuestionnaireIds();
        if (questionnaireIds != null) {
            collection.setCollectionSources(Sets.newHashSet(questionnaireIds));
        }

        List<Integer> storyIds = collectionPost.getStoryIds();
        if (storyIds != null) {
            Iterable<StoryLink> storyLinks = Iterables.transform(storyIds,
                    new Function<Integer, StoryLink>() {
                        @Override
                        public StoryLink apply(Integer storyId) {
                            return new StoryLink(storyId);
                        }
                    });
            collection.setStories(Sets.newHashSet(storyLinks));
        }

        return collection;
    }
}
