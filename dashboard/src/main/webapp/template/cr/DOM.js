/**
 * A collection of DOM manipulation methods.
 *
 * Requires:
 *    Arrays.js
 */

Arrays.augmentArrayPrototype();

var DOM =
{
    buildElementGroups: function (inDocument, inGroupDefinitions, inPrefix, inSeparator, inDepth) {
        var theGroup;
        var theElement;
        var theIndex;

        if (inSeparator === undefined)
            inSeparator = ".";
        if (inPrefix === undefined)
            inPrefix = "";
        if (inDepth === undefined)
            inDepth = 0;
        theGroup = new Array();
        if (inGroupDefinitions[inDepth] !== null)
            theIndex = inGroupDefinitions[inDepth].startIndex;
        if (theIndex === undefined)
            theIndex = 1;

        for (; ;) {
            if (inDepth === inGroupDefinitions.length - 1)
                theElement = inDocument.getElementById(inPrefix + theIndex);
            else {
                theElement = DOM.buildElementGroups(
                    inDocument,
                    inGroupDefinitions,
                    inPrefix + theIndex + inSeparator,
                    inSeparator,
                    inDepth + 1);
            }

            if (theElement == null)
                break;

            theGroup[theGroup.length] = theElement;
            theIndex++;
        }

        if (theGroup.length == 0)
            return (null);

        if (inGroupDefinitions[inDepth] !== null && inGroupDefinitions[inDepth].extractor !== undefined)
            theGroup = inGroupDefinitions[inDepth].extractor(theGroup);

        return (theGroup);
    },


    buildElementGroupsFromChildren: function (inElement, inExtractor) {
        var i;
        var max;
        var theList;
        var theElement;

        theList = new Array();
        inElement = inElement.childNodes;
        for (i = 0, max = inElement.length; i < max; i++) {
            theElement = inElement.item(i);
            if (theElement instanceof HTMLElement)
                theList[theList.length] = inExtractor(theElement);
        }
        return (theList);
    },


    extractArrayElement: function (inArray, inIndex) {
        return (inArray[inIndex]);
    },


    objectWrap: function (inObject, inPropertyName) {
        var theResult;

        theResult = new Object();
        theResult[inPropertyName] = inObject;

        return (theResult);
    },


    extractPropertiesFromAttributes: function (inElement, inObject, inPropertyNames, inPropertyTransformers) {
        var i;
        var theName;
        var theValue;

        if (inPropertyNames) {
            for (i = 0; i < inPropertyNames.length; i++) {
                theName = inPropertyNames[i];
                theValue = inElement.getAttribute(theName).value;
                if (inPropertyTransformers && inPropertyTransformers.getProperty(theName))
                    theValue = inPropertyTransformers.getProperty(theName)(theValue);
                inObject[theName] = theValue;
            }
        }
        else {
            var theAttributes;
            var theAttribute;

            for (theAttributes = inElement.attributes, i = 0; i < theAttributes.length; i++) {
                theAttribute = theAttributes.item(i);
                if (theAttribute.specified) {
                    // Browsers are inconsistent in case preservation for attribute names
                    theName = theAttribute.name.toLowerCase();
                    theValue = theAttribute.value;
                    if (inPropertyTransformers && inPropertyTransformers.getProperty(theName))
                        theValue = inPropertyTransformers.getProperty(theName)(theValue);
                    inObject[theName] = theValue;
                }
            }
        }
        return (inObject);
    },


    convertToNumber: function (inValue, inEmptyValue, inFailureValue) {
        var theNumber;

        try {
            if (inValue === null || inValue === undefined) {
                if (inEmptyValue !== undefined)
                    return (inEmptyValue);
                throw ("attempted to convert null or undefined to number")
            }

            theNumber = eval(inValue);
            if (typeof theNumber != 'number') {
                if (inFailureValue !== undefined)
                    return (inFailureValue);
                throw ("attempted to convert '" + inValue + "' to number");
            }

            return (theNumber);
        }
        catch (theError) {
            if (inFailureValue !== undefined)
                return (inFailureValue);
            throw (theError);
        }
    },


    extractPropertiesFromFirstElementInGroup: function (inElementGroup, inElementGroupPropertyName, inPropertyNames, inPropertyTransformers) {
        return (DOM.extractPropertiesFromAttributes(
            inElementGroup[0],
            DOM.objectWrap(inElementGroup, inElementGroupPropertyName),
            inPropertyNames,
            inPropertyTransformers));
    },


    extractPropertiesFromFormElements: function (inDocument, inIDs) {
        var i;
        var theResult;
        var theElement;

        // todo: eventually this should populate the result in exactly the same way as a submit, i.e. it should
        // handle radio buttons and check boxes in the same way.  It should also handle selects.
        theResult = new Properties();
        for (i = 0; i < inIDs.length; i++) {
            theElement = inDocument.getElementById(inIDs[i]);
            if (theElement === null)
                theResult.setProperty(inIDs[i], null);
            else
                theResult.setProperty(inIDs[i], theElement.value);
        }

        return (theResult);
    },


    replaceElements: function (inOldElements, inNewElements) {
        var theOldIndex;
        var i;

        for (i = 0; i < inOldElements.length; i++) {
            if (inOldElements[i] !== inNewElements[i]) {
                if (inNewElements[i].parentNode != null) {
                    theOldIndex = inOldElements.indexOf(inNewElements[i]);
                    if (theOldIndex >= 0) {
                        thePlaceholder = inNewElements[i].ownerDocument.createComment("");
                        inNewElements[i].parentNode.replaceChild(thePlaceholder, inNewElements[i]);
                        inOldElements[theOldIndex] = thePlaceholder;
                    }
                }
                inOldElements[i].parentNode.replaceChild(inNewElements[i], inOldElements[i])
            }
        }
    },


    setDisplayStyle: function (inElements, inDisplay) {
        var i;

        for (i = 0; i < inElements.length; i++)
            inElements[i].style.display = inDisplay;
    },


    getElementById: function (inID) {
        if (document.getElementById)
            return (document.getElementById(inID));
        else if (document.all)
            return (document.all[inID]);
        else
            return (null);
    },


    addClass: function (inElement, inClass) {
        DOM.removeClass(inElement, inClass);
        inElement.className += " " + inClass;
    },


    removeClass: function (inElement, inClass) {
        inElement.className = inElement.className.replace(new RegExp(" " + inClass + "\\b"), "");
    },


    walkTheDOM: function (inNode, inFunction) {
        inFunction(inNode);
        inNode = inNode.firstChild;
        while (inNode) {
            walkTheDOM(inNode, func);
            inNode = inNode.nextSibling;
        }
    },


    getElementsByClassName: function (inClassName, inNode) {
        var theResults = [];
        var processNode = function (inNodeToCompare) {
            var theClassNames, theClassNameProperty = inNodeToCompare.className, i;
            if (theClassNameProperty) {
                theClassNames = theClassNameProperty.split(' ');
                for (i = 0; i < theClassNames.length; i++) {
                    if (theClassNames[i] === inClassName) {
                        theResults.push(inNodeToCompare);
                        break;
                    }
                }
            }
        };

        DOM.walkTheDOM(inNode || document.body, processNode)

        return theResults;
    },

    processElementByClassName: function (inClassName, inFunction, inNode) {
        var theResults = DOM.getElementsByClassName(inclassName, inNode);
        for (var i = 0; i < theResults.length; i++)
            inFunction(theResults[i]);
    }

};
