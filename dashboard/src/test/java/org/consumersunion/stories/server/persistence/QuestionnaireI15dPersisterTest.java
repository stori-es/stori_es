package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.Position;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock.Size;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister.SearchByCollectionPagedParams;

import junit.framework.TestCase;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;

public class QuestionnaireI15dPersisterTest extends SpringTestCase {
    private final String[] text = {"foo", "foo en espa;ol"};
    private final String[] displayValue = {"foo display value", "valor desplegado foo"};
    private final String[] helpText = {"foo help text", "texto de ayuda foo"};
    private final String label = "foo label";
    private final String dataType = "email";
    private final int min = 1;
    private final int max = 4;

    @Inject
    private QuestionnaireI15dPersister questionnairePersister;
    @Inject
    private PersistenceService persistenceService;
    @Inject
    private CollectionPersister collectionPersister;

    public void testCreate() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM questionnaire");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long initialCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            final QuestionnaireI15d questionnaire = createQuestionnaireTemplate("Template questionnaire1");

            questionnairePersister.createQuestionnaire(questionnaire, conn);

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM questionnaire");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long postCount = results.getLong(1);
            conn.commit();

            TestCase.assertEquals("New questionnnaire count didn't match.", initialCount + 1, postCount);
            TestCase.assertTrue("Bad ID", questionnaire.getId() > 0);
            assertEquals("Unexpected version", 1, questionnaire.getVersion());
        } catch (final Exception e) {
            e.printStackTrace();
            TestCase.fail("Unexpected exception: " + e.getMessage());
        } finally {
            conn.close();
        }
    }

    public void testCreateWithOptions() throws SQLException {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM questionnaire");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long initialQuestionCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM question_options");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            final long initialOptionsCount = results.getLong(1);
            conn.commit(); // otherwise, our conn is in it's own transaction and
            // won't see the update

            final QuestionnaireI15d questionnaire = createQuestionnaireWithOptionsTemplate("Template questionnaire99");

            questionnairePersister.createQuestionnaire(questionnaire, conn);

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM questionnaire");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            long postCount = results.getLong(1);
            TestCase.assertEquals("New questionnnaire count didn't match.", initialQuestionCount + 1, postCount);
            TestCase.assertTrue("Bad ID", questionnaire.getId() > 0);
            assertEquals("Unexpected version", 1, questionnaire.getVersion());

            results = conn.createStatement().executeQuery("SELECT COUNT(*) FROM question_options");
            if (!results.next()) {
                TestCase.fail("No results");
            }
            postCount = results.getLong(1);
            TestCase.assertEquals("New options count didn't match.", initialOptionsCount + 1, postCount);
        } catch (final Exception e) {
            e.printStackTrace();
            TestCase.fail("Unexpected exception: " + e.getMessage());
        } finally {
            conn.close();
        }
    }

    public void testRetrieve() {
        final QuestionnaireI15d questionnaire = createQuestionnaireTemplate("Template questionnaire2");
        questionnairePersister.createQuestionnaire(questionnaire);

        final QuestionnaireI15d response = questionnairePersister.get(questionnaire.getId());

        assertEquals("Unexpected ID.", questionnaire.getId(), response.getId());
        assertEquals("Unexpected collections count.", 1, response.getTargetCollections().size());

        assertEquals("Number of FormElement not match", 4, response.getSurvey().getBlocks().size());
        TestCase.assertTrue("First FormElement is not a Question",
                response.getSurvey().getBlocks().get(0) instanceof Question);
        assertEquals("FormType not match", BlockType.TEXT_INPUT,
                (response.getSurvey().getBlocks().get(0)).getRenderType());
        assertEquals("Label not match", label, ((Question) response.getSurvey().getBlocks().get(0)).getLabel());
        assertEquals("Min length not match", min, ((Question) response.getSurvey().getBlocks().get(0)).getMinLength()
                .intValue());
        assertEquals("Max length not match", max, ((Question) response.getSurvey().getBlocks().get(0)).getMaxLength()
                .intValue());
        assertEquals("Data type not match", dataType,
                ((Question) response.getSurvey().getBlocks().get(0)).getDataType());
        assertEquals(text[0], ((Question) response.getSurvey().getBlocks().get(0)).getText());
        assertEquals(helpText[0], ((Question) response.getSurvey().getBlocks().get(0)).getHelpText());

        TestCase.assertTrue("Second FormElement is not a TextImageBlock",
                response.getSurvey().getBlocks().get(1) instanceof TextImageBlock);
        assertEquals("FormType not match", BlockType.TEXT_IMAGE, response.getSurvey().getBlocks()
                .get(1).getRenderType());

        assertTrue(((TextImageBlock) response.getSurvey().getBlocks().get(1)).getText().equals(text[0]));
    }

    public void testRetrieveWithOptions() {
        final QuestionnaireI15d questionnaire = createQuestionnaireWithOptionsTemplate("Template questionnaire98");

        questionnairePersister.createQuestionnaire(questionnaire);

        final QuestionnaireI15d response = questionnairePersister.get(questionnaire.getId());

        assertEquals("Unexpected ID.", questionnaire.getId(), response.getId());
        assertEquals("Unexpected collections count.", 1, response.getTargetCollections().size());

        assertEquals("Number of FormElement not match", 4, response.getSurvey().getBlocks().size());
        TestCase.assertTrue("First FormElement is not a Question",
                response.getSurvey().getBlocks().get(0) instanceof Question);
        assertEquals("FormType not match", BlockType.TEXT_INPUT,
                response.getSurvey().getBlocks().get(0).getRenderType());
        assertEquals("Label not match", label, ((Question) response.getSurvey().getBlocks().get(0)).getLabel());
        assertEquals("Min length not match", min, ((Question) response.getSurvey().getBlocks().get(0)).getMinLength()
                .intValue());
        assertEquals("Max length not match", max, ((Question) response.getSurvey().getBlocks().get(0)).getMaxLength()
                .intValue());
        assertEquals("Data type not match", dataType,
                ((Question) response.getSurvey().getBlocks().get(0)).getDataType());

        assertEquals(text[0], ((Question) response.getSurvey().getBlocks().get(0)).getText());
        assertEquals(helpText[0], ((Question) response.getSurvey().getBlocks().get(0)).getHelpText());
        // Options
        assertEquals(text[0],
                ((Question) response.getSurvey().getBlocks().get(0)).getOptions().get(0).getReportValue());
        assertEquals(displayValue[0],
                ((Question) response.getSurvey().getBlocks().get(0)).getOptions().get(0).getDisplayValue());

        TestCase.assertTrue("Second FormElement is not a TextImageBlock",
                response.getSurvey().getBlocks().get(1) instanceof TextImageBlock);
        assertEquals("FormType not match", BlockType.TEXT_IMAGE,
                response.getSurvey().getBlocks().get(1).getRenderType());

        assertTrue(((TextImageBlock) response.getSurvey().getBlocks().get(1)).getText().equals(text[0]));
    }

    public void testUpdate() {
        final QuestionnaireI15d questionnaire = createQuestionnaireTemplate("Template questionnaire6");
        final String secondTitle = "Another title";

        QuestionnaireI15d createdQuestionnaire = questionnairePersister.createQuestionnaire(questionnaire);
        createdQuestionnaire.setTargetCollections(new HashSet<Integer>());
        createdQuestionnaire.getBodyDocument().setTitle(secondTitle);
        persistenceService.process(questionnairePersister.updateQuestionnaireFunc(createdQuestionnaire));

        assertEquals("Unexpected version.", 2, questionnaire.getVersion());
        final QuestionnaireI15d response = questionnairePersister.get(questionnaire.getId());

        assertEquals("Unexpected ID.", createdQuestionnaire.getId(), response.getId());

        // Collections and Questionnaires managed from the Questionnaire side.
        assertEquals("Unexpected collection ID count.", 1, response.getTargetCollections().size());
    }

    @SuppressWarnings("unchecked")
    public void testSearchByCollection() {
        final QuestionnaireI15d template = createQuestionnaireTemplate("Template questionnaire11");
        questionnairePersister.createQuestionnaire(template);

        final Collection collection = collectionPersister.get(4);
        collection.getCollectionSources().add(template.getId());
        collectionPersister.updateCollection(collection);

        final SearchByCollectionPagedParams params = new SearchByCollectionPagedParams(4, 0, 100, ACCESS_MODE_ANY, 1);
        final List<QuestionnaireI15d> qs = questionnairePersister.searchByCollectionsPaged(params);

        TestCase.assertTrue("No questionnaires found for collection", qs.size() > 0);
        QuestionnaireI15d found = null;

        for (final QuestionnaireI15d q : qs) {
            if (q.getId() == template.getId()) {
                found = q;
                break;
            }
        }

        assertNotNull("Expected questionnaire was not found among the results", found);
        TestCase.assertTrue("Form elements for questionnaire were not retrieved",
                found.getSurvey().getBlocks().size() > 0);
    }

    /* TODO: see commented code ~line 205 in QuestionnaireI15dPersister. The retrievePartial is an optimization that
    is no
       longer straightforward now that the survey blocks are part of a separate Document, not directly part of the
       questionnaire
        @SuppressWarnings("unchecked")
        public void testSearchByCollectionWithPartialResults() {
            final QuestionnaireI15d template = createQuestionnaireTemplate("Template questionnaire19");
            PersistenceUtil.process(new QuestionnaireI15dPersister.CreateQuestionnaireFunc(template));

            final Collection collection = collectionPersister.get(4);
            collection.getCollectionSources().add(template.getId());
            collectionPersister.updateCollection(collection);

            final SearchByCollectionPagedParams params = new SearchByCollectionPagedParams(4, 0, 100,
            ACCESS_MODE_ANY, 1);
            params.setRetrievePartials();
            final List<QuestionnaireI15d> qs = persistenceService.process(new QuestionnaireI15dPersister
            .SearchByCollectionPagedFunc(params));

            assertTrue("No questionnaires found for collection", qs.size() > 0);
            QuestionnaireI15d found = null;

            for (final QuestionnaireI15d q : qs) {
                if (q.getId() == template.getId()) {
                    found = q;
                    break;
                }
            }

            assertNotNull("Expected questionnaire was not found among the results", found);
            assertFalse("Form elements found in questionnaire", found.getSurvey().getBlocks().size() > 0);
        }
    */
    private QuestionnaireI15d createQuestionnaireTemplate(final String title) {
        final QuestionnaireI15d template = new QuestionnaireI15d();
        template.getTargetCollections().add(4);
        template.setTheme(90);
        template.setPreviewKey("abcdefg");
        template.setOwner(2);
        Document bodyDocument = template.getBodyDocument();
        bodyDocument.setTitle(title);
        final int authorId = PersistersTestUtils.getRootProfileIdForOrganization(2);
        bodyDocument.setPrimaryAuthor(authorId);

        final ArrayList<Block> elements = new ArrayList<Block>();

        final Question question = new Question();
        question.setBlockType(BlockType.TEXT_INPUT);
        question.setText(text[0]);
        question.setHelpText(helpText[0]);
        question.setLabel(label);
        question.setRequired(true);
        question.setMinLength(min);
        question.setMaxLength(max);
        question.setDataType(dataType);
        elements.add(question);

        final TextImageBlock tiBlock = new TextImageBlock(text[0]);
        elements.add(tiBlock);

        final SubmitBlock submitBlock = new SubmitBlock();
        submitBlock.setBlockType(BlockType.SUBMIT);
        submitBlock.setPrompt("Done");
        submitBlock.setPosition(Position.CENTER);
        submitBlock.setSize(Size.MEDIUM);
        elements.add(submitBlock);

        final Content content = new Content(BlockType.CONTENT, text[0], TextType.PLAIN);
        elements.add(content);

        final Document survey = new Document();
        survey.setBlocks(elements);
        survey.setPrimaryAuthor(authorId);
        template.setSurvey(survey);

        Document confirmationDocument = template.getConfirmationDocument();
        confirmationDocument.addBlock(new Content(BlockType.CONTENT, "<h1>Confirmed</h1>", TextType.HTML));
        confirmationDocument.setPrimaryAuthor(authorId);
        template.setConfirmationDocument(confirmationDocument);

        return template;
    }

    private QuestionnaireI15d createQuestionnaireWithOptionsTemplate(final String title) {
        final QuestionnaireI15d template = new QuestionnaireI15d();
        template.getTargetCollections().add(4);
        template.setTheme(90);
        template.setOwner(2);
        Document bodyDocument = template.getBodyDocument();
        bodyDocument.setTitle(title);
        int authorId = PersistersTestUtils.getRootProfileIdForOrganization(2);
        bodyDocument.setPrimaryAuthor(authorId);

        final ArrayList<Block> elements = new ArrayList<Block>();

        final Question question = new Question();
        question.setBlockType(BlockType.TEXT_INPUT);
        question.setText(text[0]);
        question.setHelpText(helpText[0]);
        question.setLabel(label);
        question.setRequired(true);
        question.setMinLength(min);
        question.setMaxLength(max);
        question.setDataType(dataType);
        question.addOption(displayValue[0], text[0]);
        elements.add(question);

        final TextImageBlock tiBlock = new TextImageBlock(text[0]);
        elements.add(tiBlock);

        elements.add(new SubmitBlock());

        final Content content = new Content(BlockType.CONTENT, text[0], TextType.PLAIN);
        elements.add(content);

        Document survey = new Document();
        survey.setPrimaryAuthor(authorId);
        survey.setBlocks(elements);
        template.setSurvey(survey);

        Document confirmationDocument = template.getConfirmationDocument();
        confirmationDocument.addBlock(new Content(BlockType.CONTENT, "<h1>Confirmed</h1>", TextType.HTML));
        confirmationDocument.setPrimaryAuthor(authorId);
        template.setConfirmationDocument(confirmationDocument);
        System.out.println("primary author: " + authorId + " for " + confirmationDocument);

        return template;
    }
}
