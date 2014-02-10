/**
 * @file Provides important program functions.
 * @author Tribex
 */

require(Packages.us.derfers.tribex.rapids.Utilities);
/**
 * Contains methods that deal with the program.
 * @namespace
 */
var program = {};

/**
 * Cause the program to exit with the specified code.
 * @param code {integer} 0 for success, 1+ for error.
 */
program.exit = function (code) {
    System.exit(code);
}

/**
 * Alias to Utilities.showError();
 */
program.showError = Packages.us.derfers.tribex.rapids.Utilities.showError;


/**
 * Gets the widget object fromm the XMLWidgets Map that matches 'id'.
 * @param id The id of the widget to return
 * @return The widget object specified by 'id' from XMLWidgets
 */
program.getElementById = function (id) {
    if (__widgetList[id] !== null && __widgetList[id] !== undefined) {
        return __widgetList[id];
    }
};


/**
 * Gets an array of widget objects from the widgetList Map that match 'class_identifier'.
 * @param class_identifier The class of the widgets to return
 * @return An Array of widgets specified by the 'class_identifier' from widgetList.
 */
program.getElementsByClass = function(class_identifier) {
    var classes = [];
    for(var widgetObj in __widgetList) {
        if (__widgetList.hasOwnProperty(widgetObj)) {
            if (__widgetList[widgetObj]["class"] !== null &&
                    __widgetList[widgetObj]["class"] !== undefined &&
                    __widgetList[widgetObj]["class"] == class_identifier) {

                classes.push(__widgetList[widgetObj]);
            }
        }
    }

    if (classes.length > 0) {
        return classes;
    } else {
        return null;
    }
}

/**
 * Turns a string into an XML Document.
 * @param fragment {string} A valid XML string.
 */
program.XMLFragToDocument = function(fragment) {
    return Utilities.XMLStringToDocument("<xml>"+fragment+"</xml>");
};
