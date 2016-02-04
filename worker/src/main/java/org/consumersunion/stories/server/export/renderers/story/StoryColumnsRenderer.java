package org.consumersunion.stories.server.export.renderers.story;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.i18n.CommonI18nMessages;
import org.consumersunion.stories.server.annotations.DateCreated;
import org.consumersunion.stories.server.annotations.DateModified;
import org.consumersunion.stories.server.export.StoryCsv;
import org.consumersunion.stories.server.export.renderers.ColumnsRenderer;
import org.consumersunion.stories.server.export.renderers.DateRenderer;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.server.export.StoryColumnsName.CITY;
import static org.consumersunion.stories.server.export.StoryColumnsName.CREATED_AT;
import static org.consumersunion.stories.server.export.StoryColumnsName.GIVEN_NAME;
import static org.consumersunion.stories.server.export.StoryColumnsName.MODIFIED_AT;
import static org.consumersunion.stories.server.export.StoryColumnsName.STATE;
import static org.consumersunion.stories.server.export.StoryColumnsName.STORY_HREF;
import static org.consumersunion.stories.server.export.StoryColumnsName.STORY_ID;
import static org.consumersunion.stories.server.export.StoryColumnsName.STREET;
import static org.consumersunion.stories.server.export.StoryColumnsName.SURNAME;
import static org.consumersunion.stories.server.export.StoryColumnsName.TEXT;
import static org.consumersunion.stories.server.export.StoryColumnsName.TITLE;
import static org.consumersunion.stories.server.export.StoryColumnsName.ZIPCODE;

@Component
public class StoryColumnsRenderer implements ColumnsRenderer<StoryCsv> {
    private final DateRenderer<StoryCsv> storyDateCreatedRenderer;
    private final DateRenderer<StoryCsv> storyDateModifiedRenderer;
    private final StoryUrlRenderer storyUrlRenderer;

    @Inject
    public StoryColumnsRenderer(
            @DateCreated DateRenderer<StoryCsv> storyDateCreatedRenderer,
            @DateModified DateRenderer<StoryCsv> storyDateModifiedRenderer,
            StoryUrlRenderer storyUrlRenderer) {
        this.storyDateCreatedRenderer = storyDateCreatedRenderer;
        this.storyDateModifiedRenderer = storyDateModifiedRenderer;
        this.storyUrlRenderer = storyUrlRenderer;
    }

    @Override
    public List<String> render(StoryCsv data) {
        ArrayList<String> values = Lists.newArrayList();

        values.add(data.getId());
        values.add(getTitle(data));
        values.add(data.getText());
        values.add(storyDateCreatedRenderer.render(data));
        values.add(storyDateModifiedRenderer.render(data));
        values.add(storyUrlRenderer.render(data));

        return values;
    }

    @Override
    public List<String> getColumns() {
        return Lists.newArrayList(STORY_ID, GIVEN_NAME, SURNAME, STREET, CITY, STATE, ZIPCODE, TITLE, TEXT, CREATED_AT,
                MODIFIED_AT, STORY_HREF);
    }

    private String getTitle(StoryCsv data) {
        String title = data.getTitle();

        if (Strings.isNullOrEmpty(title)) {
            CommonI18nMessages messages = LocaleFactory.get(CommonI18nMessages.class);
            title = messages.untitled();
        }

        return title;
    }
}
