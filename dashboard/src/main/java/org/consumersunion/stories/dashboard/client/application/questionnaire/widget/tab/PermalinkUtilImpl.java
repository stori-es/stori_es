package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import org.consumersunion.stories.common.shared.model.Collection;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class PermalinkUtilImpl implements PermalinkUtil {
    private static final String STORIES_URL = GWT.getHostPageBaseURL();
    private static final String QUESTIONNAIRE_URL = STORIES_URL + "share/";
    private static final String COLLECTION_URL = STORIES_URL + "collections/";

    @Override
    public String getFullPermalink(String permalink, boolean isQuestionnaire) {
        return getBaseUrl(isQuestionnaire) + getPermalink(permalink, null);
    }

    @Override
    public String getPermalink(String permalink) {
        return getPermalink(permalink, null);
    }

    @Override
    public String getPermalink(String permalink, String previewKey) {
        String url = Strings.nullToEmpty(permalink).replaceFirst("/collections/|/questionnaires/", "");

        if (!Strings.isNullOrEmpty(previewKey)) {
            url += "?preview=" + previewKey;
        }

        return url;
    }

    @Override
    public void openPermalink(String permalinkUrl, boolean isQuestionnaire) {
        String baseUrl = getBaseUrl(isQuestionnaire);
        Window.open(baseUrl + permalinkUrl, "_blank", "");
    }

    private String getBaseUrl(boolean isQuestionnaire) {
        return isQuestionnaire ? QUESTIONNAIRE_URL : COLLECTION_URL;
    }

    @Override
    public String getStaticPart(Collection collection) {
        return getBaseUrl(collection.isQuestionnaire());
    }
}
