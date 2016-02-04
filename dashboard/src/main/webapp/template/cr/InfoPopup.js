var currentPopup;

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
function toggleInfoPopUpBox(that, elementId, event, margin) {
    var box = document.getElementById(elementId);
    if (!popupContainer)loadPopupContainer();
    if (!currentPopup || currentPopup != box || (currentPopup == box && popupContainer.style.display == "none")) {
        showInfoPopUpBox(that, elementId, event, margin);
    }
}

function toggleInfoPopUpBoxForBlobs(that, text, event, margin) {
    if (!text || text == "")return;
    if (that.getElementsByTagName('img')[0])that = that.getElementsByTagName('img')[0];
    else if (that.getElementsByTagName('span'))that = that.getElementsByTagName('span')[0];

    var box = document.getElementById('blob-text');
    if (!box) {
        var box = document.createElement('div');
        document.body.appendChild(box);
    }
    box.style.display = 'none';
    box.innerHTML = '<div class="info-popup-blob"><dl><dd class="top">&nbsp;</dd><dd class="middle" id="repos-info-pop-text">' + text + '</dd><dd class="bottom">&nbsp;</dd></dl></div>';
    box.id = 'blob-text';
    if (!popupContainer)loadPopupContainer();
    if (!currentPopup || currentPopup != box || (currentPopup == box && popupContainer.style.display == "none")) {
        showInfoPopUpBox(that, box.id, event, margin);
    }
}

function showInfoPopUpBox(that, elementId, event, margin) {
    var box = document.getElementById(elementId);
    if (currentPopup && currentPopup != box)hideInfoPopUpBox(currentPopup.id);
    setDisplayPosition(that, box, event, margin);
    currentPopup = box;
}

var boxProp = new Array;
function getDisplayProperties(that, box, event) {
    boxProp['x'] = calculateOffsetLeft(that);
    boxProp['y'] = calculateOffsetTop(that);
    boxProp['boxHeight'] = box.clientHeight;
    boxProp['boxWidth'] = box.clientWidth;
    var screenW = screen.width;
    var screenH = screen.height;
    var yy = 0;
    var xx = 0;
    if (event.pageY) {
        boxProp['yy'] = event.pageY;
        boxProp['xx'] = event.pageX;
    } else {
        boxProp['yy'] = window.pageYOffset + event.clientY;
        boxProp['xx'] = window.pageXOffset + event.clientX;
    }
    if ((boxProp['x'] + boxProp['boxWidth'] + 20) >= screenW || (boxProp['xx'] + boxProp['boxWidth'] + 20) >= screenW) {
        boxProp['leftPositioned'] = false;
    } else {
        boxProp['leftPositioned'] = true;
    }
}

function setDisplayPosition(that, box, event, margin) {
    popupContainer.innerHTML = box.innerHTML;
    popupContainer.style.position = "absolute";
    popupContainer.style.display = "block";
    getDisplayProperties(that, popupContainer, event);
    box = popupContainer;
    if (!margin)margin = 0;
    if (box.getElementsByTagName("dd").length > 0) {
        if (boxProp['leftPositioned']) {
            box.getElementsByTagName("dd")[0].className = "top";
            box.getElementsByTagName("dd")[1].className = "middle";
            box.getElementsByTagName("dd")[2].className = "bottom";
        } else {
            box.getElementsByTagName("dd")[0].className = "top-right";
            box.getElementsByTagName("dd")[1].className = "middle-right";
            box.getElementsByTagName("dd")[2].className = "bottom-right";
        }
    }
    if (document.all) {
        box.style.pixelTop = boxProp['y'] - boxProp['boxHeight'] + margin;
        if (boxProp['leftPositioned']) {
            box.style.pixelLeft = boxProp['x'] - 10 - margin;
        } else {
            box.style.pixelLeft = boxProp['x'] - boxProp['boxWidth'] + 20 + margin;
        }
    } else {
        box.style.top = (boxProp['y'] - boxProp['boxHeight'] + margin) + 'px';
        if (boxProp['leftPositioned']) {
            box.style.left = (boxProp['x'] - 18 - margin) + 'px';
        } else {
            box.style.left = (boxProp['x'] - boxProp['boxWidth'] + 22 + margin) + 'px';
        }
    }
} // end showInfo2

function hideInfoPopUpBox(floatElementId) {
    var box = document.getElementById(floatElementId);
    box.style.display = "none";
    popupContainer.style.display = "none";
}

function closeContainer(e) {
    if (!e) var e = window.event;
    var reltg = (e.relatedTarget) ? e.relatedTarget : e.toElement;
    try {
        while (reltg.tagName != "BODY") {
            if (reltg.id == "popContainer") {
                return;
            }
            reltg = reltg.parentNode;
        }
    } catch (e) {
    }
    if (popupContainer)popupContainer.style.display = "none";
}

var popupContainer;
function loadPopupContainer() {
    popupContainer = document.createElement('div');
    document.body.appendChild(popupContainer);
    popupContainer.id = "popContainer";
    popupContainer.style.display = "none";
    popupContainer.style.position = "absolute";
    popupContainer.style.zIndex = 999;
    addEventHandler(popupContainer, "mouseout", closeContainer);
}

function openPage(page) {
    OpenWin = this.open(page, "CtrlWindow", "toolbar=yes,menubar=yes,location=no,scrollbars=yes,resizable=yes,width=420,height=400");
}
