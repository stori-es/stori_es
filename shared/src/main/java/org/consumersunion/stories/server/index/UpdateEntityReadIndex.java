package org.consumersunion.stories.server.index;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.consumersunion.stories.server.index.story.StoryDocument;
import org.springframework.stereotype.Component;

@Component
public class UpdateEntityReadIndex {
    private final Indexer<StoryDocument> storyIndexer;
    private final Indexer<ProfileDocument> profileIndexer;

    @Inject
    UpdateEntityReadIndex(
            Indexer<StoryDocument> storyIndexer,
            Indexer<ProfileDocument> profileIndexer) {
        this.storyIndexer = storyIndexer;
        this.profileIndexer = profileIndexer;
    }

    public void indexStory(int id, int... subjects) {
        index(storyIndexer, id, subjects);
    }

    public void indexPerson(int id, int... subjects) {
        index(profileIndexer, id, subjects);
    }

    private <T extends Document> void index(Indexer<T> indexer, int id, int... subjects) {
        T document = indexer.get(id);

        if (document != null) {
            document.getReadAuths();
            Set<Integer> readAuths = document.getReadAuths();
            if (readAuths == null) {
                readAuths = new HashSet<Integer>();
            }

            for (int subject : subjects) {
                if (!readAuths.contains(subject)) {
                    readAuths.add(subject);
                }
            }

            indexer.index(document);
        }
    }
}
