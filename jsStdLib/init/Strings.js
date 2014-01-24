/**
 * @file Defines extra functions for the String object not defined by Rhino.  NOT STANDARD ECMASCRIPT!!!
 * @author Nateowami, Tribex
 */


if (typeof String.prototype.endsWith != 'function') {
    /**
     * Returns true if the object ends with the specified string
     * @param string {string} The string to search for
     * @returns {boolean} True if the object ends with the search string, False if it doesn't.
     */
    String.prototype.endsWith = function(string){
        return (this.indexOf(string) == this.length - string.length);
    };
}

//tell if the the string (this) contains string
if (typeof String.prototype.contains != 'function') {
    /**
     * Returns true if the object contains the specified string.
     * @param string {string} The string to search for.
     * @returns {boolean} True if the object contains the search string, False if it doesn't.
     */
    String.prototype.contains = function(string){
        return (this.indexOf(string) > -1);
    };
}

//tell if the string (this) starts with string
if (typeof String.prototype.startsWith != 'function') {
    /**
     * Returns true if the object starts with the specified string
     * @param string {string} The string to search for
     * @returns {boolean} True if the object starts with the search string, False if it doesn't.
     */
    String.prototype.startsWith = function(string){
        return (this.indexOf(string) == 0);
    };
}

