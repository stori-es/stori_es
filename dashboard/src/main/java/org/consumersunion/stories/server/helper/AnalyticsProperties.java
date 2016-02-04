package org.consumersunion.stories.server.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.consumersunion.stories.server.persistence.PersistenceUtil;

public class AnalyticsProperties {
    String apiKey = null;

    public AnalyticsProperties() {
        final Connection conn = PersistenceUtil.getConnection();
        try {
            ResultSet rs = conn.prepareStatement("SELECT value FROM accessKey WHERE accessKey='google analytics'")
                    .executeQuery();
            if (rs.next()) {
                apiKey = rs.getString(1);
            } else {
                apiKey = null;
            }
        } catch (SQLException e) {
            apiKey = null;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Nothing else we can do here.
            }
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public boolean isDisabled() {
        return apiKey == null;
    }
}
