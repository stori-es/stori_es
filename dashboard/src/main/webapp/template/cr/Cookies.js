var DocumentCookieManager =
{
    get: function (inName) {
        var theCookies;
        var theStart;
        var theLength;
        var theEnd;

        theCookies = document.cookie;
        theStart = theCookies.indexOf(inName + "=");
        if (theStart < 0)
            return (null);
        theStart = theStart + inName.length + 1;
        theEnd = theCookies.indexOf(";", theStart);
        if (theEnd < 0)
            theEnd = theCookies.length;
        return (unescape(theCookies.substring(theStart, theEnd)));
    },


    set: function (inCookie, inValue) {
        return (DocumentCookieManager.setDocumentCookie(
            inCookie.getName(),
            inValue,
            inCookie.getPath(),
            inCookie.getDomain(),
            inCookie.getExpirationDate(),
            inCookie.getSecurity()));
    },


    remove: function (inCookie) {
        return (DocumentCookieManager.setDocumentCookie(
            inCookie.getName(),
            undefined,
            inCookie.getPath(),
            inCookie.getDomain(),
            new Date(1970, 0, 1),
            undefined));
    },


    setDocumentCookie: function (inName,
                                 inValue,
                                 inPath,
                                 inDomain,
                                 inExpirationDate,
                                 inSecurity) {
        document.cookie =
            inName + "="
            + escape(inValue)
            + (inExpirationDate ? ";expires=" + inExpirationDate.toGMTString() : "")
            + (inPath ? ";path=" + inPath : "")
            + (inDomain ? ";domain=" + inDomain : "")
            + (inSecurity ? ";secure=" + inSecurity : "");
        return (this);
    }
};


function NestedCookieManager(itsCookie) {
    var self = this;


    this.get = function (inName) {
        var theProperties;

        theProperties = new Properties();
        theProperties.parseQueryString(itsCookie.getValue());
        return (theProperties.getProperty(inName));
    };


    this.set = function (inCookie, inValue) {
        var theProperties;

        theProperties = new Properties();
        theProperties.parseQueryString(itsCookie.getValue());
        theProperties.setProperty(inCookie.getName(), inValue);
        itsCookie.setValue(theProperties.asQueryString());

        return (self);
    };


    this.remove = function (inCookie) {
        var theProperties;

        theProperties = new Properties();
        theProperties.parseQueryString(itsCookie.getValue());
        theProperties.removeProperty(inCookie.getName());
        if (theProperties.getSize() == 0)
            itsCookie.remove();
        else
            itsCookie.setValue(theProperties.asQueryString());

        return (self);
    };
}


function Cookie(itsCookieManager,
                itsName,
                itsDomain,
                itsPath,
                itsExpirationDate,
                itsSecurity) {
    var self = this;


    this.getName = function () {
        return (itsName);
    };


    this.setName = function (inName) {
        itsName = inName;
        return (self);
    };


    this.getValue = function () {
        return (itsCookieManager.get(itsName));
    };


    this.setValue = function (inValue) {
        if (inValue === null || inValue === undefined)
            itsCookieManager.remove(self);
        else
            itsCookieManager.set(self, inValue);
        return (self);
    };


    this.remove = function () {
        itsCookieManager.remove(self);
    };


    this.getDomain = function () {
        return (itsDomain);
    };


    this.setDomain = function (inDomain) {
        itsDomain = inDomain;
        return (self);
    };


    this.getExpirationDate = function () {
        return (itsExpirationDate);
    };


    this.setExpirationDate = function (inExpirationDate) {
        itsExpirationDate = inExpirationDate;
        return (self);
    };


    this.getPath = function () {
        return (itsPath);
    };


    this.setPath = function (inPath) {
        itsPath = inPath;
        return (self);
    };


    this.getSecurity = function () {
        return (itsSecurity);
    };


    this.setSecurity = function (inSecurity) {
        itsSecurity = inSecurity;
        return (self);
    };
}


