/** --window/console.js (Preload)--
 * @file Holds the console object and all of its functions and parameters.
 * @author Tribex
 */

require(java.lang.System);

/**
 * Print an object to the console. Not standard ECMAScript.
 * @param object {Object} The object to print
 */
function print(object) {
    if (object == null) {
        System.out.println("null");
    } else if (object == undefined) {
        System.out.println("undefined");
    } else {
        System.out.println(object);
    }
}

/**
 * The console object
 * @namespace
 */
console = {
        /**
         * Print an object to the console. Not standard ECMAScript.
         * @param object {Object} The object to print
         */
        log : function(object) {
            if (object == null) {
                System.out.println("null");
            } else if (object == undefined) {
                System.out.println("undefined");
            } else {
                System.out.println(object);
            }
        }
}
