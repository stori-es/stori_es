package org.consumersunion.stories.common.client.place;

import java.util.List;

import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

public class ClientPlaceManager extends PlaceManagerImpl {
    private final PlaceRequest defaultPlaceRequest;

    private PlaceRequest currentHistoryPlace;

    @Inject
    ClientPlaceManager(
            EventBus eventBus,
            TokenFormatter tokenFormatter,
            PlaceHistoryHandler.Historian historian,
            @DefaultPlace String defaultPlaceNameToken) {
        super(eventBus, tokenFormatter, historian);

        this.defaultPlaceRequest = new PlaceRequest.Builder().nameToken(defaultPlaceNameToken).build();
    }

    @Override
    public void revealDefaultPlace() {
        revealPlace(defaultPlaceRequest, false);
    }

    @Override
    public void updateHistory(PlaceRequest request, boolean updateBrowserUrl) {
        List<PlaceRequest> placeHierarchy = getCurrentPlaceHierarchy();
        placeHierarchy.set(placeHierarchy.size() - 1, request);

        super.updateHistory(request, updateBrowserUrl);

        if (updateBrowserUrl) {
            currentHistoryPlace = request;
        }
    }

    public PlaceRequest getCurrentHistoryPlace() {
        return currentHistoryPlace;
    }
}
