/**
 * @file Provides important program functions, mainly exiting.
 * @author Tribex
 */

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

