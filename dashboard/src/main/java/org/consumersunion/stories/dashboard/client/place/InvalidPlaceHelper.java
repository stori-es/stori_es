package org.consumersunion.stories.dashboard.client.place;

import javax.inject.Inject;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class InvalidPlaceHelper {
    private final PlaceManager placeManager;

    @Inject
    InvalidPlaceHelper(PlaceManager placeManager) {
        this.placeManager = placeManager;
    }

    public void handle(String fallbackNameToken) {
        placeManager.unlock();
        if (placeManager.getHierarchyDepth() > 1) {
            placeManager.revealRelativePlace(-1);
        } else {
            PlaceRequest request = new PlaceRequest.Builder()
                    .nameToken(fallbackNameToken)
                    .build();
            placeManager.revealPlace(request);
        }
    }
}
