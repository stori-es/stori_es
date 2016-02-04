package org.consumersunion.stories.server.solr.story;

import java.util.List;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
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
import org.consumersunion.stories.server.solr.Indexer;
import org.consumersunion.stories.server.solr.person.ProfileDocument;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;

public class NewStoryIndexer implements Indexer {
    private final Story story;
    private final AnswerSet answerSet;
    private final QuestionnaireI15d questionnaire;
    private final ProfileSummary person;
    private final Address address;
    private final Document document;
    private final Integer answerSetId;
    private final List<Collection> collections;
    private final boolean storyBodyPrivacy;
    private final Set<Integer> readAuths;
    private final Set<String> admins;
    private final Set<String> tags;

    public NewStoryIndexer(
            Story story,
            AnswerSet answerSet,
            QuestionnaireI15d questionnaire,
            ProfileSummary person,
            Address address,
            Document document,
            Integer answerSetId,
            List<Collection> collections,
            boolean storyBodyPrivacy,
            Set<Integer> readAuths,
            Set<String> admins,
            Set<String> tags) {
        this.story = story;
        this.answerSet = answerSet;
        this.questionnaire = questionnaire;
        this.person = person;
        this.address = address;
        this.document = document;
        this.answerSetId = answerSetId;
        this.collections = collections;
        this.storyBodyPrivacy = storyBodyPrivacy;
        this.readAuths = readAuths;
        this.admins = admins;
        this.tags = tags;
    }

    public NewStoryIndexer(Story story, ProfileSummary person) {
        this(story, null, null, person, null, null, null, null, false, null, null, null);
    }

    @Override
    public void index(SolrServer solrStoryServer, SolrServer solrCollectionServer, SolrServer solrPersonServer)
            throws Exception {
        String answerSetText = answerSet == null ? null : answerSet.getText();
        IndexedStoryDocument storyDocument = new IndexedStoryDocument(story, answerSetText, person, address, document,
                answerSetId, collections, questionnaire, storyBodyPrivacy, readAuths, admins, tags,
                story.getDocuments());
        solrStoryServer.add(storyDocument.toDocument());
        solrStoryServer.commit();

        SolrQuery query = new SolrQuery("id:" + person.getProfile().getId());
        QueryResponse result = solrPersonServer.query(query);
        if (result.getResults().size() > 0) {
            ProfileDocument profileDocument = new ProfileDocument(result.getResults().get(0));
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
                    if (profileDocument.getStoryCountByCollection().containsKey(collection.getId())) {
                        profileDocument.getStoryCountByCollection().put(collection.getId(),
                                profileDocument.getStoryCountByCollection().get(collection.getId()) + 1);
                    } else { // first entry in the collection
                        profileDocument.getStoryCountByCollection().put(collection.getId(), 1);
                    }
                    profileDocument.getLastStoryDateByCollection().put(collection.getId(), story.getCreated());
                }
            }

            // we extract 'opt in' and 'preferred email format' from the questionnaire and anwser set
            if (questionnaire != null && answerSet != null) {
                for (QuestionBase question : questionnaire.getQuestions()) {
                    if (BlockType.UPDATES_OPT_IN.equals(question.getStandardMeaning())) {
                        Answer answer = answerSet.getAnswerByLabel(question.getLabel());
                        if (answer == null || answer.getFirstReportValue() == null) {
                            // if the question is asked, but there's no data, then that means it's unchecked, or false
                            profileDocument.setUpdateOptIn(false);
                        } else {
                            profileDocument.setUpdateOptIn(
                                    answer.getFirstReportValue().toLowerCase().startsWith("yes"));
                        }
                    } else if (BlockType.PREFERRED_EMAIL_FORMAT.equals(question.getStandardMeaning())) {
                        Answer answer = answerSet.getAnswerByLabel(question.getLabel());
                        profileDocument.setEmailFormat(answer.getFirstReportValue());
                    }
                }
            }

            solrPersonServer.add(profileDocument.toDocument());
            solrPersonServer.commit();
        }
    }
}
