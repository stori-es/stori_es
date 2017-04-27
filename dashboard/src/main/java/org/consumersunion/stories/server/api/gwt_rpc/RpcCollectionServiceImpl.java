package org.consumersunion.stories.server.api.gwt_rpc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcCollectionService;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.CollectionSortField;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionBase;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.model.type.ContactType;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.util.GUID;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.i18n.CommonI18nMessages;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.business_logic.CollectionService;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.exception.PermalinkAlreadyExistsException;
import org.consumersunion.stories.server.helper.geo.GeoCodingService;
import org.consumersunion.stories.server.helper.geo.Localisation;
import org.consumersunion.stories.server.index.IndexerFactory;
import org.consumersunion.stories.server.index.UpdateEntityReadIndex;
import org.consumersunion.stories.server.index.profile.NewPersonIndexer;
import org.consumersunion.stories.server.index.profile.UpdatePersonAddressIndexer;
import org.consumersunion.stories.server.index.profile.UpdatePersonIndexer;
import org.consumersunion.stories.server.index.story.NewStoryIndexer;
import org.consumersunion.stories.server.index.story.UpdateStoryAddressIndexer;
import org.consumersunion.stories.server.index.story.UpdatedStoryCollectionIndexer;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.CollectionPersister.StoryCollectionParams;
import org.consumersunion.stories.server.persistence.ContactPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.consumersunion.stories.server.persistence.SupportDataUtils;
import org.consumersunion.stories.server.persistence.SupportDataUtilsFactory;
import org.consumersunion.stories.server.persistence.TagsPersister;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.rest.api.convio.SyncFromSysPersonToConvioConstituentRequestFactory;
import org.consumersunion.stories.server.util.StringUtil;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ROOT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.AuthConstants.ROOT_ID;
import static org.consumersunion.stories.common.shared.model.document.BlockType.EMAIL;
import static org.consumersunion.stories.common.shared.model.document.BlockType.EMAIL_OTHER;
import static org.consumersunion.stories.common.shared.model.document.BlockType.EMAIL_WORK;
import static org.consumersunion.stories.common.shared.model.document.BlockType.PHONE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.PHONE_MOBILE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.PHONE_WORK;
import static org.consumersunion.stories.server.persistence.CollectionPersister.CountCollectionsResult;
import static org.consumersunion.stories.server.persistence.CollectionPersister.RemoveStoryFromCollectionFunc;
import static org.consumersunion.stories.server.persistence.ContactPersister.RetrieveContactFunc;

@SuppressWarnings("serial")
@Service("collectionService")
public class RpcCollectionServiceImpl extends RpcBaseServiceImpl implements RpcCollectionService {
    @Inject
    private CollectionPersister collectionPersister;
    @Inject
    private QuestionnaireI15dPersister questionnaireI15dPersister;
    @Inject
    private AnswerSetPersister answerSetPersister;
    @Inject
    private DocumentPersister documentPersister;
    @Inject
    private ContactService contactService;
    @Inject
    private StoryService storyService;
    @Inject
    private EventBus eventBus;
    @Inject
    private CollectionService collectionService;
    @Inject
    private TagsPersister tagsPersister;
    @Inject
    private GeoCodingService geoCodingService;
    @Inject
    private SupportDataUtilsFactory supportDataUtilsFactory;
    @Inject
    private SyncFromSysPersonToConvioConstituentRequestFactory syncFromPersonConvioFactory;
    @Inject
    private ProfilePersister profilePersister;
    @Inject
    private IndexerFactory indexerFactory;
    @Inject
    private UpdateStoryAddressIndexer updateStoryAddressIndexer;
    @Inject
    private UpdatePersonAddressIndexer updatePersonAddressIndexer;
    @Inject
    private UpdatePersonIndexer updatePersonIndexer;
    @Inject
    private UpdatedStoryCollectionIndexer updatedStoryCollectionIndexer;
    @Inject
    private UpdateEntityReadIndex updateEntityReadIndex;
    @Inject
    private NewStoryIndexer newStoryIndexer;
    @Inject
    private NewPersonIndexer newPersonIndexer;

    @Override
    public DatumResponse<Collection> createCollection(Collection collection) {
        DatumResponse<Collection> response = new DatumResponse<Collection>();

        try {
            Collection savedCollection = collectionService.createCollection(collection);
            response.setDatum(savedCollection);
        } catch (PermalinkAlreadyExistsException e) {
            String error = LocaleFactory.get(CommonI18nErrorMessages.class).permalinkUnavailable();
            response.addGlobalErrorMessage(error);
            response.setShowErrorMessages(true);
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public DatumResponse<CollectionData> getCollection(int id, int minRole) {
        return getCollectionForRole(id, minRole);
    }

    protected DatumResponse<CollectionData> getCollectionForRole(int id, Integer minRole) {
        DatumResponse<CollectionData> response = new DatumResponse<CollectionData>();
        try {
            response.setDatum(collectionService.getCollectionForRole(id, minRole));
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (Exception e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public DataResponse<CollectionData> getCollectionsForQuestionnaire(int questionnaireId) {
        DataResponse<CollectionData> response = new DataResponse<CollectionData>();
        Connection conn = PersistenceUtil.getConnection();
        try {

            PreparedStatement collectionQuery =
                    conn.prepareStatement("SELECT cs.targetCollection "
                            + "FROM collection_sources cs "
                            + "JOIN collection c ON cs.targetCollection=c.id "
                            + "WHERE cs.sourceQuestionnaire=? AND c.deleted=0");
            collectionQuery.setInt(1, questionnaireId);

            List<CollectionData> collections = new ArrayList<CollectionData>();
            ResultSet collectionResults = collectionQuery.executeQuery();
            while (collectionResults.next()) {
                // to save answer set, user must be able to see the collection;
                int collectionId = collectionResults.getInt(1);
                CollectionData collectionData = collectionService.getCollectionForRole(collectionId, ROLE_READER);
                if (!collectionData.getCollection().getDeleted()) {
                    collections.add(collectionData);
                }
            }

            response.setData(collections);
        } catch (SQLException e) {
            e.printStackTrace();
            response.addGlobalErrorMessage(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                throw new GeneralException(e);
            }
        }

        return response;
    }

    @Override
    public QuestionnaireSurveyResponse saveAnswersAndStory(final AnswerSet answersSet) {
        return persistenceService.process(
                new ProcessFunc<AnswerSet, QuestionnaireSurveyResponse>(answersSet) {
                    @Override
                    public QuestionnaireSurveyResponse process() {
                        try {
                            QuestionnaireI15d questionnaire =
                                    questionnaireI15dPersister.get(input.getQuestionnaire(), conn);

                            AuthorizationService authorizationService = authService.withConnection(conn);
                            Object collectionsResult =
                                    getTargetCollectionsForQuestionnaire(input.getQuestionnaire(), conn,
                                            authorizationService);

                            if (collectionsResult instanceof QuestionnaireSurveyResponse) {
                                return (QuestionnaireSurveyResponse) collectionsResult;
                            }

                            // First we grab or create the Person.
                            GetOrCreateProfileResult getOrCreateProfileResult =
                                    getAnswersUserForNewStorySaveAndIndex(questionnaire, input, conn);

                            ProfileSummary profile = getOrCreateProfileResult.getProfile();
                            if (profile.getProfile().getId() != ROOT_ID) {
                                input.setPrimaryAuthor(profile.getProfile().getId());
                            } else {
                                throw new GeneralException("User root not allowed to perform this action");
                            }

                            String storyTitle = LocaleFactory.get(CommonI18nMessages.class).untitled();
                            QuestionBase titleQuestion = Iterables.tryFind(questionnaire.getQuestions(),
                                    new Predicate<QuestionBase>() {
                                        @Override
                                        public boolean apply(QuestionBase input) {
                                            return BlockType.STORY_TITLE.equals(input.getBlockType());
                                        }
                                    }).orNull();

                            if (titleQuestion != null) {
                                for (Answer answer : input.getAnswers()) {
                                    if (titleQuestion.getLabel().equals(answer.getLabel())) {
                                        storyTitle = answer.getFirstReportValue();
                                    }
                                }
                            }

                            // Then we create the Story.
                            Story story = saveStory(storyTitle,
                                    getStoryContent(questionnaire, input),
                                    getOrCreateProfileResult,
                                    questionnaire,
                                    input,
                                    (List<Collection>) collectionsResult,
                                    conn,
                                    authorizationService);

                            // Now that the grants have been made, we can queue up the Convio sync.
                            syncFromPersonConvioFactory.create(profile.getProfile())
                                    .queueSysToConvioUpdates(
                                            profile.getProfile().getId(),
                                            true,
                                            null,
                                            conn);

                            // prep and return the response
                            QuestionnaireSurveyResponse response = new QuestionnaireSurveyResponse();
                            response.setStoryId(story.getId());
                            return response;
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }
                    }
                });
    }

    private Object getTargetCollectionsForQuestionnaire(
            int questionnaire,
            Connection conn,
            AuthorizationService authorizationService) {
        List<Integer> collectionIds =
                persistenceService.process(conn, new ProcessFunc<Integer, List<Integer>>(questionnaire) {
                    @Override
                    public List<Integer> process() {
                        List<Integer> collectionIds = Lists.newArrayList();
                        try {
                            PreparedStatement collectionQuery = conn.prepareStatement(
                                    "SELECT targetCollection FROM collection_sources WHERE sourceQuestionnaire=?");
                            collectionQuery.setInt(1, input);
                            ResultSet collectionResults = collectionQuery.executeQuery();

                            while (collectionResults.next()) {
                                collectionIds.add(collectionResults.getInt(1));
                            }
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }

                        return collectionIds;
                    }
                });

        List<Collection> collections = new ArrayList<Collection>();
        for (Integer collectionId : collectionIds) {
            Collection collection = collectionPersister.get(collectionId, conn);
            // to save answer set, user must be able to see the collection
            if (authorizationService.isUserAuthorized(ROLE_READER, collection)) {
                collections.add(collection);
            } else {
                QuestionnaireSurveyResponse response = new QuestionnaireSurveyResponse();
                response.addGlobalErrorMessage("Not authorized");
                return response;
            }
        }

        return collections;
    }

    private Story saveStory(
            String storyTitle,
            Map.Entry<QuestionBase, String> storyContent,
            GetOrCreateProfileResult getOrCreateProfile,
            QuestionnaireI15d questionnaire,
            AnswerSet answerSet,
            List<Collection> targetCollections,
            Connection conn,
            AuthorizationService authorizationService) throws SQLException {

        ProfileSummary profileSummary = getOrCreateProfile.getProfile();
        Profile profile = profileSummary.getProfile();

        Story story = new Story();
        story.setOwner(profile != null ? profile.getId() : null);
        story.setByLine("No Byline");
        story = storyService.createStory(story, conn);

        int orgId = questionnaire.getOwner();
        authorizationService.grantAtLeast(orgId, ROLE_ADMIN, story.getId());

        targetCollections.add(questionnaire);
        persistenceService.process(conn,
                collectionPersister.linkStoryAndCollectionFunc(story.getId(), targetCollections));

        if (profile != null) {
            authorizationService.grantAtLeast(orgId, ROLE_ADMIN, profile.getId());
            updateEntityReadIndex.indexPerson(profile.getId(), orgId);
        }

        Questionnaire questionnaireEn = questionnaire.toQuestionnaire();
        Set<String> attachedTags = autoTagStory(questionnaireEn, story);

        // if there's story content, let's save it
        Document tDoc = null;
        if (storyContent != null) {
            tDoc = new Document();
            tDoc.setTitle(storyTitle);
            tDoc.setLocale(answerSet.getLocale());
            if (storyContent.getKey() != null) {
                if (BlockType.RICH_TEXT_AREA.equals(storyContent.getKey().getBlockType().getRenderType())) {
                    tDoc.addBlock(new TextImageBlock(storyContent.getValue()));
                } else {
                    Content block = new Content(BlockType.CONTENT, storyContent.getValue(), TextType.PLAIN);
                    tDoc.addBlock(block);
                }
            }
            tDoc.setSystemEntityRelation(SystemEntityRelation.BODY);
            tDoc.setTitle(storyTitle);
            tDoc.setPermalink(StringUtil.generateRandomPassword());
            tDoc.setPublic(true);
            if (profile != null) {
                tDoc.setPrimaryAuthor(profile.getId());
                tDoc.setPrimaryAuthorFirstName(profile.getGivenName());
                tDoc.setPrimaryAuthorLastName(profile.getSurname());
            }

            tDoc.setEntity(story.getId());
            tDoc = documentPersister.create(tDoc, conn);
        }

        // saving the answer set is easy
        Integer answerSetId = null;
        Address address = null;
        if (answerSet != null) {
            answerSet.setPrimaryAuthor(profile.getId());
            answerSet.setEntity(story.getId());
            answerSet.setSystemEntityRelation(SystemEntityRelation.ANSWER_SET);
            answerSet.setPermalink(GUID.get());
            answerSet = answerSetPersister.create(answerSet, conn);

            answerSetId = answerSet.getId();

            address = saveAddress(extractAddress(story.getId(), questionnaire, answerSet), conn);
            saveContacts(questionnaire, answerSet, profile.getId(), conn);
            saveAttachments(story, questionnaire, answerSet, profile, conn);
        }

        story = persistenceService.process(conn, new StoryPersister.RetrieveStoryFunc(story.getId()));

        SupportDataUtils supportDataUtils = supportDataUtilsFactory.create(conn);
        Set<Integer> readAuths = supportDataUtils.getStoryAuths(story.getId(), ROLE_READER);
        readAuths.add(orgId);

        Set<String> admins = Sets.newLinkedHashSet(); // Ref TASK-814

        // this will also update the data associated to the Person document
        newStoryIndexer.index(story, profileSummary, answerSet, questionnaire, address, tDoc, answerSetId,
                targetCollections, tDoc != null && tDoc.isPublic() || tDoc == null, readAuths,
                admins, attachedTags);

        eventBus.post(new StoriesAddedEvent(questionnaire, Lists.newArrayList(story), profile,
                getOrCreateProfile.getPrimaryEmail()));

        return story;
    }

    /**
     * Method to retrieve user specifically for the purpose of saving and indexing a new story. The distinction is
     * necessary due to embedded logic. One should always exit this method with a user, though it is written where it
     * may not happen in erroneous situations.
     */
    private GetOrCreateProfileResult getAnswersUserForNewStorySaveAndIndex(
            QuestionnaireI15d questionnaire,
            AnswerSet answersSet,
            Connection conn) {
        String labelFName = null;
        String labelLName = null;

        List<String> emailLabels = Lists.newArrayList();
        List<String> phoneLabels = Lists.newArrayList();

        List<BlockType> emailElements = BlockType.emailElements();
        List<BlockType> phoneElements = BlockType.phoneElements();

        for (QuestionBase q : questionnaire.getQuestions()) {
            if (emailElements.contains(q.getBlockType())) {
                emailLabels.add(q.getLabel());
            } else if (phoneElements.contains(q.getBlockType())) {
                phoneLabels.add(q.getLabel());
            } else if (BlockType.FIRST_NAME.equals(q.getBlockType())) {
                labelFName = q.getLabel();
            } else if (BlockType.LAST_NAME.equals(q.getBlockType())) {
                labelLName = q.getLabel();
            }
        }

        String lastName = null;
        String firstName = null;
        Map<BlockType, String> emails = Maps.newHashMap();
        Map<BlockType, String> phones = Maps.newHashMap();
        Map<String, BlockType> typeMap = Maps.newHashMap();

        for (Answer answer : answersSet.getAnswers()) {
            String label = answer.getLabel();
            Block question = questionnaire.getQuestionByLabel(label);
            if (question != null) {
                typeMap.put(label, question.getBlockType());
                if (emailLabels.contains(answer.getLabel())) {
                    emails.put(question.getBlockType(), answer.getFirstReportValue());
                } else if (phoneLabels.contains(answer.getLabel())) {
                    phones.put(question.getBlockType(), answer.getFirstReportValue());
                } else if (labelFName != null && labelFName.equals(answer.getLabel())) {
                    firstName = answer.getFirstReportValue();
                } else if (labelLName != null && labelLName.equals(answer.getLabel())) {
                    lastName = answer.getFirstReportValue();
                }
            }
        }

        GetOrCreateProfileResult getOrCreateProfileResult =
                getProfileIdWithEmailsForOrg(questionnaire.getOwner(), emails, phones, firstName, lastName, conn);
        ProfileSummary profile = getOrCreateProfileResult.getProfile();
        Address address = extractAddress(profile.getProfile().getId(), questionnaire, answersSet);
        address = saveAddress(address, conn);

        if (address != null) {
            // the basic profile document index is created or updated in the 'getPersonIdWithEmail' method
            updatePersonAddressIndexer.index(address);
            updateStoryAddressIndexer.index(address);
        }

        return getOrCreateProfileResult;
    }

    private Address saveAddress(Address newAddress, Connection conn) {
        try {
            Localisation location = geoCodingService.geoLocate(newAddress);
            if (location != null) {
                location.updateAddress(newAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Address> addresses =
                persistenceService.process(conn, new ContactPersister.RetrieveAddress(newAddress.getEntity()));
        if (!addresses.isEmpty()) {
            Iterator<Address> iterator = addresses.iterator();
            while (iterator.hasNext()) {
                Address address = iterator.next();

                if (address.shouldMerge(newAddress)) {
                    iterator.remove();
                    break;
                }
            }
        }

        addresses.add(newAddress);
        persistenceService.process(conn, new ContactPersister.UpdateAddressForEntity(addresses));

        return newAddress;
    }

    private Address extractAddress(int entityId, QuestionnaireI15d questionnaire, AnswerSet answerSet) {
        String labelAddress1 = null;
        String labelCity = null;
        String labelState = null;
        String labelZipCode = null;
        for (QuestionBase q : questionnaire.getQuestions()) {
            if (BlockType.STREET_ADDRESS_1.equals(q.getBlockType())) {
                labelAddress1 = q.getLabel();
            } else if (BlockType.CITY.equals(q.getBlockType())) {
                labelCity = q.getLabel();
            } else if (BlockType.STATE.equals(q.getBlockType())) {
                labelState = q.getLabel();
            } else if (BlockType.ZIP_CODE.equals(q.getBlockType())) {
                labelZipCode = q.getLabel();
            }
        }

        String address1 = null;
        String city = null;
        String state = null;
        String zipCode = null;
        for (Answer answer : answerSet.getAnswers()) {
            if (labelAddress1 != null && labelAddress1.equals(answer.getLabel())) {
                address1 = readAnswer(answer);
            } else if (labelCity != null && labelCity.equals(answer.getLabel())) {
                city = readAnswer(answer);
            } else if (labelState != null && labelState.equals(answer.getLabel())) {
                state = readAnswer(answer);
            } else if (labelZipCode != null && labelZipCode.equals(answer.getLabel())) {
                zipCode = readAnswer(answer);
            }
        }

        Address newAddress = new Address(entityId);
        newAddress.setRelation(Contact.TYPE_OTHER);
        newAddress.setAddress1(address1);
        newAddress.setCity(city);
        newAddress.setState(state);
        newAddress.setPostalCode(zipCode);
        newAddress.setCountry("US");

        return newAddress;
    }

    /**
     * Auto tags are generated by the Questionnaire and applied to the Story.
     */
    private Set<String> autoTagStory(Questionnaire questionnaire, Story story) {
        if (questionnaire != null) {
            TagsPersister.TagsParams params = new TagsPersister.TagsParams(questionnaire, null);
            Set<String> tags = tagsPersister.getAutoTags(params);

            if (story != null) {
                TagsPersister.TagsParams updateParams = new TagsPersister.TagsParams(story, tags);
                tagsPersister.updateTags(updateParams);
                return tags;
            }
        }

        return null;
    }

    /**
     * Saves all the contact information from a survey. We scan the survey for any phone or email fields and add any
     * that are not already associated to the {@link org.consumersunion.stories.common.shared.model.Profile} to the
     * {@link org.consumersunion.stories.common.shared.model.Profile}'s contact list.
     *
     * @param questionnaire
     * @param answersSet
     * @param profileId
     * @param conn
     */
    private void saveContacts(
            QuestionnaireI15d questionnaire,
            AnswerSet answersSet,
            Integer profileId,
            Connection conn) {
        List<Contact> existingContacts = persistenceService.process(conn, new RetrieveContactFunc(profileId));

        Iterables.removeIf(existingContacts, new Predicate<Contact>() {
            @Override
            public boolean apply(Contact input) {
                return Contact.SOCIAL.equals(input.getType());
            }
        });

        List<Contact> newContacts = new ArrayList<Contact>();
        for (QuestionBase question : questionnaire.getQuestions()) {
            if (question.getBlockType().isStandard() &&
                    (question.getBlockType().code().startsWith("PHONE") ||
                            question.getBlockType().code().startsWith("EMAIL"))) {

                for (Answer answer : answersSet.getAnswers()) {
                    if (question.getLabel().equals(answer.getLabel())) {
                        String contact = readAnswer(answer);

                        if (contact != null && !contact.trim().isEmpty()) {
                            Contact newContact = null;
                            // See TASK-336
                            String option = question instanceof ContactBlock ?
                                    ((ContactBlock) question).getOption() : "Home";
                            if (question.getBlockType().code().startsWith("PHONE")) {
                                newContact = new Contact(profileId, Contact.MediumType.PHONE.name(), option, contact);
                            } else if (question.getBlockType().code().startsWith("EMAIL")) {
                                newContact = new Contact(profileId, Contact.MediumType.EMAIL.name(), option, contact);
                            }

                            boolean contactAlreadyExist = false;
                            if (newContact != null) {
                                for (Contact oldcontact : existingContacts) {
                                    if (oldcontact.getValue().equals(newContact.getValue())) {
                                        contactAlreadyExist = true;
                                        break;
                                    }
                                }
                            }
                            if (newContact != null && !contactAlreadyExist) {
                                newContacts.add(newContact);
                            }
                        }
                    }
                }
            }
        }

        if (!newContacts.isEmpty()) {
            existingContacts.addAll(newContacts);
            contactService.saveContacts(existingContacts, profileId, conn);
        }
    }

    private void saveAttachments(
            Story story,
            QuestionnaireI15d questionnaire,
            AnswerSet answersSet,
            Profile profile,
            Connection conn) {
        String labelAttachement;
        for (QuestionBase question : questionnaire.getQuestions()) {
            if (BlockType.ATTACHMENTS.equals(question.getBlockType())) {
                labelAttachement = question.getLabel();
                for (Answer answer : answersSet.getAnswers()) {
                    if (labelAttachement.equals(answer.getLabel())) {
                        List<String> attachmentLinks = answer.getReportValues();
                        Document tDoc;

                        for (String attachmentLink : attachmentLinks) {
                            if (!Strings.isNullOrEmpty(attachmentLink)) {
                                tDoc = new Document();
                                tDoc.setEntity(story.getId());
                                tDoc.setTitle("");

                                String[] attachmentSubmit = attachmentLink.split("@");
                                if (attachmentSubmit.length > 1) {
                                    tDoc.setTitle(attachmentSubmit[0]);
                                    tDoc.addBlock(new Content(BlockType.CONTENT, attachmentSubmit[0], TextType.PLAIN));
                                    tDoc.setPermalink(attachmentSubmit[1]);
                                } else {
                                    tDoc.addBlock(new Content(BlockType.CONTENT, attachmentSubmit[0], TextType.PLAIN));
                                    tDoc.setPermalink(attachmentSubmit[0]);
                                }

                                tDoc.setSystemEntityRelation(SystemEntityRelation.ATTACHMENT);
                                if (profile != null) {
                                    tDoc.setPrimaryAuthor(profile.getId());
                                    tDoc.setPrimaryAuthorFirstName(profile.getGivenName());
                                    tDoc.setPrimaryAuthorLastName(profile.getSurname());
                                }
                                documentPersister.create(tDoc, conn);
                            }
                        }
                    }
                }
            }
        }
    }

    private String readAnswer(Answer answer) {
        if (!answer.getReportValues().isEmpty()) {
            return Strings.emptyToNull(answer.getFirstReportValue());
        } else {
            return null;
        }
    }

    private GetOrCreateProfileResult getProfileIdWithEmailsForOrg(
            int orgId,
            Map<BlockType, String> emails,
            Map<BlockType, String> phones,
            String firstName,
            String lastName,
            Connection conn) {
        Map.Entry<BlockType, String> emailEntry = getEmail(emails);
        Map.Entry<BlockType, String> phoneEntry = getPhone(phones);
        String primaryEmail = emailEntry != null && emailEntry.getValue() != null ? emailEntry.getValue() : null;
        String primaryPhone = phoneEntry != null && phoneEntry.getValue() != null ? phoneEntry.getValue() : null;

        Profile profileWithEmail = null;
        if (!emails.isEmpty()) {
            profileWithEmail = persistenceService.process(conn,
                    new ProfilePersister.RetrieveProfileByEmailsFunc(orgId, emails.values()));
        }

        ProfileSummary profileSummary;
        if (profileWithEmail != null) {
            profileWithEmail.setGivenName(getValidValue(profileWithEmail.getGivenName(), firstName));
            profileWithEmail.setSurname(getValidValue(profileWithEmail.getSurname(), lastName));
            persistenceService.process(conn, new ProfilePersister.UpdateProfileFunc(profileWithEmail));
            updatePersonIndexer.index(profileWithEmail, primaryEmail, primaryPhone);
            profileSummary = new ProfileSummary(profileWithEmail, primaryEmail,
                    Sets.newHashSet(emails.values()), primaryPhone, Sets.newHashSet(phones.values()));
        } else {
            Profile profileToSave = new Profile();
            if ((lastName == null || lastName.trim().length() == 0)
                    && (firstName == null || firstName.trim().length() == 0)) {
                lastName = "anonymous";
            }

            profileToSave.setSurname(lastName);
            if (firstName != null && firstName.trim().length() > 0) {
                profileToSave.setGivenName(firstName);
            }

            profileToSave.setOrganizationId(orgId);
            profileToSave = profilePersister.createProfile(profileToSave);

            List<Contact> contacts = new LinkedList<Contact>();
            for (Map.Entry<BlockType, String> entry : emails.entrySet()) {
                if (entry != null && entry.getValue() != null && entry.getValue().trim().length() > 0) {
                    Contact contact = new Contact(profileToSave.getId(), Contact.MediumType.EMAIL.name(),
                            getContactType(entry.getKey()), entry.getValue());
                    contacts.add(contact);
                }
            }

            for (Map.Entry<BlockType, String> entry : phones.entrySet()) {
                if (entry != null && entry.getValue() != null && entry.getValue().trim().length() > 0) {
                    Contact contact = new Contact(profileToSave.getId(), Contact.MediumType.PHONE.name(),
                            getContactType(entry.getKey()), entry.getValue());
                    contacts.add(contact);
                }
            }

            if (!contacts.isEmpty()) {
                persistenceService.process(conn, new ContactPersister.SaveContactsFunc(contacts));
            }

            newPersonIndexer.index(profileToSave, primaryEmail, primaryPhone);
            profileSummary = new ProfileSummary(profileToSave, primaryEmail,
                    Sets.newHashSet(emails.values()), primaryPhone, Sets.newHashSet(phones.values()));
        }

        return new GetOrCreateProfileResult(profileSummary, primaryEmail);
    }

    private String getValidValue(String value1, String value2) {
        return value1 == null ? value2 : value1;
    }

    private String getContactType(BlockType elementTypeLabel) {
        if (EMAIL.equals(elementTypeLabel)
                || PHONE.equals(elementTypeLabel)) {
            return ContactType.HOME.code();
        } else if (EMAIL_WORK.equals(elementTypeLabel)
                || PHONE_WORK.equals(elementTypeLabel)) {
            return ContactType.WORK.code();
        } else if (EMAIL_OTHER.equals(elementTypeLabel)
                || BlockType.PHONE_OTHER.equals(elementTypeLabel)) {
            return ContactType.OTHER.code();
        } else if (PHONE_MOBILE.equals(elementTypeLabel)) {
            return ContactType.MOBILE.code();
        }

        return null;
    }

    private Map.Entry<BlockType, String> getEmail(Map<BlockType, String> emails) {
        List<BlockType> elementTypes = Lists.newArrayList(EMAIL, EMAIL_WORK, EMAIL_OTHER);
        for (BlockType elementType : elementTypes) {
            if (emails.containsKey(elementType)) {
                return new AbstractMap.SimpleEntry<BlockType, String>(elementType, emails.get(elementType));
            }
        }

        return null;
    }

    private Map.Entry<BlockType, String> getPhone(Map<BlockType, String> phones) {
        List<BlockType> elementTypes = Lists.newArrayList(PHONE, PHONE_WORK, PHONE_MOBILE, EMAIL_OTHER);
        for (BlockType elementType : elementTypes) {
            if (phones.containsKey(elementType)) {
                return new AbstractMap.SimpleEntry<BlockType, String>(elementType, phones.get(elementType));
            }
        }

        return null;
    }

    private AbstractMap.SimpleEntry<QuestionBase, String> getStoryContent(
            QuestionnaireI15d questionnaire,
            AnswerSet answersSet) {
        QuestionBase question = null;
        for (QuestionBase q : questionnaire.getQuestions()) {
            if (q.getBlockType().isStoryAsk()) {
                question = q;
                break;
            }
        }

        String storyContent = null;
        if (question != null && question.getLabel() != null) {
            for (Answer answer : answersSet.getAnswers()) {
                if (question.getLabel().equals(answer.getLabel())) {
                    answersSet.getAnswers().remove(answer);
                    storyContent = answer.getFirstReportValue();
                    break;
                }
            }
        }

        return new AbstractMap.SimpleEntry<QuestionBase, String>(question, storyContent);
    }

    @Override
    public CollectionDataPagedResponse getCollections(RetrievePagedCollectionsParams params) {
        CollectionDataPagedResponse response = new CollectionDataPagedResponse();

        User user = userService.getLoggedInUser();
        if (user != null) {
            if (params.getEffectiveId() == null) {
                params.setEffectiveId(getEffectiveSubject());
            }

            response.setStart(params.getStart());

            CountCollectionsResult collectionsCount = getCollectionsCount(params);
            response.setCollectionsCount(collectionsCount.getNbCollections());
            response.setQuestionnairesCount(collectionsCount.getNbQuestionnaires());
            if (response.getTotalCount() == 0) {
                response.setData(new ArrayList<CollectionData>(0));
            } else {
                response.setData(getPagedCollections(params));
            }
        } else {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).notLoggedIn());
        }
        return response;
    }

    @Override
    public PagedDataResponse<CollectionData> getCollectionsReferencingStory(
            int start,
            int length,
            SortField sortField,
            boolean ascending,
            int storyId) {
        PagedDataResponse<CollectionData> response = new PagedDataResponse<CollectionData>();

        try {
            User user = userService.getLoggedInUser(true);

            storyService.getStory(storyId);

            response = getCollections(new RetrievePagedCollectionsParams.Builder()
                    .withStart(start)
                    .withLength(length)
                    .withSortField(sortField)
                    .withAscending(ascending)
                    .withStoryId(storyId)
                    .withAccessMode(ACCESS_MODE_ANY)
                    .withPermissionMask(ROLE_READER)
                    .withEffectiveId(getEffectiveSubject(user))
                    .build());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        } catch (Exception e) {
            response = new PagedDataResponse<CollectionData>();
            response.addGlobalErrorMessage(e.getMessage());
            return response;
        }

        return response;
    }

    @SuppressWarnings("unchecked")
    private List<CollectionData> getPagedCollections(RetrievePagedCollectionsParams params) {
        List<CollectionData> cl;
        // TODO: the MIN_VALUE was the old way; not sure if still used anywhere;
        if (params.getStoryId() == null || params.getStoryId() == Integer.MIN_VALUE) {
            cl = collectionService.searchCollections(params);
        } else {
            cl = collectionService.searchCollectionsByStory(params);
        }

        return cl;
    }

    private CountCollectionsResult getCollectionsCount(RetrievePagedCollectionsParams params) {
        params = params.noLimit();

        CountCollectionsResult count;
        if (params.getStoryId() == null || params.getStoryId() == Integer.MIN_VALUE) {
            count = collectionPersister.countCollections(params);
        } else {
            count = persistenceService.process(new CollectionPersister.CountCollectionsByStory(params));
        }

        return count;
    }

    @Override
    public DatumResponse<Collection> updateCollection(Collection clientCollection) {
        DatumResponse<Collection> response = new DatumResponse<Collection>();
        if (clientCollection.getId() > 0) {
            try {
                CollectionData origCollection =
                        collectionService.getCollectionForRole(clientCollection.getId(), ROLE_CURATOR);
                if (origCollection != null) {
                    Collection resultCollection = collectionService.updateCollection(clientCollection);

                    response.setDatum(resultCollection);
                }
            } catch (Exception e) {
                response.addGlobalErrorMessage("Unexpected error: " + e.getMessage());
                throw new GeneralException("CollectionServiceImpl.updateCollection()", e);
            }
        } else {
            response.addGlobalErrorMessage("Unexpected error cannot save the collection.");
        }
        return response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CollectionDataPagedResponse getCollectionsByEffectiveRoleExcludeByStory(int storyId) {
        CollectionDataPagedResponse response = new CollectionDataPagedResponse();

        try {
            User user = userService.getLoggedInUser(true);

            storyService.getStory(storyId);
            Integer accessMode = ACCESS_MODE_EXPLICIT;
            if (authService.isSuperUser(user)) {
                accessMode = ACCESS_MODE_ROOT;
            }

            response.setData(new ArrayList<CollectionData>());
            RetrievePagedCollectionsParams params = new RetrievePagedCollectionsParams.Builder()
                    .withStart(0)
                    .withLength(0)
                    .withSortField(CollectionSortField.TITLE_A_Z)
                    .withAscending(true)
                    .withStoryId(storyId)
                    .withAccessMode(accessMode)
                    .withPermissionMask(ROLE_READER)
                    .withEffectiveId(getEffectiveSubject(user))
                    .build();

            List<CollectionData> userCollections =
                    collectionService.searchCollectionsByUserWithoutStoryAssociated(params);
            if (authService.isSuperUser(user)) {
                response.setData(userCollections);
            } else {
                response.setData(checkOwns(userCollections));
            }

            return response;
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
            return response;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CollectionDataPagedResponse errorResponse = new CollectionDataPagedResponse();
            errorResponse.addGlobalErrorMessage(e.getMessage());
            return errorResponse;
        }
    }

    @Override
    public ActionResponse linkStoriesToCollections(Set<Integer> collectionIds, Set<Integer> storyIds) {
        ActionResponse response = new ActionResponse();
        List<String> errorCollections = Lists.newArrayList();

        for (Integer collectionId : collectionIds) {
            DatumResponse<Collection> datumResponse = linkStoriesToCollection(collectionId, storyIds);

            if (datumResponse.isError()) {
                if (datumResponse.getDatum() == null) {
                    for (String message : datumResponse.getGlobalErrorMessages()) {
                        response.addGlobalErrorMessage(message);
                    }
                } else {
                    errorCollections.add(datumResponse.getDatum().getTitle());
                }
            }
        }

        if (!errorCollections.isEmpty()) {
            CommonI18nErrorMessages messages = LocaleFactory.get(CommonI18nErrorMessages.class);
            String message = messages.unableToLinkStoriesToCollections(Joiner.on(", ").join(errorCollections));
            response.addGlobalErrorMessage(message);
        }

        return response;
    }

    @Override
    public ActionResponse removeFromCollectionSources(Integer collectionId, Integer sourceCollection) {
        DatumResponse<CollectionData> collectionDataResponse = getCollectionForRole(collectionId, ROLE_CURATOR);
        if (collectionDataResponse.isError()) {
            return new ActionResponse(collectionDataResponse);
        } else {
            Collection collection = collectionDataResponse.getDatum().getCollection();
            collection.getCollectionSources().remove(sourceCollection);
            DatumResponse<Collection> collectionResponse = updateCollection(collection);

            return new ActionResponse(collectionResponse);
        }
    }

    @Override
    public DatumResponse<Collection> getCollectionByPermalink(String permalink) {
        DatumResponse<Collection> response = new DatumResponse<Collection>();

        Collection collection = collectionService.getCollectionByPermalink(permalink);
        response.setDatum(collection);

        return response;
    }

    @Override
    public DatumResponse<Collection> linkStoriesToCollection(
            int collectionId,
            java.util.Collection<Integer> storyIds) {
        DatumResponse<CollectionData> collectionResponse = getCollectionForRole(collectionId, ROLE_CURATOR);
        // TODO: we should verify that the user has read auth over all story IDs; see SYSFOUR-517
        if (collectionResponse.isError()) {
            return new DatumResponse<Collection>(collectionResponse);
        } else {
            return linkStoriesToCollectionHelper(collectionResponse.getDatum().getCollection(), storyIds);
        }
    }

    private DatumResponse<Collection> linkStoriesToCollectionHelper(
            Collection collection,
            java.util.Collection<Integer> storyIds) {
        DatumResponse<Collection> response = new DatumResponse<Collection>();
        try {
            collection = collectionService.linkStoriesToCollection(collection, storyIds);
        } catch (Exception e) {
            response.addGlobalErrorMessage(e.getMessage());
            return response;
        }

        response.setDatum(collection);

        return response;
    }

    @Override
    public DatumResponse<CollectionData> removeStoryFromCollection(int collectionId, int storyId) {
        DatumResponse<CollectionData> collectionResponse = getCollectionForRole(collectionId, ROLE_CURATOR);
        if (collectionResponse.isError()) {
            return new DatumResponse<CollectionData>(collectionResponse);
        } else {
            DatumResponse<CollectionData> response = new DatumResponse<CollectionData>();
            try {
                Story story = persistenceService.process(new StoryPersister.RetrieveStoryFunc(storyId));
                User user = userService.getLoggedInUser(true);
                if (collectionResponse.getDatum().getCollection().isQuestionnaire()
                        && !user.isProfileOwner(story.getOwner())) {
                    throw new GeneralException("Cannot remove story from a questionnaire-collection.");
                }

                StoryCollectionParams params = new StoryCollectionParams(0, 0, null, false, collectionId,
                        ACCESS_MODE_EXPLICIT, getEffectiveSubject(user), storyId);

                int result = persistenceService.process(new RemoveStoryFromCollectionFunc(params));
                if (result > 0) {
                    updatedStoryCollectionIndexer.index(storyId);
                } else {
                    CommonI18nErrorMessages messages = LocaleFactory.get(CommonI18nErrorMessages.class);
                    response.addGlobalErrorMessage(messages.notPermissionsToRemove());
                }
            } catch (NotLoggedInException e) {
                response.addGlobalErrorMessage(e.getMessage());
                response.setLoggedIn(false);
            } catch (RuntimeException e) {
                response.addGlobalErrorMessage(e.getLocalizedMessage());
            }
            return response;
        }
    }

    @Override
    public DatumResponse<Collection> checkUnusedLink(String collectionLink) {
        DatumResponse<Collection> response = new DatumResponse<Collection>();

        try {
            User loggedInUser = userService.getLoggedInUser(true);
            CollectionPersister.CollectionRetrieveKeysByURL keysByURL =
                    new CollectionPersister.CollectionRetrieveKeysByURL(collectionLink, collectionLink, loggedInUser);
            Collection collection = persistenceService.process(collectionPersister.retrieveByPermalinkFunc(keysByURL));
            response.setDatum(collection);
        } catch (GeneralException ignored) {
            // No Collection/Questionnaire with permalink. It's fine for checkUnusedLink()
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    private List<CollectionData> checkOwns(List<CollectionData> userCollections) throws NotLoggedInException {
        List<CollectionData> collectionDatas = new ArrayList<CollectionData>();
        for (CollectionData data : userCollections) {
            Integer contextOrganizationId = userService.getContextOrganizationId();
            if (contextOrganizationId != null && contextOrganizationId.equals(data.getCollection().getOwner())) {
                collectionDatas.add(data);
            }
        }

        return collectionDatas;
    }

    private static class GetOrCreateProfileResult {
        private ProfileSummary profile;
        private String primaryEmail;

        public GetOrCreateProfileResult(
                @NonNull ProfileSummary profile,
                String primaryEmail) {
            this.profile = profile;
            this.primaryEmail = primaryEmail;
        }

        @NonNull
        public ProfileSummary getProfile() {
            return profile;
        }

        @Nullable
        public String getPrimaryEmail() {
            return primaryEmail;
        }
    }
}
