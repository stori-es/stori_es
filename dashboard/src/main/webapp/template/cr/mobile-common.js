(function(){
    var testedLabel = " tested products";
    var testedLabelBefore = "";
    var pageLink = "";
    var parameterKey = "categoryId";

    var setLabel = function(label){
        testedLabel = label;
    }
    var setLabelBefore = function(label){
        testedLabelBefore = label;
    }

    var setPageLink = function(link){
        pageLink = link;
    }

    var setLinkParameterKey = function(key){
        parameterKey = key;
    }

    /**
    * @param category.id
    * @param category.name
    * @param category.count - number of items in category
    */
    window.createCategoryElement = function(category){
        if(pageLink==""){
            pageLink=CUMobileCategoryList.CAR_PAGE_LINK;
        }
        var catTitle = jQuery("<h2>",{text: category.name }).addClass('category-title');
        var paramObject = {subtype : (category.isCar ? 'cars' : 'products') , type : (category.isCar ? 'cartype' : 'category') , id : category.id}
        link = jQuery("<a>",{
            href : ParameterManager.createUrlWithOutParameters(pageLink, paramObject),
            name : '&lid='+category.name
        });

        var categoryItemSectionWrapper = jQuery("<div>").addClass("menuItem no-margin large-font clickable");
        var leftSideWrapper = 	jQuery("<div>").addClass("position-left");

        var categoryNumberOfTestedModelsParagraph = jQuery("<span>").text(testedLabelBefore + category.count + testedLabel).addClass("text-gray");
        var nextScreenImage = jQuery("<img>").attr({src:"/etc/designs/cro/mweb/images/btn_arrow_nextscreen.png"}).addClass("position-right element-of-control");

        categoryItemSectionWrapper.append(
            link.append(
                leftSideWrapper.append(
                    catTitle,
                    categoryNumberOfTestedModelsParagraph
                ),
                nextScreenImage,
                jQuery("<div>").css("clear", "both")
            )
        );

        return categoryItemSectionWrapper;
    }

    window.createCategoryElement.setLabel               = setLabel;
    window.createCategoryElement.setLabelBefore         = setLabelBefore;
    window.createCategoryElement.setPageLink            = setPageLink;
    window.createCategoryElement.setLinkParameterKey    = setLinkParameterKey;
})();

function getCategory(source, categoryId){
    var result = null;
    jQuery.each(source, function(i,value){
        if(value.id == categoryId){
            result = value;
            return false;
        }else if(value.children) {
            var res = getCategory(value.children, categoryId);
            if(res){
                result = res;
                return false;
            }
        }
    });
    return result;
}

function getParentCategoryByIdandType(source, categoryId, type){
    var tmpObject;
    tmpObject = findParentCategoryByIdandType(source, categoryId, type);
    return tmpObject;
}

function findParentCategoryByIdandType(source, categoryId, type, cacheObject){
    var result = null;
    jQuery.each(source, function(i,value){
        if(value.type == (type ? type : "supercategory")) cacheObject = value;
        if(value.id == categoryId){
            result = cacheObject ? cacheObject : "";
            return false;
        }else if(value.children) {
            var res = findParentCategoryByIdandType(value.children, categoryId, type, cacheObject);
            if(res){
                result = res;
                return false;
            }
        }
    });
    return result;
}

function toCamelCase(str) {
    if(str) return str.replace(/(?:^|\s)\w/g, function(match) {
        return match.toUpperCase();
    });
}

function onPageShow(fixFunction){
    window.onpageshow = function(evt) {
        if (evt.persisted) {
            fixFunction();
        }
    };
}

var CUMobile = CUMobile || {};
CUMobile.ERROR_TYPE = CUMobile.ERROR_TYPE || {};
CUMobile.ERROR_TYPE.CHECKED = "CHECKED";
CUMobile.ERROR_TYPE.UNCHECKED = "UNCHECKED";
CUMobile.ERROR_TYPE.COOKIE_KEY = "ERROR_REPORT";


CUMobile.setUrlParameterByName = function(url, name, value) {
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), searchResults = regex.exec(url),
        searchResult = searchResults == null ? "" : decodeURIComponent(searchResults[1].replace(/\+/g, " "));
    return url.indexOf("?") > 0 ? url.replace(searchResult, value) : url + "?" + name + "=" + value;
};

CUMobile.getPayWall = function(login, subscribe, intkey){
    if(intkey) subscribe = CUMobile.setUrlParameterByName(subscribe, "INTKEY", intkey);

    var PAYWALL_TEXT = "Ratings, Reviews, Reliability<br> "
        + "& Compare are for Subscribers Only<br> "
        + "<a href="+ login +" class='text-authentification red'>Sign In</a> or <a href="+ subscribe +" class='text-authentification red'>Subscribe</a>";
    return jQuery("<div>").html(PAYWALL_TEXT).addClass("text-bold text-center position-centred large-font");

};

(function($){

     CUMobile.setCookie = function(_n, _v, _d) { var _e = new Date();_e.setDate(_e.getDate() + _d);_c = escape(_v) + ((_e == null) ? "" : ";domain=" + '.'+/[^\.]+\.(\w+)$/.exec(document.domain)[0] + ";path=/;expires=" + _e.toUTCString());document.cookie = _n + "=" + _c;}
     CUMobile.getCookie = function(_n) {var _v = document.cookie;_s = _v.indexOf(" " + _n + "=");if (_s == -1) {_s = _v.indexOf(_n + "=");}if (_s == -1) {_v = null;return null;} else {_s = _v.indexOf("=", _s) + 1;_e = _v.indexOf(";", _s);if (_e == -1) {_e = _v.length;}_v = unescape(_v.substring(_s, _e));{return _v;}}}

    /*
     * Error information will be sent using the following request paramaters
     * type = CHECKED|UNCHECKED
     * message= if CHECKED enumeration of case
     * event= the user event that resulted in the error (top of the stack) | PAGE_LOAD
     * uri= current uri
     * */
     CUMobile.sendErrorReport = function(type, message, event, uri){
        try{
            var errorReport = CUMobile.getCookie(CUMobile.ERROR_TYPE.COOKIE_KEY);
            errorReport = errorReport == null ? 0 : parseInt(errorReport);
            if(errorReport <= 10){
                $.ajax({
                    url: (window.globalNavDomain ? window.globalNavDomain : '') + '/mweb-x-monitor.html',
                    data: {
                        type: type,
                        message: !!message ? message : "",
                        event: !!event ? event : "",
                        uri: !!uri ? uri : window.location.pathname
                    }
                })
                CUMobile.setCookie(CUMobile.ERROR_TYPE.COOKIE_KEY, ++errorReport ,1);
            }
        }catch(e){
            console.error('error[ '+ e.name +']: '+ e.message);
        }
    }
    window.onerror = function(errorMsg, url, lineNumber){
        CUMobile.sendErrorReport(CUMobile.ERROR_TYPE.UNCHECKED, "", errorMsg+" ["+url+"]["+lineNumber+"]");
    }

    CUMobile.showError = function(msg){
        if(CUMobile.preventShowErrorMsg){
            return;
        }
        $("#mainContent,#mweb-spinner").hide();
        $("#errorOccurred").show();
        CUMobile.isError = true;
        CUMobile.sendErrorReport(CUMobile.ERROR_TYPE.CHECKED, msg);
    }
    CUMobile.hideError = function(){
        $("#mainContent").show();
        $("#errorOccurred").hide();
        CUMobile.isError = false;
    }
    $(window).bind("popstate", function(e){
        if (e && e.originalEvent && e.originalEvent.state){

			CUMobile.hideError();
		}
		CUMobile.setUserReferrer();

    });

    window.addEventListener("beforeunload", function(e) {
        CUMobile.preventShowErrorMsg = true;
    });

    $(window).bind("popstate", function(e) {
        var secondLevelDomain = location.hostname.split('.').slice(-2).join('.');
        jQuery.cookie("userWantsFull", null, {path:"/", domain: secondLevelDomain});
    });

    CUMobile.setUserReferrer = function(){
        var secondLevelDomain = location.hostname.split('.').slice(-2).join('.');
        /*without checking, redirecting to correct page, after signing in from paywall page, doesn't work under Ios*/
        if ( (window.location.href).indexOf('paywall/index.htm') === -1) {
            $.cookie("userReferrer", window.location.href, {path:"/", domain: secondLevelDomain});
            $.cookie("originalUserReferrer", window.location.href, {path:"/", domain: secondLevelDomain});
        }
    }

    CUMobile.populateSiteCatalyst = function(pagename, addProps){
        if(CUMobile.siteCatalystReady){
            CUMobile.doPopulateSiteCatalyst(pagename, addProps);
        } else {
            jQuery(document).bind('siteCatalyst_ready', function() {
                CUMobile.doPopulateSiteCatalyst(pagename, addProps);
            })
        }
    }

    CUMobile.doPopulateSiteCatalyst = function(pagename, addProps){
        if(addProps){
            for(prop in addProps){
                s[prop] = addProps[prop];
            }
        }
        if(pagename){
            s.pageName = CUMobile.formatPageName(pagename);
            var sprop = s.pageName.split(':');
            for(var i = 1, len = sprop.length; i < len; i++){
                s['prop' + i] = sprop[i];
            }
            s['prop58'] = s['eVar58'] = "Mobile";
            s['prop' + sprop.length] = "";
            s.t();
        }
    }

    CUMobile.formatPageName = function(pagename){
        return pagename.replace(/( [0-9a-zA-Z&-(),])/g, function($1) {
            return $1.toUpperCase().replace(' ','');
        }).replace("Electronics&Computers","Electronics")
            .replace("Home&Garden","Home")
            .replace("Babies&Kids","Babies")
            .replace("&","And");
    }


    CUMobile.showSpinner = function(elShow, elHide){
        var innerShow = function(elShow, elHide){
            if(elShow && elShow.indexOf('#mweb-spinner') != -1) CUMobile.Spinner.onShowSpinner();
            if(elHide && elHide.indexOf('#mweb-spinner') != -1) CUMobile.Spinner.onHideSpinner();
            jQuery(elHide).hide();
            jQuery(elShow).show();
            $("img.lazy").show().lazyload({effect:"fadeIn"});
            $("img.lazy").removeClass('lazy');
        }
        if(jQuery.isReady){
            innerShow(elShow, elHide);
        }else{
            if(elHide !== ""){
                $(elHide + ", " +elShow).ready(function() {
                    innerShow(elShow, elHide);
                });
            } else {
                $(elShow).ready(function() {
                    innerShow(elShow, elHide);
                });
            }
        }
    }

    CUMobile.replaceEmptyImages = function(){
        jQuery('img').each(function(){
            jQuery(this).error(function(){
                $(this).attr('src','/etc/designs/cro/application-resources/apage/images/no_img_medium.png');
            });
        });
    }

    CUMobile.priceView = function(price){
        return typeof price !== 'undefined' ? new String(price).replace(/(\d)(?=(\d\d\d)+([^\d]|$))/g, '$1,') : "";
    }

    CUMobile.isIOS = function() {
            return /iPhone|iPad|iPod/i.test(navigator.userAgent);
    }
    CUMobile.isNativeAndroid = function() {
            var uAgent = navigator.userAgent.toLowerCase();
            return ((uAgent.indexOf('mozilla/5.0') > -1 && uAgent.indexOf('android ') > -1 && uAgent.indexOf('applewebkit') > -1) && !(uAgent.indexOf('chrome') > -1));
    }

    CUMobile.getDeviceViewByScreenSize = function() {
        var width = window.innerWidth;
        var experience = 'desktop';

        if(width <= 1200 && width >= 768) {
            experience = 'tablet';
        } else if(width < 768) {
            experience = 'mobile';
        }

        return experience;
    }
})(jQuery);