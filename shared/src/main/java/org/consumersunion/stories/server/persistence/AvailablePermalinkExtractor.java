package org.consumersunion.stories.server.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class AvailablePermalinkExtractor {
    public String extractPermalink(ResultSet rs, String basePermalink) throws SQLException {
        int max = -1;
        while (rs.next()) {
            String suffix = rs.getString(1).replace(basePermalink, "").replace("-", "");
            if (!Strings.isNullOrEmpty(suffix)) {
                try {
                    int value = Integer.valueOf(suffix);
                    if (value >= max) {
                        max = value + 1;
                    }
                } catch (NumberFormatException ignored) {
                }
            } else {
                if (max < 0) {
                    max = 0;
                }
            }
        }

        String permalink = basePermalink;
        if (max != -1) {
            permalink = permalink + "-" + max;
        }

        return permalink;
    }
}
