package org.consumersunion.stories.server.persistence;

import java.util.ArrayList;

import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;

import junit.framework.TestCase;
import net.lightoze.gwt.i18n.server.LocaleProxy;

public class QuestionnairePersisterTest extends TestCase {
    static {
        LocaleProxy.initialize();
    }

    public void testCreate() {
        final Questionnaire template = new Questionnaire();

        final Question question = new Question();
        question.setBlockType(BlockType.FIRST_NAME);
        question.setDataType("foo");
        question.setLabel("foo label");
        question.addOption("foo1", "foo2");
        question.setRequired(true);
        question.setMinLength(1);
        question.setMaxLength(4);

        final Content content = new Content();
        content.setBlockType(BlockType.CONTENT);
        content.setContent("foo");

        final ArrayList<Block> elements = new ArrayList<Block>();
        elements.add(question);
        elements.add(content);
        template.getSurvey().setBlocks(elements);
    }

    public void testUpdate() {
        final Questionnaire template = new Questionnaire();

        final Question question = new Question();
        question.setBlockType(BlockType.FIRST_NAME);
        question.setDataType("foo");
        question.setLabel("foo label");
        question.addOption("foo1", "foo2");
        question.setRequired(true);
        question.setMinLength(1);
        question.setMaxLength(4);

        final Content content = new Content();
        content.setBlockType(BlockType.CONTENT);
        content.setContent("foo");

        final ArrayList<Block> elements = new ArrayList<Block>();
        elements.add(question);
        elements.add(content);
        elements.add(new SubmitBlock());
        template.getSurvey().setBlocks(elements);
    }

    public void testDelete() {
        final Questionnaire template = new Questionnaire();

        final Question question = new Question();
        question.setBlockType(BlockType.FIRST_NAME);
        question.setDataType("foo");
        question.setLabel("foo label");
        question.addOption("foo1", "foo2");
        question.setRequired(true);
        question.setMinLength(1);
        question.setMaxLength(4);

        final Content content = new Content();
        content.setBlockType(BlockType.CONTENT);
        content.setContent("foo");

        final ArrayList<Block> elements = new ArrayList<Block>();
        elements.add(question);
        elements.add(content);
        elements.add(new SubmitBlock());
        template.getSurvey().setBlocks(elements);
    }
}
