package org.consumersunion.stories.common.shared.model.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("submit")
@org.codehaus.jackson.annotate.JsonTypeName("submit")
public class SubmitBlock extends Block {
    public static class NextDocument implements Serializable {
        private int documentId;
        private String title;
        private SystemEntityRelation relation;

        // For serialization
        public NextDocument() {
        }

        NextDocument(
                int documentId,
                String title,
                SystemEntityRelation relation) {
            this.documentId = documentId;
            this.title = title;
            this.relation = relation;
        }

        public static NextDocument fromDocument(Document document) {
            return new NextDocument(document.getId(), document.getTitle(), document.getSystemEntityRelation());
        }

        public static List<NextDocument> fromDocuments(Document... documents) {
            List<NextDocument> nextDocuments = new ArrayList<NextDocument>();
            for (Document document : documents) {
                nextDocuments.add(NextDocument.fromDocument(document));
            }

            return nextDocuments;
        }

        public int getDocumentId() {
            return documentId;
        }

        public void setDocumentId(int documentId) {
            this.documentId = documentId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public SystemEntityRelation getRelation() {
            return relation;
        }

        public void setRelation(SystemEntityRelation relation) {
            this.relation = relation;
        }
    }

    public enum Size implements Serializable {
        SMALL,
        MEDIUM,
        LARGE
    }

    public enum Position implements Serializable {
        LEFT,
        CENTER,
        RIGHT
    }

    private String prompt;
    private Size size;
    private Position position;
    private NextDocument nextDocument;
    private List<NextDocument> nextDocuments;

    public SubmitBlock(
            Size size,
            Position position) {
        this.size = size;
        this.position = position;

        setBlockType(BlockType.SUBMIT);
        nextDocuments = new ArrayList<NextDocument>();
    }

    public SubmitBlock() {
        this(Size.SMALL, Position.CENTER);
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public NextDocument getNextDocument() {
        return nextDocument;
    }

    public void setNextDocument(NextDocument nextDocument) {
        this.nextDocument = nextDocument;
    }

    public List<NextDocument> getNextDocuments() {
        return nextDocuments;
    }

    public void setNextDocuments(List<NextDocument> nextDocuments) {
        this.nextDocuments = nextDocuments;
    }

    @Override
    public Object clone() {
        SubmitBlock submitBlock = new SubmitBlock(size, position);

        submitBlock.setPrompt(prompt);
        submitBlock.setNextDocument(
                new NextDocument(nextDocument.getDocumentId(), nextDocument.getTitle(), nextDocument.relation));

        return submitBlock;
    }
}
