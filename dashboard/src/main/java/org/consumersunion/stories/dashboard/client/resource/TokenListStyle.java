package org.consumersunion.stories.dashboard.client.resource;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellList;

public interface TokenListStyle extends CellList.Resources {
    @Source({CellList.Style.DEFAULT_CSS, "css/tokenListStyle.css"})
    ListStyle cellListStyle();

    @Source("image/remove_token.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource removeToken();

    @Source("image/remove_token_hover.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource removeTokenHover();

    interface ListStyle extends CellList.Style {
    }
}
