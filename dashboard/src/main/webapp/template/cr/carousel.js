(function (jQuery) {
    var myCarousel = function (elem, opt) {
        settings = jQuery.extend({
            start: 1,
            vscroll: 1,
            btnLeft: 'left-arrow-btn',
            btnRight: 'right-arrow-btn',
            viewport: 'carousel-viewport',
            list: 'carousel-list',
            listItem: 'carousel-list-item',
            circular: true,
            visible: 4,
            scrollDuration: 500,
            disabledClass: 'disabled',
            activeItem: 0
        }, opt);
        var listItems = jQuery(elem).find("." + settings.listItem),
            list = jQuery(elem).find("." + settings.list),
            btnLeft = jQuery(elem).find("." + settings.btnLeft),
            btnRight = jQuery(elem).find("." + settings.btnRight),
            leftArrow = jQuery(elem).find(".left-arrow"),
            rightArrow = jQuery(elem).find(".right-arrow"),
            size = listItems.size(),
            vscroll = settings.vscroll,
            start = settings.start,
            width = listItems.outerWidth(),
            scrolling = false,
            currSlide = 1,
            currPos = 0,
            visible = settings.visible;

        function setCurrentSlide(intCurrSlide) {
            currSlide = intCurrSlide;
        }

        function getCurrentSlide() {
            return currSlide;
        }

        list.width(listItems.outerWidth() * size + 10 + "px");
        if (settings.visible >= size) {
            rightArrow.css({backgroundPosition: '0 0'});
            btnRight.css({cursor: 'default'});
        }
        btnLeft.css({cursor: 'default'});
        btnRight.bind("mouseover", function () {
            if (!((currSlide + visible - 1) >= size )) {
                jQuery(this).find(".right-arrow").css({backgroundPosition: '0 0'});
            }
        }).bind("mouseout", function () {
            if (!((currSlide + visible - 1) >= size )) {
                jQuery(this).find(".right-arrow").css({backgroundPosition: '0 -9px'});
            } else {
                jQuery(this).find(".right-arrow:not(.disabledClass)").css({backgroundPosition: '0 0'});
            }
        }).bind("click", function () {
            slideScroll('right')
        });
        btnLeft.bind("mouseover", function () {
            if (!(currSlide <= start)) {
                jQuery(this).find(".left-arrow:not(.disabledClass)").css({backgroundPosition: '0 0'});
            }
        }).bind("mouseout", function () {
            if (!(currSlide <= start)) {
                jQuery(this).find(".left-arrow:not(.disabledClass)").css({backgroundPosition: '0 -9px'});
            } else {
                jQuery(this).find(".left-arrow:not(.disabledClass)").css({backgroundPosition: '0 0'});
            }
        }).bind("click", function () {
            slideScroll('left')
        });
        listItems.bind("click", function () {
            listItems.eq(settings.activeItem).removeClass("on");
            settings.activeItem = listItems.index(this)
            listItems.eq(settings.activeItem).addClass("on");
        })
        function slideScroll(vscrollTo) {
            if (!scrolling) {
                switch (vscrollTo) {
                    case 'right':
                        if ((currSlide + visible) > size) {
                            return;
                        } else {
                            currSlide = currSlide + vscroll;
                            btnLeft.css({cursor: 'pointer'});
                            leftArrow.css({backgroundPosition: '0 -9px'});
                        }
                        break;
                    case 'left':
                        if (currSlide <= start) {
                            return;
                        } else {
                            currSlide = currSlide - vscroll;
                            rightArrow.css({backgroundPosition: '0 -9px'});
                        }
                        break;
                }
                ;
                currPos = (currSlide - 1) * width;
                scrolling = true;
                list.animate({
                    left: -currPos
                }, settings.scrollDuration, function () {
                    scrolling = false;

                });
            }
        };
        $(elem).find('.carousel-list-item').bind('mouseover', function () {
            $(this).find('.title-text').css('color', '#666666');
        }).bind('mouseout', function () {
            $(this).find('.title-text').css('color', '');
        });
        return {
            setCurrSlideDataAndScroll: function (currSlideTo, direction) {
                setCurrentSlide(currSlideTo);
                slideScroll(direction);
            },
            getCurrSlideData: function () {
                return getCurrentSlide();
            }
        }
    };
    jQuery.fn.carousel = function (opt) {
        return this.each(function () {
            var elem = jQuery(this);
            var myplugin = new myCarousel(elem, opt);
            elem.data("carousel", myplugin);
        });
    };
})(jQuery);
