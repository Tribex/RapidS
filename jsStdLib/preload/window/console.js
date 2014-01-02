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
			System.out.println(string);
		}
}