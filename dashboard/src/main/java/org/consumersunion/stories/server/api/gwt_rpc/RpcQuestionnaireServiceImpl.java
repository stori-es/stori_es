package org.consumersunion.stories.server.api.gwt_rpc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcQuestionnaireService;
import org.consumersunion.stories.common.client.service.response.AnswerSetAndQuestionnaireResponse;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.server.business_logic.CollectionService;
import org.consumersunion.stories.server.business_logic.QuestionnaireService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.consumersunion.stories.server.business_logic.ThemeService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister.SearchByCollectionPagedParams;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.NonNull;
import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister.CountQuestionnairesFunc;

@Service("questionnaireService")
public class RpcQuestionnaireServiceImpl extends RpcBaseServiceImpl implements RpcQuestionnaireService {
    @Inject
    private QuestionnaireI15dPersister questionnaireI15dPersister;
    @Inject
    private AnswerSetPersister answerSetPersister;
    @Inject
    private ThemeService themeService;
    @Inject
    private QuestionnaireService questionnaireService;
    @Inject
    private TagsService tagsService;
    @Inject
    private CollectionService collectionService;

    @Override
    public QuestionnaireSurveyResponse getQuestionnaireSurvey(String permalink) {
        QuestionnaireSurveyResponse response = new QuestionnaireSurveyResponse();

        try {
            Questionnaire questionnaire = questionnaireService.getQuestionnaire(permalink);
            response.setQuestionnaire(questionnaire);
            response.setupQuestionsAndContents();

            Theme theme = themeService.getTheme(questionnaire.getTheme());
            response.setTheme(theme);
        } catch (NotAuthorizedException ignored) {
            return response;
        } catch (NotFoundException ignored) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidLink());
        }

        return response;
    }

    @Override
    public DatumResponse<QuestionnaireI15d> getQuestionnaireI15d(int id) {
        DatumResponse<QuestionnaireI15d> response = new DatumResponse<QuestionnaireI15d>();

        QuestionnaireI15d questionnaire = questionnaireI15dPersister.get(id);
        response.setDatum(questionnaire);

        return response;
    }

    @Override
    public CollectionSurveyI15dResponse saveQuestionnaire(QuestionnaireI15d questionnaire) {
        CollectionSurveyI15dResponse response = new CollectionSurveyI15dResponse();
        try {
            saveQuestionnaire(response, questionnaire);
        } catch (Exception e) {
            throw new GeneralException(e);
        }

        return response;
    }

    @Override
    public CollectionSurveyI15dResponse saveQuestionnaire(
            Collection collection,
            QuestionnaireI15d questionnaire) {
        Collection dbCollection;
        try {
            CollectionData collectionData = collectionService.getCollectionForRole(collection.getId(), ROLE_CURATOR);
            dbCollection = collectionData.getCollection();
        } catch (Exception e) {
            CollectionSurveyI15dResponse response = new CollectionSurveyI15dResponse();
            response.addGlobalErrorMessage(e.getMessage());
            return response;
        }

        CollectionSurveyI15dResponse response = new CollectionSurveyI15dResponse();
        try {
            QuestionnaireI15d dbQuestionnaire = saveQuestionnaire(response, questionnaire);

            response.setCollection(dbCollection);
            if (!collection.isQuestionnaire()) {
                dbCollection.getCollectionSources().add(dbQuestionnaire.getId());
                collectionService.updateCollection(dbCollection);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }

        return response;
    }

    @Override
    public CollectionSurveyI15dResponse copyQuestionnaire(
            int sourceId,
            String newTitle,
            String summary,
            Locale locale) {
        CollectionSurveyI15dResponse response = new CollectionSurveyI15dResponse();
        try {
            QuestionnaireI15d sourceQuestionnaire = questionnaireI15dPersister.get(sourceId);

            // Create
            QuestionnaireI15d newQuestionnaire = new QuestionnaireI15d();
            newQuestionnaire.setTheme(sourceQuestionnaire.getTheme());
            Document bodyDocument = newQuestionnaire.getBodyDocument();
            bodyDocument.setTitle(newTitle);
            bodyDocument.setSummary(summary);
            bodyDocument.addBlock(new Content(BlockType.CONTENT, summary, Content.TextType.PLAIN));
            bodyDocument.setLocale(locale);

            newQuestionnaire.setConfirmationDocument(sourceQuestionnaire.getConfirmationDocument().cloneContent());
            newQuestionnaire.setSurvey(sourceQuestionnaire.getSurvey().cloneContent());

            QuestionnaireI15d dbQuestionnaire = saveQuestionnaire(response, newQuestionnaire);

            Set<String> tags = tagsService.getTags(sourceQuestionnaire);
            tagsService.setTags(dbQuestionnaire, tags);

            Set<String> autotags = tagsService.getAutoTags(sourceQuestionnaire);
            tagsService.setAutoTags(dbQuestionnaire, autotags);

            copyTargetCollections(response, sourceQuestionnaire.getId(), dbQuestionnaire.getId());

            response.setQuestionnaire(dbQuestionnaire);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(e);
        }

        return response;
    }

    private void copyTargetCollections(
            CollectionSurveyI15dResponse response,
            int sourceQuestionnaireId,
            int targetQuestionnaireId) {
        Connection conn = PersistenceUtil.getConnection();
        try {
            PreparedStatement collectionQuery =
                    conn.prepareStatement("INSERT INTO collection_sources (sourceQuestionnaire, targetCollection) " +
                            "SELECT ? AS sourceQuestionnaire, targetCollection FROM collection_sources WHERE " +
                            "sourceQuestionnaire=? ");
            collectionQuery.setInt(1, targetQuestionnaireId);
            collectionQuery.setInt(2, sourceQuestionnaireId);
            collectionQuery.execute();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new GeneralException(e);
            }
        }
    }

    @NonNull
    private QuestionnaireI15d saveQuestionnaire(
            CollectionSurveyI15dResponse response,
            QuestionnaireI15d questionnaire) throws NotLoggedInException {
        QuestionnaireI15d dbQuestionnaire;

        if (questionnaire.isNew()) {
            dbQuestionnaire = questionnaireService.createQuestionnaire(questionnaire);
        } else {
            questionnaire.setPublic(true);

            dbQuestionnaire = persistenceService.process(
                    questionnaireI15dPersister.updateQuestionnaireFunc(questionnaire));
            indexerService.process(indexerFactory.createUpdatedCollection(dbQuestionnaire));
        }

        response.setQuestionnaire(dbQuestionnaire);

        return dbQuestionnaire;
    }

    @Override
    public PagedDataResponse<QuestionnaireI15d> getQuestionnaireSummaries(
            int collectionId,
            int start,
            int length) {
        PagedDataResponse<QuestionnaireI15d> response = checkCollectionForOperation(collectionId);

        try {
            User user = userService.getLoggedInUser(true);

            SearchByCollectionPagedParams params = new SearchByCollectionPagedParams(collectionId, start, length,
                    ACCESS_MODE_EXPLICIT, getEffectiveSubject(user));
            params.setRetrievePartials();
            List<QuestionnaireI15d> summaries = questionnaireService.searchByCollectionsPaged(params);

            Integer total = persistenceService.process(new CountQuestionnairesFunc(collectionId));

            response.setData(summaries);
            response.setStart(start);
            response.setTotalCount(total);
        } catch (GeneralException e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(e.getLocalizedMessage());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
            return response;
        }
        return response;
    }

    @Override
    public PagedDataResponse<QuestionnaireI15d> getQuestionnairesExcludeAssociated(
            int collectionId,
            int start,
            int length) {
        PagedDataResponse<QuestionnaireI15d> response = checkCollectionForOperation(collectionId);

        try {
            SearchByCollectionPagedParams params = new SearchByCollectionPagedParams(collectionId, start, length,
                    ACCESS_MODE_EXPLICIT, userService.getContextOrganizationId());
            params.setRetrieveComplete();

            List<QuestionnaireI15d> summaries =
                    questionnaireI15dPersister.searchAndExcludeLinkedToCollectionFunc(params);

            response.setData(summaries);
            response.setStart(start);
            response.setTotalCount(length);
        } catch (GeneralException e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(e.getLocalizedMessage());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
            return response;
        }

        return response;
    }

    private PagedDataResponse<QuestionnaireI15d> checkCollectionForOperation(int collectionId) {
        PagedDataResponse<QuestionnaireI15d> response = new PagedDataResponse<QuestionnaireI15d>();
        try {
            collectionService.getCollectionForRole(collectionId, ROLE_READER);
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @Override
    public AnswerSetAndQuestionnaireResponse getAnswerSetAndQuestionnaire(int answerSetId) {
        AnswerSetAndQuestionnaireResponse response = new AnswerSetAndQuestionnaireResponse();
        try {
            AnswerSet answerSet = answerSetPersister.get(answerSetId);
            QuestionnaireI15d questionnaire = questionnaireI15dPersister.get(answerSet.getQuestionnaire());

            response.setAnswerSet(answerSet);
            response.setQuestionnaire(questionnaire.toQuestionnaire());
        } catch (GeneralException e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw e;
        }

        return response;
    }
}
