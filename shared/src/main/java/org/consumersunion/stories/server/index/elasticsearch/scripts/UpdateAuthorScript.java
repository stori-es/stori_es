package org.consumersunion.stories.server.index.elasticsearch.scripts;

import java.util.Iterator;
import java.util.Set;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Profile;

public class UpdateAuthorScript extends InlineScript {
    private static final String FORMAT = "ctx._source.%s = %s;";

    public UpdateAuthorScript(ProfileSummary profileSummary) {
        super(renderScript(profileSummary));
    }

    private static String renderScript(ProfileSummary profileSummary) {
        StringBuilder sb = new StringBuilder();

        Profile profile = profileSummary.getProfile();
        append(sb, "authorSurname", profile.getSurname());
        append(sb, "authorGivenName", profile.getGivenName());
        append(sb, "authorFullName", getAuthorFullName(profile));
        append(sb, "authorPrimaryEmail", profileSummary.getPrimaryEmail());
        append(sb, "authorEmails", renderArray(profileSummary.getEmails()));
        append(sb, "authorPrimaryPhone", profileSummary.getPrimaryPhone());
        append(sb, "authorPhones", renderArray(profileSummary.getPhones()));
        append(sb, "storyOwner", profile.getGivenName() + " " + profile.getSurname());

        return sb.toString();
    }

    private static String renderArray(Set<String> values) {
        StringBuilder sb = new StringBuilder("[");
        Iterator<String> it = values.iterator();
        if (it.hasNext()) {
            sb.append(escape(it));
            while (it.hasNext()) {
                sb.append(",");
                sb.append(escape(it));
            }
        }

        return sb.append("]").toString();
    }

    private static String escape(Iterator<String> it) {
        return "\\\"" + it.next() + "\\\"";
    }

    private static String getAuthorFullName(Profile profile) {
        String authorFullName;
        String authorSurname = profile.getSurname();
        String authorGivenName = profile.getGivenName();
        if (authorSurname != null && authorGivenName != null) {
            authorFullName = authorGivenName + " " + authorSurname;
        } else if (authorSurname != null) {
            authorFullName = authorSurname;
        } else if (authorGivenName != null) {
            authorFullName = authorGivenName;
        } else {
            authorFullName = null;
        }

        return authorFullName;
    }

    private static void append(StringBuilder sb, String property, String value) {
        String formattedValue = value == null || value.trim().isEmpty() ? null : value;

        sb.append(String.format(FORMAT, property, formattedValue));
    }
}
