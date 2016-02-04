package org.consumersunion.stories.server.export.renderers.storyteller;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;
import org.consumersunion.stories.server.export.StoryTellerCsv;
import org.consumersunion.stories.server.export.renderers.UrlRenderer;
import org.springframework.stereotype.Component;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

@Component
public class ProfileUrlRenderer extends UrlRenderer<StoryTellerCsv> {
    private final PlaceRequest.Builder placeBuilder = new PlaceRequest.Builder().nameToken(NameTokens.profile);

    @Inject
    public ProfileUrlRenderer(TokenFormatter tokenFormatter) {
        super(tokenFormatter);
    }

    @Override
    protected PlaceRequest getPlaceRequest(StoryTellerCsv data) {
        return placeBuilder.with(ParameterTokens.id, String.valueOf(data.getId())).build();
    }
}
