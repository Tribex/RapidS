/**
 * @file Defines extra functions for the Array object not defined by Rhino.  NOT STANDARD ECMASCRIPT!!!
 * @author Tribex
 */

/**
 * Check if an array contains a value.
 * @param obj {object} The value to search the array for.
 * @returns {boolean} true if found, false if not.
 */
Array.prototype.contains = function(obj) {
    var i = this.length;
    //Supposedly the fastest way to iterate in JavaScript. May not be true for Rhino.
    while (i--) {
        if (this[i] == obj) {
            return true;
        }
    }
    return false;
}
