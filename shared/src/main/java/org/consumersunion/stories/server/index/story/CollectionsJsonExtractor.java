package org.consumersunion.stories.server.index.story;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.server.persistence.ResultSetExtractor;
import org.json.JSONObject;

public class CollectionsJsonExtractor implements ResultSetExtractor<List<String>> {
    @Override
    public List<String> extractData(ResultSet resultSet) throws SQLException {
        List<String> collections = new ArrayList<String>();
        while (resultSet.next()) {
            try {
                JSONObject collection = new JSONObject();
                collection.put("id", resultSet.getInt(1));
                collection.put("title", resultSet.getString(2));
                collections.add(collection.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resultSet.close();

        return collections;
    }
}
