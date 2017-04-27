package org.consumersunion.stories.server.index.story;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionBase;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.profile.ProfileDocument;
import org.springframework.stereotype.Component;

@Component
public class NewStoryIndexer {
    private final Indexer<StoryDocument> storyIndexer;
    private final Indexer<ProfileDocument> profileIndexer;

    @Inject
    public NewStoryIndexer(
            Indexer<StoryDocument> storyIndexer,
            Indexer<ProfileDocument> profileIndexer) {
        this.storyIndexer = storyIndexer;
        this.profileIndexer = profileIndexer;
    }

    public void index(Story story, ProfileSummary person) {
        index(story, person, null, null, null, null, null, null, false, null, null, null);
    }

    public void index(
            Story story,
            ProfileSummary person, AnswerSet answerSet,
            QuestionnaireI15d questionnaire,
            Address address,
            Document document,
            Integer answerSetId,
            List<Collection> collections,
            boolean storyBodyPrivacy,
            Set<Integer> readAuths,
            Set<String> admins,
            Set<String> tags) {

        String answerSetText = answerSet == null ? null : answerSet.getText();
        StoryDocument storyDocument = new StoryDocument(story, answerSetText, person, address, document,
                answerSetId, collections, questionnaire, storyBodyPrivacy, readAuths, admins, tags,
                story.getDocuments());

        storyIndexer.index(storyDocument);

        ProfileDocument profileDocument = profileIndexer.get(person.getProfile().getId());
        if (profileDocument != null) {
            if (answerSet != null && !profileDocument.getQuestionnaires().contains(answerSet.getQuestionnaire())) {
                profileDocument.getQuestionnaires().add(answerSet.getQuestionnaire());
            }
            // update global story stats
            profileDocument.setLastStoryDate(story.getCreated());
            profileDocument.setStoryCount(profileDocument.getStoryCount() + 1);

            if (collections != null) {
                // Update Collection related stats
                for (Collection collection : collections) {
                    // Add the Collection references to the indexed PersonDocument
                    if (!profileDocument.getCollections().contains(collection.getId())) {
                        profileDocument.getCollections().add(collection.getId());
                    }
//                    if (profileDocument.getStoryCountByCollection().containsKey(collection.getId())) {
//                        profileDocument.getStoryCountByCollection().put(collection.getId(),
//                                profileDocument.getStoryCountByCollection().get(collection.getId()) + 1);
//                    } else { // first entry in the collection
//                        profileDocument.getStoryCountByCollection().put(collection.getId(), 1);
//                    }
//                    profileDocument.getLastStoryDateByCollection().put(collection.getId(), story.getCreated());
                    // TODO : @SOLR
                }
            }

            // we extract 'opt in' and 'preferred email format' from the questionnaire and anwser set
            if (questionnaire != null && answerSet != null) {
                for (QuestionBase question : questionnaire.getQuestions()) {
                    if (BlockType.UPDATES_OPT_IN.equals(question.getBlockType())) {
                        Answer answer = answerSet.getAnswerByLabel(question.getLabel());
                        if (answer == null || answer.getFirstReportValue() == null) {
                            // if the question is asked, but there's no data, then that means it's unchecked, or false
                            profileDocument.setUpdateOptIn(false);
                        } else {
                            profileDocument.setUpdateOptIn(
                                    answer.getFirstReportValue().toLowerCase().startsWith("yes"));
                        }
                    } else if (BlockType.PREFERRED_EMAIL_FORMAT.equals(question.getBlockType())) {
                        Answer answer = answerSet.getAnswerByLabel(question.getLabel());
                        profileDocument.setEmailFormat(answer.getFirstReportValue());
                    }
                }
            }

            profileIndexer.index(profileDocument);
        }
    }
}
