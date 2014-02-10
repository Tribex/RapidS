/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);
require(Packages.org.w3c.dom.NamedNodeMap);

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

//Widget Operations
var widgetOps = {
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
            //TODO: Phase out old WidgetOps class
            WidgetOps.addWidgetToMaps(id, widgetElement, widget, engine);
            widget = WidgetOps.getWidgetStyles(widget, id);
            WidgetOps.addWidgetToMaps(id, widgetElement, widget, engine);
            //Use this instead.
            widgetOps.storeWidget(id, widgetElement, widget);
        },

        storeWidget : function(widgetID, widgetElement, widget) {

            //Create a HashMap to hold Widget ID and class, as well as other parameters.
            var widgetObject = {};


            Utilities.debugMsg("Adding widget "+widgetID+" to widgets object.", 3);

            //Add the widget to the widgetMap
            widgetObject[widgetID] = widget;

            //If the ID is set, add it to the Object list so that we can get it later.
            var widgetAttributes = widgetElement.getAttributes();

            //Iterate through all the attributes of the widget and add them to the widgetMap
            for (var i=0; i < widgetAttributes.getLength(); i++) {
                widgetObject[widgetAttributes.item(i).getNodeName()] = widgetAttributes.item(i).getTextContent();
            }


            widgetObject["element"] = widgetElement.getNodeName();
            widgetObject["id"] = widgetID;

            //Add the temporary widgetMap to the XMLWidgets array.
            widgets[widgetID] = widgetObject;
        }
};


//Hold current UI widgets
var widgets = {

}

