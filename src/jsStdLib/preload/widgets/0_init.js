/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
importClass(Packages.us.derfers.tribex.rapids.Globals);
importClass(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
importClass(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);

var widgets = {
	widgetTypes : {
		
	},
		
	//Used to add a new widget to the GUI
	addWidget : function(name, func) {
		this[name] = func;
	},

	//Used to register a new widget type.
	registerWidget : function (name, element, description, func) {
		this.widgetTypes[name] = {element : element, description : description, loader : func} 
	} ,
}
