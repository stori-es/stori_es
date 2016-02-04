package org.consumersunion.stories.server.export.renderers;

import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

public abstract class UrlRenderer<T> implements ColumnRenderer<T> {
    private final TokenFormatter tokenFormatter;

    protected UrlRenderer(TokenFormatter tokenFormatter) {
        this.tokenFormatter = tokenFormatter;
    }

    @Override
    public final String render(T data) {
        return "https://stori.es/stories.jsp#" + tokenFormatter.toPlaceToken(getPlaceRequest(data));
    }

    protected abstract PlaceRequest getPlaceRequest(T data);
}
