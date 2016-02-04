package org.consumersunion.stories.server.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;

public class PersistersTestUtils {
    public static int getRootProfileIdForOrganization(int orgId) {
        return PersistenceUtil.process(new ProcessFunc<Integer, Integer>(orgId) {
            @Override
            public Integer process() {
                try {
                    PreparedStatement select =
                            conn.prepareStatement("SELECT id FROM profile WHERE user=0 AND organization=?");

                    select.setInt(1, input);

                    ResultSet resultSet = select.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }
}
