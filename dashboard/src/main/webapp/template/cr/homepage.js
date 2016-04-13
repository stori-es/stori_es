/*!
* jQuery Cycle2; version: 2.1.6 build: 20141007
* http://jquery.malsup.com/cycle2/
* Copyright (c) 2014 M. Alsup; Dual licensed: MIT/GPL
*/
!function(a){"use strict";function b(a){return(a||"").toLowerCase()}var c="2.1.6";a.fn.cycle=function(c){var d;return 0!==this.length||a.isReady?this.each(function(){var d,e,f,g,h=a(this),i=a.fn.cycle.log;if(!h.data("cycle.opts")){(h.data("cycle-log")===!1||c&&c.log===!1||e&&e.log===!1)&&(i=a.noop),i("--c2 init--"),d=h.data();for(var j in d)d.hasOwnProperty(j)&&/^cycle[A-Z]+/.test(j)&&(g=d[j],f=j.match(/^cycle(.*)/)[1].replace(/^[A-Z]/,b),i(f+":",g,"("+typeof g+")"),d[f]=g);e=a.extend({},a.fn.cycle.defaults,d,c||{}),e.timeoutId=0,e.paused=e.paused||!1,e.container=h,e._maxZ=e.maxZ,e.API=a.extend({_container:h},a.fn.cycle.API),e.API.log=i,e.API.trigger=function(a,b){return e.container.trigger(a,b),e.API},h.data("cycle.opts",e),h.data("cycle.API",e.API),e.API.trigger("cycle-bootstrap",[e,e.API]),e.API.addInitialSlides(),e.API.preInitSlideshow(),e.slides.length&&e.API.initSlideshow()}}):(d={s:this.selector,c:this.context},a.fn.cycle.log("requeuing slideshow (dom not ready)"),a(function(){a(d.s,d.c).cycle(c)}),this)},a.fn.cycle.API={opts:function(){return this._container.data("cycle.opts")},addInitialSlides:function(){var b=this.opts(),c=b.slides;b.slideCount=0,b.slides=a(),c=c.jquery?c:b.container.find(c),b.random&&c.sort(function(){return Math.random()-.5}),b.API.add(c)},preInitSlideshow:function(){var b=this.opts();b.API.trigger("cycle-pre-initialize",[b]);var c=a.fn.cycle.transitions[b.fx];c&&a.isFunction(c.preInit)&&c.preInit(b),b._preInitialized=!0},postInitSlideshow:function(){var b=this.opts();b.API.trigger("cycle-post-initialize",[b]);var c=a.fn.cycle.transitions[b.fx];c&&a.isFunction(c.postInit)&&c.postInit(b)},initSlideshow:function(){var b,c=this.opts(),d=c.container;c.API.calcFirstSlide(),"static"==c.container.css("position")&&c.container.css("position","relative"),a(c.slides[c.currSlide]).css({opacity:1,display:"block",visibility:"visible"}),c.API.stackSlides(c.slides[c.currSlide],c.slides[c.nextSlide],!c.reverse),c.pauseOnHover&&(c.pauseOnHover!==!0&&(d=a(c.pauseOnHover)),d.hover(function(){c.API.pause(!0)},function(){c.API.resume(!0)})),c.timeout&&(b=c.API.getSlideOpts(c.currSlide),c.API.queueTransition(b,b.timeout+c.delay)),c._initialized=!0,c.API.updateView(!0),c.API.trigger("cycle-initialized",[c]),c.API.postInitSlideshow()},pause:function(b){var c=this.opts(),d=c.API.getSlideOpts(),e=c.hoverPaused||c.paused;b?c.hoverPaused=!0:c.paused=!0,e||(c.container.addClass("cycle-paused"),c.API.trigger("cycle-paused",[c]).log("cycle-paused"),d.timeout&&(clearTimeout(c.timeoutId),c.timeoutId=0,c._remainingTimeout-=a.now()-c._lastQueue,(c._remainingTimeout<0||isNaN(c._remainingTimeout))&&(c._remainingTimeout=void 0)))},resume:function(a){var b=this.opts(),c=!b.hoverPaused&&!b.paused;a?b.hoverPaused=!1:b.paused=!1,c||(b.container.removeClass("cycle-paused"),0===b.slides.filter(":animated").length&&b.API.queueTransition(b.API.getSlideOpts(),b._remainingTimeout),b.API.trigger("cycle-resumed",[b,b._remainingTimeout]).log("cycle-resumed"))},add:function(b,c){var d,e=this.opts(),f=e.slideCount,g=!1;"string"==a.type(b)&&(b=a.trim(b)),a(b).each(function(){var b,d=a(this);c?e.container.prepend(d):e.container.append(d),e.slideCount++,b=e.API.buildSlideOpts(d),e.slides=c?a(d).add(e.slides):e.slides.add(d),e.API.initSlide(b,d,--e._maxZ),d.data("cycle.opts",b),e.API.trigger("cycle-slide-added",[e,b,d])}),e.API.updateView(!0),g=e._preInitialized&&2>f&&e.slideCount>=1,g&&(e._initialized?e.timeout&&(d=e.slides.length,e.nextSlide=e.reverse?d-1:1,e.timeoutId||e.API.queueTransition(e)):e.API.initSlideshow())},calcFirstSlide:function(){var a,b=this.opts();a=parseInt(b.startingSlide||0,10),(a>=b.slides.length||0>a)&&(a=0),b.currSlide=a,b.reverse?(b.nextSlide=a-1,b.nextSlide<0&&(b.nextSlide=b.slides.length-1)):(b.nextSlide=a+1,b.nextSlide==b.slides.length&&(b.nextSlide=0))},calcNextSlide:function(){var a,b=this.opts();b.reverse?(a=b.nextSlide-1<0,b.nextSlide=a?b.slideCount-1:b.nextSlide-1,b.currSlide=a?0:b.nextSlide+1):(a=b.nextSlide+1==b.slides.length,b.nextSlide=a?0:b.nextSlide+1,b.currSlide=a?b.slides.length-1:b.nextSlide-1)},calcTx:function(b,c){var d,e=b;return e._tempFx?d=a.fn.cycle.transitions[e._tempFx]:c&&e.manualFx&&(d=a.fn.cycle.transitions[e.manualFx]),d||(d=a.fn.cycle.transitions[e.fx]),e._tempFx=null,this.opts()._tempFx=null,d||(d=a.fn.cycle.transitions.fade,e.API.log('Transition "'+e.fx+'" not found.  Using fade.')),d},prepareTx:function(a,b){var c,d,e,f,g,h=this.opts();return h.slideCount<2?void(h.timeoutId=0):(!a||h.busy&&!h.manualTrump||(h.API.stopTransition(),h.busy=!1,clearTimeout(h.timeoutId),h.timeoutId=0),void(h.busy||(0!==h.timeoutId||a)&&(d=h.slides[h.currSlide],e=h.slides[h.nextSlide],f=h.API.getSlideOpts(h.nextSlide),g=h.API.calcTx(f,a),h._tx=g,a&&void 0!==f.manualSpeed&&(f.speed=f.manualSpeed),h.nextSlide!=h.currSlide&&(a||!h.paused&&!h.hoverPaused&&h.timeout)?(h.API.trigger("cycle-before",[f,d,e,b]),g.before&&g.before(f,d,e,b),c=function(){h.busy=!1,h.container.data("cycle.opts")&&(g.after&&g.after(f,d,e,b),h.API.trigger("cycle-after",[f,d,e,b]),h.API.queueTransition(f),h.API.updateView(!0))},h.busy=!0,g.transition?g.transition(f,d,e,b,c):h.API.doTransition(f,d,e,b,c),h.API.calcNextSlide(),h.API.updateView()):h.API.queueTransition(f))))},doTransition:function(b,c,d,e,f){var g=b,h=a(c),i=a(d),j=function(){i.animate(g.animIn||{opacity:1},g.speed,g.easeIn||g.easing,f)};i.css(g.cssBefore||{}),h.animate(g.animOut||{},g.speed,g.easeOut||g.easing,function(){h.css(g.cssAfter||{}),g.sync||j()}),g.sync&&j()},queueTransition:function(b,c){var d=this.opts(),e=void 0!==c?c:b.timeout;return 0===d.nextSlide&&0===--d.loop?(d.API.log("terminating; loop=0"),d.timeout=0,e?setTimeout(function(){d.API.trigger("cycle-finished",[d])},e):d.API.trigger("cycle-finished",[d]),void(d.nextSlide=d.currSlide)):void 0!==d.continueAuto&&(d.continueAuto===!1||a.isFunction(d.continueAuto)&&d.continueAuto()===!1)?(d.API.log("terminating automatic transitions"),d.timeout=0,void(d.timeoutId&&clearTimeout(d.timeoutId))):void(e&&(d._lastQueue=a.now(),void 0===c&&(d._remainingTimeout=b.timeout),d.paused||d.hoverPaused||(d.timeoutId=setTimeout(function(){d.API.prepareTx(!1,!d.reverse)},e))))},stopTransition:function(){var a=this.opts();a.slides.filter(":animated").length&&(a.slides.stop(!1,!0),a.API.trigger("cycle-transition-stopped",[a])),a._tx&&a._tx.stopTransition&&a._tx.stopTransition(a)},advanceSlide:function(a){var b=this.opts();return clearTimeout(b.timeoutId),b.timeoutId=0,b.nextSlide=b.currSlide+a,b.nextSlide<0?b.nextSlide=b.slides.length-1:b.nextSlide>=b.slides.length&&(b.nextSlide=0),b.API.prepareTx(!0,a>=0),!1},buildSlideOpts:function(c){var d,e,f=this.opts(),g=c.data()||{};for(var h in g)g.hasOwnProperty(h)&&/^cycle[A-Z]+/.test(h)&&(d=g[h],e=h.match(/^cycle(.*)/)[1].replace(/^[A-Z]/,b),f.API.log("["+(f.slideCount-1)+"]",e+":",d,"("+typeof d+")"),g[e]=d);g=a.extend({},a.fn.cycle.defaults,f,g),g.slideNum=f.slideCount;try{delete g.API,delete g.slideCount,delete g.currSlide,delete g.nextSlide,delete g.slides}catch(i){}return g},getSlideOpts:function(b){var c=this.opts();void 0===b&&(b=c.currSlide);var d=c.slides[b],e=a(d).data("cycle.opts");return a.extend({},c,e)},initSlide:function(b,c,d){var e=this.opts();c.css(b.slideCss||{}),d>0&&c.css("zIndex",d),isNaN(b.speed)&&(b.speed=a.fx.speeds[b.speed]||a.fx.speeds._default),b.sync||(b.speed=b.speed/2),c.addClass(e.slideClass)},updateView:function(a,b){var c=this.opts();if(c._initialized){var d=c.API.getSlideOpts(),e=c.slides[c.currSlide];!a&&b!==!0&&(c.API.trigger("cycle-update-view-before",[c,d,e]),c.updateView<0)||(c.slideActiveClass&&c.slides.removeClass(c.slideActiveClass).eq(c.currSlide).addClass(c.slideActiveClass),a&&c.hideNonActive&&c.slides.filter(":not(."+c.slideActiveClass+")").css("visibility","hidden"),0===c.updateView&&setTimeout(function(){c.API.trigger("cycle-update-view",[c,d,e,a])},d.speed/(c.sync?2:1)),0!==c.updateView&&c.API.trigger("cycle-update-view",[c,d,e,a]),a&&c.API.trigger("cycle-update-view-after",[c,d,e]))}},getComponent:function(b){var c=this.opts(),d=c[b];return"string"==typeof d?/^\s*[\>|\+|~]/.test(d)?c.container.find(d):a(d):d.jquery?d:a(d)},stackSlides:function(b,c,d){var e=this.opts();b||(b=e.slides[e.currSlide],c=e.slides[e.nextSlide],d=!e.reverse),a(b).css("zIndex",e.maxZ);var f,g=e.maxZ-2,h=e.slideCount;if(d){for(f=e.currSlide+1;h>f;f++)a(e.slides[f]).css("zIndex",g--);for(f=0;f<e.currSlide;f++)a(e.slides[f]).css("zIndex",g--)}else{for(f=e.currSlide-1;f>=0;f--)a(e.slides[f]).css("zIndex",g--);for(f=h-1;f>e.currSlide;f--)a(e.slides[f]).css("zIndex",g--)}a(c).css("zIndex",e.maxZ-1)},getSlideIndex:function(a){return this.opts().slides.index(a)}},a.fn.cycle.log=function(){window.console&&console.log&&console.log("[cycle2] "+Array.prototype.join.call(arguments," "))},a.fn.cycle.version=function(){return"Cycle2: "+c},a.fn.cycle.transitions={custom:{},none:{before:function(a,b,c,d){a.API.stackSlides(c,b,d),a.cssBefore={opacity:1,visibility:"visible",display:"block"}}},fade:{before:function(b,c,d,e){var f=b.API.getSlideOpts(b.nextSlide).slideCss||{};b.API.stackSlides(c,d,e),b.cssBefore=a.extend(f,{opacity:0,visibility:"visible",display:"block"}),b.animIn={opacity:1},b.animOut={opacity:0}}},fadeout:{before:function(b,c,d,e){var f=b.API.getSlideOpts(b.nextSlide).slideCss||{};b.API.stackSlides(c,d,e),b.cssBefore=a.extend(f,{opacity:1,visibility:"visible",display:"block"}),b.animOut={opacity:0}}},scrollHorz:{before:function(a,b,c,d){a.API.stackSlides(b,c,d);var e=a.container.css("overflow","hidden").width();a.cssBefore={left:d?e:-e,top:0,opacity:1,visibility:"visible",display:"block"},a.cssAfter={zIndex:a._maxZ-2,left:0},a.animIn={left:0},a.animOut={left:d?-e:e}}}},a.fn.cycle.defaults={allowWrap:!0,autoSelector:".cycle-slideshow[data-cycle-auto-init!=false]",delay:0,easing:null,fx:"fade",hideNonActive:!0,loop:0,manualFx:void 0,manualSpeed:void 0,manualTrump:!0,maxZ:100,pauseOnHover:!1,reverse:!1,slideActiveClass:"cycle-slide-active",slideClass:"cycle-slide",slideCss:{position:"absolute",top:0,left:0},slides:"> img",speed:500,startingSlide:0,sync:!0,timeout:4e3,updateView:0},a(document).ready(function(){a(a.fn.cycle.defaults.autoSelector).cycle()})}(jQuery),/*! Cycle2 autoheight plugin; Copyright (c) M.Alsup, 2012; version: 20130913 */
function(a){"use strict";function b(b,d){var e,f,g,h=d.autoHeight;if("container"==h)f=a(d.slides[d.currSlide]).outerHeight(),d.container.height(f);else if(d._autoHeightRatio)d.container.height(d.container.width()/d._autoHeightRatio);else if("calc"===h||"number"==a.type(h)&&h>=0){if(g="calc"===h?c(b,d):h>=d.slides.length?0:h,g==d._sentinelIndex)return;d._sentinelIndex=g,d._sentinel&&d._sentinel.remove(),e=a(d.slides[g].cloneNode(!0)),e.removeAttr("id name rel").find("[id],[name],[rel]").removeAttr("id name rel"),e.css({position:"static",visibility:"hidden",display:"block"}).prependTo(d.container).addClass("cycle-sentinel cycle-slide").removeClass("cycle-slide-active"),e.find("*").css("visibility","hidden"),d._sentinel=e}}function c(b,c){var d=0,e=-1;return c.slides.each(function(b){var c=a(this).height();c>e&&(e=c,d=b)}),d}function d(b,c,d,e){var f=a(e).outerHeight();c.container.animate({height:f},c.autoHeightSpeed,c.autoHeightEasing)}function e(c,f){f._autoHeightOnResize&&(a(window).off("resize orientationchange",f._autoHeightOnResize),f._autoHeightOnResize=null),f.container.off("cycle-slide-added cycle-slide-removed",b),f.container.off("cycle-destroyed",e),f.container.off("cycle-before",d),f._sentinel&&(f._sentinel.remove(),f._sentinel=null)}a.extend(a.fn.cycle.defaults,{autoHeight:0,autoHeightSpeed:250,autoHeightEasing:null}),a(document).on("cycle-initialized",function(c,f){function g(){b(c,f)}var h,i=f.autoHeight,j=a.type(i),k=null;("string"===j||"number"===j)&&(f.container.on("cycle-slide-added cycle-slide-removed",b),f.container.on("cycle-destroyed",e),"container"==i?f.container.on("cycle-before",d):"string"===j&&/\d+\:\d+/.test(i)&&(h=i.match(/(\d+)\:(\d+)/),h=h[1]/h[2],f._autoHeightRatio=h),"number"!==j&&(f._autoHeightOnResize=function(){clearTimeout(k),k=setTimeout(g,50)},a(window).on("resize orientationchange",f._autoHeightOnResize)),setTimeout(g,30))})}(jQuery),/*! caption plugin for Cycle2;  version: 20130306 */
function(a){"use strict";a.extend(a.fn.cycle.defaults,{caption:"> .cycle-caption",captionTemplate:"{{slideNum}} / {{slideCount}}",overlay:"> .cycle-overlay",overlayTemplate:"<div>{{title}}</div><div>{{desc}}</div>",captionModule:"caption"}),a(document).on("cycle-update-view",function(b,c,d,e){if("caption"===c.captionModule){a.each(["caption","overlay"],function(){var a=this,b=d[a+"Template"],f=c.API.getComponent(a);f.length&&b?(f.html(c.API.tmpl(b,d,c,e)),f.show()):f.hide()})}}),a(document).on("cycle-destroyed",function(b,c){var d;a.each(["caption","overlay"],function(){var a=this,b=c[a+"Template"];c[a]&&b&&(d=c.API.getComponent("caption"),d.empty())})})}(jQuery),/*! command plugin for Cycle2;  version: 20140415 */
function(a){"use strict";var b=a.fn.cycle;a.fn.cycle=function(c){var d,e,f,g=a.makeArray(arguments);return"number"==a.type(c)?this.cycle("goto",c):"string"==a.type(c)?this.each(function(){var h;return d=c,f=a(this).data("cycle.opts"),void 0===f?void b.log('slideshow must be initialized before sending commands; "'+d+'" ignored'):(d="goto"==d?"jump":d,e=f.API[d],a.isFunction(e)?(h=a.makeArray(g),h.shift(),e.apply(f.API,h)):void b.log("unknown command: ",d))}):b.apply(this,arguments)},a.extend(a.fn.cycle,b),a.extend(b.API,{next:function(){var a=this.opts();if(!a.busy||a.manualTrump){var b=a.reverse?-1:1;a.allowWrap===!1&&a.currSlide+b>=a.slideCount||(a.API.advanceSlide(b),a.API.trigger("cycle-next",[a]).log("cycle-next"))}},prev:function(){var a=this.opts();if(!a.busy||a.manualTrump){var b=a.reverse?1:-1;a.allowWrap===!1&&a.currSlide+b<0||(a.API.advanceSlide(b),a.API.trigger("cycle-prev",[a]).log("cycle-prev"))}},destroy:function(){this.stop();var b=this.opts(),c=a.isFunction(a._data)?a._data:a.noop;clearTimeout(b.timeoutId),b.timeoutId=0,b.API.stop(),b.API.trigger("cycle-destroyed",[b]).log("cycle-destroyed"),b.container.removeData(),c(b.container[0],"parsedAttrs",!1),b.retainStylesOnDestroy||(b.container.removeAttr("style"),b.slides.removeAttr("style"),b.slides.removeClass(b.slideActiveClass)),b.slides.each(function(){var d=a(this);d.removeData(),d.removeClass(b.slideClass),c(this,"parsedAttrs",!1)})},jump:function(a,b){var c,d=this.opts();if(!d.busy||d.manualTrump){var e=parseInt(a,10);if(isNaN(e)||0>e||e>=d.slides.length)return void d.API.log("goto: invalid slide index: "+e);if(e==d.currSlide)return void d.API.log("goto: skipping, already on slide",e);d.nextSlide=e,clearTimeout(d.timeoutId),d.timeoutId=0,d.API.log("goto: ",e," (zero-index)"),c=d.currSlide<d.nextSlide,d._tempFx=b,d.API.prepareTx(!0,c)}},stop:function(){var b=this.opts(),c=b.container;clearTimeout(b.timeoutId),b.timeoutId=0,b.API.stopTransition(),b.pauseOnHover&&(b.pauseOnHover!==!0&&(c=a(b.pauseOnHover)),c.off("mouseenter mouseleave")),b.API.trigger("cycle-stopped",[b]).log("cycle-stopped")},reinit:function(){var a=this.opts();a.API.destroy(),a.container.cycle()},remove:function(b){for(var c,d,e=this.opts(),f=[],g=1,h=0;h<e.slides.length;h++)c=e.slides[h],h==b?d=c:(f.push(c),a(c).data("cycle.opts").slideNum=g,g++);d&&(e.slides=a(f),e.slideCount--,a(d).remove(),b==e.currSlide?e.API.advanceSlide(1):b<e.currSlide?e.currSlide--:e.currSlide++,e.API.trigger("cycle-slide-removed",[e,b,d]).log("cycle-slide-removed"),e.API.updateView())}}),a(document).on("click.cycle","[data-cycle-cmd]",function(b){b.preventDefault();var c=a(this),d=c.data("cycle-cmd"),e=c.data("cycle-context")||".cycle-slideshow";a(e).cycle(d,c.data("cycle-arg"))})}(jQuery),/*! hash plugin for Cycle2;  version: 20130905 */
function(a){"use strict";function b(b,c){var d;return b._hashFence?void(b._hashFence=!1):(d=window.location.hash.substring(1),void b.slides.each(function(e){if(a(this).data("cycle-hash")==d){if(c===!0)b.startingSlide=e;else{var f=b.currSlide<e;b.nextSlide=e,b.API.prepareTx(!0,f)}return!1}}))}a(document).on("cycle-pre-initialize",function(c,d){b(d,!0),d._onHashChange=function(){b(d,!1)},a(window).on("hashchange",d._onHashChange)}),a(document).on("cycle-update-view",function(a,b,c){c.hash&&"#"+c.hash!=window.location.hash&&(b._hashFence=!0,window.location.hash=c.hash)}),a(document).on("cycle-destroyed",function(b,c){c._onHashChange&&a(window).off("hashchange",c._onHashChange)})}(jQuery),/*! loader plugin for Cycle2;  version: 20131121 */
function(a){"use strict";a.extend(a.fn.cycle.defaults,{loader:!1}),a(document).on("cycle-bootstrap",function(b,c){function d(b,d){function f(b){var f;"wait"==c.loader?(h.push(b),0===j&&(h.sort(g),e.apply(c.API,[h,d]),c.container.removeClass("cycle-loading"))):(f=a(c.slides[c.currSlide]),e.apply(c.API,[b,d]),f.show(),c.container.removeClass("cycle-loading"))}function g(a,b){return a.data("index")-b.data("index")}var h=[];if("string"==a.type(b))b=a.trim(b);else if("array"===a.type(b))for(var i=0;i<b.length;i++)b[i]=a(b[i])[0];b=a(b);var j=b.length;j&&(b.css("visibility","hidden").appendTo("body").each(function(b){function g(){0===--i&&(--j,f(k))}var i=0,k=a(this),l=k.is("img")?k:k.find("img");return k.data("index",b),l=l.filter(":not(.cycle-loader-ignore)").filter(':not([src=""])'),l.length?(i=l.length,void l.each(function(){this.complete?g():a(this).load(function(){g()}).on("error",function(){0===--i&&(c.API.log("slide skipped; img not loaded:",this.src),0===--j&&"wait"==c.loader&&e.apply(c.API,[h,d]))})})):(--j,void h.push(k))}),j&&c.container.addClass("cycle-loading"))}var e;c.loader&&(e=c.API.add,c.API.add=d)})}(jQuery),/*! pager plugin for Cycle2;  version: 20140415 */
function(a){"use strict";function b(b,c,d){var e,f=b.API.getComponent("pager");f.each(function(){var f=a(this);if(c.pagerTemplate){var g=b.API.tmpl(c.pagerTemplate,c,b,d[0]);e=a(g).appendTo(f)}else e=f.children().eq(b.slideCount-1);e.on(b.pagerEvent,function(a){b.pagerEventBubble||a.preventDefault(),b.API.page(f,a.currentTarget)})})}function c(a,b){var c=this.opts();if(!c.busy||c.manualTrump){var d=a.children().index(b),e=d,f=c.currSlide<e;c.currSlide!=e&&(c.nextSlide=e,c._tempFx=c.pagerFx,c.API.prepareTx(!0,f),c.API.trigger("cycle-pager-activated",[c,a,b]))}}a.extend(a.fn.cycle.defaults,{pager:"> .cycle-pager",pagerActiveClass:"cycle-pager-active",pagerEvent:"click.cycle",pagerEventBubble:void 0,pagerTemplate:"<span>&bull;</span>"}),a(document).on("cycle-bootstrap",function(a,c,d){d.buildPagerLink=b}),a(document).on("cycle-slide-added",function(a,b,d,e){b.pager&&(b.API.buildPagerLink(b,d,e),b.API.page=c)}),a(document).on("cycle-slide-removed",function(b,c,d){if(c.pager){var e=c.API.getComponent("pager");e.each(function(){var b=a(this);a(b.children()[d]).remove()})}}),a(document).on("cycle-update-view",function(b,c){var d;c.pager&&(d=c.API.getComponent("pager"),d.each(function(){a(this).children().removeClass(c.pagerActiveClass).eq(c.currSlide).addClass(c.pagerActiveClass)}))}),a(document).on("cycle-destroyed",function(a,b){var c=b.API.getComponent("pager");c&&(c.children().off(b.pagerEvent),b.pagerTemplate&&c.empty())})}(jQuery),/*! prevnext plugin for Cycle2;  version: 20140408 */
function(a){"use strict";a.extend(a.fn.cycle.defaults,{next:"> .cycle-next",nextEvent:"click.cycle",disabledClass:"disabled",prev:"> .cycle-prev",prevEvent:"click.cycle",swipe:!1}),a(document).on("cycle-initialized",function(a,b){if(b.API.getComponent("next").on(b.nextEvent,function(a){a.preventDefault(),b.API.next()}),b.API.getComponent("prev").on(b.prevEvent,function(a){a.preventDefault(),b.API.prev()}),b.swipe){var c=b.swipeVert?"swipeUp.cycle":"swipeLeft.cycle swipeleft.cycle",d=b.swipeVert?"swipeDown.cycle":"swipeRight.cycle swiperight.cycle";b.container.on(c,function(){b._tempFx=b.swipeFx,b.API.next()}),b.container.on(d,function(){b._tempFx=b.swipeFx,b.API.prev()})}}),a(document).on("cycle-update-view",function(a,b){if(!b.allowWrap){var c=b.disabledClass,d=b.API.getComponent("next"),e=b.API.getComponent("prev"),f=b._prevBoundry||0,g=void 0!==b._nextBoundry?b._nextBoundry:b.slideCount-1;b.currSlide==g?d.addClass(c).prop("disabled",!0):d.removeClass(c).prop("disabled",!1),b.currSlide===f?e.addClass(c).prop("disabled",!0):e.removeClass(c).prop("disabled",!1)}}),a(document).on("cycle-destroyed",function(a,b){b.API.getComponent("prev").off(b.nextEvent),b.API.getComponent("next").off(b.prevEvent),b.container.off("swipeleft.cycle swiperight.cycle swipeLeft.cycle swipeRight.cycle swipeUp.cycle swipeDown.cycle")})}(jQuery),/*! progressive loader plugin for Cycle2;  version: 20130315 */
function(a){"use strict";a.extend(a.fn.cycle.defaults,{progressive:!1}),a(document).on("cycle-pre-initialize",function(b,c){if(c.progressive){var d,e,f=c.API,g=f.next,h=f.prev,i=f.prepareTx,j=a.type(c.progressive);if("array"==j)d=c.progressive;else if(a.isFunction(c.progressive))d=c.progressive(c);else if("string"==j){if(e=a(c.progressive),d=a.trim(e.html()),!d)return;if(/^(\[)/.test(d))try{d=a.parseJSON(d)}catch(k){return void f.log("error parsing progressive slides",k)}else d=d.split(new RegExp(e.data("cycle-split")||"\n")),d[d.length-1]||d.pop()}i&&(f.prepareTx=function(a,b){var e,f;return a||0===d.length?void i.apply(c.API,[a,b]):void(b&&c.currSlide==c.slideCount-1?(f=d[0],d=d.slice(1),c.container.one("cycle-slide-added",function(a,b){setTimeout(function(){b.API.advanceSlide(1)},50)}),c.API.add(f)):b||0!==c.currSlide?i.apply(c.API,[a,b]):(e=d.length-1,f=d[e],d=d.slice(0,e),c.container.one("cycle-slide-added",function(a,b){setTimeout(function(){b.currSlide=1,b.API.advanceSlide(-1)},50)}),c.API.add(f,!0)))}),g&&(f.next=function(){var a=this.opts();if(d.length&&a.currSlide==a.slideCount-1){var b=d[0];d=d.slice(1),a.container.one("cycle-slide-added",function(a,b){g.apply(b.API),b.container.removeClass("cycle-loading")}),a.container.addClass("cycle-loading"),a.API.add(b)}else g.apply(a.API)}),h&&(f.prev=function(){var a=this.opts();if(d.length&&0===a.currSlide){var b=d.length-1,c=d[b];d=d.slice(0,b),a.container.one("cycle-slide-added",function(a,b){b.currSlide=1,b.API.advanceSlide(-1),b.container.removeClass("cycle-loading")}),a.container.addClass("cycle-loading"),a.API.add(c,!0)}else h.apply(a.API)})}})}(jQuery),/*! tmpl plugin for Cycle2;  version: 20121227 */
function(a){"use strict";a.extend(a.fn.cycle.defaults,{tmplRegex:"{{((.)?.*?)}}"}),a.extend(a.fn.cycle.API,{tmpl:function(b,c){var d=new RegExp(c.tmplRegex||a.fn.cycle.defaults.tmplRegex,"g"),e=a.makeArray(arguments);return e.shift(),b.replace(d,function(b,c){var d,f,g,h,i=c.split(".");for(d=0;d<e.length;d++)if(g=e[d]){if(i.length>1)for(h=g,f=0;f<i.length;f++)g=h,h=h[i[f]]||c;else h=g[c];if(a.isFunction(h))return h.apply(g,e);if(void 0!==h&&null!==h&&h!=c)return h}return c})}})}(jQuery);
/* Plugin for Cycle2; Copyright (c) 2012 M. Alsup; v20141007 */
!function(a){"use strict";a.event.special.swipe=a.event.special.swipe||{scrollSupressionThreshold:10,durationThreshold:1e3,horizontalDistanceThreshold:30,verticalDistanceThreshold:75,setup:function(){var b=a(this);b.bind("touchstart",function(c){function d(b){if(g){var c=b.originalEvent.touches?b.originalEvent.touches[0]:b;e={time:(new Date).getTime(),coords:[c.pageX,c.pageY]},Math.abs(g.coords[0]-e.coords[0])>a.event.special.swipe.scrollSupressionThreshold&&b.preventDefault()}}var e,f=c.originalEvent.touches?c.originalEvent.touches[0]:c,g={time:(new Date).getTime(),coords:[f.pageX,f.pageY],origin:a(c.target)};b.bind("touchmove",d).one("touchend",function(){b.unbind("touchmove",d),g&&e&&e.time-g.time<a.event.special.swipe.durationThreshold&&Math.abs(g.coords[0]-e.coords[0])>a.event.special.swipe.horizontalDistanceThreshold&&Math.abs(g.coords[1]-e.coords[1])<a.event.special.swipe.verticalDistanceThreshold&&g.origin.trigger("swipe").trigger(g.coords[0]>e.coords[0]?"swipeleft":"swiperight"),g=e=void 0})})}},a.event.special.swipeleft=a.event.special.swipeleft||{setup:function(){a(this).bind("swipe",a.noop)}},a.event.special.swiperight=a.event.special.swiperight||a.event.special.swipeleft}(jQuery);
/*jquery.mb.YTPlayer 30-05-2015
 _ jquery.mb.components 
 _ email: matteo@open-lab.com 
 _ Copyright (c) 2001-2015. Matteo Bicocchi (Pupunzi); 
 _ blog: http://pupunzi.open-lab.com 
 _ Open Lab s.r.l., Florence - Italy 
 */
function onYouTubeIframeAPIReady(){ytp.YTAPIReady||(ytp.YTAPIReady=!0,jQuery(document).trigger("YTAPIReady"))}var ytp=ytp||{};!function(jQuery,ytp){var getYTPVideoID=function(a){var b,c;return a.indexOf("youtu.be")>0?(b=a.substr(a.lastIndexOf("/")+1,a.length),c=b.indexOf("?list=")>0?b.substr(b.lastIndexOf("="),b.length):null,b=c?b.substr(0,b.lastIndexOf("?")):b):a.indexOf("http")>-1?(b=a.match(/[\\?&]v=([^&#]*)/)[1],c=a.indexOf("list=")>0?a.match(/[\\?&]list=([^&#]*)/)[1]:null):(b=a.length>15?null:a,c=b?null:a),{videoID:b,playlistID:c}};jQuery.mbYTPlayer={name:"jquery.mb.YTPlayer",version:"2.9.3",author:"Matteo Bicocchi",apiKey:"",defaults:{containment:"body",ratio:"auto",videoURL:null,playlistURL:null,startAt:0,stopAt:0,autoPlay:!0,vol:50,addRaster:!1,opacity:1,quality:"default",mute:!1,loop:!0,showControls:!0,showAnnotations:!1,showYTLogo:!0,stopMovieOnBlur:!0,realfullscreen:!0,gaTrack:!0,optimizeDisplay:!0,onReady:function(a){}},controls:{play:"P",pause:"p",mute:"M",unmute:"A",onlyYT:"O",showSite:"R",ytLogo:"Y"},locationProtocol:"https:",buildPlayer:function(options){return this.each(function(){var YTPlayer=this,$YTPlayer=jQuery(YTPlayer);YTPlayer.loop=0,YTPlayer.opt={},YTPlayer.state={},YTPlayer.filtersEnabled=!0,YTPlayer.filters={grayscale:{value:0,unit:"%"},hue_rotate:{value:0,unit:"deg"},invert:{value:0,unit:"%"},opacity:{value:0,unit:"%"},saturate:{value:0,unit:"%"},sepia:{value:0,unit:"%"},brightness:{value:0,unit:"%"},contrast:{value:0,unit:"%"},blur:{value:0,unit:"px"}},$YTPlayer.addClass("mb_YTPlayer");var property=$YTPlayer.data("property")&&"string"==typeof $YTPlayer.data("property")?eval("("+$YTPlayer.data("property")+")"):$YTPlayer.data("property");"undefined"!=typeof property&&"undefined"!=typeof property.vol&&(property.vol=0===property.vol?property.vol=1:property.vol),jQuery.extend(YTPlayer.opt,jQuery.mbYTPlayer.defaults,options,property),YTPlayer.hasChanged||(YTPlayer.defaultOpt={},jQuery.extend(YTPlayer.defaultOpt,jQuery.mbYTPlayer.defaults,options,property)),YTPlayer.isRetina=window.retina||window.devicePixelRatio>1;var isIframe=function(){var a=!1;try{self.location.href!=top.location.href&&(a=!0)}catch(b){a=!0}return a};YTPlayer.canGoFullScreen=!(jQuery.browser.msie||jQuery.browser.opera||isIframe()),YTPlayer.canGoFullScreen||(YTPlayer.opt.realfullscreen=!1),$YTPlayer.attr("id")||$YTPlayer.attr("id","video_"+(new Date).getTime());var playerID="mbYTP_"+YTPlayer.id;YTPlayer.isAlone=!1,YTPlayer.hasFocus=!0;var videoID=this.opt.videoURL?getYTPVideoID(this.opt.videoURL).videoID:$YTPlayer.attr("href")?getYTPVideoID($YTPlayer.attr("href")).videoID:!1,playlistID=this.opt.videoURL?getYTPVideoID(this.opt.videoURL).playlistID:$YTPlayer.attr("href")?getYTPVideoID($YTPlayer.attr("href")).playlistID:!1;YTPlayer.videoID=videoID,YTPlayer.playlistID=playlistID,YTPlayer.opt.showAnnotations=YTPlayer.opt.showAnnotations?"0":"3";var playerVars={autoplay:0,modestbranding:1,controls:0,showinfo:0,rel:0,enablejsapi:1,version:3,playerapiid:playerID,origin:"*",allowfullscreen:!0,wmode:"transparent",iv_load_policy:YTPlayer.opt.showAnnotations};document.createElement("video").canPlayType&&jQuery.extend(playerVars,{html5:1}),jQuery.browser.msie&&jQuery.browser.version<9&&(this.opt.opacity=1);var playerBox=jQuery("<div/>").attr("id",playerID).addClass("playerBox"),overlay=jQuery("<div/>").css({position:"absolute",top:0,left:0,width:"100%",height:"100%"}).addClass("YTPOverlay");if(YTPlayer.isSelf="self"==YTPlayer.opt.containment,YTPlayer.defaultOpt.containment=YTPlayer.opt.containment=jQuery("self"==YTPlayer.opt.containment?this:YTPlayer.opt.containment),YTPlayer.isBackground="body"==YTPlayer.opt.containment.get(0).tagName.toLowerCase(),!YTPlayer.isBackground||!ytp.backgroundIsInited){var isPlayer=YTPlayer.opt.containment.is(jQuery(this));if(YTPlayer.canPlayOnMobile=isPlayer&&0===jQuery(this).children().length,isPlayer?YTPlayer.isPlayer=!0:$YTPlayer.hide(),jQuery.browser.mobile&&!YTPlayer.canPlayOnMobile)return void $YTPlayer.remove();var wrapper=jQuery("<div/>").addClass("mbYTP_wrapper").attr("id","wrapper_"+playerID);if(wrapper.css({position:"absolute",zIndex:0,minWidth:"100%",minHeight:"100%",left:0,top:0,overflow:"hidden",opacity:0}),playerBox.css({position:"absolute",zIndex:0,width:"100%",height:"100%",top:0,left:0,overflow:"hidden"}),wrapper.append(playerBox),YTPlayer.opt.containment.children().not("script, style").each(function(){"static"==jQuery(this).css("position")&&jQuery(this).css("position","relative")}),YTPlayer.isBackground?(jQuery("body").css({boxSizing:"border-box"}),wrapper.css({position:"fixed",top:0,left:0,zIndex:0}),$YTPlayer.hide()):"static"==YTPlayer.opt.containment.css("position")&&YTPlayer.opt.containment.css({position:"relative"}),YTPlayer.opt.containment.prepend(wrapper),YTPlayer.wrapper=wrapper,playerBox.css({opacity:1}),jQuery.browser.mobile||(playerBox.after(overlay),YTPlayer.overlay=overlay),YTPlayer.isBackground||overlay.on("mouseenter",function(){YTPlayer.controlBar&&YTPlayer.controlBar.addClass("visible")}).on("mouseleave",function(){YTPlayer.controlBar&&YTPlayer.controlBar.removeClass("visible")}),ytp.YTAPIReady)setTimeout(function(){jQuery(document).trigger("YTAPIReady")},100);else{jQuery("#YTAPI").remove();var tag=jQuery("<script></script>").attr({src:jQuery.mbYTPlayer.locationProtocol+"//www.youtube.com/iframe_api?v="+jQuery.mbYTPlayer.version,id:"YTAPI"});jQuery("head").prepend(tag)}jQuery(document).on("YTAPIReady",function(){YTPlayer.isBackground&&ytp.backgroundIsInited||YTPlayer.isInit||(YTPlayer.isBackground&&(ytp.backgroundIsInited=!0),YTPlayer.opt.autoPlay="undefined"==typeof YTPlayer.opt.autoPlay?YTPlayer.isBackground?!0:!1:YTPlayer.opt.autoPlay,YTPlayer.opt.vol=YTPlayer.opt.vol?YTPlayer.opt.vol:100,jQuery.mbYTPlayer.getDataFromAPI(YTPlayer),jQuery(YTPlayer).on("YTPChanged",function(){if(!YTPlayer.isInit){if(YTPlayer.isInit=!0,jQuery.browser.mobile&&YTPlayer.canPlayOnMobile){if(YTPlayer.opt.containment.outerWidth()>jQuery(window).width()){YTPlayer.opt.containment.css({maxWidth:"100%"});var h=.6*YTPlayer.opt.containment.outerWidth();YTPlayer.opt.containment.css({maxHeight:h})}return void new YT.Player(playerID,{videoId:YTPlayer.videoID.toString(),height:"100%",width:"100%",events:{onReady:function(a){YTPlayer.player=a.target,playerBox.css({opacity:1}),YTPlayer.wrapper.css({opacity:1})}}})}new YT.Player(playerID,{videoId:YTPlayer.videoID.toString(),playerVars:playerVars,events:{onReady:function(a){if(YTPlayer.player=a.target,!YTPlayer.isReady){YTPlayer.isReady=YTPlayer.isPlayer&&!YTPlayer.opt.autoPlay?!1:!0,YTPlayer.playerEl=YTPlayer.player.getIframe(),$YTPlayer.optimizeDisplay(),YTPlayer.videoID=videoID,jQuery(window).on("resize.YTP",function(){$YTPlayer.optimizeDisplay()}),jQuery.mbYTPlayer.checkForState(YTPlayer);var b=jQuery.Event("YTPUnstarted");b.time=YTPlayer.player.time,YTPlayer.canTrigger&&jQuery(YTPlayer).trigger(b)}},onStateChange:function(event){if("function"==typeof event.target.getPlayerState){var state=event.target.getPlayerState();if(YTPlayer.state!=state){YTPlayer.state=state;var eventType;switch(state){case-1:eventType="YTPUnstarted";break;case 0:eventType="YTPEnd";break;case 1:eventType="YTPStart",YTPlayer.controlBar&&YTPlayer.controlBar.find(".mb_YTPPlaypause").html(jQuery.mbYTPlayer.controls.pause),"undefined"!=typeof _gaq&&eval(YTPlayer.opt.gaTrack)&&_gaq.push(["_trackEvent","YTPlayer","Play",YTPlayer.hasData?YTPlayer.videoData.title:YTPlayer.videoID.toString()]),"undefined"!=typeof ga&&eval(YTPlayer.opt.gaTrack)&&ga("send","event","YTPlayer","play",YTPlayer.hasData?YTPlayer.videoData.title:YTPlayer.videoID.toString());break;case 2:eventType="YTPPause",YTPlayer.controlBar&&YTPlayer.controlBar.find(".mb_YTPPlaypause").html(jQuery.mbYTPlayer.controls.play);break;case 3:YTPlayer.player.setPlaybackQuality(YTPlayer.opt.quality),eventType="YTPBuffering",YTPlayer.controlBar&&YTPlayer.controlBar.find(".mb_YTPPlaypause").html(jQuery.mbYTPlayer.controls.play);break;case 5:eventType="YTPCued"}var YTPEvent=jQuery.Event(eventType);YTPEvent.time=YTPlayer.player.time,YTPlayer.canTrigger&&jQuery(YTPlayer).trigger(YTPEvent)}}},onPlaybackQualityChange:function(a){var b=a.target.getPlaybackQuality(),c=jQuery.Event("YTPQualityChange");c.quality=b,jQuery(YTPlayer).trigger(c)},onError:function(a){150==a.data&&(console.log("Embedding this video is restricted by Youtube."),YTPlayer.isPlayList&&jQuery(YTPlayer).playNext()),2==a.data&&YTPlayer.isPlayList&&jQuery(YTPlayer).playNext(),"function"==typeof YTPlayer.opt.onError&&YTPlayer.opt.onError($YTPlayer,a)}}})}}))})}})},getDataFromAPI:function(a){if(a.videoData=jQuery.mbStorage.get("YYTPlayer_data_"+a.videoID),a.videoData)setTimeout(function(){a.opt.ratio="auto"==a.opt.ratio?"16/9":a.opt.ratio,a.dataReceived=!0,jQuery(a).trigger("YTPChanged");var b=jQuery.Event("YTPData");b.prop={};for(var c in a.videoData)b.prop[c]=a.videoData[c];jQuery(a).trigger(b)},500),a.hasData=!0;else if(jQuery.mbYTPlayer.apiKey)jQuery.getJSON("https://www.googleapis.com/youtube/v3/videos?id="+a.videoID+"&key="+jQuery.mbYTPlayer.apiKey+"&part=snippet",function(b){function c(b){a.videoData={},a.videoData.id=a.videoID,a.videoData.channelTitle=b.channelTitle,a.videoData.title=b.title,a.videoData.description=b.description.length<400?b.description:b.description.substring(0,400)+" ...",a.videoData.aspectratio="auto"==a.opt.ratio?"16/9":a.opt.ratio,a.opt.ratio=a.videoData.aspectratio,a.videoData.thumb_max=b.thumbnails.maxres?b.thumbnails.maxres.url:null,a.videoData.thumb_high=b.thumbnails.high?b.thumbnails.high.url:null,a.videoData.thumb_medium=b.thumbnails.medium?b.thumbnails.medium.url:null,jQuery.mbStorage.set("YYTPlayer_data_"+a.videoID,a.videoData)}a.dataReceived=!0,jQuery(a).trigger("YTPChanged"),c(b.items[0].snippet),a.hasData=!0;var d=jQuery.Event("YTPData");d.prop={};for(var e in a.videoData)d.prop[e]=a.videoData[e];jQuery(a).trigger(d)});else{if(setTimeout(function(){jQuery(a).trigger("YTPChanged")},50),a.isPlayer&&!a.opt.autoPlay){var b="https://i.ytimg.com/vi/"+a.videoID+"/hqdefault.jpg";a.opt.containment.css({background:"rgba(0,0,0,0.5) url("+b+") center center",backgroundSize:"cover"}),a.opt.backgroundUrl=b}a.videoData=null,a.opt.ratio="auto"==a.opt.ratio?"16/9":a.opt.ratio}jQuery(a).off("YTPData.YTPlayer").on("YTPData.YTPlayer",function(){if(!a.hasData&&(a.hasData=!0,a.isPlayer&&!a.opt.autoPlay)){var b=a.videoData.thumb_max||a.videoData.thumb_high||a.videoData.thumb_medium;a.opt.containment.css({background:"rgba(0,0,0,0.5) url("+b+") center center",backgroundSize:"cover"}),a.opt.backgroundUrl=b}}),a.isPlayer&&!a.opt.autoPlay&&(a.loading=jQuery("<div/>").addClass("loading").html("Loading").hide(),jQuery(a).append(a.loading),a.loading.fadeIn())},removeStoredData:function(){jQuery.mbStorage.remove()},getVideoData:function(){var a=this.get(0);return a.videoData},getVideoID:function(){var a=this.get(0);return a.videoID||!1},setVideoQuality:function(a){var b=this.get(0);jQuery.browser.chrome||b.player.setPlaybackQuality(a)},playlist:function(a,b,c){var d=this,e=d.get(0);return e.isPlayList=!0,b&&(a=jQuery.shuffle(a)),e.videoID||(e.videos=a,e.videoCounter=0,e.videoLength=a.length,jQuery(e).data("property",a[0]),jQuery(e).mb_YTPlayer()),"function"==typeof c&&jQuery(e).on("YTPChanged",function(){c(e)}),jQuery(e).on("YTPEnd",function(){jQuery(e).playNext()}),d},playNext:function(){var a=this.get(0);return a.videoCounter++,a.videoCounter>=a.videoLength&&(a.videoCounter=0),jQuery(a).changeMovie(a.videos[a.videoCounter]),this},playPrev:function(){var a=this.get(0);return a.videoCounter--,a.videoCounter<0&&(a.videoCounter=a.videoLength-1),jQuery(a).changeMovie(a.videos[a.videoCounter]),this},changeMovie:function(a){var b=this.get(0);b.opt.startAt=0,b.opt.stopAt=0,b.opt.mute=!0,b.hasData=!1,b.hasChanged=!0,a&&jQuery.extend(b.opt,b.defaultOpt,a),b.videoID=getYTPVideoID(b.opt.videoURL).videoID,jQuery(b.playerEl).CSSAnimate({opacity:0},200,function(){return jQuery(b).YTPGetPlayer().cueVideoByUrl(encodeURI(jQuery.mbYTPlayer.locationProtocol+"//www.youtube.com/v/"+b.videoID),1,b.opt.quality),jQuery.mbYTPlayer.checkForState(b),jQuery(b).optimizeDisplay(),jQuery.mbYTPlayer.getDataFromAPI(b),this})},getPlayer:function(){return jQuery(this).get(0).player},playerDestroy:function(){var a=this.get(0);ytp.YTAPIReady=!1,ytp.backgroundIsInited=!1,a.isInit=!1,a.videoID=null;var b=a.wrapper;return b.remove(),jQuery("#controlBar_"+a.id).remove(),clearInterval(a.checkForStartAt),clearInterval(a.getState),this},fullscreen:function(real){function hideMouse(){YTPlayer.overlay.css({cursor:"none"})}function RunPrefixMethod(a,b){for(var c,d,e=["webkit","moz","ms","o",""],f=0;f<e.length&&!a[c];){if(c=b,""==e[f]&&(c=c.substr(0,1).toLowerCase()+c.substr(1)),c=e[f]+c,d=typeof a[c],"undefined"!=d)return e=[e[f]],"function"==d?a[c]():a[c];f++}}function launchFullscreen(a){RunPrefixMethod(a,"RequestFullScreen")}function cancelFullscreen(){(RunPrefixMethod(document,"FullScreen")||RunPrefixMethod(document,"IsFullScreen"))&&RunPrefixMethod(document,"CancelFullScreen")}var YTPlayer=this.get(0);"undefined"==typeof real&&(real=YTPlayer.opt.realfullscreen),real=eval(real);var controls=jQuery("#controlBar_"+YTPlayer.id),fullScreenBtn=controls.find(".mb_OnlyYT"),videoWrapper=YTPlayer.isSelf?YTPlayer.opt.containment:YTPlayer.wrapper;if(real){var fullscreenchange=jQuery.browser.mozilla?"mozfullscreenchange":jQuery.browser.webkit?"webkitfullscreenchange":"fullscreenchange";jQuery(document).off(fullscreenchange).on(fullscreenchange,function(){var a=RunPrefixMethod(document,"IsFullScreen")||RunPrefixMethod(document,"FullScreen");a?(jQuery(YTPlayer).YTPSetVideoQuality("default"),jQuery(YTPlayer).trigger("YTPFullScreenStart")):(YTPlayer.isAlone=!1,fullScreenBtn.html(jQuery.mbYTPlayer.controls.onlyYT),jQuery(YTPlayer).YTPSetVideoQuality(YTPlayer.opt.quality),videoWrapper.removeClass("fullscreen"),videoWrapper.CSSAnimate({opacity:YTPlayer.opt.opacity},500),videoWrapper.css({zIndex:0}),YTPlayer.isBackground?jQuery("body").after(controls):YTPlayer.wrapper.before(controls),jQuery(window).resize(),jQuery(YTPlayer).trigger("YTPFullScreenEnd"))})}return YTPlayer.isAlone?(jQuery(document).off("mousemove.YTPlayer"),YTPlayer.overlay.css({cursor:"auto"}),real?cancelFullscreen():(videoWrapper.CSSAnimate({opacity:YTPlayer.opt.opacity},500),videoWrapper.css({zIndex:0})),fullScreenBtn.html(jQuery.mbYTPlayer.controls.onlyYT),YTPlayer.isAlone=!1):(jQuery(document).on("mousemove.YTPlayer",function(a){YTPlayer.overlay.css({cursor:"auto"}),clearTimeout(YTPlayer.hideCursor),jQuery(a.target).parents().is(".mb_YTPBar")||(YTPlayer.hideCursor=setTimeout(hideMouse,3e3))}),hideMouse(),real?(videoWrapper.css({opacity:0}),videoWrapper.addClass("fullscreen"),launchFullscreen(videoWrapper.get(0)),setTimeout(function(){videoWrapper.CSSAnimate({opacity:1},1e3),YTPlayer.wrapper.append(controls),jQuery(YTPlayer).optimizeDisplay(),YTPlayer.player.seekTo(YTPlayer.player.getCurrentTime()+.1,!0)},500)):videoWrapper.css({zIndex:1e4}).CSSAnimate({opacity:1},1e3),fullScreenBtn.html(jQuery.mbYTPlayer.controls.showSite),YTPlayer.isAlone=!0),this},toggleLoops:function(){var a=this.get(0),b=a.opt;return 1==b.loop?b.loop=0:(b.startAt?a.player.seekTo(b.startAt):a.player.playVideo(),b.loop=1),this},play:function(){var a=this.get(0);if(a.isReady){var b=jQuery("#controlBar_"+a.id),c=b.find(".mb_YTPPlaypause");return c.html(jQuery.mbYTPlayer.controls.pause),a.player.playVideo(),a.wrapper.CSSAnimate({opacity:a.isAlone?1:a.opt.opacity},2e3),jQuery(a.playerEl).CSSAnimate({opacity:1},1e3),jQuery(a).css("background-image","none"),this}},togglePlay:function(a){var b=this.get(0);return 1==b.state?this.YTPPause():this.YTPPlay(),"function"==typeof a&&a(b.state),this},stop:function(){var a=this.get(0),b=jQuery("#controlBar_"+a.id),c=b.find(".mb_YTPPlaypause");return c.html(jQuery.mbYTPlayer.controls.play),a.player.stopVideo(),this},pause:function(){var a=this.get(0),b=jQuery("#controlBar_"+a.id),c=b.find(".mb_YTPPlaypause");return c.html(jQuery.mbYTPlayer.controls.play),a.player.pauseVideo(),this},seekTo:function(a){var b=this.get(0);return b.player.seekTo(a,!0),this},setVolume:function(a){var b=this.get(0);return a||b.opt.vol||0!=b.player.getVolume()?!a&&b.player.getVolume()>0||a&&b.opt.vol==a?b.isMute?jQuery(b).YTPUnmute():jQuery(b).YTPMute():(b.opt.vol=a,b.player.setVolume(b.opt.vol),b.volumeBar&&b.volumeBar.length&&b.volumeBar.updateSliderVal(a)):jQuery(b).YTPUnmute(),this},mute:function(){var a=this.get(0);if(!a.isMute){a.player.mute(),a.isMute=!0,a.player.setVolume(0),a.volumeBar&&a.volumeBar.length&&a.volumeBar.width()>10&&a.volumeBar.updateSliderVal(0);var b=jQuery("#controlBar_"+a.id),c=b.find(".mb_YTPMuteUnmute");c.html(jQuery.mbYTPlayer.controls.unmute),jQuery(a).addClass("isMuted"),a.volumeBar&&a.volumeBar.length&&a.volumeBar.addClass("muted");var d=jQuery.Event("YTPMuted");return d.time=a.player.time,a.canTrigger&&jQuery(a).trigger(d),this}},unmute:function(){var a=this.get(0);if(a.isMute){a.player.unMute(),a.isMute=!1,a.player.setVolume(a.opt.vol),a.volumeBar&&a.volumeBar.length&&a.volumeBar.updateSliderVal(a.opt.vol>10?a.opt.vol:10);var b=jQuery("#controlBar_"+a.id),c=b.find(".mb_YTPMuteUnmute");c.html(jQuery.mbYTPlayer.controls.mute),jQuery(a).removeClass("isMuted"),a.volumeBar&&a.volumeBar.length&&a.volumeBar.removeClass("muted");var d=jQuery.Event("YTPUnmuted");return d.time=a.player.time,a.canTrigger&&jQuery(a).trigger(d),this}},applyFilter:function(a,b){var c=this.get(0);return c.filters[a].value=b,c.filtersEnabled&&this.YTPEnableFilters(),this},applyFilters:function(a){var b=this.get(0);return this.on("YTPReady",function(){for(var c in a)b.filters[c].value=a[c],jQuery(b).YTPApplyFilter(c,a[c]);jQuery(b).trigger("YTPFiltersApplied")}),this},toggleFilter:function(a,b){return this.each(function(){var c=this;c.filters[a].value?c.filters[a].value=0:c.filters[a].value=b,c.filtersEnabled&&jQuery(this).YTPEnableFilters()})},toggleFilters:function(a){return this.each(function(){var b=this;b.filtersEnabled?(jQuery(b).trigger("YTPDisableFilters"),jQuery(b).YTPDisableFilters()):(jQuery(b).YTPEnableFilters(),jQuery(b).trigger("YTPEnableFilters")),"function"==typeof a&&a(b.filtersEnabled)})},disableFilters:function(){return this.each(function(){var a=this,b=jQuery(a.playerEl);b.css("-webkit-filter",""),b.css("filter",""),a.filtersEnabled=!1})},enableFilters:function(){return this.each(function(){var a=this,b=jQuery(a.playerEl),c="";for(var d in a.filters)a.filters[d].value&&(c+=d.replace("_","-")+"("+a.filters[d].value+a.filters[d].unit+") ");b.css("-webkit-filter",c),b.css("filter",c),a.filtersEnabled=!0})},removeFilter:function(a,b){return this.each(function(){"function"==typeof a&&(b=a,a=null);var c=this;if(a)jQuery(this).YTPApplyFilter(a,0),"function"==typeof b&&b(a);else for(var d in c.filters)jQuery(this).YTPApplyFilter(d,0),"function"==typeof b&&b(d)})},manageProgress:function(){var a=this.get(0),b=jQuery("#controlBar_"+a.id),c=b.find(".mb_YTPProgress"),d=b.find(".mb_YTPLoaded"),e=b.find(".mb_YTPseekbar"),f=c.outerWidth(),g=Math.floor(a.player.getCurrentTime()),h=Math.floor(a.player.getDuration()),i=g*f/h,j=0,k=100*a.player.getVideoLoadedFraction();return d.css({left:j,width:k+"%"}),e.css({left:0,width:i}),{totalTime:h,currentTime:g}},buildControls:function(YTPlayer){var data=YTPlayer.opt;if(data.showYTLogo=data.showYTLogo||data.printUrl,!jQuery("#controlBar_"+YTPlayer.id).length){YTPlayer.controlBar=jQuery("<span/>").attr("id","controlBar_"+YTPlayer.id).addClass("mb_YTPBar").css({whiteSpace:"noWrap",position:YTPlayer.isBackground?"fixed":"absolute",zIndex:YTPlayer.isBackground?1e4:1e3}).hide();var buttonBar=jQuery("<div/>").addClass("buttonBar"),playpause=jQuery("<span>"+jQuery.mbYTPlayer.controls.play+"</span>").addClass("mb_YTPPlaypause ytpicon").click(function(){1==YTPlayer.player.getPlayerState()?jQuery(YTPlayer).YTPPause():jQuery(YTPlayer).YTPPlay()}),MuteUnmute=jQuery("<span>"+jQuery.mbYTPlayer.controls.mute+"</span>").addClass("mb_YTPMuteUnmute ytpicon").click(function(){0==YTPlayer.player.getVolume()?jQuery(YTPlayer).YTPUnmute():jQuery(YTPlayer).YTPMute()}),volumeBar=jQuery("<div/>").addClass("mb_YTPVolumeBar").css({display:"inline-block"});YTPlayer.volumeBar=volumeBar;var idx=jQuery("<span/>").addClass("mb_YTPTime"),vURL=data.videoURL?data.videoURL:"";vURL.indexOf("http")<0&&(vURL=jQuery.mbYTPlayer.locationProtocol+"//www.youtube.com/watch?v="+data.videoURL);var movieUrl=jQuery("<span/>").html(jQuery.mbYTPlayer.controls.ytLogo).addClass("mb_YTPUrl ytpicon").attr("title","view on YouTube").on("click",function(){window.open(vURL,"viewOnYT")}),onlyVideo=jQuery("<span/>").html(jQuery.mbYTPlayer.controls.onlyYT).addClass("mb_OnlyYT ytpicon").on("click",function(){jQuery(YTPlayer).YTPFullscreen(data.realfullscreen)}),progressBar=jQuery("<div/>").addClass("mb_YTPProgress").css("position","absolute").click(function(a){timeBar.css({width:a.clientX-timeBar.offset().left}),YTPlayer.timeW=a.clientX-timeBar.offset().left,YTPlayer.controlBar.find(".mb_YTPLoaded").css({width:0});var b=Math.floor(YTPlayer.player.getDuration());YTPlayer["goto"]=timeBar.outerWidth()*b/progressBar.outerWidth(),YTPlayer.player.seekTo(parseFloat(YTPlayer["goto"]),!0),YTPlayer.controlBar.find(".mb_YTPLoaded").css({width:0})}),loadedBar=jQuery("<div/>").addClass("mb_YTPLoaded").css("position","absolute"),timeBar=jQuery("<div/>").addClass("mb_YTPseekbar").css("position","absolute");progressBar.append(loadedBar).append(timeBar),buttonBar.append(playpause).append(MuteUnmute).append(volumeBar).append(idx),data.showYTLogo&&buttonBar.append(movieUrl),(YTPlayer.isBackground||eval(YTPlayer.opt.realfullscreen)&&!YTPlayer.isBackground)&&buttonBar.append(onlyVideo),YTPlayer.controlBar.append(buttonBar).append(progressBar),YTPlayer.isBackground?jQuery("body").after(YTPlayer.controlBar):(YTPlayer.controlBar.addClass("inlinePlayer"),YTPlayer.wrapper.before(YTPlayer.controlBar)),volumeBar.simpleSlider({initialval:YTPlayer.opt.vol,scale:100,orientation:"h",callback:function(a){0==a.value?jQuery(YTPlayer).YTPMute():jQuery(YTPlayer).YTPUnmute(),YTPlayer.player.setVolume(a.value),YTPlayer.isMute||(YTPlayer.opt.vol=a.value)}})}},checkForState:function(YTPlayer){var interval=YTPlayer.opt.showControls?100:1e3;return clearInterval(YTPlayer.getState),jQuery.contains(document,YTPlayer)?(jQuery.mbYTPlayer.checkForStart(YTPlayer),void(YTPlayer.getState=setInterval(function(){var prog=jQuery(YTPlayer).YTPManageProgress(),$YTPlayer=jQuery(YTPlayer),data=YTPlayer.opt,startAt=YTPlayer.opt.startAt?YTPlayer.opt.startAt:0,stopAt=YTPlayer.opt.stopAt>YTPlayer.opt.startAt?YTPlayer.opt.stopAt:0;if(stopAt=stopAt<YTPlayer.player.getDuration()?stopAt:0,YTPlayer.player.time!=prog.currentTime){var YTPEvent=jQuery.Event("YTPTime");YTPEvent.time=YTPlayer.player.time,jQuery(YTPlayer).trigger(YTPEvent)}if(YTPlayer.player.time=prog.currentTime,0==YTPlayer.player.getVolume()?$YTPlayer.addClass("isMuted"):$YTPlayer.removeClass("isMuted"),YTPlayer.opt.showControls&&(prog.totalTime?YTPlayer.controlBar.find(".mb_YTPTime").html(jQuery.mbYTPlayer.formatTime(prog.currentTime)+" / "+jQuery.mbYTPlayer.formatTime(prog.totalTime)):YTPlayer.controlBar.find(".mb_YTPTime").html("-- : -- / -- : --")),eval(YTPlayer.opt.stopMovieOnBlur)&&(document.hasFocus()?document.hasFocus()&&!YTPlayer.hasFocus&&-1!=YTPlayer.state&&0!=YTPlayer.state&&(YTPlayer.hasFocus=!0,$YTPlayer.YTPPlay()):1==YTPlayer.state&&(YTPlayer.hasFocus=!1,$YTPlayer.YTPPause())),YTPlayer.controlBar&&YTPlayer.controlBar.outerWidth()<=400&&!YTPlayer.isCompact?(YTPlayer.controlBar.addClass("compact"),YTPlayer.isCompact=!0,!YTPlayer.isMute&&YTPlayer.volumeBar&&YTPlayer.volumeBar.updateSliderVal(YTPlayer.opt.vol)):YTPlayer.controlBar&&YTPlayer.controlBar.outerWidth()>400&&YTPlayer.isCompact&&(YTPlayer.controlBar.removeClass("compact"),YTPlayer.isCompact=!1,!YTPlayer.isMute&&YTPlayer.volumeBar&&YTPlayer.volumeBar.updateSliderVal(YTPlayer.opt.vol)),1==YTPlayer.player.getPlayerState()&&(parseFloat(YTPlayer.player.getDuration()-3)<YTPlayer.player.getCurrentTime()||stopAt>0&&parseFloat(YTPlayer.player.getCurrentTime())>stopAt)){if(YTPlayer.isEnded)return;if(YTPlayer.isEnded=!0,setTimeout(function(){YTPlayer.isEnded=!1},2e3),YTPlayer.isPlayList){clearInterval(YTPlayer.getState);var YTPEnd=jQuery.Event("YTPEnd");return YTPEnd.time=YTPlayer.player.time,void jQuery(YTPlayer).trigger(YTPEnd)}data.loop?YTPlayer.player.seekTo(startAt,!0):(YTPlayer.player.pauseVideo(),YTPlayer.wrapper.CSSAnimate({opacity:0},1e3,function(){var a=jQuery.Event("YTPEnd");a.time=YTPlayer.player.time,jQuery(YTPlayer).trigger(a),YTPlayer.player.seekTo(startAt,!0),YTPlayer.isBackground||YTPlayer.opt.containment.css({background:"rgba(0,0,0,0.5) url("+YTPlayer.opt.backgroundUrl+") center center",backgroundSize:"cover"})}))}},interval))):(jQuery(YTPlayer).YTPPlayerDestroy(),clearInterval(YTPlayer.getState),void clearInterval(YTPlayer.checkForStartAt))},checkForStart:function(a){var b=jQuery(a);if(!jQuery.contains(document,a))return void jQuery(a).YTPPlayerDestroy();if(jQuery.browser.chrome&&(a.opt.quality="default"),a.player.pauseVideo(),jQuery(a).muteYTPVolume(),jQuery("#controlBar_"+a.id).remove(),a.opt.showControls&&jQuery.mbYTPlayer.buildControls(a),a.opt.addRaster){var c="dot"==a.opt.addRaster?"raster-dot":"raster";a.overlay.addClass(a.isRetina?c+" retina":c)}else a.overlay.removeClass(function(a,b){var c=b.split(" "),d=[];return jQuery.each(c,function(a,b){/raster.*/.test(b)&&d.push(b)}),d.push("retina"),d.join(" ")});a.checkForStartAt=setInterval(function(){jQuery(a).YTPMute();var c=a.opt.startAt?a.opt.startAt:1,d=a.player.getVideoLoadedFraction()>c/a.player.getDuration();if(a.player.getDuration()>0&&a.player.getCurrentTime()>=c&&d){clearInterval(a.checkForStartAt),a.isReady=!0,"function"==typeof a.opt.onReady&&a.opt.onReady(a);var e=jQuery.Event("YTPReady");jQuery(a).trigger(e),a.player.pauseVideo(),a.opt.mute||jQuery(a).YTPUnmute(),a.canTrigger=!0,a.opt.autoPlay?(b.YTPPlay(),b.css("background-image","none"),jQuery(a.playerEl).CSSAnimate({opacity:1},1e3),a.wrapper.CSSAnimate({opacity:a.isAlone?1:a.opt.opacity},1e3)):(a.player.pauseVideo(),a.isPlayer||(jQuery(a.playerEl).CSSAnimate({opacity:1},1e3),a.wrapper.CSSAnimate({opacity:a.isAlone?1:a.opt.opacity},1e3))),a.isPlayer&&!a.opt.autoPlay&&(a.loading.html("Ready"),setTimeout(function(){a.loading.fadeOut()},100)),a.controlBar&&a.controlBar.slideDown(1e3)}else c>=0&&a.player.seekTo(c,!0)},1e3)},formatTime:function(a){var b=Math.floor(a/60),c=Math.floor(a-60*b);return(9>=b?"0"+b:b)+" : "+(9>=c?"0"+c:c)}},jQuery.fn.toggleVolume=function(){var a=this.get(0);if(a)return a.player.isMuted()?(jQuery(a).YTPUnmute(),!0):(jQuery(a).YTPMute(),!1)},jQuery.fn.optimizeDisplay=function(){var a=this.get(0),b=a.opt,c=jQuery(a.playerEl),d={},e=a.wrapper;d.width=e.outerWidth(),d.height=e.outerHeight();var f=24,g=100,h={};b.optimizeDisplay?(h.width=d.width+d.width*f/100,h.height="16/9"==b.ratio?Math.ceil(9*d.width/16):Math.ceil(3*d.width/4),h.marginTop=-((h.height-d.height)/2),h.marginLeft=-(d.width*(f/2)/100),h.height<d.height&&(h.height=d.height+d.height*f/100,h.width="16/9"==b.ratio?Math.floor(16*d.height/9):Math.floor(4*d.height/3),h.marginTop=-(d.height*(f/2)/100),h.marginLeft=-((h.width-d.width)/2)),h.width+=g,h.height+=g,h.marginTop-=g/2,h.marginLeft-=g/2):(h.width="100%",h.height="100%",h.marginTop=0,h.marginLeft=0),c.css({width:h.width,height:h.height,marginTop:h.marginTop,marginLeft:h.marginLeft})},jQuery.shuffle=function(a){for(var b=a.slice(),c=b.length,d=c;d--;){var e=parseInt(Math.random()*c),f=b[d];b[d]=b[e],b[e]=f}return b},jQuery.fn.YTPlayer=jQuery.mbYTPlayer.buildPlayer,jQuery.fn.YTPlaylist=jQuery.mbYTPlayer.playlist,jQuery.fn.YTPPlayNext=jQuery.mbYTPlayer.playNext,jQuery.fn.YTPPlayPrev=jQuery.mbYTPlayer.playPrev,jQuery.fn.YTPChangeMovie=jQuery.mbYTPlayer.changeMovie,jQuery.fn.YTPGetVideoID=jQuery.mbYTPlayer.getVideoID,jQuery.fn.YTPGetPlayer=jQuery.mbYTPlayer.getPlayer,jQuery.fn.YTPPlayerDestroy=jQuery.mbYTPlayer.playerDestroy,jQuery.fn.YTPFullscreen=jQuery.mbYTPlayer.fullscreen,jQuery.fn.YTPPlay=jQuery.mbYTPlayer.play,jQuery.fn.YTPTogglePlay=jQuery.mbYTPlayer.togglePlay,jQuery.fn.YTPToggleLoops=jQuery.mbYTPlayer.toggleLoops,jQuery.fn.YTPStop=jQuery.mbYTPlayer.stop,jQuery.fn.YTPPause=jQuery.mbYTPlayer.pause,jQuery.fn.YTPSeekTo=jQuery.mbYTPlayer.seekTo,jQuery.fn.YTPMute=jQuery.mbYTPlayer.mute,jQuery.fn.YTPUnmute=jQuery.mbYTPlayer.unmute,jQuery.fn.YTPToggleVolume=jQuery.mbYTPlayer.toggleVolume,jQuery.fn.YTPSetVolume=jQuery.mbYTPlayer.setVolume,jQuery.fn.YTPSetVideoQuality=jQuery.mbYTPlayer.setVideoQuality,jQuery.fn.YTPManageProgress=jQuery.mbYTPlayer.manageProgress,jQuery.fn.YTPGetVideoData=jQuery.mbYTPlayer.getVideoData,jQuery.fn.YTPApplyFilter=jQuery.mbYTPlayer.applyFilter,jQuery.fn.YTPApplyFilters=jQuery.mbYTPlayer.applyFilters,jQuery.fn.YTPToggleFilter=jQuery.mbYTPlayer.toggleFilter,jQuery.fn.YTPToggleFilters=jQuery.mbYTPlayer.toggleFilters,jQuery.fn.YTPRemoveFilter=jQuery.mbYTPlayer.removeFilter,jQuery.fn.YTPDisableFilters=jQuery.mbYTPlayer.disableFilters,jQuery.fn.YTPEnableFilters=jQuery.mbYTPlayer.enableFilters,jQuery.fn.mb_YTPlayer=jQuery.mbYTPlayer.buildPlayer,jQuery.fn.playNext=jQuery.mbYTPlayer.playNext,jQuery.fn.playPrev=jQuery.mbYTPlayer.playPrev,jQuery.fn.changeMovie=jQuery.mbYTPlayer.changeMovie,jQuery.fn.getVideoID=jQuery.mbYTPlayer.getVideoID,jQuery.fn.getPlayer=jQuery.mbYTPlayer.getPlayer,jQuery.fn.playerDestroy=jQuery.mbYTPlayer.playerDestroy,jQuery.fn.fullscreen=jQuery.mbYTPlayer.fullscreen,jQuery.fn.buildYTPControls=jQuery.mbYTPlayer.buildControls,jQuery.fn.playYTP=jQuery.mbYTPlayer.play,jQuery.fn.toggleLoops=jQuery.mbYTPlayer.toggleLoops,jQuery.fn.stopYTP=jQuery.mbYTPlayer.stop,jQuery.fn.pauseYTP=jQuery.mbYTPlayer.pause,jQuery.fn.seekToYTP=jQuery.mbYTPlayer.seekTo,jQuery.fn.muteYTPVolume=jQuery.mbYTPlayer.mute,jQuery.fn.unmuteYTPVolume=jQuery.mbYTPlayer.unmute,jQuery.fn.setYTPVolume=jQuery.mbYTPlayer.setVolume,jQuery.fn.setVideoQuality=jQuery.mbYTPlayer.setVideoQuality,jQuery.fn.manageYTPProgress=jQuery.mbYTPlayer.manageProgress,jQuery.fn.YTPGetDataFromFeed=jQuery.mbYTPlayer.getVideoData}(jQuery,ytp),!function($){function uncamel(a){return a.replace(/([A-Z])/g,function(a){return"-"+a.toLowerCase()})}function setUnit(a,b){return"string"!=typeof a||a.match(/^[\-0-9\.]+$/)?""+a+b:a}function setFilter(a,b,c){var d=uncamel(b),e=jQuery.browser.mozilla?"":$.CSS.sfx;a[e+"filter"]=a[e+"filter"]||"",c=setUnit(c>$.CSS.filters[b].max?$.CSS.filters[b].max:c,$.CSS.filters[b].unit),a[e+"filter"]+=d+"("+c+") ",delete a[b]}eval(function(a,b,c,d,e,f){if(e=function(a){return a},!"".replace(/^/,String)){for(;c--;)f[c]=d[c]||c;d=[function(a){return f[a]}],e=function(){return"\\w+"},c=1}for(;c--;)d[c]&&(a=a.replace(new RegExp("\\b"+e(c)+"\\b","g"),d[c]));return a}('29 11=17.53;24(!2.9){2.9={};2.9.34=!1;2.9.22=!1;2.9.45=!1;2.9.42=!1;2.9.40=!1;2.9.28=!1;2.9.56=11;2.9.16=17.51;2.9.13=""+47(17.23);2.9.18=26(17.23,10);29 32,12,20;24(-1!=(12=11.15("33")))2.9.45=!0,2.9.16="33",2.9.13=11.14(12+6),-1!=(12=11.15("25"))&&(2.9.13=11.14(12+8));27 24(-1!=(12=11.15("58")))2.9.28=!0,2.9.16="36 38 39",2.9.13=11.14(12+5);27 24(-1!=11.15("57")){2.9.28=!0;2.9.16="36 38 39";29 30=11.15("59:")+3,43=30+4;2.9.13=11.14(30,43)}27-1!=(12=11.15("41"))?(2.9.22=!0,2.9.40=!0,2.9.16="41",2.9.13=11.14(12+7)):-1!=(12=11.15("31"))?(2.9.22=!0,2.9.42=!0,2.9.16="31",2.9.13=11.14(12+7),-1!=(12=11.15("25"))&&(2.9.13=11.14(12+8))):-1!=(12=11.15("68"))?(2.9.22=!0,2.9.16="31",2.9.13=11.14(12+7),-1!=(12=11.15("25"))&&(2.9.13=11.14(12+8))):-1!=(12=11.15("35"))?(2.9.34=!0,2.9.16="35",2.9.13=11.14(12+8)):(32=11.37(" ")+1)<(12=11.37("/"))&&(2.9.16=11.14(32,12),2.9.13=11.14(12+1),2.9.16.63()==2.9.16.64()&&(2.9.16=17.51));-1!=(20=2.9.13.15(";"))&&(2.9.13=2.9.13.14(0,20));-1!=(20=2.9.13.15(" "))&&(2.9.13=2.9.13.14(0,20));2.9.18=26(""+2.9.13,10);67(2.9.18)&&(2.9.13=""+47(17.23),2.9.18=26(17.23,10));2.9.69=2.9.18}2.9.46=/65/19.21(11);2.9.49=/66/19.21(11);2.9.48=/60|61|55/19.21(11);2.9.50=/33 52/19.21(11);2.9.44=/54/19.21(11);2.9.62=2.9.46||2.9.49||2.9.48||2.9.44||2.9.50;',10,70,"||jQuery|||||||browser||nAgt|verOffset|fullVersion|substring|indexOf|name|navigator|majorVersion|i|ix|test|webkit|appVersion|if|Version|parseInt|else|msie|var|start|Safari|nameOffset|Opera|mozilla|Firefox|Microsoft|lastIndexOf|Internet|Explorer|chrome|Chrome|safari|end|windowsMobile|opera|android|parseFloat|ios|blackberry|operaMobile|appName|Mini|userAgent|IEMobile|iPod|ua|Trident|MSIE|rv|iPhone|iPad|mobile|toLowerCase|toUpperCase|Android|BlackBerry|isNaN|AppleWebkit|version".split("|"),0,{})),
jQuery.support.CSStransition=function(){var a=document.body||document.documentElement,b=a.style;return void 0!==b.transition||void 0!==b.WebkitTransition||void 0!==b.MozTransition||void 0!==b.MsTransition||void 0!==b.OTransition}(),$.CSS={name:"mb.CSSAnimate",author:"Matteo Bicocchi",version:"2.0.0",transitionEnd:"transitionEnd",sfx:"",filters:{blur:{min:0,max:100,unit:"px"},brightness:{min:0,max:400,unit:"%"},contrast:{min:0,max:400,unit:"%"},grayscale:{min:0,max:100,unit:"%"},hueRotate:{min:0,max:360,unit:"deg"},invert:{min:0,max:100,unit:"%"},saturate:{min:0,max:400,unit:"%"},sepia:{min:0,max:100,unit:"%"}},normalizeCss:function(a){var b=jQuery.extend(!0,{},a);jQuery.browser.webkit||jQuery.browser.opera?$.CSS.sfx="-webkit-":jQuery.browser.mozilla?$.CSS.sfx="-moz-":jQuery.browser.msie&&($.CSS.sfx="-ms-");for(var c in b){"transform"===c&&(b[$.CSS.sfx+"transform"]=b[c],delete b[c]),"transform-origin"===c&&(b[$.CSS.sfx+"transform-origin"]=a[c],delete b[c]),"filter"!==c||jQuery.browser.mozilla||(b[$.CSS.sfx+"filter"]=a[c],delete b[c]),"blur"===c&&setFilter(b,"blur",a[c]),"brightness"===c&&setFilter(b,"brightness",a[c]),"contrast"===c&&setFilter(b,"contrast",a[c]),"grayscale"===c&&setFilter(b,"grayscale",a[c]),"hueRotate"===c&&setFilter(b,"hueRotate",a[c]),"invert"===c&&setFilter(b,"invert",a[c]),"saturate"===c&&setFilter(b,"saturate",a[c]),"sepia"===c&&setFilter(b,"sepia",a[c]);var d="";"x"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" translateX("+setUnit(a[c],"px")+")",delete b[c]),"y"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" translateY("+setUnit(a[c],"px")+")",delete b[c]),"z"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" translateZ("+setUnit(a[c],"px")+")",delete b[c]),"rotate"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" rotate("+setUnit(a[c],"deg")+")",delete b[c]),"rotateX"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" rotateX("+setUnit(a[c],"deg")+")",delete b[c]),"rotateY"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" rotateY("+setUnit(a[c],"deg")+")",delete b[c]),"rotateZ"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" rotateZ("+setUnit(a[c],"deg")+")",delete b[c]),"scale"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" scale("+setUnit(a[c],"")+")",delete b[c]),"scaleX"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" scaleX("+setUnit(a[c],"")+")",delete b[c]),"scaleY"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" scaleY("+setUnit(a[c],"")+")",delete b[c]),"scaleZ"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" scaleZ("+setUnit(a[c],"")+")",delete b[c]),"skew"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" skew("+setUnit(a[c],"deg")+")",delete b[c]),"skewX"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" skewX("+setUnit(a[c],"deg")+")",delete b[c]),"skewY"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" skewY("+setUnit(a[c],"deg")+")",delete b[c]),"perspective"===c&&(d=$.CSS.sfx+"transform",b[d]=b[d]||"",b[d]+=" perspective("+setUnit(a[c],"px")+")",delete b[c])}return b},getProp:function(a){var b=[];for(var c in a)b.indexOf(c)<0&&b.push(uncamel(c));return b.join(",")},animate:function(a,b,c,d,e){return this.each(function(){function f(){g.called=!0,g.CSSAIsRunning=!1,h.off($.CSS.transitionEnd+"."+g.id),clearTimeout(g.timeout),h.css($.CSS.sfx+"transition",""),"function"==typeof e&&e.apply(g),"function"==typeof g.CSSqueue&&(g.CSSqueue(),g.CSSqueue=null)}var g=this,h=jQuery(this);g.id=g.id||"CSSA_"+(new Date).getTime();var i=i||{type:"noEvent"};if(g.CSSAIsRunning&&g.eventType==i.type&&!jQuery.browser.msie&&jQuery.browser.version<=9)return void(g.CSSqueue=function(){h.CSSAnimate(a,b,c,d,e)});if(g.CSSqueue=null,g.eventType=i.type,0!==h.length&&a){if(a=$.normalizeCss(a),g.CSSAIsRunning=!0,"function"==typeof b&&(e=b,b=jQuery.fx.speeds._default),"function"==typeof c&&(d=c,c=0),"string"==typeof c&&(e=c,c=0),"function"==typeof d&&(e=d,d="cubic-bezier(0.65,0.03,0.36,0.72)"),"string"==typeof b)for(var j in jQuery.fx.speeds){if(b==j){b=jQuery.fx.speeds[j];break}b=jQuery.fx.speeds._default}if(b||(b=jQuery.fx.speeds._default),"string"==typeof e&&(d=e,e=null),!jQuery.support.CSStransition){for(var k in a){if("transform"===k&&delete a[k],"filter"===k&&delete a[k],"transform-origin"===k&&delete a[k],"auto"===a[k]&&delete a[k],"x"===k){var l=a[k],m="left";a[m]=l,delete a[k]}if("y"===k){var l=a[k],m="top";a[m]=l,delete a[k]}("-ms-transform"===k||"-ms-filter"===k)&&delete a[k]}return void h.delay(c).animate(a,b,e)}var n={"default":"ease","in":"ease-in",out:"ease-out","in-out":"ease-in-out",snap:"cubic-bezier(0,1,.5,1)",easeOutCubic:"cubic-bezier(.215,.61,.355,1)",easeInOutCubic:"cubic-bezier(.645,.045,.355,1)",easeInCirc:"cubic-bezier(.6,.04,.98,.335)",easeOutCirc:"cubic-bezier(.075,.82,.165,1)",easeInOutCirc:"cubic-bezier(.785,.135,.15,.86)",easeInExpo:"cubic-bezier(.95,.05,.795,.035)",easeOutExpo:"cubic-bezier(.19,1,.22,1)",easeInOutExpo:"cubic-bezier(1,0,0,1)",easeInQuad:"cubic-bezier(.55,.085,.68,.53)",easeOutQuad:"cubic-bezier(.25,.46,.45,.94)",easeInOutQuad:"cubic-bezier(.455,.03,.515,.955)",easeInQuart:"cubic-bezier(.895,.03,.685,.22)",easeOutQuart:"cubic-bezier(.165,.84,.44,1)",easeInOutQuart:"cubic-bezier(.77,0,.175,1)",easeInQuint:"cubic-bezier(.755,.05,.855,.06)",easeOutQuint:"cubic-bezier(.23,1,.32,1)",easeInOutQuint:"cubic-bezier(.86,0,.07,1)",easeInSine:"cubic-bezier(.47,0,.745,.715)",easeOutSine:"cubic-bezier(.39,.575,.565,1)",easeInOutSine:"cubic-bezier(.445,.05,.55,.95)",easeInBack:"cubic-bezier(.6,-.28,.735,.045)",easeOutBack:"cubic-bezier(.175, .885,.32,1.275)",easeInOutBack:"cubic-bezier(.68,-.55,.265,1.55)"};n[d]&&(d=n[d]),h.off($.CSS.transitionEnd+"."+g.id);var o=$.CSS.getProp(a),p={};$.extend(p,a),p[$.CSS.sfx+"transition-property"]=o,p[$.CSS.sfx+"transition-duration"]=b+"ms",p[$.CSS.sfx+"transition-delay"]=c+"ms",p[$.CSS.sfx+"transition-timing-function"]=d,setTimeout(function(){h.one($.CSS.transitionEnd+"."+g.id,f),h.css(p)},1),g.timeout=setTimeout(function(){return g.called||!e?(g.called=!1,void(g.CSSAIsRunning=!1)):(h.css($.CSS.sfx+"transition",""),e.apply(g),g.CSSAIsRunning=!1,void("function"==typeof g.CSSqueue&&(g.CSSqueue(),g.CSSqueue=null)))},b+c+10)}})}},$.fn.CSSAnimate=$.CSS.animate,$.normalizeCss=$.CSS.normalizeCss,$.fn.css3=function(a){return this.each(function(){var b=$(this),c=$.normalizeCss(a);b.css(c)})}}(jQuery);var nAgt=navigator.userAgent;if(!jQuery.browser){jQuery.browser={},jQuery.browser.mozilla=!1,jQuery.browser.webkit=!1,jQuery.browser.opera=!1,jQuery.browser.safari=!1,jQuery.browser.chrome=!1,jQuery.browser.msie=!1,jQuery.browser.ua=nAgt,jQuery.browser.name=navigator.appName,jQuery.browser.fullVersion=""+parseFloat(navigator.appVersion),jQuery.browser.majorVersion=parseInt(navigator.appVersion,10);var nameOffset,verOffset,ix;if(-1!=(verOffset=nAgt.indexOf("Opera")))jQuery.browser.opera=!0,jQuery.browser.name="Opera",jQuery.browser.fullVersion=nAgt.substring(verOffset+6),-1!=(verOffset=nAgt.indexOf("Version"))&&(jQuery.browser.fullVersion=nAgt.substring(verOffset+8));else if(-1!=(verOffset=nAgt.indexOf("OPR")))jQuery.browser.opera=!0,jQuery.browser.name="Opera",jQuery.browser.fullVersion=nAgt.substring(verOffset+4);else if(-1!=(verOffset=nAgt.indexOf("MSIE")))jQuery.browser.msie=!0,jQuery.browser.name="Microsoft Internet Explorer",jQuery.browser.fullVersion=nAgt.substring(verOffset+5);else if(-1!=nAgt.indexOf("Trident")){jQuery.browser.msie=!0,jQuery.browser.name="Microsoft Internet Explorer";var start=nAgt.indexOf("rv:")+3,end=start+4;jQuery.browser.fullVersion=nAgt.substring(start,end)}else-1!=(verOffset=nAgt.indexOf("Chrome"))?(jQuery.browser.webkit=!0,jQuery.browser.chrome=!0,jQuery.browser.name="Chrome",jQuery.browser.fullVersion=nAgt.substring(verOffset+7)):-1!=(verOffset=nAgt.indexOf("Safari"))?(jQuery.browser.webkit=!0,jQuery.browser.safari=!0,jQuery.browser.name="Safari",jQuery.browser.fullVersion=nAgt.substring(verOffset+7),-1!=(verOffset=nAgt.indexOf("Version"))&&(jQuery.browser.fullVersion=nAgt.substring(verOffset+8))):-1!=(verOffset=nAgt.indexOf("AppleWebkit"))?(jQuery.browser.webkit=!0,jQuery.browser.name="Safari",jQuery.browser.fullVersion=nAgt.substring(verOffset+7),-1!=(verOffset=nAgt.indexOf("Version"))&&(jQuery.browser.fullVersion=nAgt.substring(verOffset+8))):-1!=(verOffset=nAgt.indexOf("Firefox"))?(jQuery.browser.mozilla=!0,jQuery.browser.name="Firefox",jQuery.browser.fullVersion=nAgt.substring(verOffset+8)):(nameOffset=nAgt.lastIndexOf(" ")+1)<(verOffset=nAgt.lastIndexOf("/"))&&(jQuery.browser.name=nAgt.substring(nameOffset,verOffset),jQuery.browser.fullVersion=nAgt.substring(verOffset+1),jQuery.browser.name.toLowerCase()==jQuery.browser.name.toUpperCase()&&(jQuery.browser.name=navigator.appName));-1!=(ix=jQuery.browser.fullVersion.indexOf(";"))&&(jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix)),-1!=(ix=jQuery.browser.fullVersion.indexOf(" "))&&(jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix)),jQuery.browser.majorVersion=parseInt(""+jQuery.browser.fullVersion,10),isNaN(jQuery.browser.majorVersion)&&(jQuery.browser.fullVersion=""+parseFloat(navigator.appVersion),jQuery.browser.majorVersion=parseInt(navigator.appVersion,10)),jQuery.browser.version=jQuery.browser.majorVersion}jQuery.browser.android=/Android/i.test(nAgt),jQuery.browser.blackberry=/BlackBerry|BB|PlayBook/i.test(nAgt),jQuery.browser.ios=/iPhone|iPad|iPod|webOS/i.test(nAgt),jQuery.browser.operaMobile=/Opera Mini/i.test(nAgt),jQuery.browser.windowsMobile=/IEMobile|Windows Phone/i.test(nAgt),jQuery.browser.kindle=/Kindle|Silk/i.test(nAgt),jQuery.browser.mobile=jQuery.browser.android||jQuery.browser.blackberry||jQuery.browser.ios||jQuery.browser.windowsMobile||jQuery.browser.operaMobile||jQuery.browser.kindle,jQuery.isMobile=jQuery.browser.mobile,jQuery.isAndroidDefault=jQuery.browser.android&&!/chrome/i.test(nAgt),!function(a){/iphone|ipod|ipad|android|ie|blackberry|fennec/.test(navigator.userAgent.toLowerCase());var b="ontouchstart"in window||window.navigator&&window.navigator.msPointerEnabled&&window.MSGesture||window.DocumentTouch&&document instanceof DocumentTouch||!1;a.simpleSlider={defaults:{initialval:0,scale:100,orientation:"h",readonly:!1,callback:!1},events:{start:b?"touchstart":"mousedown",end:b?"touchend":"mouseup",move:b?"touchmove":"mousemove"},init:function(c){return this.each(function(){var d=this,e=a(d);e.addClass("simpleSlider"),d.opt={},a.extend(d.opt,a.simpleSlider.defaults,c),a.extend(d.opt,e.data());var f="h"==d.opt.orientation?"horizontal":"vertical",g=a("<div/>").addClass("level").addClass(f);e.prepend(g),d.level=g,e.css({cursor:"default"}),"auto"==d.opt.scale&&(d.opt.scale=a(d).outerWidth()),e.updateSliderVal(),d.opt.readonly||(e.on(a.simpleSlider.events.start,function(a){b&&(a=a.changedTouches[0]),d.canSlide=!0,e.updateSliderVal(a),e.css({cursor:"col-resize"}),a.preventDefault(),a.stopPropagation()}),a(document).on(a.simpleSlider.events.move,function(c){b&&(c=c.changedTouches[0]),d.canSlide&&(a(document).css({cursor:"default"}),e.updateSliderVal(c),c.preventDefault(),c.stopPropagation())}).on(a.simpleSlider.events.end,function(){a(document).css({cursor:"auto"}),d.canSlide=!1,e.css({cursor:"auto"})}))})},updateSliderVal:function(b){function c(a,b){return Math.floor(100*a/b)}var d=this,e=d.get(0);e.opt.initialval="number"==typeof e.opt.initialval?e.opt.initialval:e.opt.initialval(e);var f=a(e).outerWidth(),g=a(e).outerHeight();e.x="object"==typeof b?b.clientX+document.body.scrollLeft-d.offset().left:"number"==typeof b?b*f/e.opt.scale:e.opt.initialval*f/e.opt.scale,e.y="object"==typeof b?b.clientY+document.body.scrollTop-d.offset().top:"number"==typeof b?(e.opt.scale-e.opt.initialval-b)*g/e.opt.scale:e.opt.initialval*g/e.opt.scale,e.y=d.outerHeight()-e.y,e.scaleX=e.x*e.opt.scale/f,e.scaleY=e.y*e.opt.scale/g,e.outOfRangeX=e.scaleX>e.opt.scale?e.scaleX-e.opt.scale:e.scaleX<0?e.scaleX:0,e.outOfRangeY=e.scaleY>e.opt.scale?e.scaleY-e.opt.scale:e.scaleY<0?e.scaleY:0,e.outOfRange="h"==e.opt.orientation?e.outOfRangeX:e.outOfRangeY,e.value="undefined"!=typeof b?"h"==e.opt.orientation?e.x>=d.outerWidth()?e.opt.scale:e.x<=0?0:e.scaleX:e.y>=d.outerHeight()?e.opt.scale:e.y<=0?0:e.scaleY:"h"==e.opt.orientation?e.scaleX:e.scaleY,"h"==e.opt.orientation?e.level.width(c(e.x,f)+"%"):e.level.height(c(e.y,g)),"function"==typeof e.opt.callback&&e.opt.callback(e)}},a.fn.simpleSlider=a.simpleSlider.init,a.fn.updateSliderVal=a.simpleSlider.updateSliderVal}(jQuery),!function(a){a.mbCookie={set:function(a,b,c,d){b=JSON.stringify(b),c||(c=7),d=d?"; domain="+d:"";var e,f=new Date;f.setTime(f.getTime()+864e5*c),e="; expires="+f.toGMTString(),document.cookie=a+"="+b+e+"; path=/"+d},get:function(a){for(var b=a+"=",c=document.cookie.split(";"),d=0;d<c.length;d++){for(var e=c[d];" "==e.charAt(0);)e=e.substring(1,e.length);if(0==e.indexOf(b))return JSON.parse(e.substring(b.length,e.length))}return null},remove:function(b){a.mbCookie.set(b,"",-1)}},a.mbStorage={set:function(a,b){b=JSON.stringify(b),localStorage.setItem(a,b)},get:function(a){return localStorage[a]?JSON.parse(localStorage[a]):null},remove:function(a){a?localStorage.removeItem(a):localStorage.clear()}}}(jQuery);
/*
$(document).ready(function () {
    var $welcomeMsg, WEEK;

    $welcomeMsg = $("#welcomeMsg");
    WEEK = 7;

    if ($.cookie) {
        if ($.cookie("AlphaRDH2015") !== "Y") {
            $welcomeMsg.show();

            //Cookie name/value :: RDH2015=Y, expiration 4 weeks.
            $.cookie("AlphaRDH2015", "Y", {expires: WEEK * 4, path : '/'});

        }
    }

    $(this).on('click', '.closeWelcomeMesage', function () {
        $welcomeMsg.hide();
    });

});*/

var HOME = HOME || {};

if(typeof CQ !== 'undefined'){
    HOME.ToutLinkWidget = CQ.Ext.extend(CQ.form.CompositeField, {

        /**
         * @private
         * @type CQ.Ext.form.TextField
         */
        hiddenField: null,

        /**
         * @private
         * @type CQ.Ext.form.ComboBox
         */
        titleLinkField: null,

        /**
         * @private
         * @type CQ.Ext.form.TextField
         */
        urlField: null,

        /**
         * @private
         * @type String
         */
        valueDelimiter: "||",

        constructor: function(config) {
            config = config || { };
            var defaults = {
                "border": false,
                "layout": "table",
                "columns":2
            };
            config = CQ.Util.applyDefaults(config, defaults);
            HOME.ToutLinkWidget.superclass.constructor.call(this, config);
        },

        // overriding CQ.Ext.Component#initComponent
        initComponent: function() {
            HOME.ToutLinkWidget.superclass.initComponent.call(this);

            this.hiddenField = new CQ.Ext.form.Hidden({
                name: this.name
            });
            this.add(this.hiddenField);

            this.titleLinkField = new CQ.Ext.form.TextField({
                cls:"home-toutlinkwidget-title",
                width: 400,
                allowBlank: false,
                listeners: {
                    change: {
                        scope:this,
                        fn: this.updateHidden
                    }
                },
                optionsProvider: this.optionsProvider
            });
            this.add(this.titleLinkField);

            this.urlField = new CQ.Ext.form.TextField({
                cls: "home-toutlinkwidget-url",
                width: 400,
                allowBlank: false,
                listeners: {
                    change: {
                        scope:this,
                        fn:this.updateHidden
                    }
                }
            });
            this.add(this.urlField);
        },

        // overriding CQ.form.CompositeField#processPath
        processPath: function(path) {
            this.titleLinkField.processPath(path);
        },

        // overriding CQ.form.CompositeField#processRecord
        processRecord: function(record, path) {
            this.titleLinkField.processRecord(record, path);
        },

        // overriding CQ.form.CompositeField#setValue
        setValue: function(value) {
            var parts = value.split(this.valueDelimiter);
            this.titleLinkField.setValue(parts[0]);
            this.urlField.setValue(parts[1]);
            this.hiddenField.setValue(value);
        },

        // overriding CQ.form.CompositeField#getValue
        getValue: function() {
            return this.getRawValue();
        },

        // overriding CQ.form.CompositeField#getRawValue
        getRawValue: function() {
            if (!this.titleLinkField) {
                return null;
            }
            return this.titleLinkField.getValue() + this.valueDelimiter +
                   this.urlField.getValue();
        },

        // private
        updateHidden: function() {
            this.hiddenField.setValue(this.getValue());
        }

    });

    // register xtype
    CQ.Ext.reg("toutWidget", HOME.ToutLinkWidget);
}

(function($) {
    function hideSubscribeButton() {
        if(CRUserInfo && CRUserInfo.hasCRO()) {
            $('.subscr-callout').hide();

            if(window.innerWidth < 768) {
               var bottom = $('.productCards-cq-wrapper').height() + $('.subscribe-cq-wrapper').height();
               $('.hero-media').css('bottom', bottom);
            }
        }
    }

    if (CRUserInfo && typeof CRUserInfo.ready === 'function') {
        CRUserInfo.ready().then(hideSubscribeButton);

    } else {
        $(document).on('userInfo_ready', hideSubscribeButton);
    }

})(jQuery);



(function($){
$(document).ready(function() {
    var $subscribeRollover = $('.subscribe-rollover');
        $subscribeRollover.on('click', (function () {
	    document.location.href = $('.subscribe-rollover', this).data('url');
            trackPageLink('subscribe_step2_digital','statistics');
	}));
});
})(jQuery);

(function ($) {
    $(document).ready(function () {

        var $statistics, fromTop;

        $statistics = $('.statistics');

        if (!$statistics.length) {
            return;
        }

        $(".subscribe-rollover").click(function () {
            document.location.href = $(this).attr("data-url");
        });

        /*$("#sub-component").hover(function(){
         $(".subscribe-rollover").animate({right:0},"fast");
         },function(){
         $(".subscribe-rollover").animate({right:"-38%"},"fast");
         });*/


        fromTop = $statistics.offset().top - 300;

        $(window).scroll(function () {
            //Get the current scroll position
            var currentScroll = $(window).scrollTop();

            //If the current scroll is greater that the element position, active the animation.
            if (currentScroll >= fromTop) {
                $(".subscribe-rollover").animate({top: 0}, "slow");
            }
        });

        if (typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode())) {
            new Sortable('.statistics .container .row', '.statsItem-container');
        }
    });
})(jQuery);
function Sortable(inContainer, inItem) {

    var self = this;

    function init() {
        $(inContainer).sortable(
        { items:  inItem,
            start: function(event, ui) {
                el = ui.item;
                el.startPosition = $(this).children(inItem).index(el);
            }
        });

        $(inContainer).bind("sortstop", function(event, ui) {
            var element = $(this).context;
            el = ui.item;
            el.newPosition = $(this).children(inItem).index(el);

            var newPath = $(el).find('.sortable-path').attr('path');
            if (newPath && el.startPosition != el.newPosition && (el.startPosition != -1 || el.newPosition != -1)) {
                var update = "startPosition=" + el.startPosition + "&newPosition=" + el.newPosition;
                window.location = newPath + '.move.html?' + update;
            }
        });

    }

    init();
}
(function($){
$(document).ready(function() {
    $(document).on('click touchstart', function (e) {
        var container = $(".results-outer-box");
        if (!container.is(e.target) && container.has(e.target).length === 0) {
            container.css('display', 'none');
        }
    });
    var mobileBreakpoint = 768;
    var searchForm = $("#search-form");

    function adaptSearchFormToMobile (form, resolution) {
        var actionAttr = (resolution === 'mobile' ? '/cro/mobile/search/index.htm' : '/cro/search.htm'),
            nameAttr = (resolution === 'mobile' ? 'q' : 'query');

        if (form){
            form.attr('action', actionAttr);
            form.find('input').attr('name', nameAttr);
        }
    }

    if (window.innerWidth < mobileBreakpoint) {
        $('#search').attr('placeholder',$('#search').attr('placeholder')+' '+$('#cfa-label').text().toLowerCase());
        adaptSearchFormToMobile(searchForm, "mobile")
    }

    $(window).on('resize', function() {
        if (window.innerWidth < mobileBreakpoint) {
            adaptSearchFormToMobile(searchForm, "mobile");
            if($('#search').attr('placeholder') === 'Search') {
                $('#search').attr('placeholder',$('#search').attr('placeholder')+' '+$('#cfa-label').text().toLowerCase());
            }
        } else {
            $('#search').attr('placeholder', 'Search');
            adaptSearchFormToMobile(searchForm);
        }
    });
});
})(jQuery);

(function ($) {

    $(document).ready(function () {

        var $productItem = $('.productItem-container');
        var isMobile = (window.innerWidth < 768);

        $(window).on('resize', function() {
             isMobile = (window.innerWidth < 768);
        });


        /*
         * HR-383
         * */
        $productItem.each(function () {
            var $hover = $(this).find('span');
            $hover.css('bottom', -$hover.outerHeight());
        });

        $productItem.hover(function () {
            if(!isMobile) {
                var $hover = $(this).find('span');

                $(this).find('.hover-bg img').lazyload().trigger('appear');

                $hover.animate({bottom: 0}, 'fast');
                $(this).append('<div class="overlay"></div>');
            }

        }, function () {
            if(!isMobile) {
                var $hover = $(this).find('span');

                $hover.animate({bottom: -$hover.outerHeight()}, 'fast');
                $(this).find('.overlay').remove();
            }
        });


        $productItem.on('click', function () {
            var linkName = $('.productItem', this).data('url').replace('/cro/', '').replace('.htm', '');
            var slot = $(this).index() + 1;
            trackPageLink(linkName + '_image_' + slot, 'product_card');

            document.location.href = $('.productItem', this).data('url');

            return false;
        });

        $productItem.find('.poster-img').lazyload({effect: 'fadeIn'});

        if (typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode())) {
            new Sortable('.productCards', '.productItem-container');
        }
    });
})(jQuery);
!function ($, $d, $v) {
    $($d).ready(function () {
        $v.items.each(function () {
            $v.applyRollOver($(this));
        }).off("mouseenter mouseleave").hover(function () {
            if(!$v.isMobile()) {
                $v.show($(this), 0, function(){
                    this.find(".hover-bg img").lazyload().trigger("appear");
                });
                $(this).append('<div class="overlay"></div>');
            }
        }, function () {
            if(!$v.isMobile()) {
                $v.show($(this));
                $(this).find(".overlay").remove();
            }
        });
    });
}(jQuery, document, (function($, $w) {
    var $items = $(".productItem-container"),
    	_isMobile = function() {
    	    return (typeof $w.orientation !== "undefined" || "ontouchstart" in $w);
    	},
    	throttle = function(func, ctx) {
            clearTimeout(func.timerID);
            func.timerID = setTimeout(function(){
                func.apply(ctx);
            }, 500);
        },
    	rollOverCls = "no-rollover",
        resp = {
            items: $items,
            isMobile: _isMobile,
            applyRollOver: function(el) {
                var $hover = ((this.isMobile()) ? el.addClass(rollOverCls) : el.removeClass(rollOverCls)).find("span");
                $hover.css("bottom", -this.adjust($hover.outerHeight()));
            },
            adjust: function(v) {
                var m = Math;
                if (isNaN(v) || !(typeof v === "number"))
                    return 0;
                return m.max(v, m.pow(m.E, +m.log(v).toFixed(1)));
            },
            show: function(ctx, v, cb) {
                var $hover = ctx.find("span"),
                    _v = (typeof v === "number") ? v : -this.adjust($hover.outerHeight());
                if (typeof cb === "function")
                    cb.call(ctx);
                $hover.animate({bottom: _v}, "fast");
            }
        };
    return resp;
})(jQuery, window));
var CUNewsletter = {};

CUNewsletter.signup = function () {
    var theErrorCount = 0;
    var theEmail = document.getElementById('userEmail').value;
    if (theEmail == '') {
        alert('Please enter an e-mail address');
        theErrorCount++;
    }
    else if (theEmail.indexOf("\@") == -1 || theEmail.indexOf("\.") == -1) {
        alert('Please enter a valid e-mail address');
        theErrorCount++;
    }

    //submit form if no errors
    if (theErrorCount == 0) {
        var requestUrl = "http://consumerreports.org/bin/emailnewsletter?email=" + theEmail;
        jQuery.ajax({
            url: requestUrl,
            dataType: 'jsonp',
            contentType: "application/json; charset=utf-8",
            timeout: 1000,
            //async: true,
            cache: false,
            jsonpCallback: 'CUNewsletter.callback',
            error: function (x, t, m) {
            }
        });
    }
};

CUNewsletter.callback = function (obj) {
    var theSource, theRedirectUrl;

    var theEmail = document.getElementById('userEmail').value;
    var theHref = window.location.href;
    theSource = (theHref.indexOf("\/cars\/") > 0) ? "cars" : "PreferenceCenter";


    if (typeof obj.email === "undefined" || typeof obj.firstname === "undefined" || typeof obj.lastname === "undefined" || obj.email == "" || obj.firstname == "" || obj.lastname == "") {
        theRedirectUrl = 'http://web.consumerreports.org/newsletter/email-newsletters-step1.html?email=' + theEmail + '&source=' + theSource;
    } else {
        theRedirectUrl = 'http://web.consumerreports.org/newsletter/email-newsletters-step2.html?email=' + theEmail + '&source=' + theSource;
    }

    setTimeout(function () {
        window.location = theRedirectUrl;
    }, 100);

};
(function ($) {
    $(document).ready(function () {
        if (typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode())) {
            new Sortable('.newsList_second .container .row', '.newsItem-container');
        }
    });
})(jQuery);

(function($){
$(document).ready(function() {
    $('.newsList').each(function(){
        var newsList = $(this);
        var newsNumber = $('.newsNumber', newsList).val();
        var moreNewsNumber = $('.moreNewsNumber', newsList).val();

        $('.newsItem-container', newsList).slice(0, newsNumber).find(".newsItem").show();

        if($('.btn-more', newsList).length){
            $('.btn-more', newsList).click(function(){
                $('.newsItem-container', newsList).slice(newsNumber, newsNumber + moreNewsNumber).find(".newsItem").show();
                $(this).hide();
                $('.btn-all', newsList).removeClass('hidden');
            });
        } else {
            $('.btn-all', newsList).removeClass('hidden');
        }
    });

    var count = 0;

    /*$('.newsItemHeader').each(function() {
        if(count > 0){
            $(".newsItemHeader:eq("+ count +")").next().remove();
            $(".newsItemHeader:eq("+ count +")").remove();
        }

        count = count + 1;
    });*/


});
})(jQuery);
(function ($) {
    $(document).ready(function () {

        var $newsItem = $('.newsItem-container');
        $newsItem.on('click', (function (e) {
            document.location.href = $('.newsItem', this).data('url');
            var linkName = $('.news-item .title', this).text().replace(/ /g,'_');
            var slot = $(this).index() + 1;
            trackPageLink(linkName + '_image_' + slot, 'four_pane_feature');
        }));

        $(".newsItem .brightcoveVideo").click(function (event) {
            event.stopPropagation();
        });

        $(".newsItem .poster-img").lazyload({effect: "fadeIn"});

        if (typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode())) {
            new Sortable('.newsList .container .row', '.newsItem-container');
        }
    });
})(jQuery);

(function($){
$(document).ready(function() {
    $('.newsList').each(function(){
        var newsList = $(this);
        var newsNumber = $('.newsNumber', newsList).val();
        var moreNewsNumber = $('.moreNewsNumber', newsList).val();

        $('.newsItem-container', newsList).slice(0, newsNumber).find(".newsItem").show();

        if($('.btn-more', newsList).length){
            $('.btn-more', newsList).click(function(){
                $('.newsItem-container', newsList).slice(newsNumber, newsNumber + moreNewsNumber).find(".newsItem").show();
                $(this).hide();
                $('.btn-all', newsList).removeClass('hidden');
            });
        } else {
            $('.btn-all', newsList).removeClass('hidden');
        }
    });

    var count = 0;

    $('.newsItemHeader').each(function() {
        if(count > 0){
            $(".newsItemHeader:eq("+ count +")").next().remove();
            $(".newsItemHeader:eq("+ count +")").remove();
        }

        count = count + 1;
    });


});
})(jQuery);
(function ($) {
    $(document).ready(function () {
        if (typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode())) {
            new Sortable('.productNavItems', '.productNavItem-container');
        }
    });
})(jQuery);

$(document).ready(function(){
    $('.all-ratings-link').each(function(){
        var cfaItem = $(this).closest('li').find('.cfa-item');
        $(this).attr('href', cfaItem.attr('href'));
        $(this).text('All ' + cfaItem.text() + ' >');
        $(this).on('click', (function () {
            trackPageLink(cfaItem.text().toLowerCase().replace(/ /g,'_'),'subheader');
        }));
    });
});

$(document).ready(function () {
    var $window, $body, headerHeight, $stickyElement, $stickyElementPosition;

    $window = $(window);
    $body = $("body");
    $stickyElement = $('.top-secondary', 'header');

    var classCount = $(".productNavItem").find("a");

    $.each(classCount, function (i, val) {
        if ($(val).find("img").attr("src") == "") {
            $($(val)).find("img").css("max-height", "100px");
        }
    });

    var isEditMode = typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode());


    if (!$body.hasClass("health") && !$body.hasClass("cars")) {
        if(!isEditMode) {
            $("ul.categories li").hover(function () {
                $(this).children(":eq(0)").addClass("subActive");
                $(this).find(".topProdDD").show();
            }, function () {
                $(this).children(":eq(0)").removeClass("subActive");
                $(this).find(".topProdDD").hide();
            });
        } else {
            $("ul.categories li").click(function(e){
                if($(e.target).hasClass("cfa-item")){
                    $(".topProdDD").hide();
                    $(this).find(".topProdDD").show();
                    return false;
                }
            });
        }
    }

    $(".flyout-close-btn").click(function () {
        $(".topProdDD").hide();
    });

    $("ul.social li:eq(3)").click(function () {
        $(this).css("display", "none");
        $(".socialViewMore").css("display", "inline-block");
    });


    $stickyElementPosition = $stickyElement.position();
    if ($stickyElementPosition) {

        headerHeight = $stickyElementPosition.top;

        function stickyNavigation() {

            if ($window.scrollTop() >= headerHeight) {
                $stickyElement.addClass('sticky');
            } else {
                $stickyElement.removeClass('sticky');
            }

        }
    
        $(".sticky-search .searchIcon").toggle(function(){
            $(this).find('img').attr('src','/etc/designs/cr/images/common/close-btn-white.png');
            $(this).css({'border':'none','border-radius':0});
            $('.search-area').slideDown( "fast" );
            $('#stickySearch').focus();
        }, function(){
            $(this).find('img').attr('src','/etc/designs/cr/images/common/search_icon.png');
            $(this).css({'border':'2px solid #fff','border-radius':'50%'});
            $('.search-area').slideUp( "fast" );
        })

        $window.on('scroll', stickyNavigation);

        stickyNavigation();
    }
});
$('.main-articles__content').on('swipeleft', function(){
        var container = $('.main-articles__content').width();
        var content = $('.main-articles__content-wrap').outerWidth();
        if (content > container) {
            $('.main-articles__content-wrap').animate({left: container - content - 20}, 300);
        }
    });

    $('.main-articles__content').on('swiperight', function(){
        $('.main-articles__content-wrap').animate({left: 0}, 300)
    });

    $('body').on('swipeleft swiperight', function(e){
        e.preventDefault();
    });
(function($){
$(document).ready(function() {
    $("div.letterPresident").lazyload({effect:"fadeIn"});
});
})(jQuery);
(function($){
$(document).ready(function() {
    $(".cr-image-lazy").lazyload({effect:"fadeIn"});
});
})(jQuery);
function configHeroSection(config) {
    var $container, selector, _preset, $player, $playControl;


    function isMobile() {
        try {
            document.createEvent("TouchEvent");
            return true;
        }
        catch (e) {
            return false;
        }
    }


    if (config.id && config.videoURL && !isMobile()) {
        selector = '#' + config.id;

        /**
         *  @configuration:  https://github.com/pupunzi/jquery.mb.YTPlayer/wiki
         */

        _preset = {
            videoURL: config.videoURL,
            containment: selector + ' .videoWrapper',
            startAt: 0,
            quality: 'large',
            mute: true,
            autoPlay: config.autoPlay === 'true',
            showControls: 0,
            loop: config.loop === 'true',
            // opacity: 1,
            ratio: 'auto',
            showYTLogo: false,

            locationProtocol: '//'
        };

        $container = jQuery(selector);

        $player = $container.find('.player');


        $player.YTPlayer(_preset);

        $playControl = $container.find('.videoControl');

        function setPlay() {
            $container.parents('.section').addClass('paused');
            $playControl.addClass('play').show().removeClass('pause').html('Play');
        }

        function setPause() {
            $container.parents('.section').removeClass('paused');
            $playControl.addClass('pause').show().removeClass('play').html('Pause');
        }

        $player.on('YTPReady YTPPause YTPEnd', setPlay);
        $player.on('YTPStart', setPause);


        $playControl.on('click', function () {
            $player.YTPTogglePlay();
        });


    }
}

(function ($) {
    $(document).ready(function() {
        if(window.innerWidth < 768) {
           var bottom = $('.productCards-cq-wrapper').height() + $('.subscribe-cq-wrapper').height();
           $('.hero-media').css('bottom', bottom);
        }
    });

    $(window).on('resize', function() {
        if(window.innerWidth < 768) {
            var bottom = $('.productCards-cq-wrapper').height() + $('.subscribe-cq-wrapper').height();
            $('.hero-media').css('bottom', bottom);
        } else {
            $('.hero-media').css('bottom', 'auto');
        }
    });
})(jQuery);


(function ($) {
    var isEditMode = typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode());
    if (isEditMode) {
        CQ.WCM.on('editablesready', function() {
            $(".featuredContainer").addClass("cycle-slideshow").cycle();
        });
    }

    $(function () {
        var $body = $('body');
        var galleries = $('.featured-news');

        if($body.attr('class')) {
            $('.featured-news').css('margin', '0 0 70px');
        }

        for (var i = 0; i < galleries.length; i++) {
            (function(i) {
                $(galleries[i]).on('cycle-update-view', function() {
                    pagingTopCalc.apply(galleries[i]);
                });
            })(i);
        }

        $(window).on('resize', function() {
            for (var i = 0; i < galleries.length; i++) {
                (function(i) {
                    pagingTopCalc.apply(galleries[i]);
                })(i);
            }
        });

        function pagingTopCalc() {
            if(window.innerWidth < 1200 ) {
                var pager = $(this).find('.pager.mobile');
                var blobsNumber = pager.find('span').length;

                if(blobsNumber > 1) {
                    var top = $(this).find('.cycle-slide-active .featureImg img').height();
                    pager.css('top', top + 'px');
                } else {
                    pager.hide();
                }

                frameHeightCalc.apply(this);
            } else {
                $(this).css('height', 'auto');
            }
        }

        function frameHeightCalc() {
            var height = $(this).find('.cycle-slide-active').height();
            $(this).css('height', height);
        }
    });
})(jQuery);
(function ($) {
    $(function () {

        var isEditMode = typeof CQ != 'undefined' && CQ.WCM && (CQ.WCM.isEditMode() || CQ.WCM.isDesignMode());

        if (isEditMode) {
            $(".action-button-cq-wrapper").show();
            $(".convio-subscribe-cq-wrapper").show();

        } else {


            function renderJoinMovementForm() {

                if (CRUserInfo.hasData() && CRUserInfo.getName()) {
                    $(".convio-subscribe-cq-wrapper").remove();
                    $(".action-button-cq-wrapper").show();

                } else {


                    var $inputEmail = $('#cons-email');
                    var $emailWrap = $('.cons_email');
                    var $emailError = $('.email-error-message');
                    var $inputZipCode = $('#cons_zip_code');
                    var $zipCodeWrap = $('.cons_zip_code');
                    var $zipCodeError = $('.zip-code-error-message');
                    var $formConvio = $('form', '.convioSubscribe');
                    var invalidClass = 'invalid';


                    function isValidEmail(emailVal) {
                        // @todo: Move to some global object
                        var EMAIL_RE = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

                        return EMAIL_RE.test(emailVal);
                    }

                    function isValidZipCode(zipCodeVal) {
                        var ZIP_CODE_RE = /^[a-z0-9- ]{1,10}$/i;
                        return (zipCodeVal.search(/  | -|- |--/) == -1) && zipCodeVal.search(/-| /) != 0 && ZIP_CODE_RE.test(zipCodeVal);
                    }

                    function isEmpty(value) {
                        return !value || (value.length == 0);
                    }

                    function validateEmail() {
                        if (isEmpty($inputEmail.val())) {
                            $emailError.text("E-mail is required");
                            $emailWrap.addClass(invalidClass);
                        } else if (!isValidEmail($inputEmail.val())) {
                            $emailError.text("Invalid email format");
                            $emailWrap.addClass(invalidClass);
                        } else {
                            $emailWrap.removeClass(invalidClass);
                            return true;
                        }
                        return false;
                    }

                    function validateZipCode() {
                        if (isEmpty($inputZipCode.val())) {
                            $zipCodeError.text("Zip code is required");
                            $zipCodeWrap.addClass(invalidClass);
                        } else if (!isValidZipCode($inputZipCode.val())) {
                            $zipCodeError.text("Invalid zip code");
                            $zipCodeWrap.addClass(invalidClass);
                        } else {
                            $zipCodeWrap.removeClass(invalidClass);
                            return true;
                        }
                        return false;
                    }

                    function validateForm() {
                        var validEmail = validateEmail();
                        var validZipCode = validateZipCode();
                        return validEmail && validZipCode;
                    }

                    $formConvio.on('submit', validateForm);

                    var emailTimer;
                    $inputEmail.on('keyup change', function () {
                        $emailWrap.removeClass(invalidClass);
                        clearTimeout(emailTimer);
                        emailTimer = setTimeout(validateEmail, 500);
                    });

                    $inputZipCode.on('keyup change', validateZipCode);


                    $(".action-button-cq-wrapper").remove();
                    $(".convio-subscribe-cq-wrapper").show();
                }
            }


            if (CRUserInfo && typeof CRUserInfo.ready === 'function') {
                CRUserInfo.ready().then(renderJoinMovementForm);

            } else {
                $(document).on('userInfo_ready', renderJoinMovementForm);
            }

        }

    });
})(jQuery);
/** @namespace window.videojs */
(function ($) {

    // It's good idea to include lodash or underscore for that utils
    /**
     * Debounce a function when you want it to execute only once after a defined interval of time.
     * If the event occurs multiple times within the interval, the interval is reset each time.
     * @param fn
     * @param delay
     * @returns {Function}
     */
    function debounce(fn, delay) {
        var timer = null;
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = setTimeout(function () {
                fn.apply(context, args);
            }, delay);
        };
    }


    $(document).ready(function () {

        var $videos = $(".brightcoveVideo .vjs-cr"),
            resumeCls = "resume-action";

        if ($videos.size()) {

            if (typeof window.videojs !== 'function') {
                console.warn('Brightcove Video Error: videojs function is not defined');
                return;
            }

            var clickEvent = 'click';

            if ('ontouchstart' in document) {
                clickEvent = 'tap';
            }

            function videoJsReady() {
                var userInactivate;
                var player = this;
                var bigPlayButton = player.bigPlayButton;
                var Holder = function(){
                	var proto = Array.prototype;
                	return {
                		length: 0,
                		set: function(v) {
							if (v)
								proto.push.apply(this, [v]);
                		},
                		isHoldOn: function(type) {
                			return !!this.length;
                		}
                	}
                }();

                player.enableTouchActivity();

                function handle(e) {
					var target = e.currentTarget || e.target;
					if (target) {
					    target = $().add(target);
						var p = target.parent();
						if (p && p.hasClass("vjs-paused")) {
						    target.removeClass(resumeCls);
							player.play();
						} else {
						    target.addClass(resumeCls);
							player.pause();
						}

					}
                    return false;
                }

                player.on('pause', function () {
                    bigPlayButton.addClass("visible");
                    bigPlayButton.addClass(resumeCls);
                    player.removeClass("top-layer");
                });

                userInactivate = debounce(function () {
                    player.userActive(false);
                }, 2000);

                player.on('play', function () {
                    bigPlayButton.removeClass("visible");
                    bigPlayButton.removeClass(resumeCls);
                    player.addClass("top-layer");


                    // VideoJS on-click behaves not as expected so kept both approaches (jQuery/VideoJS) for now.
                    if (clickEvent === 'click') {
                        if (!Holder.isHoldOn())
                            Holder.set($(".brightcoveVideo .vjs-cr.vjs-has-started.vjs-user-active:not(.vjs-fullscreen) .vjs-big-play-button").on(clickEvent, handle));
                    } else {
						if (!Holder.isHoldOn())
							Holder.set(bigPlayButton.on(clickEvent, handle) | this.on(clickEvent, handle));
                    }
					userInactivate();
                });
            }


            $videos.each(function (i, video) {
                videojs(video).ready(videoJsReady);
            });
        }
    });
})(jQuery);
var oas_tag = oas_tag || {};
var DxTag = function (options) {
    oas_tag.url = options.url || 'oascentral.consumerreports.org'; //Define OAS URL
    oas_tag.site_page = options.site_page;
    oas_tag.query = options.query;
    oas_tag.taxonomy = options.taxonomy;
    oas_tag.analytics = options.analytics || true;
    oas_tag.version = '1';
    oas_tag.implMode = options.implMode;
    oas_tag.loadAd = oas_tag.loadAd || function () {};
    var tags = getTags(jQuery(options.tagSelector));
    var theAdCookie = readCookie("userAds");
    if (!theAdCookie || !CRUserInfo.getUser()) {
        theAdCookie = "";
    }
    else {
        if ((theAdCookie.length >= 1) && (theAdCookie.substr(theAdCookie.length - 1) != '&')) {
            theAdCookie = theAdCookie + "&";
        }
    }

    if(!oas_tag.taxonomy) {
        oas_tag.taxonomy = "status=active" + OAS_rdl + "&if_nt_CookieAccept=" + OAS_CA + "&XE";
    }

    if(!oas_tag.query) {
        oas_tag.query = encodeURIComponent(window.location.href) + "&" + theAdCookie + "article=112345";
    }

    function renderTags() {
        if (tags) {
            var theDivList = $.each(tags, function (index, dxTag) {
                oas_tag.loadAd(dxTag.position);
            });
        }
    }

    function getTags(elements) {
        return $.map(elements, function (dxTag, index) {
            var positionId = dxTag.id;
            var width = $(dxTag).attr("width");
            var height = $(dxTag).attr("height");
            dxTag.id = "oas_" + positionId;
            return {position: positionId, width: width, height: height}
        });
    }

    oas_tag.sizes = function () { //size is required [width, height]
        $.each(tags, function (index, tag) {
            oas_tag.definePos(tag.position, [tag.width, tag.height]);
        });
    };

    //load dxtag library and render the data
    this.init = function () {
        var oas = document.createElement('script'),
            protocol = 'https:' == document.location.protocol ? 'https://' : 'http://',
            node = document.getElementsByTagName('script')[0];
        oas.type = 'text/javascript';
        oas.async = true;
        oas.src = protocol + oas_tag.url + '/om/' + oas_tag.version + '.js';
        var onLoad = function () {
            renderTags();
            if (options.onLoad) {
                options.onLoad(oas_tag);
            }
        };
        oas.addEventListener('load', onLoad, false);
        node.parentNode.insertBefore(oas, node);
    };

};

;jQuery(function($) {
    var mobileBreakpoint = 768;
    if (window.innerWidth < mobileBreakpoint) {
        $('.featured-news').addClass('mobile');
    }
});
/*
    Examples of use:
    1) as background
    <div data-src="<768: urlFor768, <1200: urlFor1200, >1200: originalImageUrl"></div>
    2) as image
    <img data-src = "<768: urlFor768, <1200: urlFor1200, >1200: originalImageUrl"/>
*/

(function() {
    function makeBackgroundResponsive() {

        var viewport = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;

        //get all elements with data-original attribute
        var withDataSrc = document.querySelectorAll('[data-src]');

        //work with all elements
        for( var i = 0; i < withDataSrc.length; i++ ) {

            var elementWithBG = withDataSrc[i],
                sourceAttr = 'data-src',
                sourcesQuery = elementWithBG.getAttribute(sourceAttr),
                tag = elementWithBG.tagName,
                sourcesArray;

            if( !sourcesQuery ) {
                continue;
            }

            sourcesArray = sourcesQuery.split(',');
            for( var j = 0; j < sourcesArray.length; j++ ) {
                var regExpr = /\d+:/,
                    inputText = sourcesArray[j],
                    resolution = parseInt(regExpr.exec(inputText)[0], 10),
                    source = inputText.split(regExpr)[1].trim(),
                    orderSign = inputText.split(regExpr)[0].trim();

                if( source.indexOf('/content/dam') !== -1 || source === '' ) {
                    if( (orderSign === '<' && viewport <= resolution) || (orderSign === '>' && viewport > resolution) ) {
                        if( tag === 'IMG') {
                            var lazy = elementWithBG.classList.contains("cr-image-lazy");
                            elementWithBG.setAttribute('data-original', source);
                            if(lazy){
                                if($.fn.lazyload ){
                                    $(elementWithBG).lazyload({effect:"fadeIn"});
                                }
                            }else{
                                elementWithBG.setAttribute('src', source);
                            }
                        }  else {
                            elementWithBG.style.backgroundImage = 'url("' + source + '")';
                        }
                        break;
                    }
                } else {
                    source = sourcesArray[sourcesArray.length - 1].split(regExpr)[1].trim();
                    elementWithBG.setAttribute('src', source);
                    break;
                }
            }
        }
    }
    window.addEventListener('load', makeBackgroundResponsive, false);
})();
