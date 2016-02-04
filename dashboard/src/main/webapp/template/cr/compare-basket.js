/**
 *  Users can add up to MAX_ITEMS on the compare page.  When a user is on a non-compare page,
 *  such as the Ratings chart, or the Selector, or the Recommended page, they can add additional
 *  items up to MAX_ITEMS (taking into account how many items are already in the compare page).
 *  If a user views a non-compare page for a different supercategory, they can add MAX_ITEMS
 *  models again, even if the compare chart is full.  When on a non-compare page, items are added
 *  to the "Holding Area".  When the user actually hits the COMPARE button, the holding area
 *  should be copied to the compare area.  This way, if a user has TVs in their compare chart and
 *  then goes to the refrigerators page, they can check and uncheck models without having the TVs
 *  interfere with the process, but as long as they do not hit the COMPARE button they can return
 *  to the compare page and still see their TVs.  Users can only add to the holding area, not
 *  directly to the compare area.  When the holding area is copied to the compare area, the holding
 *  area is left unchanged so that users may continue to add there.
 */

/**
 * This Javascript file requires these other Javascript files:
 *  REQUIRES: Cookies.js
 *  REQUIRES: json2.js
 *  REQUIRES: Properties.js
 */

/**
 *  Declare the CUCompareBasket object
 */
if (CUCompareBasket == null) var CUCompareBasket = {};

/**
 *  Maximum number of items to compare at once
 */
CUCompareBasket.MAX_ITEMS = 5;

/**
 *  Used only by the readCookie and writeCookie methods - do not use otherwise.
 *  REQUIRES: Cookies.js
 */
CUCompareBasket.itsHost = "." + window.location.hostname.split(".").slice(-2).join(".");
CUCompareBasket.itsCompareCookieParam = new Cookie(DocumentCookieManager, "CUCompareBasket", CUCompareBasket.itsHost, "/");

/**
 * This field is used to be able to determine what breadcrumb, etc. to display on an
 * empty model page after all models have been removed.
 */
CUCompareBasket.itsLastKnownModel = null;

/**
 *  Call this function when on the Ratings page, Selector page, or Recommended page whenever a
 *  user sets a checkbox to checked.
 *  REQUIRES: json2.js
 *  INPUT: inModelID should be the CU Model ID of the model to be added
 *  INPUT: inSupercategoryID should be the CU Group ID of the supercategory of the model to be added
 *  INPUT: inModelName should be the display name of the model to be added
 *  RETURNS: true or false (false if the holding area is full)
 */
CUCompareBasket.addItemToHoldingArea = function (inModelID, inSupercategoryID, inBrandName, inModelName) {
    var theData = CUCompareBasket.readCookie();
    var theHoldingArea = CUCompareBasket.getHoldingAreaItems(inSupercategoryID, true);
    for (var i = 0; (i < theHoldingArea.length); i++) {
        if (theHoldingArea[i].id == inModelID) {
            return true;
        }
    }
    if (theHoldingArea.length < CUCompareBasket.MAX_ITEMS) {
        for (var i = 0; (i < theHoldingArea.length); i++) {
            if (theHoldingArea[i].id == inModelID) {
                return true;
            }
        }
        var theItem = {};
        theItem.id = inModelID;
        theItem.gid = inSupercategoryID;
        theItem.b = inBrandName;
        theItem.n = inModelName;
        theHoldingArea.push(theItem);
        theData.setProperty("h", JSON.stringify(theHoldingArea));
        CUCompareBasket.writeCookie(theData);
        return true;
    }
    else {
        return false;
    }
};

/**
 *  Call this function when on the Ratings page, Selector page, or Recommended page whenever a
 *  user unsets/clears a checkbox, or removes the item from the "You have chosen to compare"
 *  module on the page.
 *  INPUT: inModelID should be the CU Model ID of the model to be removed
 */
CUCompareBasket.removeItemFromHoldingArea = function (inModelID) {
    var theData = CUCompareBasket.readCookie();
    if (theData) {
        var theHoldingData = theData.getProperty("h");
        if (theHoldingData) {
            var theHoldingArea = JSON.parse(theHoldingData);
            var theNewHoldingArea = [];
            for (var i = 0; (i < theHoldingArea.length); i++) {
                if (theHoldingArea[i].id != inModelID) {
                    theNewHoldingArea.push(theHoldingArea[i]);
                }
            }
            theData.setProperty("h", JSON.stringify(theNewHoldingArea));
            CUCompareBasket.writeCookie(theData);
        }
    }
};

/**
 *  Call this function when on the Compare page whenever a user removes an item from the
 *  comparison.  This function will also remove the item from the holding area.
 *  INPUT: inModelID should be the CU Model ID of the model to be removed
 */
CUCompareBasket.removeItemFromCompare = function (inModelID) {
    var theData = CUCompareBasket.readCookie();
    if (theData) {
        var theCompareData = theData.getProperty("c");
        if (theCompareData) {
            var theCompareArea = JSON.parse(theCompareData);
            var theNewCompareArea = [];
            for (var i = 0; (i < theCompareArea.length); i++) {
                if (theCompareArea[i].id.toLowerCase() != inModelID) {
                    theNewCompareArea.push(theCompareArea[i]);
                }
            }
            if (theNewCompareArea.length > 0) {
                CUCompareBasket.itsLastKnownModel = theNewCompareArea[theNewCompareArea.length - 1];
            }
            theData.setProperty("c", JSON.stringify(theNewCompareArea));
            CUCompareBasket.writeCookie(theData);
        }
    }
    CUCompareBasket.removeItemFromHoldingArea(inModelID);
};

/**
 *  This function should be used when first entering the Ratings page, Selector page, or Recommended
 *  page to initially populate the contents of the "You have chosen to compare" module (it will return
 *  an Array of items).  If the items in the holding area are for a different supercategory than the
 *  one being specified, the holding area will be cleared automatically.
 *  REQUIRES: json2.js
 *  INPUT: inSupercategoryID should be the CU Group ID of the supercategory being viewed
 *  INPUT: inSuppressDefault is optional and should be set to true only while the user is modifying (adding/deleting
 *         models) while on the Ratings page, Selector page, or Recommended page.  If set to true, when the holding
 *         area is empty, it will stay empty.  If it is missing or set to false, when the holding area is empty it
 *         will be filled with the current contents of the compare area (if any).  The reason is that if a user adds
 *         items from one supercategory and then copies them from the holding area to the compare area, and then
 *         visits another supercategory without making changes to the compare area and then returns to the first
 *         supercategory, the holding area will be empty but we would like it to be filled with the current contents
 *         of the compare area.  However, that same behavior will prevent a user from completely purposefully emptying
 *         the contents of the holding area.
 *  RETURNS: an Array of objects containing item data
 */
CUCompareBasket.getHoldingAreaItems = function (inSupercategoryID, inSuppressDefault) {
    var theCompareData;
    var theCompareArea;
    var theSuppressDefault = false;
    if ((inSuppressDefault === undefined) || (inSuppressDefault == undefined)) {
        theSuppressDefault = false;
    }
    else {
        theSuppressDefault = inSuppressDefault;
    }
    var theData = CUCompareBasket.readCookie();
    if (theData) {
        var theHoldingData = theData.getProperty("h");
        if (theHoldingData) {
            var theHoldingArea = JSON.parse(theHoldingData);
            if (theHoldingArea.length > 0) {
                if (theHoldingArea[0].gid != inSupercategoryID) {
                    theCompareData = theData.getProperty("c");
                    if (theCompareData) {
                        theCompareArea = JSON.parse(theCompareData);
                        if (theCompareArea.length > 0) {
                            if (theCompareArea[0].gid == inSupercategoryID) {
                                CUCompareBasket.copyCompareToHoldingArea();
                                return theCompareArea;
                            }
                        }
                    }
                    theHoldingArea = [];
                    theData.setProperty("h", JSON.stringify(theHoldingArea));
                    CUCompareBasket.writeCookie(theData);
                }
            }
            else {
                if (!theSuppressDefault) {
                    theCompareData = theData.getProperty("c");
                    if (theCompareData) {
                        theCompareArea = JSON.parse(theCompareData);
                        if (theCompareArea.length > 0) {
                            if (theCompareArea[0].gid == inSupercategoryID) {
                                CUCompareBasket.copyCompareToHoldingArea();
                                return theCompareArea;
                            }
                        }
                    }
                }
            }
        }
        else {
            theHoldingArea = [];
            theData.setProperty("h", JSON.stringify(theHoldingArea));
            CUCompareBasket.writeCookie(theData);
        }
    }
    else {
        theHoldingArea = [];
        theData.setProperty("h", JSON.stringify(theHoldingArea));
        CUCompareBasket.writeCookie(theData);
    }
    return theHoldingArea;
};

/**
 *  This function will return an Array of items currently in the compare area.
 *  REQUIRES: json2.js
 *  RETURNS: an Array of objects containing item data
 */
CUCompareBasket.getCompareItems = function () {
    var theData = CUCompareBasket.readCookie();
    if (theData) {
        var theCompareData = theData.getProperty("c");
        if (theCompareData) {
            var theCompareArea = JSON.parse(theCompareData);
            if (theCompareArea.length > 0) {
                CUCompareBasket.itsLastKnownModel = theCompareArea[theCompareArea.length - 1];
            }
            return theCompareArea;
        }
        else {
            return [];
        }
    }
    else {
        return [];
    }
};

/**
 *  This function should be called to get the data for the last known model seen in
 *  the Compare Basket, even once it is empty.  This is used to display the breadcrumb
 *  or other product-specific data on an empty compare chart.
 */
CUCompareBasket.getLastKnownModel = function () {
    return CUCompareBasket.itsLastKnownModel;
};

/**
 *  This function should be called when the user clicks on the COMPARE button on the Ratings page,
 *  Selector page, or Recommended page prior to sending the user to the Compare page itself.  This
 *  function will simply clear the existing compare area and copy the contents of the holding area
 *  into the compare area.
 *  REQUIRES: json2.js
 */
CUCompareBasket.copyHoldingAreaToCompare = function () {
    var theData = CUCompareBasket.readCookie();
    theData.setProperty("c", theData.getProperty("h"));
    CUCompareBasket.writeCookie(theData);
    var theCompareData = theData.getProperty("c");
    if (theCompareData) {
        var theCompareArea = JSON.parse(theCompareData);
        if (theCompareArea.length > 0) {
            CUCompareBasket.itsLastKnownModel = theCompareArea[theCompareArea.length - 1];
        }
    }
};

/**
 *  Should not be called independently, but just called by other functions in this
 *  file.  Specifically, this is called by the getHoldingAreaItems function.  The
 *  copyCompareToHoldingArea function copies the compare area to the holding area
 *  and rewrites the cookie on the browser to reflect that.  This function should
 *  be called when the user returns to a Ratings, Selector, or Recommended page
 *  that is for the same supercategory as the models currently in the compare area.
 *  This guarantees that if the user has, say, televisions in the compare area and
 *  then visits the refrigerators page and clicks on a few checkboxes (which
 *  updates the holding area) but never hits the COMPARE button, then when the user
 *  returns to the television ratings page, they will still see in their holding
 *  area the televisions they have already added to compare, and not refrigerators.
 */
CUCompareBasket.copyCompareToHoldingArea = function () {
    var theData = CUCompareBasket.readCookie();
    theData.setProperty("h", theData.getProperty("c"));
    CUCompareBasket.writeCookie(theData);
};

/**
 *  Should not be called independently, but just called by other functions in
 *  this file.  Specifically, this function is used by the following functions:
 *  addItemToHoldingArea, removeItemFromHoldingArea, removeItemFromCompare,
 *  getHoldingAreaItems, getCompareItems, copyHoldingAreaToCompare, copyCompareToHoldingArea.
 *  This function reads a cookie from the browser - the cookie is expected to
 *  be formatted as a query string, which is then parsed into a Properties
 *  object (which should represent both the state of the compare area and
 *  the holding area).
 *
 *  CHANGE ON 06-JULY-2010: Use client-side session variable, in addition to
 *  cookies.  First we check to see if a client-side session variable is set.
 *  If so, we use it.  If not, we defer to the cookie.  The client-side
 *  session variables are per-window only, so if the user opens a new tab the
 *  data is not preserved in that new tab.  That's the reason to have the cookie
 *  as a fall-back.  However, we would prefer to use the session variable when
 *  it is available because it is not subject to the space limitations that
 *  cookies are subject to.  And in IE, the limit is rather small (4k for all
 *  cookies in the domain) and when the limit is passed the most recently set
 *  cookie is deleted.
 *
 *  REQUIRES: Properties.js and Cookies.js and sessvars.js
 *  RETURNS: a Properties object
 */
CUCompareBasket.readCookie = function () {
    var theData = new Properties();
    var theCompareCookieValue = CUCompareBasket.itsCompareCookieParam.getValue();
    if (sessvars.compare) {
        var theCompareSessionValue = sessvars.compare.value;
        if (theCompareSessionValue != undefined) {
            theData.parseQueryString(theCompareSessionValue);
            return theData;
        }
    }
    theData.parseQueryString(theCompareCookieValue);
    return theData;
};

/**
 *  Should not be called independently, but just called by other functions in
 *  this file.  Specifically, this function is used by the following functions:
 *  addItemToHoldingArea, removeItemFromHoldingArea, removeItemFromCompare,
 *  getHoldingAreaItems, copyHoldingAreaToCompare, copyCompareToHoldingArea.
 *  This function converts a given Properties object (which should represent
 *  both the state of the compare area and the holding area) into the
 *  query string format and then stores that query string in a cookie on the
 *  browser.
 *
 *  CHANGE ON 06-JULY-2010: Use client-side session variable, in addition to
 *  cookies.
 *
 *  REQUIRES: Properties.js and Cookies.js and sessvars.js
 *  INPUT: inData should be a Properties object
 */
CUCompareBasket.writeCookie = function (inData) {
    var theCompareCookieValue = inData.asQueryString();
    sessvars.compare = {"value": theCompareCookieValue};
    if (!sessvars.compare) {
        CUCompareBasket.itsCompareCookieParam.setValue(theCompareCookieValue);
    }
    sessvars.$.flush();
};
