package org.consumersunion.stories.dashboard.client;

import java.util.Map;

import org.mockito.ArgumentMatcher;

import com.google.common.collect.Maps;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class PlaceRequestMatcher extends ArgumentMatcher<PlaceRequest> {
    private final String nameToken;
    private final Map<String, String> params;

    public PlaceRequestMatcher(String nameToken, Map<String, String> params) {
        this.nameToken = nameToken;
        this.params = params;
    }

    public PlaceRequestMatcher(String nameToken) {
        this(nameToken, Maps.<String, String>newHashMap());
    }

    @Override
    public boolean matches(Object argument) {
        PlaceRequest other = (PlaceRequest) argument;

        boolean parametersMatches = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            parametersMatches &= entry.getValue().equals(other.getParameter(entry.getKey(), "\uFFFF"));
        }

        return parametersMatches && other.matchesNameToken(nameToken);
    }
}
