package org.consumersunion.stories.common.shared.model;

public class Theme extends SystemEntity {

    private String name;
    private String themePage;

    public Theme() {

    }

    public Theme(int id, int version) {
        super(id, version);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThemePage() {
        return themePage;
    }

    public void setThemePage(String themePage) {
        this.themePage = themePage;
    }
}
