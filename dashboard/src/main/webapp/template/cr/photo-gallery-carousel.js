(function (jQuery) {
    var myPhotoGalleryCarousel = function (elem, opt) {
        settings = jQuery.extend({
            start: 1,
            vscroll: 5,
            btnLeft: 'left-arrow-btn',
            btnRight: 'right-arrow-btn',
            viewport: 'carousel-viewport',
            list: 'carousel-list',
            listItem: 'carousel-list-item',
            circular: true,
            visible: 5,
            scrollDuration: 500,
            disabledClass: 'disabled',
            activeItem: 0,
            expandButton: 'expand-tool',
            closeButton: 'close-btn',
            images: [],
            container: 'jqEnlargeCarousel',
            carouselContainer: 'photo-gallery-large-image-carousel',
            largeImageContainer: 'photo-gallery-large-container'
        }, opt);
        var mainContainer = jQuery("#" + settings.container);

        function getPhotoGalleryCarouselContent(options) {
            var images = options.images;
            var retval = "<div class='largeImage'>" +
                "<a href='javascript:void(0);' class='close'>" +
                "<img src='/cro/application-resources/modules/toolbar/images/close_recently_viewed.gif' alt='Close' />" +
                "</a>" +
                "<div id='photo-gallery-large-container'>" +
                "</div>" +
                "</div>";
            if (images.length > 1) {
                retval +=
                    "<div id='photo-gallery-large-image-carousel' class='carousel-videos'>" +
                    "<div class='" + options.btnLeft + "' style='cursor: default;'>" +
                    "<div class='arrow-border'>" +
                    "<div class='left-arrow' style='background-position: 0px 0px;'>&nbsp;</div>" +
                    "</div>" +
                    "</div>" +
                    "<div class='" + options.viewport + "'>" +
                    "<div id='photo-gallery-large-image-carousel-list' class='" + options.list + "'>";
                for (var i = 0; i < images.length; i++) {
                    if ((images[i].large != "") && (typeof images[i].large !== "undefined")) {
                        if (i == 0) {
                            retval += "<div class='" + options.listItem + " on'><img src='" + images[i].largeThumbnail + "'></div>";
                        }
                        else {
                            retval += "<div class='" + options.listItem + "'><img src='" + images[i].largeThumbnail + "'></div>";
                        }
                    }
                }
                retval += "</div>" +
                    "</div>" +
                    "<div class='" + options.btnRight + "'>" +
                    "<div class='arrow-border'>" +
                    "<div class='right-arrow'>&nbsp;</div>" +
                    "</div>" +
                    "</div>" +
                    "</div>"
            }
            return retval;
        }

        mainContainer.html(getPhotoGalleryCarouselContent(settings));
        jQuery("#" + settings.carouselContainer).carousel(settings);
        jQuery("#" + settings.largeImageContainer).html('<img id="photo-gallery-large-image" src="' + settings.images[0].large + '">');
        jQuery("#" + settings.carouselContainer).find("." + settings.listItem).bind("click", function () {
            for (var i = 0; i < settings.images.length; i++) {
                if (jQuery(this).find("img").attr("src") == settings.images[i].largeThumbnail) {
                    jQuery("#photo-gallery-large-image").attr("src", settings.images[i].large);
                }
            }
        });
        jQuery("#" + settings.container).dialog({
            "width": 730,
            modal: true,
            open: function (event, ui) {
                jQuery(this).parent().find(".ui-dialog-titlebar-close").hide();
                window.setTimeout(function () {
                    jQuery(document).unbind('mousedown.dialog-overlay').unbind('mouseup.dialog-overlay');
                }, 100);
            }
        });
        jQuery(window).resize(function () {
            $("#" + settings.container).dialog("option", "position", "center");
        });
        jQuery("#" + settings.expandButton).bind("click", function () {
            jQuery("#" + settings.container).dialog("open");
        });
        jQuery("." + settings.container).find(".close").bind("click", function () {
            jQuery("#" + settings.container).dialog("close");
        });
        jQuery(".ui-widget-overlay").live("click", function () {
            jQuery("#" + settings.container).dialog("close");
        });
    };
    jQuery.fn.photoGallaryCarousel = function (opt) {
        return this.each(function () {
            var elem = jQuery(this);
            var myplugin = new myPhotoGalleryCarousel(elem, opt);
            elem.data("myPhotoGalleryCarousel", myplugin);
        });
    };
})(jQuery);
