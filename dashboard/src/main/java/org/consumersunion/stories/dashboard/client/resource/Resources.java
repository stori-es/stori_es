package org.consumersunion.stories.dashboard.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {
    @Source("css/generalStyle.css")
    GeneralStyle generalStyleCss();

    @Source("css/collectionsStyle.css")
    CollectionsStyle collectionsStyle();

    @Source("css/buttonStyle.css")
    ButtonStyle buttonStyleCss();

    @Source("css/builderStyle.css")
    BuilderStyle builderStyleCss();

    @Source("css/blockConfigurator.css")
    BlockConfiguratorStyle blockConfigurators();

    @Source("css/cardsStyle.css")
    CardsStyle cards();

    @Source("css/gwtUploadStyle.css")
    GwtUploadStyle gwtUploadStyle();

    @Source("image/logo.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource storiesLogo();

    @Source("image/selectedMenu.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource selectedMenu();

    @Source("image/searchIcon.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource searchIcon();

    @Source("image/delete.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource delete();

    @Source("image/deleteHover.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource deleteHover();

    @Source("image/addButton.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource addButton();

    @Source("image/addButtonHover.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource addButtonHover();

    @Source("image/link.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource linkIcon();

    @Source("image/clearSearch.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource clearSearch();

    @Source("image/mapView.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource mapView();

    @Source("image/listView.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource listView();

    @Source("image/warning.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource warning();

    @Source("image/dragbg.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.Horizontal)
    ImageResource dragBackground();

    @Source("image/watchCheckbox.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource watchCheckbox();

    @Source("image/onOffCheckbox.png")
    @ImageResource.ImageOptions(repeatStyle = ImageResource.RepeatStyle.None)
    ImageResource onOffCheckbox();
}
