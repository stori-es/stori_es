var Arrays =
{
    isArray: function (inObject) {
        return (inObject && typeof inObject == 'object' && inObject.constructor == Array);
    },


    assignIndexProperty: function (inArray, inPropertyName) {
        var i;

        for (i = 0; i < inArray.length; i++)
            inArray[i][inPropertyName] = i;
        return (inArray);
    },


    extractByIndexes: function (inDataArray, inIndexArray) {
        var theResult;
        var i;

        theResult = new Array(inIndexArray.length);
        for (i = 0; i < inIndexArray.length; i++)
            theResult[i] = inDataArray[inIndexArray[i]];

        return (theResult);
    },


    extractFromElementProperty: function (inArray, inPropertyName) {
        var theResult;
        var i;

        theResult = new Array(inArray.length);
        for (i = 0; i < inArray.length; i++)
            theResult[i] = inArray[i][inPropertyName];

        return (theResult);
    },


    flatten: function (inArray) {
        var theResult;

        function flattenArray(inArray, inResultIndex, outArray) {
            var i;

            for (i = 0; i < inArray.length; i++) {
                if (Arrays.isArray(inArray[i]))
                    inResultIndex = flattenArray(inArray[i], inResultIndex, outArray);
                else
                    outArray[inResultIndex++] = inArray[i];
            }

            return (inResultIndex);
        }


        function getSize(inArray) {
            var i;
            var theSize;

            for (i = 0, theSize = 0; i < inArray.length; i++) {
                if (Arrays.isArray(inArray[i]))
                    theSize += getSize(inArray[i]) - 1;
            }

            return (theSize + inArray.length);
        }

        theResult = new Array(getSize(inArray));
        flattenArray(inArray, 0, theResult);

        return (theResult);
    },


    vectorizeIterator: function (inIterator) {
        var theResult;

        theResult = new Array();
        while (inIterator.hasNext())
            theResult[theResult.length] = inIterator.next();

        return (theResult);
    },


    Iterator: function (itsArray) {
        var i;


        this.hasNext = function () {
            return (i < itsArray.length);
        };


        this.next = function () {
            return (itsArray[i++]);
        };


        i = 0;
    },


    TreeIterator: function (inArray) {
        return (new Iterators.RecursiveIterator(new Arrays.Iterator(inArray), Arrays.treeIteratorFactory));
    },


    treeIteratorFactory: function (inArray) {
        if (Arrays.isArray(inArray))
            return (new Arrays.TreeIterator(inArray));
        return (null);
    },


    // Compatibility functions, can be added to Array.prototype if desired

    indexOf: function (inValue) {
        var i;

        for (i = 0; i < this.length; i++) {
            if (this[i] == inValue)
                return (i);
        }

        return (-1);
    },


    augmentArrayPrototype: function () {
        if (!Array.prototype.indexOf)
            Array.prototype.indexOf = Arrays.indexOf;
    }
};
