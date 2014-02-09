/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
importClass(Packages.us.derfers.tribex.rapids.Globals);
importClass(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
importClass(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);

//A list of registered widgets. Populated by widgetTypes.registerWidget()
var widgetTypes = {
        //Used to add a new widget to the GUI
        addWidget : function(name, func) {
            this[name] = func;
        },

        //Used to register a new widget type.
        registerWidget : function (element, loader, styleBlacklist, styleWhitelist, customData) {
            this[element] = {element : element, loader : loader, styleBlacklist : styleBlacklist, styleWhitelist : styleWhitelist}
        },

}

//Hold current UI widgets
var widgets = {
        //Iterate through listener types and set listeners if they exist
        initializeWidget : function (widget, widgetElement, engine, prependID) {
            if (prependID === null || prependID === undefined) {
                prependID = "";
            }

            for (var i=0; i < Globals.listenerTypesArray.length; i++) {
                var listenerType = Globals.listenerTypesArray[i];
                //Add a listener for listenerType if specified
                if (widgetElement.getAttributeNode(listenerType) != null) {
                    WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
                }

            }
            var id = WidgetOps.getWidgetId(widgetElement, prependID);
            WidgetOps.addWidgetToMaps(id, widgetElement, widget, engine);
            widget = WidgetOps.getWidgetStyles(widget, id);
            WidgetOps.addWidgetToMaps(id, widgetElement, widget, engine);
            widgets[id] = {widget : widget, parent : widget.getParent()}
        },
}
