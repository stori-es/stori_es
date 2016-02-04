/*!
 * jQzoom Evolution Library v2.3  - Javascript Image magnifier
 * http://www.mind-projects.it
 *
 * Copyright 2011, Engineer Marco Renzi
 * Licensed under the BSD license.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * Date: 03 May 2011 22:16:00
 */
(function ($) {
    //GLOBAL VARIABLES
    var isIE6 = ($.browser.msie && $.browser.version < 7);
    var body = $(document.body);
    var window = $(window);
    var jqzoompluging_disabled = false; //disabilita globalmente il plugin

    $.fn.jqzoom = function (options) {
        return this.each(function () {
            new jqzoom(this, options);
            /*
             var node = this.nodeName.toLowerCase();
             if (node == 'a') {
             new jqzoom(this, options);
             }*/
        });
    };

    jqzoom = function (ele, options) {

        //BEGIN: Carousel
        var carouselSettings = $.extend({}, $.jqcarousel.defaults, options.carousel || {});
        carouselSettings.images = options.images;

        function getCarouselContent(options) {
            var images = options.images;
            var visibilityString = (images[0].extraLarge != "" && !isIDevice()) ? "visibility: visible;" : "visibility: hidden;";
            var retVal =
                "<div style='margin-top: 20px;'>" +
                "<div id='image-content'>" +
                "<a href='" + images[0].extraLarge + "' id='jqzoom' class='jqzoom' rel='gal1'  title='' >" +
                "<img src='" + images[0].medium + "'  title='' ></a>" +
                "</div>" +

                "<div class='photo-tools'>" +
                "<div class='zoom-tool' style='" + visibilityString + "'>" +
                "<img src='/cro/application-resources/modules/jqzoom/images/ICN_zoom_off.png' alt='zoom' />" +
                "</div>";

            if (options.hasExpandTool == "true") {
                retVal += "<div class='expand-tool' id='expand-tool'><img id='icn_expand' src='/cro/application-resources/modules/jqzoom/images/ICN_expand_off.png' alt='expand' /></div>";
            }
            retVal += "</div>";

            if (images.length > 1) {
                retVal += "<div id='photo-gallery-small-carousel' class='carousel-images'>" +
                    "<div class='left-arrow-btn'>" +
                    "<div class='arrow-border'>" +
                    "<div class='left-arrow'>" +
                    "&nbsp;" +
                    "</div>" +
                    "</div>" +
                    "</div>" +
                    "<div class='carousel-viewport'>" +
                    "<div class='carousel-list'>";


                if (images != undefined) {
                    for (var i = 0; i < images.length; i++) {
                        retVal += "<div class='carousel-list-item";
                        retVal += (i == 0) ? " on'" : "'";
                        retVal += "><a><img width='65' height='47'" +
                            "src='" + images[i].thumbnail + "'/> </a>" +
                            "</div>";
                    }
                }


                retVal += "</div>" +
                    "</div>" +
                    "<div class='right-arrow-btn'>" +
                    "<div class='arrow-border'>" +
                    "<div class='right-arrow'>" +
                    "&nbsp;" +
                    "</div>" +
                    "</div>" +
                    "</div>" +
                    "</div>";
            }

            if (images.length == 1 && images[0].extraLarge != undefined && images[0].extraLarge != '') {
                retVal += "<div style='clear:both'>&nbsp;</div>"; //to match the height of the zoom window.
            } else {
                retVal += "<div style='clear:both'>&nbsp;</div>"; //to match the height of the zoom window.
            }

            return retVal;
        }

        var carouselElement = jQuery(getCarouselContent(options));
        jQuery(ele).append(carouselElement);


        var listItems = jQuery(ele).find("." + carouselSettings.listItem),
            list = jQuery(ele).find("." + carouselSettings.list),
            btnLeft = jQuery(ele).find("." + carouselSettings.btnLeft),
            btnRight = jQuery(ele).find("." + carouselSettings.btnRight),
            leftArrow = jQuery(ele).find(".left-arrow"),
            rightArrow = jQuery(ele).find(".right-arrow"),
            expandTool = jQuery(ele).find("#expand-tool"),
            size = listItems.size(),
            vscroll = carouselSettings.vscroll,
            start = carouselSettings.start,
            width = listItems.outerWidth(),
            scrolling = false,
            currSlide = 1,
            currPos = 0,
            visible = carouselSettings.visible;

        expandTool.bind("click", options.expandzoomfn);
        list.width(listItems.outerWidth() * size + 10 + "px");
        if (carouselSettings.visible >= size) {
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
        listItems.bind("click", function (e) {
            if (listItems.index(this) != carouselSettings.activeItem) {
                e.preventDefault();
                listItems.eq(carouselSettings.activeItem).removeClass("on");
                carouselSettings.activeItem = listItems.index(this)
                listItems.eq(carouselSettings.activeItem).addClass("on");
                obj.swapimage(carouselSettings.activeItem);
                return false;
            }
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
                }, carouselSettings.scrollDuration, function () {
                    scrolling = false;
                });
            }
        };

        var el = $(ele).find('.jqzoom');
        var api = null;
        api = $(el).data("jqzoom");
        if (api) return api;
        var obj = this;
        var settings = $.extend({}, $.jqzoom.defaults, options || {});
        obj.el = el;
        el.rel = $(el).attr('rel');
        //ANCHOR ELEMENT
        el.zoom_active = false;
        el.zoom_disabled = false; //to disable single zoom instance
        el.largeimageloading = false; //tell us if large image is loading
        el.largeimageloaded = false; //tell us if large image is loaded
        el.activeItem = carouselSettings.activeItem;
        el.scale = {};
        el.timer = null;
        el.mousepos = {};
        el.mouseDown = false;
        $(el).css({
            'outline-style': 'none',
            'text-decoration': 'none'
        });
        //BASE IMAGE
        var img = $("img:eq(0)", el);
        el.title = $(el).attr('title');
        el.imagetitle = img.attr('title');
        var zoomtitle = ($.trim(el.title).length > 0) ? el.title : el.imagetitle;
        var smallimage = new Smallimage(img);
        var lens = new Lens();
        var stage = new Stage();
        var largeimage = new Largeimage();
        var loader = new Loader();
        //preventing default click,allowing the onclick event [exmple: lightbox]
        $(el).bind('click', function (e) {
            e.preventDefault();
            return false;
        });
        //setting the default zoomType if not in settings
        var zoomtypes = ['standard', 'drag', 'innerzoom', 'reverse'];
        if ($.inArray($.trim(settings.zoomType), zoomtypes) < 0) {
            settings.zoomType = 'standard';
        }
        $.extend(obj, {
            create: function () { //create the main objects
                //create ZoomPad
                if ($(".zoomPad", el).length == 0) {
                    el.zoomPad = $('<div/>').addClass('zoomPad');
                    img.wrap(el.zoomPad);
                }
                if (settings.zoomType == 'innerzoom') {
                    settings.zoomWidth = smallimage.w;
                    settings.zoomHeight = smallimage.h;
                }
                //creating ZoomPup
                if ($(".zoomPup", el).length == 0) {
                    lens.append();
                }
                //creating zoomWindow
                if ($(".zoomWindow", el).length == 0) {
                    stage.append();
                }
                //creating Preload
                if ($(".zoomPreload", el).length == 0) {
                    loader.append();
                }
                //preloading images
                if (settings.preloadImages || settings.zoomType == 'drag' || settings.alwaysOn) {
                    obj.load();
                }
                obj.init();
            },
            init: function () {
                //drag option
                if (settings.zoomType == 'drag') {
                    $(".zoomPad", el).mousedown(function () {
                        el.mouseDown = true;
                    });
                    $(".zoomPad", el).mouseup(function () {
                        el.mouseDown = false;
                    });
                    document.body.ondragstart = function () {
                        return false;
                    };
                    $(".zoomPad", el).css({
                        cursor: 'default'
                    });
                    $(".zoomPup", el).css({
                        cursor: 'move'
                    });
                }
                if (settings.zoomType == 'innerzoom') {
                    $(".zoomWrapper", el).css({
                        cursor: 'crosshair'
                    });
                }
                if (options.hasExpandTool == "true" && options.expandzoomfn != undefined && options.expandzoomfn != "") {
                    $(".zoomPad", el).bind('click', options.expandzoomfn);
                }
                $(".zoomPad", el).bind('mouseenter mouseover', function (event) {
                    if (!isIDevice()) {
                        if (el.zoom_disabled == false) {
                            img.attr('title', '');
                            $(el).attr('title', '');
                            el.zoom_active = true;
                            //if loaded then activate else load large image
                            smallimage.fetchdata();


                            var url = $(el).attr('href');
                            if (url != "") {
                                if (el.largeimageloaded) {
                                    obj.activate(event);
                                } else {
                                    obj.load();
                                }
                            }
                        }
                    }
                });
                $(".zoomPad", el).bind('mouseleave', function (event) {
                    if (!isIDevice()) {
                        obj.deactivate();
                    }
                });
                $(".zoomPad", el).bind('mousemove', function (e) {
                    if (!isIDevice()) {
                        if (el.zoom_disabled == false) {
                            //prevent fast mouse mevements not to fire the mouseout event
                            if (e.pageX > smallimage.pos.r || e.pageX < smallimage.pos.l || e.pageY < smallimage.pos.t || e.pageY > smallimage.pos.b) {
                                lens.setcenter();
                                return false;
                            }
                            el.zoom_active = true;
                            if (el.largeimageloaded && !$('.zoomWindow', el).is(':visible')) {
                                obj.activate(e);
                            }
                            if (el.largeimageloaded && (settings.zoomType != 'drag' || (settings.zoomType == 'drag' && el.mouseDown))) {
                                lens.setposition(e);
                            }
                        }
                    }
                });
            },
            load: function () {
                if (!isIDevice()) {
                    if (el.largeimageloaded == false && el.largeimageloading == false) {
                        var url = $(el).attr('href');
                        el.largeimageloading = true;
                        largeimage.loadimage(url);
                    }
                }

            },
            activate: function (e) {
                if (!isIDevice()) {
                    clearTimeout(el.timer);
                    //show lens and zoomWindow
                    lens.show();
                    stage.show();
                }
            },
            deactivate: function (e) {
                switch (settings.zoomType) {
                    case 'drag':
                        //nothing or lens.setcenter();
                        break;
                    default:
                        img.attr('title', el.imagetitle);
                        $(el).attr('title', el.title);
                        if (settings.alwaysOn) {
                            lens.setcenter();
                        } else {
                            stage.hide();
                            lens.hide();
                        }
                        break;
                }
                el.zoom_active = false;
            },

            swapimage: function (index) {
                el.largeimageloading = false;
                el.largeimageloaded = false;
                el.activeItem = index;
                var options = new Object();
                options.smallimage = settings.images[index].medium;
                options.largeimage = settings.images[index].extraLarge;
                if (typeof options.smallimage !== "undefined") {
                    var smallimage = options.smallimage;
                    img.attr('src', smallimage);
                }
                if (typeof options.largeimage !== "undefined" && options.largeimage != "" && !isIDevice()) {
                    jQuery(".zoom-tool").css('visibility', 'visible');
                    var largeimage = options.largeimage;
                    $(el).attr('href', largeimage);
                    el.zoom_disabled = false;
                } else {
                    jQuery(".zoom-tool").css('visibility', 'hidden');
                    el.zoom_disabled = true;
                }
                lens.hide();
                stage.hide();
                if (options.largeimage != "" && !isIDevice()) {
                    obj.load();
                }
                return false;
            }


        });
        //sometimes image is already loaded and onload will not fire
        if (img[0].complete) {
            //fetching data from sallimage if was previously loaded
            smallimage.fetchdata();
            if ($(".zoomPad", el).length == 0) obj.create();
        }
        /*========================================================,
         |   Smallimage
         |---------------------------------------------------------:
         |   Base image into the anchor element
         `========================================================*/

        function Smallimage(image) {
            var $obj = this;
            this.node = image[0];
            this.findborder = function () {
                var bordertop = 0;
                bordertop = image.css('border-top-width');
                btop = '';
                var borderleft = 0;
                borderleft = image.css('border-left-width');
                bleft = '';
                if (bordertop) {
                    for (i = 0; i < 3; i++) {
                        var x = [];
                        x = bordertop.substr(i, 1);
                        if (isNaN(x) == false) {
                            btop = btop + '' + bordertop.substr(i, 1);
                        } else {
                            break;
                        }
                    }
                }
                if (borderleft) {
                    for (i = 0; i < 3; i++) {
                        if (!isNaN(borderleft.substr(i, 1))) {
                            bleft = bleft + borderleft.substr(i, 1)
                        } else {
                            break;
                        }
                    }
                }
                $obj.btop = (btop.length > 0) ? eval(btop) : 0;
                $obj.bleft = (bleft.length > 0) ? eval(bleft) : 0;
            };
            this.fetchdata = function () {
                $obj.findborder();
                $obj.w = image.width();
                $obj.h = image.height();
                $obj.ow = image.outerWidth();
                $obj.oh = image.outerHeight();
                $obj.pos = image.offset();
                $obj.pos.l = image.offset().left + $obj.bleft;
                $obj.pos.t = image.offset().top + $obj.btop;
                $obj.pos.r = $obj.w + $obj.pos.l;
                $obj.pos.b = $obj.h + $obj.pos.t;
                $obj.rightlimit = image.offset().left + $obj.ow;
                $obj.bottomlimit = image.offset().top + $obj.oh;

            };
            this.node.onerror = function () {
                alert('Problems while loading image.');
                throw 'Problems while loading image.';
            };
            this.node.onload = function () {
                $obj.fetchdata();
                if ($(".zoomPad", el).length == 0) obj.create();
            };
            return $obj;
        };
        /*========================================================,
         |  Loader
         |---------------------------------------------------------:
         |  Show that the large image is loading
         `========================================================*/

        function Loader() {
            var $obj = this;
            this.append = function () {
                this.node = $('<div/>').addClass('zoomPreload').css('visibility', 'hidden').html(settings.preloadText);
                $('.zoomPad', el).append(this.node);
            };
            this.show = function () {
                this.node.top = (smallimage.oh - this.node.height()) / 2;
                this.node.left = (smallimage.ow - this.node.width()) / 2;
                //setting position
                this.node.css({
                    top: this.node.top,
                    left: this.node.left,
                    position: 'absolute',
                    visibility: 'visible'
                });
            };
            this.hide = function () {
                this.node.css('visibility', 'hidden');
            };
            return this;
        }

        /*========================================================,
         |   Lens
         |---------------------------------------------------------:
         |   Lens over the image
         `========================================================*/

        function Lens() {
            var $obj = this;
            this.node = $('<div/>').addClass('zoomPup');
            //this.nodeimgwrapper = $("<div/>").addClass('zoomPupImgWrapper');
            this.append = function () {
                $('.zoomPad', el).append($(this.node).hide());
                if (settings.zoomType == 'reverse') {
                    this.image = new Image();
                    this.image.src = smallimage.node.src; // fires off async
                    $(this.node).empty().append(this.image);
                }
            };
            this.setdimensions = function () {
                this.node.w = (parseInt((settings.zoomWidth) / el.scale.x) > smallimage.w ) ? smallimage.w : (parseInt(settings.zoomWidth / el.scale.x));
                this.node.h = (parseInt((settings.zoomHeight) / el.scale.y) > smallimage.h ) ? smallimage.h : (parseInt(settings.zoomHeight / el.scale.y));
                this.node.top = (smallimage.oh - this.node.h - 2) / 2;
                this.node.left = (smallimage.ow - this.node.w - 2) / 2;
                //centering lens
                this.node.css({
                    top: 0,
                    left: 0,
                    width: this.node.w + 'px',
                    height: this.node.h + 'px',
                    position: 'absolute',
                    display: 'none',
                    borderWidth: 1 + 'px'
                });
                if (settings.zoomType == 'reverse') {
                    this.image.src = smallimage.node.src;
                    $(this.node).css({
                        'opacity': 1
                    });

                    $(this.image).css({
                        position: 'absolute',
                        display: 'block',
                        left: -(this.node.left + 1 - smallimage.bleft) + 'px',
                        top: -(this.node.top + 1 - smallimage.btop) + 'px'
                    });

                }
            };
            this.setcenter = function () {
                //calculating center position
                this.node.top = (smallimage.oh - this.node.h - 2) / 2;
                this.node.left = (smallimage.ow - this.node.w - 2) / 2;
                //centering lens
                this.node.css({
                    top: this.node.top,
                    left: this.node.left
                });
                if (settings.zoomType == 'reverse') {
                    $(this.image).css({
                        position: 'absolute',
                        display: 'block',
                        left: -(this.node.left + 1 - smallimage.bleft) + 'px',
                        top: -(this.node.top + 1 - smallimage.btop) + 'px'
                    });

                }
                //centering large image
                largeimage.setposition();
            };
            this.setposition = function (e) {
                el.mousepos.x = e.pageX;
                el.mousepos.y = e.pageY;
                var lensleft = 0;
                var lenstop = 0;

                function overleft(lens) {
                    return el.mousepos.x - (lens.w) / 2 < smallimage.pos.l;
                }

                function overright(lens) {
                    return el.mousepos.x + (lens.w) / 2 > smallimage.pos.r;

                }

                function overtop(lens) {
                    return el.mousepos.y - (lens.h) / 2 < smallimage.pos.t;
                }

                function overbottom(lens) {
                    return el.mousepos.y + (lens.h) / 2 > smallimage.pos.b;
                }

                lensleft = el.mousepos.x + smallimage.bleft - smallimage.pos.l - (this.node.w + 2) / 2;
                lenstop = el.mousepos.y + smallimage.btop - smallimage.pos.t - (this.node.h + 2) / 2;
                if (overleft(this.node)) {
                    lensleft = smallimage.bleft - 1;
                } else if (overright(this.node)) {
                    lensleft = smallimage.w + smallimage.bleft - this.node.w - 1;
                }
                if (overtop(this.node)) {
                    lenstop = smallimage.btop - 1;
                } else if (overbottom(this.node)) {
                    lenstop = smallimage.h + smallimage.btop - this.node.h - 1;
                }

                this.node.left = lensleft;
                this.node.top = lenstop;
                this.node.css({
                    'left': lensleft + 'px',
                    'top': lenstop + 'px'
                });
                if (settings.zoomType == 'reverse') {
                    if ($.browser.msie && $.browser.version > 7) {
                        $(this.node).empty().append(this.image);
                    }

                    $(this.image).css({
                        position: 'absolute',
                        display: 'block',
                        left: -(this.node.left + 1 - smallimage.bleft) + 'px',
                        top: -(this.node.top + 1 - smallimage.btop) + 'px'
                    });
                }

                largeimage.setposition();
            };
            this.hide = function () {
                img.css({
                    'opacity': 1
                });
                this.node.hide();
            };
            this.show = function () {

                if (settings.zoomType != 'innerzoom' && (settings.lens || settings.zoomType == 'drag')) {
                    this.node.show();
                }

                if (settings.zoomType == 'reverse') {
                    img.css({
                        'opacity': settings.imageOpacity
                    });
                }
            };
            this.getoffset = function () {
                var o = {};
                o.left = $obj.node.left;
                o.top = $obj.node.top;
                return o;
            };
            return this;
        };
        /*========================================================,
         |   Stage
         |---------------------------------------------------------:
         |   Window area that contains the large image
         `========================================================*/

        function Stage() {
            var $obj = this;
            //this.node = $("<div class='zoomWindow'><div class='zoomWrapper'><div class='zoomWrapperTitle'></div><div class='zoomWrapperImage'></div></div></div>");
            this.node = $("<div class='zoomWindow'><div class='zoomWrapper'><div class='zoomWrapperImage'></div></div></div>");
            this.ieframe = $('<iframe class="zoomIframe" src="javascript:\'\';" marginwidth="0" marginheight="0" align="bottom" scrolling="no" frameborder="0" ></iframe>');
            this.setposition = function () {
                this.node.leftpos = 0;
                this.node.toppos = 0;
                if (settings.zoomType != 'innerzoom') {
                    //positioning
                    switch (settings.position) {
                        case "left":
                            this.node.leftpos = (smallimage.pos.l - smallimage.bleft - Math.abs(settings.xOffset) - settings.zoomWidth > 0) ? (0 - settings.zoomWidth - Math.abs(settings.xOffset)) : (smallimage.ow + Math.abs(settings.xOffset));
                            this.node.toppos = Math.abs(settings.yOffset);
                            break;
                        case "top":
                            this.node.leftpos = Math.abs(settings.xOffset);
                            this.node.toppos = (smallimage.pos.t - smallimage.btop - Math.abs(settings.yOffset) - settings.zoomHeight > 0) ? (0 - settings.zoomHeight - Math.abs(settings.yOffset)) : (smallimage.oh + Math.abs(settings.yOffset));
                            break;
                        case "bottom":
                            this.node.leftpos = Math.abs(settings.xOffset);
                            this.node.toppos = (smallimage.pos.t - smallimage.btop + smallimage.oh + Math.abs(settings.yOffset) + settings.zoomHeight < screen.height) ? (smallimage.oh + Math.abs(settings.yOffset)) : (0 - settings.zoomHeight - Math.abs(settings.yOffset));
                            break;
                        default:
                            this.node.leftpos = (smallimage.rightlimit + Math.abs(settings.xOffset) + settings.zoomWidth < screen.width) ? (smallimage.ow + Math.abs(settings.xOffset)) : (0 - settings.zoomWidth - Math.abs(settings.xOffset));
                            this.node.toppos = Math.abs(settings.yOffset);
                            break;
                    }
                }
                this.node.css({
                    'left': this.node.leftpos + 'px',
                    'top': this.node.toppos + 'px'
                });
                return this;
            };
            this.append = function () {
                $('.zoomPad', el).append(this.node);
                this.node.css({
                    position: 'absolute',
                    display: 'none',
                    zIndex: 5001
                });
                if (settings.zoomType == 'innerzoom') {
                    this.node.css({
                        cursor: 'default'
                    });
                    var thickness = (smallimage.bleft == 0) ? 1 : smallimage.bleft;
                    $('.zoomWrapper', this.node).css({
                        borderWidth: thickness + 'px'
                    });
                }

                $('.zoomWrapper', this.node).css({
                    width: Math.round(settings.zoomWidth) + 'px',
                    borderWidth: thickness + 'px'
                });
                $('.zoomWrapperImage', this.node).css({
                    width: '100%',
                    height: Math.round(settings.zoomHeight) + 'px'
                });
                //zoom title
                $('.zoomWrapperTitle', this.node).css({
                    width: '100%',
                    position: 'absolute'
                });

                $('.zoomWrapperTitle', this.node).hide();
                if (settings.title && zoomtitle.length > 0) {
                    $('.zoomWrapperTitle', this.node).html(zoomtitle).show();
                }
                $obj.setposition();
            };
            this.hide = function () {
                switch (settings.hideEffect) {
                    case 'fadeout':
                        this.node.fadeOut(settings.fadeoutSpeed, function () {
                        });
                        break;
                    default:
                        this.node.hide();
                        break;
                }
                this.ieframe.hide();
            };
            this.show = function () {
                switch (settings.showEffect) {
                    case 'fadein':
                        this.node.fadeIn();
                        this.node.fadeIn(settings.fadeinSpeed, function () {
                        });
                        break;
                    default:
                        this.node.show();
                        break;
                }
                if (isIE6 && settings.zoomType != 'innerzoom') {
                    this.ieframe.width = this.node.width();
                    this.ieframe.height = this.node.height();
                    this.ieframe.left = this.node.leftpos;
                    this.ieframe.top = this.node.toppos;
                    this.ieframe.css({
                        display: 'block',
                        position: "absolute",
                        left: this.ieframe.left,
                        top: this.ieframe.top,
                        zIndex: 99,
                        width: this.ieframe.width + 'px',
                        height: this.ieframe.height + 'px'
                    });
                    $('.zoomPad', el).append(this.ieframe);
                    this.ieframe.show();
                }
                ;
            };
        };
        /*========================================================,
         |   LargeImage
         |---------------------------------------------------------:
         |   The large detailed image
         `========================================================*/

        function Largeimage() {
            var $obj = this;
            this.node = new Image();
            this.loadimage = function (url) {
                //showing preload
                loader.show();
                this.url = url;
                this.node.style.position = 'absolute';
                this.node.style.border = '0px';
                this.node.style.display = 'none';
                this.node.style.left = '-5000px';
                this.node.style.top = '0px';
                document.body.appendChild(this.node);
                this.node.src = url; // fires off async
            };
            this.fetchdata = function () {
                var image = $(this.node);
                var scale = {};
                this.node.style.display = 'block';
                $obj.w = image.width();
                $obj.h = image.height();
                $obj.pos = image.offset();
                $obj.pos.l = image.offset().left;
                $obj.pos.t = image.offset().top;
                $obj.pos.r = $obj.w + $obj.pos.l;
                $obj.pos.b = $obj.h + $obj.pos.t;
                scale.x = ($obj.w / smallimage.w);
                scale.y = ($obj.h / smallimage.h);
                el.scale = scale;
                document.body.removeChild(this.node);
                $('.zoomWrapperImage', el).empty().append(this.node);
                //setting lens dimensions;
                lens.setdimensions();
            };
            this.node.onerror = function () {
                alert('Problems while loading the big image.');
                throw 'Problems while loading the big image.';
            };
            this.node.onload = function () {
                //fetching data
                $obj.fetchdata();
                loader.hide();
                el.largeimageloading = false;
                el.largeimageloaded = true;
                if (settings.zoomType == 'drag' || settings.alwaysOn) {
                    lens.show();
                    stage.show();
                    lens.setcenter();
                }
            };
            this.setposition = function () {
                var left = -el.scale.x * (lens.getoffset().left - smallimage.bleft + 1);
                var top = -el.scale.y * (lens.getoffset().top - smallimage.btop + 1);
                $(this.node).css({
                    'left': left + 'px',
                    'top': top + 'px'
                });
            };
            return this;
        };
        $(el).data("jqzoom", obj);

    };
    //es. $.jqzoom.disable('#jqzoom1');
    $.jqzoom = {
        defaults: {
            zoomType: 'standard',
            //innerzoom/standard/reverse/drag
            zoomWidth: 300,
            //zoomWindow  default width
            zoomHeight: 300,
            //zoomWindow  default height
            xOffset: 10,
            //zoomWindow x offset, can be negative(more on the left) or positive(more on the right)
            yOffset: 0,
            //zoomWindow y offset, can be negative(more on the left) or positive(more on the right)
            position: "right",
            //zoomWindow default position
            preloadImages: true,
            //image preload
            preloadText: 'Loading zoom',
            title: true,
            lens: true,
            imageOpacity: 0.4,
            alwaysOn: false,
            showEffect: 'show',
            //show/fadein
            hideEffect: 'hide',
            //hide/fadeout
            fadeinSpeed: 'slow',
            //fast/slow/number
            fadeoutSpeed: '2000',
            //fast/slow/number
            expandzoomfn: undefined
        },
        disable: function (el) {
            var api = $(el).data('jqzoom');
            api.disable();
            return false;
        },
        enable: function (el) {
            var api = $(el).data('jqzoom');
            api.enable();
            return false;
        },
        disableAll: function (el) {
            jqzoompluging_disabled = true;
        },
        enableAll: function (el) {
            jqzoompluging_disabled = false;
        }
    };
    $.jqcarousel = {
        defaults: {
            start: 1,
            vscroll: 3,
            btnLeft: 'left-arrow-btn',
            btnRight: 'right-arrow-btn',
            viewport: 'carousel-viewport',
            list: 'carousel-list',
            listItem: 'carousel-list-item',
            circular: true,
            visible: 3,
            scrollDuration: 500,
            disabledClass: 'disabled',
            activeItem: 0,
            expandTool: 'expand-tool'
        }
    };
})(jQuery);


function isIDevice() {
    return (
        (navigator.platform.indexOf("iPhone") != -1) ||
        (navigator.platform.indexOf("iPod") != -1) ||
        (navigator.platform.indexOf("iPad") != -1)
    );
}
