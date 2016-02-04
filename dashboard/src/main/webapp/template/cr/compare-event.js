/**
 *  Declare the CUCompareEvent object
 */
if (CUCompareEvent == null) var CUCompareEvent = {};

/**
 *  Should not be called independently but just used by the compare code.
 *  See: http://www.dustindiaz.com/rock-solid-addevent/
 *  This was required to preserve the "this" keyword in the passed-in function in Internet Explorer
 */
CUCompareEvent.addEvent = function (inObject, inType, inFunction) {
    if (inObject.addEventListener) {
        inObject.addEventListener(inType, inFunction, false);
        EventCache.add(inObject, inType, inFunction);
    }
    else if (inObject.attachEvent) {
        inObject["e" + inType + inFunction] = inFunction;
        inObject[inType + inFunction] = function () {
            inObject["e" + inType + inFunction](window.event);
        };
        inObject.attachEvent("on" + inType, inObject[inType + inFunction]);
        EventCache.add(inObject, inType, inFunction);
    }
    else {
        inObject["on" + inType] = inObject["e" + inType + inFunction];
    }
};

/**
 *  Should not be called independently but just used by the compare code.
 *  See: http://www.dustindiaz.com/rock-solid-addevent/
 *  This was required to preserve the "this" keyword in the passed-in function in Internet Explorer
 */
var EventCache = function () {
    var theListEvents = [];
    return {
        theListEvents: theListEvents,
        add: function (node, sEventName, fHandler) {
            theListEvents.push(arguments);
        },
        flush: function () {
            var i, theItem;
            for (i = theListEvents.length - 1; i >= 0; i = i - 1) {
                theItem = theListEvents[i];
                if (theItem[0].removeEventListener) {
                    theItem[0].removeEventListener(theItem[1], theItem[2], theItem[3]);
                }
                ;
                if (theItem[1].substring(0, 2) != "on") {
                    theItem[1] = "on" + theItem[1];
                }
                ;
                if (theItem[0].detachEvent) {
                    theItem[0].detachEvent(theItem[1], theItem[2]);
                }
                ;
                theItem[0][theItem[1]] = null;
            }
            ;
        }
    };
}();

/**
 *  Should not be called independently but just used by the compare code.
 *  See: http://www.dustindiaz.com/rock-solid-addevent/
 *  This was required to preserve the "this" keyword in the passed-in function in Internet Explorer
 */
CUCompareEvent.addEvent(window, 'unload', EventCache.flush);

