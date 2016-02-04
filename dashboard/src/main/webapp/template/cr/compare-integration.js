/**
 *  Declare the CUCompareIntegration object
 */
if (CUCompareIntegration == null) var CUCompareIntegration = {};

/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompareIntegration.empty = function (inNode) {
    while (inNode.firstChild) {
        inNode.removeChild(inNode.firstChild);
    }
};

/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompareIntegration.onLoad = function () {
    var theCheckboxes = document.getElementsByTagName("input");
    var theChosenBox = document.getElementsByName("compare-basket");
    if (typeof theChosenBox[0] == "undefined") return;
    if (typeof modelID != "undefined" && typeof theChosenBox != "undefined") {
        var theSupercategoryID = theChosenBox[0].getAttribute("groupid");
        var theHoldingArea = CUCompareBasket.getHoldingAreaItems(theSupercategoryID, true);
    }
    if (typeof modelID != "undefined" && theHoldingArea.length < 5) CUCompareIntegration.add(modelID);
    for (var i = 0; (i < theCheckboxes.length); i++) {
        if ((theCheckboxes[i].getAttribute("type") == "checkbox") && (theCheckboxes[i].getAttribute("name") == "compare")) {
            theCheckboxes[i].disabled = true;
            CUCompareEvent.addEvent(theCheckboxes[i], "click", function () {
                if (this.checked) {
                    CUCompareIntegration.add(this.value);
                } else {
                    CUCompareIntegration.remove(this.value);
                }
            });
        }
    }
    CUCompareIntegration.fillInChosenToCompare(false);
};

/**
 *  This method is used when the addEvent is not triggering for checkboxes under textarea tag.
 */
function onClickFromTextArea(checkBoxId) {
    var checkBoxElement = document.getElementById(checkBoxId);
    if (checkBoxElement.checked) {
        CUCompareIntegration.add(checkBoxElement.value);
    }
    else {
        CUCompareIntegration.remove(checkBoxElement.value);
    }
    CUCompareIntegration.fillInChosenToCompare(true);
}

CUCompareIntegration.goToModelPage = function (inGroupID, inModelID) {
    var theHttpRequest;

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
    theHttpRequest.open("GET", CUCompareIntegration.getDataURL(inGroupID, inModelID), false);
    theHttpRequest.send('');
    var theModel = eval("(" + theHttpRequest.responseText + ")");
    window.location = theModel["overview url"];
};

/**
 *  Should not be called independently, but just used by the compare code.
 */
CUCompareIntegration.getDataURL = function (inSupercategoryID, inModelID) {
    var theModelID = ("" + inModelID).toLowerCase(); // required because some model IDs contain letters ("D"/"d")
    return ("/cro/resources/content/products/scripts/comparedata/" + inSupercategoryID + "/" + theModelID + ".js");
};

/**
 *  Should not be called independently, but just used by the compare code.
 *  REQUIRES: compare-basket.js
 */
CUCompareIntegration.fillInChosenToCompare = function (inAddRemoveToken) {
    var theCheckboxes = document.getElementsByTagName("input");
    for (var i = 0; (i < theCheckboxes.length); i++) {
        if ((theCheckboxes[i].getAttribute("type") == "checkbox") && (theCheckboxes[i].getAttribute("name") == "compare")) {
            theCheckboxes[i].disabled = true;
        }
    }
    var theChosenBox = document.getElementsByName("compare-basket");
    for (var j = 0; (j < theChosenBox.length); j++) {
        var theSupercategoryID = theChosenBox[j].getAttribute("groupid");
        var theHoldingAreaArray;

        if (inAddRemoveToken === undefined) {
            theHoldingAreaArray = CUCompareBasket.getHoldingAreaItems(theSupercategoryID, false);
        }
        else if (!inAddRemoveToken) {
            theHoldingAreaArray = CUCompareBasket.getHoldingAreaItems(theSupercategoryID, false);
        }
        else {
            theHoldingAreaArray = CUCompareBasket.getHoldingAreaItems(theSupercategoryID, true);
        }

        CUCompareIntegration.empty(theChosenBox[j]);
        for (i = 0; (i < theCheckboxes.length); i++) {
            if ((theCheckboxes[i].getAttribute("type") == "checkbox") && (theCheckboxes[i].getAttribute("name") == "compare")) {
                theCheckboxes[i].checked = false;
            }
        }
        if (typeof modelID != "undefined") {
            if (theHoldingAreaArray.length == 0) {
                theChosenBox[j].style.display = "none";
            } else {
                theChosenBox[j].style.display = "block";
            }
        }
        if (theHoldingAreaArray.length > 0) {
            if (typeof modelID != "undefined") {
                var theDivCover = document.createElement("div");
                theDivCover.setAttribute("class", "chosen-models");
                var theTextNode = document.createTextNode(" You have chosen to compare: ");
                var theDivTextNode = document.createElement("div");
                theDivTextNode.appendChild(theTextNode);
                theChosenBox[j].appendChild(theDivTextNode);
                for (i = 0; (i < theHoldingAreaArray.length); i++) {
                    var theCheckboxRow = document.getElementById(theHoldingAreaArray[i].id);
                    if (theCheckboxRow) {
                        var theCheckbox = theCheckboxRow.getElementsByTagName("input");
                        if (theCheckbox) {
                            theCheckbox[0].checked = true;
                        }
                    }
                    var theDiv = document.createElement("div");
                    theDiv.setAttribute("class", "chosenBlock");
                    theDiv.style.position = "relative";
                    var theXA = document.createElement("a");
                    theXA.setAttribute("href", "javascript:CUCompareIntegration.remove('" + theHoldingAreaArray[i].id + "')");
                    var theimg = document.createElement("img");
                    theimg.setAttribute("src", "http://www.consumerreports.org/cro/resources/content/images/products/selector_killx.gif");
                    theXA.appendChild(theimg);
                    theXADiv = document.createElement("div");
                    theXADiv.setAttribute("class", "chosenX");
                    theXADiv.style.position = "absolute";
                    theXADiv.appendChild(theXA);
                    theDiv.appendChild(theXADiv);

                    var theLink = document.createElement("a");
                    theLink.setAttribute("href", "javascript:CUCompareIntegration.goToModelPage(" + theHoldingAreaArray[i].gid + ",'" + theHoldingAreaArray[i].id + "')");
                    if (theHoldingAreaArray[i].id == modelID) {
                        theLink = document.createElement("p");
                        theLink.style.margin = 0;
                        theLink.style.padding = 0;
                    }

                    var spanTag = document.createElement("span");
                    var model = "";
                    if (theHoldingAreaArray[i].b != '' && theHoldingAreaArray[i].b != 'NA') {
                        model = "<b>" + theHoldingAreaArray[i].b + "</b> " + theHoldingAreaArray[i].n;
                    } else {
                        model = "<b>" + theHoldingAreaArray[i].n + "</b> ";
                    }
                    spanTag.innerHTML = model;
                    theLink.appendChild(spanTag);

                    theLinkDiv = document.createElement("div");
                    theLinkDiv.setAttribute("class", "chosenLink");
                    theLinkDiv.style.paddingLeft = "21px";
                    theLinkDiv.appendChild(theLink);
                    theDiv.appendChild(theLinkDiv);
                    theChosenBox[j].appendChild(theDiv);
                }

            } else {
                var theTextRow = document.createElement("TR");
                var theDottedColumn = document.createElement("TH");
                theDottedColumn.setAttribute("id", "dotted");
                theDottedColumn.setAttribute("class", "dotted");
                theDottedColumn.setAttribute("align", "center");
                theDottedColumn.setAttribute("width", "28px");
                var theTextColumn = document.createElement("TH");
                var theTextNode = document.createTextNode(" You have chosen to compare: ");
                theTextColumn.setAttribute("id", "choosen-model");
                theTextColumn.setAttribute("class", "choosen-model");
                theTextColumn.style.backgroundImage = 'none';
                theTextColumn.setAttribute("align", "left");
                theTextColumn.setAttribute("width", "500px");
                theTextColumn.appendChild(theTextNode);
                theTextRow.appendChild(theDottedColumn);
                theTextRow.appendChild(theTextColumn);
                theChosenBox[j].appendChild(theTextRow);

                for (i = 0; (i < theHoldingAreaArray.length); i++) {
                    var theCheckboxRow = document.getElementById(theHoldingAreaArray[i].id);
                    if (theCheckboxRow) {
                        var theCheckbox = theCheckboxRow.getElementsByTagName("input");
                        if (theCheckbox) {
                            theCheckbox[0].checked = true;
                        }
                    }
                    var theTR = document.createElement("TR");
                    var theTH1 = document.createElement("TH");   //FIRST TH
                    if (i == (theHoldingAreaArray.length - 1)) {
                        theTH1.setAttribute("id", "dotted-last");
                        theTH1.setAttribute("class", "dotted-last");
                    }
                    else {
                        theTH1.setAttribute("id", "dotted");
                        theTH1.setAttribute("class", "dotted");
                    }
                    theTH1.setAttribute("align", "center");
                    theTH1.setAttribute("width", "28px");
                    var theXA = document.createElement("a");
                    theXA.setAttribute("href", "javascript:CUCompareIntegration.remove('" + theHoldingAreaArray[i].id + "')");
                    var theimg = document.createElement("img");
                    theimg.setAttribute("src", "http://www.consumerreports.org/cro/resources/content/images/products/selector_killx.gif");
                    theimg.setAttribute("height", "9");
                    theimg.setAttribute("width", "9");
                    theimg.setAttribute("class", "dotted");
                    theXA.appendChild(theimg);
                    theTH1.appendChild(theXA);
                    var theTH2 = document.createElement("TH");   //SECOND TH
                    if (i == (theHoldingAreaArray.length - 1)) {
                        theTH2.setAttribute("id", "choosen-model-last");
                        theTH2.setAttribute("class", "choosen-model-last");
                    }
                    else {
                        theTH2.setAttribute("id", "choosen-model");
                        theTH2.setAttribute("class", "choosen-model");
                    }
                    theTH2.setAttribute("width", "500px");
                    theTH2.style.backgroundImage = 'none';
                    var theLink = document.createElement("a");
                    theLink.setAttribute("href", "javascript:CUCompareIntegration.goToModelPage(" + theHoldingAreaArray[i].gid + ",'" + theHoldingAreaArray[i].id + "')");
                    if (typeof modelID != "undefined" && theHoldingAreaArray[i].id == modelID)theLink = document.createElement("p");
                    var spanTag = document.createElement("span");
                    var model = "";
                    if (theHoldingAreaArray[i].b != '' && theHoldingAreaArray[i].b != 'NA') {
                        model = "<b>" + theHoldingAreaArray[i].b + "</b> " + theHoldingAreaArray[i].n;
                    } else {
                        model = "<b>" + theHoldingAreaArray[i].n + "</b> ";
                    }
                    spanTag.innerHTML = model;
                    theLink.appendChild(spanTag);
                    theTH2.appendChild(theLink);
                    theTR.appendChild(theTH1);    // <!--Adding to TR-->
                    theTR.appendChild(theTH2);
                    theChosenBox[j].appendChild(theTR);
                }

            }
        }
    }
    for (i = 0; (i < theCheckboxes.length); i++) {
        if ((theCheckboxes[i].getAttribute("type") == "checkbox") && (theCheckboxes[i].getAttribute("name") == "compare")) {
            theCheckboxes[i].disabled = false;
        }
    }

};

/**
 *  Call this function when on the Ratings page, Selector page, or Recommended page whenever a
 *  user unsets/clears a checkbox, or removes the item from the "You have chosen to compare"
 *  module on the page.
 *  REQUIRES: compare-basket.js
 *  INPUT: inModelID should be the CU Model ID of the model to be removed
 */
CUCompareIntegration.remove = function (inModelID) {
    CUCompareBasket.removeItemFromHoldingArea(inModelID);
    CUCompareIntegration.fillInChosenToCompare(true);
};

/**
 *  Call this function when on the Ratings page, Selector page, or Recommended page whenever a
 *  user sets a checkbox to checked.
 *  REQUIRES: compare-basket.js
 *  INPUT: inModelID should be the CU Model ID of the model to be added
 */
CUCompareIntegration.add = function (inModelID) {
    var theChosenBox = document.getElementsByName("compare-basket");
    var theSupercategoryID = theChosenBox[0].getAttribute("groupid");
    var theCheckboxRow = document.getElementById(inModelID);
    var theBrandName = theCheckboxRow.getAttribute("brandname");
    var theModelName = theCheckboxRow.getAttribute("modelname");
    var theSuccess = CUCompareBasket.addItemToHoldingArea(inModelID, theSupercategoryID, theBrandName, theModelName);
    if (!theSuccess) {
        if (theCheckboxRow) {
            var theCheckbox = theCheckboxRow.getElementsByTagName("input");
            if (theCheckbox) {
                theCheckbox[0].checked = false;
            }
        }
        createCustomAlert(theCheckboxRow, (typeof modelID == "undefined") ? "left" : "extraLeft", "A maximum of 5 models are supported for comparison.");
        CUCompareIntegration.fillInChosenToCompare(true);
    }
    else {
        CUCompareIntegration.fillInChosenToCompare(true);
    }
};

function calculateOffsetTop(field) {
    return calculateOffset(field, "offsetTop");
}
function calculateOffsetLeft(field) {
    return calculateOffset(field, "offsetLeft");
}
function calculateOffset(field, attr) {
    var offset = 0;
    while (field) {
        offset += field[attr];
        field = field.offsetParent;
    }
    return offset;
}

function createCustomAlert(element, position, txt) {
    if (document.getElementById("errorMsg")) return;
    var mObj = document.getElementsByTagName("body")[0].appendChild(document.createElement("div"));
    mObj.id = "errorMsg";

    // create the DIV that will be the alert
    var alertObj = mObj.appendChild(document.createElement("div"));

    // MSIE doesnt treat position:fixed correctly, so this compensates for positioning the alert
    if (document.all) alertObj.style.top = document.documentElement.scrollTop + "px";

    // center the alert box
    alertObj.style.left = (document.documentElement.scrollWidth - alertObj.offsetWidth) / 2 + "px";

    // create a paragraph element to contain the txt argument
    var msg = alertObj.appendChild(document.createElement("p"));
    msg.innerHTML = txt;
    if ((document.getElementById("errorMsg").style.visibility == 'visible')) {
        mObj.style.visibility = "hidden";
    }
    // create an anchor element to use as the confirmation button.
    var btn = alertObj.appendChild(document.createElement("a"));

    var btnimg = btn.appendChild(document.createElement("img"));

    btnimg.src = "http://www.consumerreports.org/cro/resources/content/cars/images/types/b_ok.gif";
    // set up the onclick event to remove the alert when the anchor is clicked
    btn.href = "#";
    // set up the onclick event to remove the alert when the anchor is clicked

    btn.onclick = function () {
        removeCustomAlert();
        return false;
    };
    var x = calculateOffsetLeft(element);
    var y = calculateOffsetTop(element);
    // make sure its as tall as it needs to be to overlay all the content on the page
    mObj.style.visibility = "visible";
    var box = document.getElementById('errorMsg');
    var boxHeight = box.clientHeight;
    var boxWidth = box.clientWidth;
    if (document.all) {
        if (position == 'left') {
            box.style.pixelLeft = x - boxWidth + 290;
        } else if (position == 'extraLeft') {
            box.style.pixelLeft = x - boxWidth + 165;
        } else {
            box.style.pixelLeft = x + 40;
        }
        box.style.pixelTop = (y - boxHeight + 20);
    } else {
        if (position == 'left') {
            box.style.left = (x - boxWidth + 290) + 'px';
        } else if (position == 'extraLeft') {
            box.style.left = (x - boxWidth + 165) + 'px';
        } else {
            box.style.left = (x + 40) + 'px';
        }
        box.style.top = (y - boxHeight + 10) + 'px';
    }

}

function removeCustomAlert() {
    document.getElementsByTagName("body")[0].removeChild(document.getElementById("errorMsg"));
}
/**
 *  Should not be called independently, but just used by the compare code.
 *  REQUIRES: compare-event.js
 */
CUCompareEvent.addEvent(window, "load", CUCompareIntegration.onLoad);
