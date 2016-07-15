package org.consumersunion.stories.common.shared.model.questionnaire;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class QuestionnaireBase extends Collection {
    private Document survey;
    private Document confirmationDocument;
    private Document nextDocument;
    private Set<Integer> targetCollections;

    private transient Map<String, QuestionBase> questionsMap;

    public QuestionnaireBase() {
        this(DEFAULT_ID, DEFAULT_VERSION);
    }

    public QuestionnaireBase(final int id, final int version) {
        super(id, version);

        setQuestionnaire(true);

        survey = new Document();
        survey.setEntity(getId());
        survey.setSystemEntityRelation(SystemEntityRelation.SURVEY);
    }

    @Override
    public Set<Integer> getTargetCollections() {
        if (targetCollections == null) {
            this.targetCollections = new HashSet<Integer>();
        }
        return targetCollections;
    }

    /**
     * WARNING: The target {@link Collection Collections} are managed from the {@link Collection} class as {@linkplain}
     * Questionnaire} 'sources'. The target {@link Collection Collections} are provided for convenience, and the setter
     * is here because this would ideally be passed in as an immutable constant upon construction, but between the
     * non-obvious requirements of GWT, Spring, and GIN, and in the absence of a thorough coding standard, it's not
     * clear what we can and cannot do so we provide a setter as a default. But users should understand that setting
     * this value outside the context of initial construction is not a meaningful operation.
     */
    @Override
    public void setTargetCollections(Set<Integer> targetCollections) {
        this.targetCollections = targetCollections;
    }

    public SubmitBlock getSubmitBlock() {
        for (Block block : getBlocks()) {
            if (block instanceof SubmitBlock) {
                return (SubmitBlock) block;
            }
        }

        throw new IllegalStateException("Questionnaires should always have a submit block");
    }

    @Override
    public List<Block> getBlocks() {
        return getSurvey().getBlocks();
    }

    @Override
    public void setBlocks(List<Block> blocks) {
        getSurvey().setBlocks(blocks);
    }

    public List<? extends QuestionBase> getQuestions() {
        List<QuestionBase> list = new ArrayList<QuestionBase>();

        Document survey = getSurvey();
        if (survey.getBlocks() != null) {
            for (Block element : survey.getBlocks()) {
                if (element instanceof QuestionBase) {
                    list.add((QuestionBase) element);
                }
            }
        }

        return list;
    }

    public QuestionBase getQuestionByLabel(String key) {
        return getQuestionsAsMap().get(key);
    }

    public Document getConfirmationDocument() {
        if (confirmationDocument == null) {
            confirmationDocument = new Document();
            confirmationDocument.setEntity(getId());
            confirmationDocument.setSystemEntityRelation(SystemEntityRelation.CONTENT);
            confirmationDocument.setLocale(getBodyDocument().getLocale());
        }

        return confirmationDocument;
    }

    public void setConfirmationDocument(Document confirmationDocument) {
        this.confirmationDocument = confirmationDocument;
    }

    /**
     * The list of questions associated to the Questionnaire.
     */
    public Document getSurvey() {
        Preconditions.checkNotNull(survey);

        return survey;
    }

    /**
     * @see #getSurvey()
     */
    public void setSurvey(final Document survey) {
        this.survey = survey;
        createQuestionsMap();
    }

    protected final Map<String, QuestionBase> getQuestionsAsMap() {
        // In previous iteration, it was not necessary to call the 'createQuestionsMap()' here. The map was created from
        // the 'setBlocks()' alone. However, in latest (e.g. git commit 6972a87837), the questionsMap was null
        // when we called 'matches()'. It doesn't seem all that costly just to call it every time, though it's not the
        // most elegant.
        createQuestionsMap();
        return questionsMap;
    }

    private void createQuestionsMap() {
        questionsMap = Maps.newHashMap();

        if (survey != null) {
            for (Block block : survey.getBlocks()) {
                if (block instanceof QuestionBase) {
                    QuestionBase question = (QuestionBase) block;
                    questionsMap.put(question.getLabel(), question);
                }
            }
        }
    }

    public void setNextDocument(Document nextDocument) {
        this.nextDocument = nextDocument;
    }

    public Document getNextDocument() {
        return nextDocument;
    }
}
