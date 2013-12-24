/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
importClass(Packages.us.derfers.tribex.rapids.Globals);
importClass(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
importClass(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);

var widgets = {
	
	//Used to add a new widget
	addWidget : function(name, func) {
		this[name] = func;
	}
}
