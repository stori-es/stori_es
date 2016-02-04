package org.consumersunion.stories.dashboard.client.application.ui;

public enum BuilderTab {
    QUESTIONS("Questions/Content"),
    CONTENT("Content"),
    ON_SUBMIT("On Submit"),
    PUBLICATION("Publication");

    private final String label;

    BuilderTab(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
