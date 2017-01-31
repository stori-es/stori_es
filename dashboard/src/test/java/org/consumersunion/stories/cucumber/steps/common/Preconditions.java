package org.consumersunion.stories.cucumber.steps.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcCollectionService;
import org.consumersunion.stories.common.client.service.RpcDocumentService;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.cucumber.utils.TestParameters;
import org.consumersunion.stories.cucumber.utils.WebDriverManager;
import org.consumersunion.stories.server.api.gwt_rpc.RpcCollectionServiceImpl;
import org.consumersunion.stories.server.api.gwt_rpc.RpcDocumentServiceImpl;
import org.consumersunion.stories.server.api.gwt_rpc.RpcStoryServiceImpl;
import org.consumersunion.stories.server.api.gwt_rpc.RpcUserServiceImpl;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.OrganizationPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import cucumber.annotation.After;
import cucumber.annotation.Before;
import cucumber.annotation.en.Given;
import net.lightoze.gwt.i18n.server.LocaleProxy;

import static org.consumersunion.stories.server.util.ApplicationContextProvider.getApplicationContext;

public class Preconditions {
    static {
        LocaleProxy.initialize();
    }

    private static final int CONSUMERS_UNION = 2;
    private static final int YOUR_HEALTH_SECURITY_THEME = 90;

    private final WebDriverManager webDriverManager;

    @Inject
    private CollectionPersister collectionPersister;
    @Inject
    private QuestionnaireI15dPersister questionnaireI15dPersister;
    @Inject
    private OrganizationPersister organizationPersister;

    @Inject
    public Preconditions(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;

        ApplicationContext applicationContext = getApplicationContext();
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }

    @Before
    public void createAndStartService() throws IOException {
        webDriverManager.setupDriver();
    }

    @After
    public void createAndStopService() {
        webDriverManager.tearDownDriver();
    }

    @Given("^no preconditions$")
    public void noPreconditions() {
    }

    @Given("^I am logged out$")
    public void iAmLoggedOUt() {
        webDriverManager.getWebDriver().get(TestParameters.signOutUrl);
        webDriverManager.clearAllCookies();
    }

    @Given("^a pre-existing organization '([^']*)'$")
    public void aPreExistingOrganization(String orgName) {
        final Organization organization = new Organization();
        organization.setName(orgName);

        organizationPersister.create(organization);
    }

    @Given("^a pre-existing '([^']*)' user account '([^']*)' with password '([^']*)'$")
    public void aPreExistingUserAccount(String orgName, String handle, String password) throws SQLException {
        final User newUser = new User();
        newUser.setActive(true);
        newUser.setHandle(handle);

        final Connection conn = PersistenceUtil.getConnection();
        int orgId = -1;
        try {
            final PreparedStatement stmt = conn.prepareStatement("SELECT id FROM organization WHERE name=?");
            stmt.setString(1, orgName);
            final ResultSet results = stmt.executeQuery();
            if (results.next()) {
                orgId = results.getInt(1);
            }
        } finally {
            conn.close();
        }
        if (orgId == -1) {
            throw new GeneralException("Bad org name '" + orgName + "'.");
        }

        final RpcUserServiceImpl userService = getApplicationContext().getBean(RpcUserServiceImpl.class);
        userService.createAccountInternal(null, newUser, password, "What color is the sky?", "blue", "John", "Doe", orgId);
    }

    @Given("^a pre-existing Story '([^']+)' sourced from Questionnaire '([^']+)'")
    public void aPreExistingStory(String storyPermalink, String questionnairePermalink) throws Exception {
        QuestionnaireI15d questionnaire = getQuestionnaire(questionnairePermalink);

        AnswerSet answerSet = new AnswerSet();
        answerSet.setLocale(Locale.ENGLISH);
        answerSet.setQuestionnaire(questionnaire.getId());

        RpcCollectionService collectionService = new RpcCollectionServiceImpl();
        QuestionnaireSurveyResponse response = collectionService.saveAnswersAndStory(answerSet);

        Story story = getStory(response.getStoryId());
        story.setPermalink(storyPermalink);

        PersistenceUtil.process(new StoryPersister.UpdateStoryFunc(story));
    }

    @Given("^a pre-existing Story '([^']+)' body is modified to '([^']+)'")
    public void aPreExistingStoryEdit(String storyPermalink, String newContent) throws Exception {
        Story story = getStory(storyPermalink);

        Document document;
        Integer documentId = story.getOnlyDocument(SystemEntityRelation.BODY);

        RpcStoryServiceImpl storyService = new RpcStoryServiceImpl();

        if (documentId != null) {
            RpcDocumentService documentService = new RpcDocumentServiceImpl();
            document = documentService.getDocument(documentId).getDatum();
        } else {
            document = new Document();
            document.setSystemEntityRelation(SystemEntityRelation.BODY);
        }

        document.addBlock(new Content(BlockType.CONTENT, newContent, TextType.HTML));

        storyService.updateStory(story);
    }

    @Given("^a pre-existing Collection '([^']+)'")
    public Collection aPreExistingCollection(String permaLink) throws Exception {
        Collection collection = new Collection();
        collection.setPermalink(permaLink);
        collection.getBodyDocument().setTitle(permaLink);
        collection.setOwner(CONSUMERS_UNION);

        return collectionPersister.create(collection);
    }

    @Given("^a pre-existing Questionnaire '([^']+)'")
    public QuestionnaireI15d aPreExistingQuestionnaire(String permaLink) throws Exception {
        QuestionnaireI15d questionnaire = new QuestionnaireI15d();
        questionnaire.setPermalink(permaLink);
        questionnaire.getBodyDocument().setTitle(permaLink);
        questionnaire.setOwner(CONSUMERS_UNION);
        questionnaire.setTheme(YOUR_HEALTH_SECURITY_THEME);

        return questionnaireI15dPersister.createQuestionnaire(questionnaire);
    }

    @Given("^a pre-existing Collection '([^']+)' is a target of Questionnaire '([^']+)'")
    public void aPreExistingCollectionIsTargetOfQuestionnaire(String collectionPermalink,
            String questionnairePermalink) throws Exception {
        Collection collection = aPreExistingCollection(collectionPermalink);
        QuestionnaireI15d questionnaire = getQuestionnaire(questionnairePermalink);

        collection.getCollectionSources().add(questionnaire.getId());

        collectionPersister.updateCollection(collection);
    }

    @Given("^the l10n selection is '([^']*)'$")
    public void setL10n(String l10nCode) {
    }

    private Story getStory(String storyPermalink) {
        return PersistenceUtil.process(new StoryPersister.RetrieveByPermalink(storyPermalink));
    }

    private Story getStory(Integer storyId) throws SQLException {
        return PersistenceUtil.process(new StoryPersister.RetrieveStoryFunc(storyId));
    }

    private QuestionnaireI15d getQuestionnaire(String questionnairePermalink) throws SQLException {
        int questionnaireId = -1;

        final Connection conn = PersistenceUtil.getConnection();
        try {
            final PreparedStatement stmt = conn.prepareStatement("SELECT id FROM questionnaire WHERE permalink=?");
            stmt.setString(1, questionnairePermalink);
            final ResultSet results = stmt.executeQuery();
            if (results.next()) {
                questionnaireId = results.getInt(1);
            }
        } finally {
            conn.close();
        }

        QuestionnaireI15d questionnaire = null;
        if (questionnaireId != -1) {
            questionnaire = questionnaireI15dPersister.get(questionnaireId);
        }

        if (questionnaire == null) {
            throw new IllegalArgumentException(
                    "The questionnaire with permalink <" + questionnairePermalink + "> doesn't exist");
        }

        return questionnaire;
    }
}
