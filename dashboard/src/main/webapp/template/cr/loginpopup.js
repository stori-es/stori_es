function addInputSubmitEvent(e) {
    if (window.event) {
        e = window.event;
        if (e.keyCode == 13) {
            if (typeof document.forms["login"].submit === "undefined") {
                document.forms["login"][0].submit();
                return false;
            }
            else {
                document.forms["login"].submit();
                return false;
            }
        }
    }
}

jQuery(document).ready(function () {
    jQuery('#dialog').dialog({
        closeText: 'close',
        autoOpen: false,
        width: 800,
        modal: true
    }).parent().removeClass("ui-corner-all");
    jQuery(".icon-lock").bind("mouseenter", function () {
        if (jQuery(this).is("img")) {
            jQuery(this).attr("src", "/cro/application-resources/modules/loginpopup/images/icon_lock_on.png");
        } else {
            jQuery(this).siblings(".icon-lock").attr("src", "/cro/application-resources/modules/loginpopup/images/icon_lock_on.png");
        }
    }).bind("mouseleave", function () {
        if (jQuery(this).is("img")) {
            jQuery(this).attr("src", "/cro/application-resources/modules/loginpopup/images/icon_lock.png");
        } else {
            jQuery(this).siblings(".icon-lock").attr("src", "/cro/application-resources/modules/loginpopup/images/icon_lock.png");
        }
    })
    jQuery("#sign-in-menu-link").dialog({
        autoOpen: false,
        width: 230,
        modal: true,
        dialogClass: 'no-title'
    }).siblings('div.ui-dialog-titlebar').remove();
    jQuery("#sign-in-menu-link").parent().removeClass("ui-widget").removeClass("ui-widget-content").removeClass("ui-corner-all");
    jQuery("#sign-in-menu-link").removeClass("ui-widget").removeClass("ui-widget-content").removeClass("ui-corner-all");
    jQuery.fn.logInMenu = function (options) {
        jQuery(this).each(function () {
            jQuery(this).bind('mouseenter', function (e) {
                var $popup = jQuery("#log-in-popup-wrap");
                $popup.css({'display': 'block', 'position': 'absolute'});
                $popup.css({
                    'left': jQuery(this).offset().left - 10 + "px",
                    'top': jQuery(this).offset().top - 64 + "px"
                });
            });
        });
    };
    jQuery(".icon-lock,#log-in-popup-wrap").bind('mouseleave', function (e) {
        if (jQuery(e.relatedTarget).parents(".icon-lock,#log-in-popup-wrap").length > 0) {
            return;
        } else {
            jQuery("#log-in-popup-wrap").hide();
        }
    });
    jQuery("#log-in-popup-wrap").bind('mouseleave', function () {
        jQuery(this).hide();
    })
    jQuery('.icon-lock').logInMenu();
    jQuery("a.sign-in").click(function () {
        jQuery("#sign-in-menu-link").parent().css({position: "fixed"});
        var top = jQuery(this).offset().top - jQuery(window).scrollTop();
        jQuery('#sign-in-menu-link').dialog("option", "position", [null, top]);
        jQuery('#sign-in-menu-link').dialog('open');
        return false;
    });
});
