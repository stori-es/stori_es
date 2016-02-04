package org.consumersunion.stories.server.export.renderers.story;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;
import org.consumersunion.stories.server.export.StoryCsv;
import org.consumersunion.stories.server.export.renderers.UrlRenderer;
import org.springframework.stereotype.Component;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

@Component
public class StoryUrlRenderer extends UrlRenderer<StoryCsv> {
    private final PlaceRequest.Builder placeBuilder = new PlaceRequest.Builder().nameToken(NameTokens.story);

    @Inject
    public StoryUrlRenderer(TokenFormatter tokenFormatter) {
        super(tokenFormatter);
    }

    @Override
    protected PlaceRequest getPlaceRequest(StoryCsv data) {
        return placeBuilder.with(ParameterTokens.id, String.valueOf(data.getId())).build();
    }
}
