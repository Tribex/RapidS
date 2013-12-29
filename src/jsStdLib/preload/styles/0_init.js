/** --styles/0_init.js (Preload)--
 * Creates the styles object for styling and layout of widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Will be needed by most of the style types
require(Packages.java.lang.Integer);

var styles = {
		//Styles that effect the widget's layout and positioning
		layoutStyles : {},
		
		//Styles that effect the widget's appearance and behavior.
		widgetStyles : {},
		
		//Register function for widget layouts
		registerLayoutStyle : function (name, description, func) {
			this.layoutStyles[name] = {name : name, description : description, apply : func} 
		},
};
