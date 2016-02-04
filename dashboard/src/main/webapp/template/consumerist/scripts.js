(function ($) {
    // Automatically set positioning of Donate Button
    if ($(document).width() > 1000) {
        if ($.browser.msie) {
            var posWidth = (($(document).width() - 1066) / 2);
            $("#donate-button").css("right", posWidth);
        } else {
            var posWidth = (($(document).width() - 1044) / 2);
            $("#donate-button").css("right", posWidth);
        }
    }
    window.onresize = function () {
        if ($(document).width() > 1000) {
            if ($.browser.msie) {
                var posWidth = (($(document).width() - 1066) / 2);
                $("#donate-button").css("right", posWidth);
            } else {
                var posWidth = (($(document).width() - 1044) / 2);
                $("#donate-button").css("right", posWidth);
            }
        }
    };

    if ($('#comment').length > 0) {
        // Clear Text in Comment Box on Focus
        $('#comment').focus(function () {
            if ($(this).val() == 'Enter text…')
                $(this).val('');
        })
        $('#comment').blur(function () {
            if ($(this).val() == '')
                $(this).val('Enter text…');
        })
    }

    /*if ( $('ul#tabs').length > 0 ) {
     $('div.tabs').hide();
     $('div.tabs:first').show();
     $('ul#tabs li:first a').addClass('active');

     $('ul#tabs li a').on('click', function() {
     $('ul#tabs li a').removeClass('active');
     $(this).addClass('active');
     var currentTab = $(this).attr('href');
     $('div.tabs').hide();
     $(currentTab).show();
     return false;
     });
     }*/

})(jQuery);
