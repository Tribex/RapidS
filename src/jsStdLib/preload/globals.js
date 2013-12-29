/** --Globals.js (Preload)--
 * This contains the global variables which will be accessed by user scripts.
 * 
 * The goal of this file is to provide variables which are as close to the standard browser ones to
 * minimize confusion and transition. 
 */

//Proper access to the Window class. Needed to fit with both JavaScript and Java conventions at the same time.
var window = Window;

var system = {
		showError : Packages.us.derfers.tribex.rapids.Utilities.showError,
}