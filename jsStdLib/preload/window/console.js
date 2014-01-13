/** --window/console.js (Preload)--
 * Holds the console object and all of its functions and parameters.
 */
require(java.lang.System);

function print(string) {
	System.out.println(string);
}
console = {
		//console.log function. Prints *string and a newline.
		log : function(string) {
		    if (string == null) {
		        System.out.println("null");
		    } else if (string == undefined) {
		        System.out.println("undefined");
		    } else {
		        System.out.println(string);
		    }
		}
}