/**
 *  Declare the CUCompare object
 */
var theFirstItem = null;
if (CUCompare == null) var CUCompare = {};

/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompare.empty = function (inNode) {
    if (inNode != undefined) {
        while (inNode.firstChild) {
            inNode.removeChild(inNode.firstChild);
        }
    }
};

/**
 *  Open and close various sections of the table.
 */
CUCompare.toggle = function (inSection) {
    var theSection = document.getElementById("compare-body-" + inSection);
    var theImage = document.getElementById(inSection + "-image");
    theImage.src = ('none' == theSection.style.display ? 'http://www.consumerreports.org/cro/resources/content/cars/images/compare/icon_open.gif' : 'http://www.consumerreports.org/cro/resources/content/cars/images/compare/icon_close.gif');
    theSection.style.display = ('none' == theSection.style.display ? '' : 'none');
};


/**
 *  Call this function when on the Compare page whenever a user removes an item from the
 *  comparison.  This function will also remove the item from the holding area.
 *  REQUIRES: compare-core.js
 *  INPUT: inModelID should be the CU Model ID of the model to be removed
 */
CUCompare.remove = function (inModelID) {
    CUCompareCore.remove(inModelID);
};

CUCompare.trimText = function (inText) {
    if (inText.length <= 13) {
        return inText;
    }
    else {
        // TODO: Look at old truncation methods we've used in the past to make sure we don't break a tag and to add ellipses
        return (inText.substring(0, 13) + "...");
    }
};

/**
 *  Should not be called independently, but just used by the compare code.  This is used
 *  to get price and user review data SYNCHRONOUSLY (not asynchronously) from the webstack.
 */
CUCompare.getLiveData = function (inUrl) {
    var theHttpRequest;
    var theData;

    if (window.XMLHttpRequest) {
        theHttpRequest = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        try {
            theHttpRequest = new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e) {
            try {
                theHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch (e) {
            }
        }
    }
    if (!theHttpRequest) {
        alert("noHttpRequest");
        return null;
    }
    theHttpRequest.open("GET", inUrl, false);
    theHttpRequest.send('');
    return eval("(" + theHttpRequest.responseText + ")");
};

/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompare.getLiveDataURL = function (inMasterID, inDefaultPrice, inPageID, inPgId) {
    return ("/cro/compare-livedata.htm?masterID=" + inMasterID + "&defaultPrice=" + escape(inDefaultPrice) + "&pageID=" + inPageID + "&pgID=" + inPgId);
};


/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompare.isLiveURL = function (inUrl) {
    var theHttpRequest;
    var theData;

    if (window.XMLHttpRequest) {
        theHttpRequest = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        try {
            theHttpRequest = new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e) {
            try {
                theHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch (e) {
            }
        }

    }
    if (!theHttpRequest) {
        alert("noHttpRequest");
        return null;
    }

    theHttpRequest.open("GET", inUrl, false);
    theHttpRequest.send('');
    return theHttpRequest.status;

};

/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompare.getURL = function (inUrl) {
    var theHttpRequest;
    var theData;

    if (window.XMLHttpRequest) {
        theHttpRequest = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        try {
            theHttpRequest = new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e) {
            try {
                theHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            }
            catch (e) {
            }
        }

    }
    if (!theHttpRequest) {
        alert("noHttpRequest");
        return null;
    }

    theHttpRequest.open("GET", inUrl, false);
    theHttpRequest.send('');
    return theHttpRequest;
};

CUCompare.printGlossaryPopup = function (inTitle, inID, inParentElement, inText) {
    var theGlossaryPopup = document.createElement("div");
    theGlossaryPopup.setAttribute("id", inID);
    theGlossaryPopup.style["display"] = "none";
    theGlossaryPopup.innerHTML = '<div class="popup_ratings"><dl><dd class="top">&nbsp;</dd><dd class="middle"><b>' + inTitle + '</b><br><br>' + inText + '</dd><dd class="bottom">&nbsp;</dd></dl></div>';
    inParentElement.appendChild(theGlossaryPopup);
};

CUCompare.printMagnifyPopup = function (inTitle, inID, inParentElement, inImageURL, inOverviewURL, inIconImage) {
    var theMagnifyPopup = document.createElement("div");
    theMagnifyPopup.setAttribute("id", inID);
    theMagnifyPopup.style["display"] = "none";
    if (inIconImage != "")
        theMagnifyPopup.innerHTML = '<div class="popup_ratings" style="margin:20px;"><dl><dd class="top">&nbsp;</dd><dd class="middle"><img class="topRight" alt="" src="' + inIconImage + '"/><img alt="image" width="150" height="109" src=\"' + inImageURL + '\" class="popPhoto"/><br><a href=\"' + inOverviewURL + '\"><span class="model-brand">' + inTitle + '</span></a></dd><dd class="bottom">&nbsp;</dd></dl></div>';
    else
        theMagnifyPopup.innerHTML = '<div class="popup_ratings" style="margin:20px;"><dl><dd class="top">&nbsp;</dd><dd class="middle"><img alt="" width="150" height="109" src=\"' + inImageURL + '\" class="popPhoto"/><br><a href=\"' + inOverviewURL + '\"><span class="model-brand">' + inTitle + '</span></a></dd><dd class="bottom">&nbsp;</dd></dl></div>';
    inParentElement.appendChild(theMagnifyPopup);

};

CUCompare.printModelPopup = function (inBrand, inModel, inID, inParentElement, inURL) {
    inModel = inModel.replace(/&amp;/g, "\&");
    var theModelPopup = document.createElement("div");
    theModelPopup.setAttribute("id", inID);
    theModelPopup.style["display"] = "none";
    theModelPopup.innerHTML = '<div class="popup_ratings" style="position:relative; left:20px; margin:0px;"><dl><dd class="top">&nbsp;</dd><dd class="middle" style="color:#176FCC; font:bold 12px Arial,Helvetica,sans-serif;"><a href = \"' + inURL + '\">' + inBrand + '<br><span style="color:#176FCC; font:11px Arial,Helvetica,sans-serif;">' + inModel + '</span></dd><dd class="bottom">&nbsp;</dd></dl></div>';
    inParentElement.appendChild(theModelPopup);
};

CUCompare.getGlossaryText = function (inAttributeName, inString) {
    var theProductTypeArray = [];
    var theDisplayTypeArray = [];
    var iTextRatingAttributeArray = [];
    var iTextDisplayRatingAttributeArray = [];
    var iTextRatingAttribute = '';
    var found = false;
    var x = 0;
    var k = 0;
    if (inString == 'ratings') {
        for (var i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
            if (CUCompareCore.itsCompareData[i].ratings[inAttributeName] && (theProductTypeArray.toString()).indexOf(CUCompareCore.itsCompareData[i]["Product Type"]) < 0) {
                theProductTypeArray[k] = CUCompareCore.itsCompareData[i]["Product Type"];
                iTextRatingAttributeArray[k] = CUCompareCore.itsCompareData[i].ratings[inAttributeName].text;
                k++;
            }
        }
    }
    else if (inString == 'specs') {
        for (var i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
            if (CUCompareCore.itsCompareData[i].specs[inAttributeName] && (theProductTypeArray.toString()).indexOf(CUCompareCore.itsCompareData[i]["Product Type"]) < 0) {
                theProductTypeArray[k] = CUCompareCore.itsCompareData[i]["Product Type"];
                iTextRatingAttributeArray[k] = CUCompareCore.itsCompareData[i].specs[inAttributeName].text;
                k++;
            }
        }
    }
    for (var i = 0; (i < theProductTypeArray.length); i++) {
        if ((theDisplayTypeArray.toString()).indexOf(theProductTypeArray[i]) < 0) {
            theDisplayTypeArray[x] = theProductTypeArray[i];
            iTextDisplayRatingAttributeArray[x] = iTextRatingAttributeArray[i];
            found = true;
        }

        for (var j = 0; (j < theProductTypeArray.length); j++) {
            if (i != j) {
                if (iTextRatingAttributeArray[i] == iTextRatingAttributeArray[j] && (theDisplayTypeArray.toString()).indexOf(theProductTypeArray[j]) < 0) {
                    theDisplayTypeArray[x] = theDisplayTypeArray[x] + ', ' + theProductTypeArray[j];
                }
            }
        }

        if (found) {
            x++;
            found = false;
        }

    }

    for (var i = 0; (i < theDisplayTypeArray.length); i++) {
        var theTypesArray = theDisplayTypeArray[i].split(',');
        var productString;
        if (theTypesArray.length > 1) {
            for (var x = 0; x < theTypesArray.length; x++) {
                if (x == 0)
                    productString = theTypesArray[x];
                else if (x == (theTypesArray.length) - 1)
                    productString = productString + ' and ' + theTypesArray[x];
                else
                    productString = productString + ', ' + theTypesArray[x];
            }
        }
        else
            productString = theDisplayTypeArray[i];

        iTextRatingAttribute = iTextRatingAttribute + '<b>' + productString + '</b><br>' + iTextDisplayRatingAttributeArray[i] + '<br><br>';
    }

    return iTextRatingAttribute;
}
/**
 *  Should not be called independently, but just used by the compare code.
 *  REQUIRES: compare-event.js
 *  REQUIRES: compare-core.js
 */
CUCompare.populateChartHeaderInfo = function () {
    var theBreadcrumb = document.getElementById("compareBreadcrumb");
    var theTitle = document.getElementById("compareTitle");
    //var theChartHead = document.getElementById("chartHead");
    CUCompare.empty(theBreadcrumb);
    CUCompare.empty(theTitle);
    //CUCompare.empty(theChartHead);
    var thetitleStyle = document.createElement("h1");
    theTitle.appendChild(thetitleStyle);

    if (CUCompareCore.itsCompareData.length > 0) {
        theFirstItem = CUCompareCore.itsCompareData[0];
    }
    else {
        theFirstItem = CUCompareCore.getLastKnownData();
    }
    if (theFirstItem != null) {
        document.title = theFirstItem.canonicalsupercategory + ' comparison chart ';

        var theHome = document.createElement("a");
        theHome.setAttribute("href", "/cro/index.htm");
        theHome.appendChild(document.createTextNode("Home"));
        var theFranchise = document.createElement("a");
        theFranchise.setAttribute("href", theFirstItem["franchiseurl"]);
        theFranchise.appendChild(document.createTextNode(theFirstItem["franchise"]));
        var theSubfranchise = document.createElement("a");
        theSubfranchise.setAttribute("href", theFirstItem.subfranchiseurl);
        theSubfranchise.appendChild(document.createTextNode(theFirstItem.subfranchise));
        var theSupercategory = document.createElement("a");
        theSupercategory.setAttribute("href", theFirstItem.supercategoryurl);
        theSupercategory.appendChild(document.createTextNode(theFirstItem.supercategory));
        theBreadcrumb.appendChild(theHome);
        theBreadcrumb.appendChild(document.createTextNode(" > "));
        theBreadcrumb.appendChild(theFranchise);
        theBreadcrumb.appendChild(document.createTextNode(" > "));
        theBreadcrumb.appendChild(theSubfranchise);
        theBreadcrumb.appendChild(document.createTextNode(" > "));
        theBreadcrumb.appendChild(theSupercategory);
        theBreadcrumb.appendChild(document.createTextNode(" > "));
        theBreadcrumb.appendChild(document.createTextNode(theFirstItem.canonicalsupercategory + ' comparison chart '));

        //thetitleStyle.appendChild(document.createTextNode(theFirstItem.supercategory));
        //theChartHead.appendChild(document.createTextNode(theFirstItem.canonicalsupercategory+' comparison chart '));

        thetitleStyle.appendChild(document.createTextNode(theFirstItem.canonicalsupercategory + ' comparison chart '));

        /*
         if(theFirstItem["franchise"] == "cars")
         {
         var theMainCarNav = document.getElementById("main-nav-cars-image");
         theMainCarNav.src = '/cro/resources/content/images/mainnav_cars_seo_2.gif';
         }
         else if (theFirstItem["franchise"] == "Home & garden")
         {
         var theMainHomeNav = document.getElementById("main-nav-home-image");
         theMainHomeNav.src = '/cro/resources/content/images/mainnav_homegard_seo_2.gif';
         }
         else if (theFirstItem["franchise"] == "Electronics & computers")
         {
         var theMainElecNav = document.getElementById("main-nav-electronics-image");
         theMainElecNav.src = '/cro/resources/content/images/mainnav_electronics_seo_2.gif';
         }
         else if (theFirstItem["franchise"] == "Appliances")
         {
         var theMainAppNav = document.getElementById("main-nav-appliances-image");
         theMainAppNav.src = '/cro/resources/content/images/mainnav_appliance_seo_2.gif';
         }
         else if (theFirstItem["franchise"] == "Babies & kids")
         {
         var theMainBabiesNav = document.getElementById("main-nav-babies-image");
         theMainBabiesNav.src = '/cro/resources/content/images/mainnav_babieskids_seo_2.gif';
         }
         else{}
         */
    }
    else {
        document.title = 'Product comparison chart';
        thetitleStyle.appendChild(document.createTextNode("Products"));

        var theHome = document.createElement("a");
        theHome.setAttribute("href", "/cro/index.htm");
        theHome.appendChild(document.createTextNode("Home"));
        theBreadcrumb.appendChild(theHome);
        /*
         var theMainCarNav = document.getElementById("main-nav-cars-image");
         theMainCarNav.src = '/cro/resources/content/images/mainnav_cars_seo.gif';
         var theMainHomeNav = document.getElementById("main-nav-home-image");
         theMainHomeNav.src = '/cro/resources/content/images/mainnav_homegard_seo.gif';
         var theMainElecNav = document.getElementById("main-nav-electronics-image");
         theMainElecNav.src = '/cro/resources/content/images/mainnav_electronics_seo.gif';
         var theMainAppNav = document.getElementById("main-nav-appliances-image");
         theMainAppNav.src = '/cro/resources/content/images/mainnav_appliance_seo.gif';
         var theMainBabiesNav = document.getElementById("main-nav-babies-image");
         theMainBabiesNav.src = '/cro/resources/content/images/mainnav_babieskids_seo.gif';
         */
    }
};


/**
 *  Should not be called independently, but just used by the compare code.
 *  REQUIRES: compare-event.js
 *  REQUIRES: compare-core.js
 */
CUCompare.drawTable = function () {

    var theCompareHeadRemoveRow = document.getElementById("compare-head-remove-row");
    var theCompareHeadImageRow = document.getElementById("compare-head-image-row");
    var theCompareHeadNameRow = document.getElementById("compare-head-name-row");
    var theRatingsBody = document.getElementById("compare-body-ratings");
    var theSpecsBody = document.getElementById("compare-body-specs");
    if (CUCompareCore.itsSpecAttributes.length <= 0 && theSpecsBody != null) {
        theSpecsBody.parentNode.removeChild(theSpecsBody);
    }
    var theReviewsBody = document.getElementById("compare-body-reviews");
    var theRatingsHead = document.getElementById("compare-body-ratings-head");
    theRatingsHead.setAttribute("className", "section-head") || theRatingsHead.setAttribute("class", "section-head");
    var theSpecsHead = document.getElementById("compare-body-specs-head");
    if (theSpecsHead != null) {
        theSpecsHead.setAttribute("className", "section-head") || theSpecsHead.setAttribute("class", "section-head");
        if (CUCompareCore.itsSpecAttributes.length <= 0)
            theSpecsHead.parentNode.removeChild(theSpecsHead);
    }
    var theReviewsHead = document.getElementById("compare-body-reviews-head");
    theReviewsHead.setAttribute("className", "section-head") || theReviewsHead.setAttribute("class", "section-head");
    var theSummaryBody = document.getElementById("compare-body-summary");
    CUCompare.empty(theCompareHeadRemoveRow);
    CUCompare.empty(theCompareHeadImageRow);
    CUCompare.empty(theCompareHeadNameRow);
    CUCompare.empty(theRatingsBody);

    if (theSpecsBody != null)
        CUCompare.empty(theSpecsBody);
    CUCompare.empty(theReviewsBody);
    CUCompare.empty(theRatingsHead);
    if (theSpecsHead != null)
        CUCompare.empty(theSpecsHead);
    CUCompare.empty(theReviewsHead);
    CUCompare.empty(theSummaryBody);
    var thePropertyColumn = document.createElement("th");
    thePropertyColumn.setAttribute("id", "corner");
    thePropertyColumn.setAttribute("className", "select") || thePropertyColumn.setAttribute("class", "select");
    thePropertyColumn.rowSpan = 3;
    theCompareHeadRemoveRow.appendChild(thePropertyColumn);

    var theRatingsHeaderRow = document.createElement("tr");
    theRatingsHeaderRow.setAttribute("className", "section-head top") || theRatingsHeaderRow.setAttribute("class", "section-head top");
    var theRatingsHeaderRowCell = document.createElement("td");
    var theRatingsHeaderRowImage = document.createElement("img");
    theRatingsHeaderRowImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_open.gif");
    theRatingsHeaderRowImage.setAttribute("className", "arrow") || theRatingsHeaderRowImage.setAttribute("class", "arrow");
    theRatingsHeaderRowImage.setAttribute("id", "ratings-image");
    CUCompareEvent.addEvent(theRatingsHeaderRowCell, "click", function () {
        CUCompare.toggle('ratings');
    });
    theRatingsHeaderRowCell.appendChild(theRatingsHeaderRowImage);
    var theRatingsHeaderRowCellText = document.createTextNode(" Ratings");
    theRatingsHeaderRowCell.appendChild(theRatingsHeaderRowCellText);
    theRatingsHeaderRow.appendChild(theRatingsHeaderRowCell);

    if (CUCompareCore.itsSpecAttributes.length > 0) {
        var theSpecsHeaderRow = document.createElement("tr");
        theSpecsHeaderRow.setAttribute("className", "section-head top") || theSpecsHeaderRow.setAttribute("class", "section-head top");
        var theSpecsHeaderRowCell = document.createElement("td");
        var theSpecsHeaderRowImage = document.createElement("img");
        theSpecsHeaderRowImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_open.gif");
        theSpecsHeaderRowImage.setAttribute("className", "arrow") || theSpecsHeaderRowImage.setAttribute("class", "arrow");
        theSpecsHeaderRowImage.setAttribute("id", "specs-image");
        CUCompareEvent.addEvent(theSpecsHeaderRowCell, "click", function () {
            CUCompare.toggle('specs');
        });
        theSpecsHeaderRowCell.appendChild(theSpecsHeaderRowImage);
        var theSpecsHeaderRowCellText = document.createTextNode(" Features & Specs");
        theSpecsHeaderRowCell.appendChild(theSpecsHeaderRowCellText);
        theSpecsHeaderRow.appendChild(theSpecsHeaderRowCell);
    }

    var theReviewsHeaderRow = document.createElement("tr");
    theReviewsHeaderRow.setAttribute("className", "section-head top") || theReviewsHeaderRow.setAttribute("class", "section-head top");
    var theReviewsHeaderRowCell = document.createElement("td");
    var theReviewsHeaderRowImage = document.createElement("img");
    theReviewsHeaderRowImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_open.gif");
    theReviewsHeaderRowImage.setAttribute("className", "arrow") || theReviewsHeaderRowImage.setAttribute("class", "arrow");
    theReviewsHeaderRowImage.setAttribute("id", "reviews-image");
    CUCompareEvent.addEvent(theReviewsHeaderRowCell, "click", function () {
        CUCompare.toggle('reviews');
    });
    theReviewsHeaderRowCell.appendChild(theReviewsHeaderRowImage);
    var theReviewsHeaderRowCellText = document.createTextNode(" User Reviews");
    theReviewsHeaderRowCell.appendChild(theReviewsHeaderRowCellText);
    theReviewsHeaderRow.appendChild(theReviewsHeaderRowCell);

    for (i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
        var theItem = CUCompareCore.itsCompareData[i];
        var theItemRemoveNode = document.createElement("th");
        theItemRemoveNode.setAttribute("className", "model-bar") || theItemRemoveNode.setAttribute("class", "model-bar");
        var theItemImageNode = document.createElement("th");
        var theItemNameNode = document.createElement("th");

        var theItemImage = document.createElement("img");
        if (theItem["small image"]) {
            var theItemImageLink = document.createElement("a");
            theItemImageLink.setAttribute("href", theItem["overview url"]);
            if ((theItem["small image"]).indexOf('missing') < 0) {
                theItemImage.setAttribute("src", theItem["small image"]);
                theItemImage.setAttribute("width", "95");
                theItemImage.setAttribute("height", "59");
                theItemImage.setAttribute("className", "model-image") || theItemImage.setAttribute("class", "model-image");
            }
            else {
                theItemImage.setAttribute("src", "/cro/resources/content/products/images/compare/no_img_txt.png");
                theItemImage.setAttribute("width", "85");
                theItemImage.setAttribute("height", "8");
                theItemImage.setAttribute("className", "no-image") || theItemImage.setAttribute("class", "no-image");
            }
            theItemImageLink.appendChild(theItemImage);
            var theItemImageMagnifyDiv = document.createElement("div");
            theItemImageMagnifyDiv.setAttribute("className", "magnify") || theItemImageMagnifyDiv.setAttribute("class", "magnify");
            var magnifyPopupID = theItem.id + 'magnifyPopupID';
            theItemImageMagnifyDiv.innerHTML = '<img src="/cro/resources/content/products/mid-level/images/icon_enlarge.gif" alt="magnify" onmouseout = "closeToggleInfoPopUpBox(event)" onmouseover = "openToggleInfoPopUpBox(this,\'' + magnifyPopupID + '\',event, 20);" >';
            var iconImageSrc = "";

            if (theItem.isDontBuy == "1" || theItem.isDontBuy == "2")
                iconImageSrc = "/cro/resources/content/images/products/label_dontbuy.png";
            else if (theItem.subcategory && theItem.subcategory.toUpperCase().indexOf("DON'T BUY:") >= 0)
                iconImageSrc = "/cro/resources/content/images/products/icon_dontbuy.gif";
            else if (theItem.isBestBuy)
                iconImageSrc = "/cro/resources/content/images/products/icon_bestbuy.gif";
            else if (theItem.isRecommended)
                iconImageSrc = "/cro/resources/content/images/products/icon_recommended.gif";
            else
                iconImageSrc = "";
            CUCompare.printMagnifyPopup(theItem.name, magnifyPopupID, theItemImageMagnifyDiv, theItem["small image"], theItem["overview url"], iconImageSrc);

            var theItemImageIconsDiv = document.createElement("div");
            theItemImageIconsDiv.setAttribute("className", "icons") || theItemImageIconsDiv.setAttribute("class", "icons");
            if (theItem["Test Status"] == 'Tested') {
                if (theItem.subcategory && theItem.subcategory.toUpperCase().indexOf("DON'T BUY:") >= 0) {
                    var theItemBestBuyDiv = document.createElement("div");
                    theItemBestBuyDiv.setAttribute("className", "best-buy") || theItemBestBuyDiv.setAttribute("class", "best-buy");
                    var theSubcategoryText = theItem.subcategory;
                    theSubcategoryText = '<b>Don&rsquo;t Buy: </b>' + theSubcategoryText.substring(theSubcategoryText.indexOf(":") + 1, theSubcategoryText.length);
                    theItemBestBuyDiv.innerHTML = '<img src = "/cro/resources/content/images/products/icon_dontbuy.gif" style="cursor:pointer;cursor:hand;" alt = "do not buy" width="13" height="13" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSubcategoryText + '\',event, 0);">';
                    theItemImageIconsDiv.appendChild(theItemBestBuyDiv);
                }
                else if ((theItem.isRecommended) || (theItem.isBestBuy) || theItem.isDontBuy == "1" || theItem.isDontBuy == "2") {
                    var theItemBestBuyDiv = document.createElement("div");
                    theItemBestBuyDiv.setAttribute("className", "best-buy") || theItemBestBuyDiv.setAttribute("class", "best-buy");

                    if (theItem.isDontBuy == "1" || theItem.isDontBuy == "2") {
                        theItemBestBuyDiv.innerHTML = '<img src = "/cro/resources/content/images/products/icon_dontbuy.gif" alt = "bestbuy" width="13" height="13">';
                    } else if (theItem.isBestBuy) {
                        theItemBestBuyDiv.innerHTML = '<img src = "/cro/resources/content/images/products/icon_bestbuy.gif" alt = "bestbuy" width="13" height="13">';
                    } else {
                        theItemBestBuyDiv.innerHTML = '<img src = "/cro/resources/content/images/products/icon_recommended.gif" alt = "recommended" width="13" height="13">';
                    }
                    theItemImageIconsDiv.appendChild(theItemBestBuyDiv);
                }
            }

            theItemImageIconsDiv.appendChild(theItemImageMagnifyDiv);
            var theItemImageMainDiv = document.createElement("div");
            theItemImageMainDiv.setAttribute("className", "subhead") || theItemImageMainDiv.setAttribute("class", "subhead");
            theItemImageMainDiv.appendChild(theItemImageIconsDiv);
            theItemImageMainDiv.appendChild(theItemImageLink);
            theItemImageNode.appendChild(theItemImageMainDiv);
        }


        var theItemNameMainDiv = document.createElement("div");
        theItemNameMainDiv.setAttribute("className", "model-item") || theItemNameMainDiv.setAttribute("class", "model-item");
        var theItemNameLink = document.createElement("a");
        theItemNameLink.setAttribute("href", theItem["overview url"]);
        var theItemNameSpan = document.createElement("span");
        theItemNameSpan.setAttribute("className", "model-name") || theItemNameSpan.setAttribute("class", "model-name");
        var brandName = theItem.brand;
        brandName = brandName.replace(/&amp;/g, "\&");
        brandName = brandName.replace(/&quot;/g, "\"");
        var modelName = theItem.model;
        modelName = modelName.replace(/&amp;/g, "\&");
        modelName = modelName.replace(/&quot;/g, "\"");

        if (brandName != '') {
            var theItemName = document.createTextNode(brandName + " ");
            theItemNameSpan.appendChild(theItemName);
            theItemNameLink.appendChild(theItemNameSpan);
            theItemNameLink.appendChild(document.createTextNode(modelName + " "));
        } else {
            var theItemName = document.createTextNode(modelName + " ");
            theItemNameSpan.appendChild(theItemName);
            theItemNameLink.appendChild(theItemNameSpan);
        }


        theItemNameMainDiv.appendChild(theItemNameLink);

        var theRemoveNode = document.createElement("a");
        var theRemoveNodeImage = document.createElement("img");
        theRemoveNodeImage.setAttribute("src", "/cro/resources/content/cars/images/compare/b_cars_compare_remove.gif");
        theRemoveNode.setAttribute("href", "javascript:CUCompare.remove(\'" + theItem.id + "\');");

        theItemNameNode.appendChild(theItemNameMainDiv);
        theRemoveNode.appendChild(theRemoveNodeImage);
        theItemRemoveNode.appendChild(theRemoveNode);

        theCompareHeadRemoveRow.appendChild(theItemRemoveNode);
        theCompareHeadImageRow.appendChild(theItemImageNode);
        theCompareHeadNameRow.appendChild(theItemNameNode);

        var theRatingsName = document.createElement("td");
        var modelNamePopupID = theItem.id + 'modelNamePopupID';

        theRatingsName.innerHTML = '<div class="model-item"><a href="' + theItem["overview url"] + '"><span style="cursor:hand" class="model-name" onmouseout="closeToggleInfoPopUpBox(event)" onmouseover="openToggleInfoPopUpBox(this,\'' + modelNamePopupID + '\',event, 0);">' + CUCompare.trimText(theItem.name.replace(/&amp;/g, "\&")) + '</span></a></div>';
        //theRatingsName.innerHTML = "<div class='model-item'><a href='" + theItem["overview url"] + "'><span style='cursor:hand' class='model-name' onmouseout=\"closeToggleInfoPopUpBox(event)\" onmouseover=\"openToggleInfoPopUpBox(this,'" + modelNamePopupID + "',event, 20);\">" + CUCompare.trimText(theItem.name.replace(/&amp;/g,"\&")) + "</span></a></div>";
        CUCompare.printModelPopup(theItem.brand, theItem.model, modelNamePopupID, theRatingsName, theItem["overview url"]);

        if (CUCompareCore.itsSpecAttributes.length > 0) {
            var theSpecsName = document.createElement("td");
            var modelNameSpecsPopupID = theItem.id + 'modelNameSpecsPopupID';

            theSpecsName.innerHTML = '<div class="model-item"><a href="' + theItem["overview url"] + '"><span class = "model-name" onmouseout = "closeToggleInfoPopUpBox(event)" onmouseover = "openToggleInfoPopUpBox(this,\'' + modelNameSpecsPopupID + '\',event, 0);">' + CUCompare.trimText(theItem.name.replace(/&amp;/g, "\&")) + '</span></a></div>';
            CUCompare.printModelPopup(theItem.brand, theItem.model, modelNameSpecsPopupID, theSpecsName, theItem["overview url"]);
        }

        var theReviewsName = document.createElement("td");
        var modelNameReviewsPopupID = theItem.id + 'modelNameReviewsPopupID';

        theReviewsName.innerHTML = '<div class="model-item"><a href="' + theItem["overview url"] + '"><span class = "model-name" onmouseout = "closeToggleInfoPopUpBox(event)" onmouseover = "openToggleInfoPopUpBox(this,\'' + modelNameReviewsPopupID + '\',event, 0);">' + CUCompare.trimText(theItem.name.replace(/&amp;/g, "\&")) + '</span></a></div>';
        CUCompare.printModelPopup(theItem.brand, theItem.model, modelNameReviewsPopupID, theReviewsName, theItem["overview url"]);

        theRatingsHeaderRow.appendChild(theRatingsName);
        if (CUCompareCore.itsSpecAttributes.length > 0)
            theSpecsHeaderRow.appendChild(theSpecsName);
        theReviewsHeaderRow.appendChild(theReviewsName);
    }

    if (CUCompareCore.itsCompareData.length < CUCompareBasket.MAX_ITEMS) {
        for (i = CUCompareCore.itsCompareData.length; (i < CUCompareBasket.MAX_ITEMS); i++) {
            var theItemBlankRemoveNode = document.createElement("th");
            theItemBlankRemoveNode.setAttribute("className", "model-bar-blank") || theItemBlankRemoveNode.setAttribute("class", "model-bar-blank");
            theItemBlankRemoveNode.appendChild(document.createTextNode("\u00a0"));
            var theItemBlankImageNode = document.createElement("th");
            theItemBlankImageNode.setAttribute("className", "blank") || theItemBlankImageNode.setAttribute("class", "blank");
            var theItemBlankImageSubNode = document.createElement("div");
            theItemBlankImageSubNode.setAttribute("className", "subhead") || theItemBlankImageSubNode.setAttribute("class", "subhead");
            var theItemBlankImage = document.createElement("img");
            var theImageSrcSet = 0;
            if (CUCompareCore.itsCompareData.length > 0) {
                var theFirstItem = CUCompareCore.itsCompareData[0];
                var theResponse = CUCompare.getURL('/cro/resources/content/products/images/compare/' + theFirstItem["group id"] + '-small.png');
                var statusCode = theResponse.status;
                if (statusCode == 200) {
                    if (theResponse.responseText.indexOf("Page Not Found") == -1) {
                        theItemBlankImage.setAttribute("src", "/cro/resources/content/products/images/compare/" + theFirstItem["group id"] + "-small.png");
                        theImageSrcSet = 1;
                    }
                }
            }
            if (theImageSrcSet == 0) {
                if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
                    theItemBlankImage.setAttribute("src", "/cro/resources/images/product-selector/1x1_blank.gif");
                    theItemBlankImage.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/cro/resources/content/products/images/compare/blob_small.png', sizingMethod='image');"; // MODCOMP-149
                    theItemBlankImage.style.marginTop = "12px";
                    theItemBlankImage.style.marginLeft = "20px";
                }
                else {
                    theItemBlankImage.setAttribute("src", "/cro/resources/content/products/images/compare/blob_small.png");
                }
            }
            theItemBlankImage.setAttribute("className", "model-image") || theItemBlankImage.setAttribute("class", "model-image");
            theItemBlankImage.setAttribute("width", "95");
            theItemBlankImage.setAttribute("height", "69");
            theItemBlankImageNode.appendChild(theItemBlankImageSubNode);
            theItemBlankImageSubNode.appendChild(theItemBlankImage);
            var theItemBlankNameNode = document.createElement("th");
            theItemBlankNameNode.appendChild(document.createTextNode("\u00a0"));

            theCompareHeadRemoveRow.appendChild(theItemBlankRemoveNode);
            theCompareHeadImageRow.appendChild(theItemBlankImageNode);
            theCompareHeadNameRow.appendChild(theItemBlankNameNode);

            var theBlankRatingsName = document.createElement("td");
            theBlankRatingsName.appendChild(document.createTextNode("\u00a0"));
            var theBlankSpecsName = document.createElement("td");
            theBlankSpecsName.appendChild(document.createTextNode("\u00a0"));
            var theBlankReviewsName = document.createElement("td");
            theBlankReviewsName.appendChild(document.createTextNode("\u00a0"));

            theRatingsHeaderRow.appendChild(theBlankRatingsName);
            if (CUCompareCore.itsSpecAttributes.length > 0)
                theSpecsHeaderRow.appendChild(theBlankSpecsName);
            theReviewsHeaderRow.appendChild(theBlankReviewsName);
        }
    }

    theRatingsHead.appendChild(theRatingsHeaderRow);
    if (CUCompareCore.itsSpecAttributes.length > 0)
        theSpecsHead.appendChild(theSpecsHeaderRow);
    theReviewsHead.appendChild(theReviewsHeaderRow);

    var theValue;

    for (var theKey = 0; theKey < CUCompareCore.itsSummaryAttributes.length; theKey++) {
        theValue = CUCompareCore.itsSummaryAttributes[theKey];
        var theSummaryRow = document.createElement("tr");
        var theProperty = document.createElement("td");
        theProperty.setAttribute("className", "data-cell") || theProperty.setAttribute("class", "data-cell");

        var thePropertyText = document.createTextNode(theValue);
        var theSummaryIconImage = document.createElement("img");
        theSummaryIconImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_info.gif");
        theSummaryIconImage.setAttribute("className", "info") || theSummaryIconImage.setAttribute("class", "info");
        theSummaryIconImage.setAttribute("width", "10");
        theSummaryIconImage.setAttribute("height", "10");
        theProperty.appendChild(theSummaryIconImage);
        if (theValue == "Product Type") {
            if (CUCompareCore.itsCompareData.length > 0) {
                var theFirstItem = CUCompareCore.itsCompareData[0];

                if (theFirstItem.isProductType) {
                    var typePopupID = 'typeInfoPopup';
                    CUCompare.printGlossaryPopup("Product Type", typePopupID, theProperty, "Product type indicates the model's category classification. For example, a television might be classified as LCD, plasma, front projector, etc.");
                    CUCompareEvent.addEvent(theSummaryIconImage, "mouseover", function () {
                        openToggleInfoPopUpBox(this, typePopupID, 'onmouseover', 0);
                    });
                    CUCompareEvent.addEvent(theSummaryIconImage, "mouseout", function () {
                        closeToggleInfoPopUpBox('onmouseout');
                    });
                }
                else
                    continue;
            }

        }

        /*else if (theValue == "Test Status")
         {
         var testPopupID = 'testInfoPopup';
         CUCompare.printGlossaryPopup("Test Status", testPopupID, theProperty, "Test status indicates whether the item has been tested by our experts; is currently in our test labs; or has not been tested.");
         CUCompareEvent.addEvent(theSummaryIconImage, "mouseover", function() {openToggleInfoPopUpBox(this,testPopupID,'onmouseover', 0);});
         CUCompareEvent.addEvent(theSummaryIconImage, "mouseout", function() {closeToggleInfoPopUpBox('onmouseout');});
         }*/
        else if (theValue == "Brand Reliability") {
            var brandPopupID = 'brandInfoPopup';
            CUCompare.printGlossaryPopup("Brand Reliability", brandPopupID, theProperty, "Brand reliability displays overall brand repair rates for models purchased over the past few years.");
            CUCompareEvent.addEvent(theSummaryIconImage, "mouseover", function () {
                openToggleInfoPopUpBox(this, brandPopupID, 'onmouseover', 0);
            });
            CUCompareEvent.addEvent(theSummaryIconImage, "mouseout", function () {
                closeToggleInfoPopUpBox('onmouseout');
            });
        }
        else if (theValue == "Price & Shop") {
            var pricePopupID = 'priceInfoPopup';
            CUCompare.printGlossaryPopup("Price & Shop", pricePopupID, theProperty, "Price & Shop lists an approximate retail price or price range. Clicking the \"Price & Shop\" button will show you product pricing and availability information from a number of online retailers. If you wish, you can purchase the item from one of those vendors. Please note: The Price & Shop program may not have sellers available for certain models, though the model may still be available at retail outlets or online.");
            CUCompareEvent.addEvent(theSummaryIconImage, "mouseover", function () {
                openToggleInfoPopUpBox(this, pricePopupID, 'onmouseover', 0);
            });
            CUCompareEvent.addEvent(theSummaryIconImage, "mouseout", function () {
                closeToggleInfoPopUpBox('onmouseout');
            });
        }
        else {
        }

        theProperty.appendChild(thePropertyText);
        theSummaryRow.appendChild(theProperty);
        for (i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
            var theItem = CUCompareCore.itsCompareData[i];
            var theItemColumn = document.createElement("td");

            var thePriceButtonDiv = document.createElement("div");
            thePriceButtonDiv.setAttribute("className", "price-shop-button") || thePriceButtonDiv.setAttribute("class", "price-shop-button");
            if (theValue == "Brand Reliability") {
                if (theItem["reliability url"]) {
                    var theReliabilityLink = document.createElement("a");
                    theReliabilityLink.setAttribute("href", theItem["reliability url"]);
                    theReliabilityLink.appendChild(document.createTextNode("View Brand Reliability"));
                    theItemColumn.appendChild(theReliabilityLink);
                }
                else {
                    theItemColumn.appendChild(document.createTextNode("-"));
                }
            }

            else if (theValue == "Price & Shop") {
                var theDefaultPrice = theItem["price"];
                var theMasterID = theItem["master id"];
                var thePageID = theItem["page id"];
                var thePgId = theItem["pgid"];

                var theLiveData = CUCompare.getLiveData(CUCompare.getLiveDataURL(theMasterID, theDefaultPrice, thePageID, thePgId));
                var theSellerCount = theLiveData.sellerCount;
                var theMinPrice = theLiveData.minPrice;
                var theMaxPrice = theLiveData.maxPrice;
                var theReviewCount = theLiveData.reviewCount;
                var theReviewAverage = theLiveData.reviewAverage;
                theItem.reviewCount = theReviewCount;
                theItem.reviewAverage = theReviewAverage;
                var thePriceText;

                if (theMasterID > 0) {
                    if (theMinPrice != theMaxPrice) {
                        thePriceText = document.createTextNode(theMinPrice + " - " + theMaxPrice);
                        theItemColumn.appendChild(thePriceText);
                    }
                    else {
                        thePriceText = document.createTextNode(theMinPrice);
                        theItemColumn.appendChild(thePriceText);
                    }
                }
                else {
                    if (theDefaultPrice != null) {
                        thePriceText = document.createTextNode(theDefaultPrice);
                        theItemColumn.appendChild(thePriceText);
                    }
                    else {
                        thePriceText = document.createTextNode("NA");
                        theItemColumn.appendChild(thePriceText);
                    }

                }

                if (theItem.isDontBuy != "1" && theItem.isDontBuy != "2") {
                    if (theItem["shop url"]) {
                        thePriceButtonDiv.innerHTML = '<a href=\"' + theItem["shop url"] + '\"><img height="18" width="86" src="/cro/resources/content/products/compare/b_priceandshop_sm.gif" alt="price and shop" /></a>';
                        theItemColumn.appendChild(thePriceButtonDiv);
                    }
                    else {
                        theItemColumn.appendChild(document.createElement("br"));
                        thePriceButtonDiv.appendChild(document.createTextNode("No sellers available"));
                        theItemColumn.appendChild(thePriceButtonDiv);
                    }
                }

            }
            else {
                if (theItem[theValue]) {
                    if (theItem[theValue] == "In test")
                        theItemColumn.appendChild(document.createTextNode("In testing"));
                    else if (theItem[theValue] == "Similar")
                        theItemColumn.appendChild(document.createTextNode("Not Tested"));
                    else
                        theItemColumn.appendChild(document.createTextNode(theItem[theValue]));
                }
                else {
                    theItemColumn.appendChild(document.createTextNode("\u00a0"));
                }
            }


            theSummaryRow.appendChild(theItemColumn);
        }

        if (CUCompareCore.itsCompareData.length < CUCompareBasket.MAX_ITEMS) {
            for (i = CUCompareCore.itsCompareData.length; (i < CUCompareBasket.MAX_ITEMS); i++) {
                var theItemColumn = document.createElement("td");
                theItemColumn.appendChild(document.createTextNode("\u00a0"));
                theSummaryRow.appendChild(theItemColumn);
            }
        }

        theSummaryBody.appendChild(theSummaryRow);
    }


    var theLegendRow = document.createElement("tr");
    theLegendRow.setAttribute("className", "compare-button") || theLegendRow.setAttribute("class", "compare-button");
    var theLegendCell = document.createElement("td");
    theLegendCell.colSpan = "6" || theLegendCell.setAttribute("colspan", "6");
    if (CUCompareCore.itsCompareProductTypes.length > 1) {
        theLegendCell.innerHTML = '<div class="learn">Important note about comparing ratings across product types. <a href="javascript:void(0);" onclick="showLearnMorePopup(event);">Learn more</a></div>';
    }
    var theRatingsCompareKeyDiv = document.createElement("div");
    theRatingsCompareKeyDiv.setAttribute("className", "legend") || theRatingsCompareKeyDiv.setAttribute("class", "legend");
    theRatingsCompareKeyDiv.innerHTML = '<img src="/cro/resources/content/products/compare/key_product_blobs_only.gif" width="185" height="30"/>';

    theLegendCell.appendChild(theRatingsCompareKeyDiv);
    theLegendRow.appendChild(theLegendCell);
    theRatingsBody.appendChild(theLegendRow);

    //var theAttribute;
    var theAttributeValue;
    var theRatingPopupID;
    var theAttributeID;
    var theAttributeName;
    var theAttributeGlossaryText;
    var theRatingsIconImage;
    for (var theAttribute = 0; theAttribute < CUCompareCore.itsRatingAttributes.length; theAttribute++) {
        theAttributeValue = CUCompareCore.itsRatingAttributes[theAttribute];
        var theAttributeRow = document.createElement("tr");
        var theProperty = document.createElement("td");
        theProperty.setAttribute("className", "data-cell") || theProperty.setAttribute("class", "data-cell");
        theRatingsIconImage = document.createElement("img");
        theRatingsIconImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_info.gif");
        theRatingsIconImage.setAttribute("className", "info") || theRatingsIconImage.setAttribute("class", "info");
        theRatingsIconImage.setAttribute("id", theAttributeValue.ID);
        theRatingsIconImage.setAttribute("width", "10");
        theRatingsIconImage.setAttribute("height", "10");
        theProperty.appendChild(theRatingsIconImage);

        theAttributeGlossaryText = CUCompare.getGlossaryText(theAttributeValue.name, 'ratings');

        if (typeof(theAttributeGlossaryText) == 'string') {
            theAttributeGlossaryText = theAttributeGlossaryText.replace(/&quot;/g, "\"");
            theAttributeGlossaryText = theAttributeGlossaryText.replace(/&amp;/g, "\&");
            theAttributeGlossaryText = theAttributeGlossaryText.replace(/&deg;/g, " degrees ");
            theAttributeGlossaryText = theAttributeGlossaryText.replace(/&lt;/g, "<");
            theAttributeGlossaryText = theAttributeGlossaryText.replace(/&gt;/g, ">");
        }
        theAttributeName = theAttributeValue.name;
        if (theAttributeName == "Overall score")
            theAttributeName = theAttributeName + ' (Out of 100)';
        theAttributeID = theAttributeValue.ID;
        theRatingPopupID = theAttributeID + 'InfoPopup';

        CUCompare.printGlossaryPopup(theAttributeName, theRatingPopupID, theProperty, theAttributeGlossaryText);
        CUCompareEvent.addEvent(theRatingsIconImage, "mouseover", function (event) {
            var event = window.event || event;
            openToggleInfoPopUpBox(this, ((event.target) ? event.target : event.srcElement).id + 'InfoPopup', event, 0);
        });
//	addEventHandler(theRatingsIconImage,"mouseover",function(event, theRatingspopupId) {openToggleInfoPopUpBox(this,theRatingspopupId,event, 0);});
        CUCompareEvent.addEvent(theRatingsIconImage, "mouseout", function (event) {
            closeToggleInfoPopUpBox(event);
        });

        var thePropertyText = document.createTextNode(theAttributeName);

        theProperty.appendChild(thePropertyText);
        theAttributeRow.appendChild(theProperty);
        for (i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
            var theItem = CUCompareCore.itsCompareData[i];
            var theItemColumn = document.createElement("td");
            theItemColumn.setAttribute("className", "item-score") || theItemColumn.setAttribute("class", "item-score");
            if (theItem.ratings[theAttributeValue.name]) {
                var theData = theItem.ratings[theAttributeValue.name];
                theItemColumn.setAttribute("id", theItem.id + theAttributeValue.ID + 'BlobPopup');
                if (theAttributeValue.name == "Overall score") {
                    theItemBar = document.createElement("div");
                    theItemBar.setAttribute("className", "overbar") || theItemBar.setAttribute("class", "overbar");
                    if (theData.value && theData.value > 0) {
                        theItemSpan = document.createElement("span");
                        theItemSpan.appendChild(document.createTextNode(theData.value));
                        theItemBar.appendChild(theItemSpan);
                        if (theData.value > 0 && theData.value <= 20)
                            theItemBar.appendChild(document.createTextNode(" Poor"));
                        if (theData.value > 20 && theData.value <= 40)
                            theItemBar.appendChild(document.createTextNode(" Fair"));
                        if (theData.value > 40 && theData.value <= 60)
                            theItemBar.appendChild(document.createTextNode(" Good"));
                        if (theData.value > 60 && theData.value <= 80)
                            theItemBar.appendChild(document.createTextNode(" Very Good"));
                        if (theData.value > 80)
                            theItemBar.appendChild(document.createTextNode(" Excellent"));

                    }
                    else if (theData.value == 0) {
                        theItemBar.appendChild(document.createTextNode("\u00a0"));
                    }
                    else {
                        if (theItem["Test Status"] == "In test")
                            theItemBar.appendChild(document.createTextNode("In testing"));
                        else
                            theItemBar.appendChild(document.createTextNode("Not Tested"));
                    }
                    theItemColumn.appendChild(theItemBar);
                }
                else {
                    if (theData.type == "blob") {
                        if (theData.value) {
                            var theBlobValue = CUCompareCore.itsBlobNames[theData.value];
                            var theBlobAttributeName = theAttributeValue.name;
                            theItemColumn.innerHTML = '<img style="cursor:pointer;cursor:hand;" src="http://www.consumerreports.org/cro/resources/content/images/blobs/blob_' + theData.value + '.gif" width="11" height="11" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theBlobAttributeName + ' : ' + theBlobValue + '\',event, 0);">';
                        }
                        else {
                            theItemColumn.appendChild(document.createTextNode("-"));
                        }
                    }
                    else if (theData.type == "boolean") {
                        if (theData.value) {
                            var theBlobAttributeName = theAttributeValue.name;
                            theItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theBlobAttributeName + '\',event, 0);">Yes</div>';
                        }
                        else {
                            theItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theBlobAttributeName + '\',event, 0);">No</div>';
                        }
                    }
                    else {
                        if (theData.value && theData.value != 'NA' && theData.value != 'Data not available') {
                            var theBlobAttributeName = theAttributeValue.name;
                            var theDataValue = theData.value;
                            if (typeof(theDataValue) == 'string') {
                                theDataValue = theDataValue.replace(/&quot;/g, "\"");
                                theItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theBlobAttributeName + '\',event, 0);">' + theDataValue + '</div>';
                            }
                            else if (typeof(theDataValue) == 'number') {
                                if (theDataValue < 0)
                                    theItemColumn.appendChild(document.createTextNode("-"));
                                else
                                    theItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theBlobAttributeName + '\',event, 0);">' + theDataValue + '</div>';
                            }
                            else
                                theItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theBlobAttributeName + '\',event, 0);">' + theDataValue + '</div>';
                        }
                        else {
                            theItemColumn.appendChild(document.createTextNode("-"));
                        }
                    }
                }
            }
            else
                theItemColumn.appendChild(document.createTextNode("-"));

            theAttributeRow.appendChild(theItemColumn);
        }

        if (CUCompareCore.itsCompareData.length < CUCompareBasket.MAX_ITEMS) {
            for (i = CUCompareCore.itsCompareData.length; (i < CUCompareBasket.MAX_ITEMS); i++) {
                var theItemColumn = document.createElement("td");
                theItemColumn.appendChild(document.createTextNode("\u00a0"));
                theAttributeRow.appendChild(theItemColumn);
            }
        }


        theRatingsBody.appendChild(theAttributeRow);
    }
    if (CUCompareCore.itsSpecAttributes.length > 0) {
        var theSpecPopupID;
        for (var theAttribute = 0; theAttribute < CUCompareCore.itsSpecAttributes.length; theAttribute++) {
            theAttributeValue = CUCompareCore.itsSpecAttributes[theAttribute];
            var theSpecsAttributeRow = document.createElement("tr");
            var theSpecsProperty = document.createElement("td");
            theSpecsProperty.setAttribute("className", "data-cell") || theSpecsProperty.setAttribute("class", "data-cell");
            var theSpecsIconImage = document.createElement("img");
            theSpecsIconImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_info.gif");
            theSpecsIconImage.setAttribute("className", "info") || theSpecsIconImage.setAttribute("class", "info");
            theSpecsIconImage.setAttribute("id", theAttributeValue.ID);
            theSpecsIconImage.setAttribute("width", "10");
            theSpecsIconImage.setAttribute("height", "10");
            theSpecsProperty.appendChild(theSpecsIconImage);

            theAttributeGlossaryText = CUCompare.getGlossaryText(theAttributeValue.name, 'specs');

            if (typeof(theAttributeGlossaryText) == 'string') {
                theAttributeGlossaryText = theAttributeGlossaryText.replace(/&quot;/g, "\"");
                theAttributeGlossaryText = theAttributeGlossaryText.replace(/&amp;/g, "\&");
            }
            theAttributeName = theAttributeValue.name;
            theAttributeID = theAttributeValue.ID;
            theSpecPopupID = theAttributeID + 'InfoPopup';

            CUCompare.printGlossaryPopup(theAttributeName, theSpecPopupID, theSpecsProperty, theAttributeGlossaryText);
            CUCompareEvent.addEvent(theSpecsIconImage, "mouseover", function (event) {
                var event = window.event || event;
                openToggleInfoPopUpBox(this, ((event.target) ? event.target : event.srcElement).id + 'InfoPopup', event, 0);
            });
            CUCompareEvent.addEvent(theSpecsIconImage, "mouseout", function (event) {
                closeToggleInfoPopUpBox(event);
            });


            var theSpecsPropertyText = document.createTextNode(theAttributeValue.name);

            theSpecsProperty.appendChild(theSpecsPropertyText);
            theSpecsAttributeRow.appendChild(theSpecsProperty);
            for (i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
                var theItem = CUCompareCore.itsCompareData[i];
                var theSpecsItemColumn = document.createElement("td");
                theSpecsItemColumn.setAttribute("className", "item-score") || theSpecsItemColumn.setAttribute("class", "item-score");
                if (theItem.specs[theAttributeValue.name]) {
                    var theData = theItem.specs[theAttributeValue.name];
                    var theSpecsBlobAttributeName = theAttributeValue.name;
                    if (theData.type == "blob") {
                        if (theData.value) {
                            var theSpecsBlobValue = CUCompareCore.itsBlobNames[theData.value];
                            theSpecsItemColumn.innerHTML = '<img style="cursor:pointer;cursor:hand;" src="http://www.consumerreports.org/cro/resources/content/images/blobs/blob_' + theData.value + '.gif" width="11" height="11" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSpecsBlobAttributeName + ' : ' + theSpecsBlobValue + '\',event, 0);">';
                        }
                        else {
                            theSpecsItemColumn.appendChild(document.createTextNode("-"));
                        }
                    }
                    else if (theData.type == "boolean") {
                        if (theData.value) {
                            theSpecsItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSpecsBlobAttributeName + '\',event, 0);">Yes</div>';
                        }
                        else {
                            theSpecsItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSpecsBlobAttributeName + '\',event, 0);">No</div>';
                        }
                    }
                    else {
                        if (theData.value && theData.value != 'NA' && theData.value != 'Data not available') {
                            var theDataValue = theData.value;
                            if (typeof(theDataValue) == 'string') {
                                theDataValue = theDataValue.replace(/&quot;/g, "\"");
                                theSpecsItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSpecsBlobAttributeName + '\',event, 0);">' + theDataValue + '</div>';
                            }
                            else if (typeof(theDataValue) == 'number') {
                                if (theDataValue < 0)
                                    theSpecsItemColumn.appendChild(document.createTextNode("-"));
                                else
                                    theSpecsItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSpecsBlobAttributeName + '\',event, 0);">' + theDataValue + '</div>';
                            }
                            else
                                theSpecsItemColumn.innerHTML = '<div style="cursor:pointer; cursor:hand;" onmouseout="closeToggleInfoPopUpBox(event);" onmouseover="toggleInfoPopUpBoxForBlobs(this,\'' + theSpecsBlobAttributeName + '\',event, 0);">' + theDataValue + '</div>';
                        }
                        else {
                            theSpecsItemColumn.appendChild(document.createTextNode("-"));
                        }
                    }
                }
                else
                    theSpecsItemColumn.appendChild(document.createTextNode("-"));
                theSpecsAttributeRow.appendChild(theSpecsItemColumn);
            }

            if (CUCompareCore.itsCompareData.length < CUCompareBasket.MAX_ITEMS) {
                for (i = CUCompareCore.itsCompareData.length; (i < CUCompareBasket.MAX_ITEMS); i++) {
                    var theSpecsItemColumn = document.createElement("td");
                    theSpecsItemColumn.setAttribute("className", "item-score") || theSpecsItemColumn.setAttribute("class", "item-score");
                    theSpecsItemColumn.appendChild(document.createTextNode("\u00a0"));
                    theSpecsAttributeRow.appendChild(theSpecsItemColumn);

                }
            }

            theSpecsBody.appendChild(theSpecsAttributeRow);
        }

        var theLastSpecsAttributeRow = document.createElement("tr");
        var theLastSpecsProperty = document.createElement("td");
        theLastSpecsProperty.setAttribute("className", "data-cell") || theLastSpecsProperty.setAttribute("class", "data-cell");
        theLastSpecsProperty.appendChild(document.createTextNode("\u00a0"));
        theLastSpecsAttributeRow.appendChild(theLastSpecsProperty);
        for (i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
            var theItem = CUCompareCore.itsCompareData[i];
            var hasSpecsURL = false;
            for (var theAttribute = 0; theAttribute < CUCompareCore.itsSpecAttributes.length; theAttribute++) {
                theAttributeValue = CUCompareCore.itsSpecAttributes[theAttribute];
                if (theItem.specs[theAttributeValue.name]) {
                    hasSpecsURL = true;
                    break;
                }
            }
            var theLastSpecsItemColumn = document.createElement("td");
            theLastSpecsItemColumn.setAttribute("className", "item-score") || theLastSpecsItemColumn.setAttribute("class", "item-score");
            if (hasSpecsURL) {
                theLastSpecsItemColumn.innerHTML = '<a href = \"' + theItem["features url"] + '\">View Full Features & Specs</a>';
            }
            else
                theLastSpecsItemColumn.innerHTML = '<div style="color:#778899;">View Full Features & Specs</div>';

            theLastSpecsAttributeRow.appendChild(theLastSpecsItemColumn);
        }

        if (CUCompareCore.itsCompareData.length < CUCompareBasket.MAX_ITEMS) {
            for (i = CUCompareCore.itsCompareData.length; (i < CUCompareBasket.MAX_ITEMS); i++) {
                var theLastSpecsItemColumn = document.createElement("td");
                theLastSpecsItemColumn.setAttribute("className", "item-score") || theLastSpecsItemColumn.setAttribute("class", "item-score");
                theLastSpecsItemColumn.appendChild(document.createTextNode("\u00a0"));
                theLastSpecsAttributeRow.appendChild(theLastSpecsItemColumn);

            }
        }

        theSpecsBody.appendChild(theLastSpecsAttributeRow);
    }

    var theReviewsAttributeRow = document.createElement("tr");
    theReviewsAttributeRow.setAttribute("id", "user-reviews");
    var theReviewsProperty = document.createElement("td");
    theReviewsProperty.setAttribute("className", "data-cell") || theReviewsProperty.setAttribute("class", "data-cell");
    var theReviewsIconImage = document.createElement("img");
    theReviewsIconImage.setAttribute("src", "/cro/resources/content/cars/images/cars-premium/icon_info.gif");
    theReviewsIconImage.setAttribute("className", "info") || theReviewsIconImage.setAttribute("class", "info");
    theReviewsIconImage.setAttribute("width", "10");
    theReviewsIconImage.setAttribute("height", "10");
    theReviewsProperty.appendChild(theReviewsIconImage);
    var reviewPopupID = 'reviewInfoPopup';

    var theAttributeName = 'User Reviews';
    var theAttributeGlossaryText = "User reviews are written by people a lot like you. The authors have used the product and want to tell others about their experience. We expect that those sharing their opinions actually own the items they are reviewing but we can't verify that. There's a chance a manufacturer or retailer has submitted a review in order to promote a particular item, and that opinion may not be as honest as you would like. If you spot a review you find suspicious, please use the \"flag this review\" link provided as part of the review to report the problem.";
    CUCompare.printGlossaryPopup(theAttributeName, reviewPopupID, theReviewsProperty, theAttributeGlossaryText);
    CUCompareEvent.addEvent(theReviewsIconImage, "mouseover", function (event) {
        openToggleInfoPopUpBox(this, reviewPopupID, event, 0);
    });
    CUCompareEvent.addEvent(theReviewsIconImage, "mouseout", function () {
        closeToggleInfoPopUpBox('onmouseover');
    });

    var theReviewsPropertyText = document.createTextNode("User Reviews");

    theReviewsProperty.appendChild(theReviewsPropertyText);
    theReviewsAttributeRow.appendChild(theReviewsProperty);
    for (i = 0; (i < CUCompareCore.itsCompareData.length); i++) {
        var theItem = CUCompareCore.itsCompareData[i];
        var theReviewsItemColumn = document.createElement("td");
        theReviewsItemColumn.setAttribute("className", "item-score") || theReviewsItemColumn.setAttribute("class", "item-score");
        theReviewsItemColumn.style.paddingTop = '12px';
        theReviewsItemColumn.style.paddingBottom = '12px';
        var theReviewsStarRatingDiv = document.createElement("div");
        theReviewsStarRatingDiv.setAttribute("className", "stars-rating") || theReviewsStarRatingDiv.setAttribute("class", "stars-rating");
        var theReviewsStarRatingImage = document.createElement("img");
        theReviewsStarRatingImage.setAttribute("width", "83");
        theReviewsStarRatingImage.setAttribute("height", "195");
        if (theItem.reviewAverage) {
            var theMarginTop = Math.round(parseFloat(theItem.reviewAverage) * 36);
            theMarginTop = "-" + theMarginTop + "px";
            theReviewsStarRatingImage.style.marginTop = theMarginTop;
        }
        else {
            theReviewsStarRatingImage.style.marginTop = '-0px';
        }
        theReviewsStarRatingImage.setAttribute("src", "http://www.consumerreports.org/cro/resources/content/products/models/images/stars_small.gif");
        theReviewsStarRatingDiv.appendChild(theReviewsStarRatingImage);
        var theReviewsDiv = document.createElement("div");
        theReviewsDiv.setAttribute("className", "reviews") || theReviewsDiv.setAttribute("class", "reviews");
        if (theItem.reviewCount) {
            theReadReviewsLink = document.createElement("a");
            theReadReviewsLink.setAttribute("href", theItem["write review url"] + "#readReview");
            theReadReviewsLink.appendChild(document.createTextNode(theItem.reviewCount + " reviews"));
            theReviewsDiv.appendChild(document.createTextNode("(based on "));
            theReviewsDiv.appendChild(theReadReviewsLink);
            theReviewsDiv.appendChild(document.createTextNode(")"));
        }
        else {
            theReviewsDiv.appendChild(document.createTextNode("(0 Reviews)"));
        }

        theWriteReviewsLink = document.createElement("a");
        theWriteReviewsLink.setAttribute("href", theItem["write review url"]);
        theWriteReviewsLink.setAttribute("className", "write-link") || theWriteReviewsLink.setAttribute("class", "write-link");
        theWriteReviewsLink.appendChild(document.createTextNode("Write a review"));
        theReviewsDiv.appendChild(document.createElement("br"));
        theReviewsDiv.appendChild(document.createElement("br"));
        if (theItem.reviewCount) {
            theReviewsDiv.appendChild(document.createTextNode("Already own it?"));
        }
        else {
            theReviewsDiv.appendChild(document.createTextNode("Be the first to review this product. "));

        }
        theReviewsDiv.appendChild(theWriteReviewsLink);
        theReviewsItemColumn.appendChild(document.createTextNode("Average User Rating"));
        theReviewsItemColumn.appendChild(theReviewsStarRatingDiv);
        theReviewsItemColumn.appendChild(theReviewsDiv);
        theReviewsAttributeRow.appendChild(theReviewsItemColumn);
    }

    if (CUCompareCore.itsCompareData.length < CUCompareBasket.MAX_ITEMS) {
        for (i = CUCompareCore.itsCompareData.length; (i < CUCompareBasket.MAX_ITEMS); i++) {

            var theReviewsItemColumn = document.createElement("td");
            theReviewsItemColumn.appendChild(document.createTextNode("\u00a0"));
            theReviewsAttributeRow.appendChild(theReviewsItemColumn);
        }
    }

    theReviewsBody.appendChild(theReviewsAttributeRow);


    if (CUCompareCore.itsCompareData.length == 0) {
        theFirstItem = CUCompareCore.getLastKnownData();
        var theContentBody = document.getElementById("content-body");
        CUCompare.empty(theContentBody);
        theContentBody = document.getElementById("content-body");
        if (theContentBody != null) {
            theContentBody.style.height = '800px';
            //theContentBody.style.marginLeft = '20px';
            if (theFirstItem)
                theContentBody.innerHTML = '<div class="tools"> </div><div id="chartHead" class = "chart-header" width="920px">Your comparison chart is currently empty</div><div class="navigation-item" width="920px" style="padding-left:20px;"><a style="text-decoration:none; color:#176FCC;" href="' + theFirstItem.categoryurl + '"> View Ratings for ' + theFirstItem.supercategory + '</a><br><a style="text-decoration:none; color:#176FCC;" href="/cro/index.htm"> Visit our home page</a><br></div>';
            else
                theContentBody.innerHTML = '<div class="tools"> </div><div id="chartHead" class = "chart-header" width="920px">Your comparison chart is currently empty</div><div class="navigation-item" width="920px" style="padding-left:20px;"><a style="text-decoration:none; color:#176FCC;" href="/cro/index.htm"> Visit our home page</a><br></div>';
        }
    }


    var theCompareArray = CUCompareBasket.getCompareItems();
    var scProducts = "";
    if (theCompareArray.length > 0) {
        for (i = 0; (i < theCompareArray.length); i++) {
            var theItem = theCompareArray[i];
            scProducts += ";" + theItem.gid + "/" + theItem.id + ((i < theCompareArray.length - 1) ? "," : "");
        }
    }
    if (updateCompareEventToSC !== undefined) {
        updateCompareEventToSC(scProducts);
    }

};


