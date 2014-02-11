/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);

//A list of registered widgets. Populated by __widgetTypes.registerWidget()
var __widgetTypes = {
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
var __widgetOps = {
        __NO__ID : 0,
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
            var id = this.getWidgetId(widgetElement, prependID);
            //TODO: Phase out old WidgetOps class
            this.storeWidget(id, widgetElement, widget);
            WidgetOps.getWidgetStyles(id);
        },

        storeWidget : function(widgetID, widgetElement, widget) {

            //Create an object to hold the widget and its attributes.
            var widgetObject = {};

            Utilities.debugMsg("Adding widget "+widgetID+" to __widgetList object.", 3);

            //Add the widget itself to the widgetObject
            widgetObject["widget"] = widget;

            //Get a list of the attributes
            var widgetAttributes = widgetElement.getAttributes();

            //Iterate through all the attributes of the widget and add them to the widgetObject
            for (var i=0; i < widgetAttributes.getLength(); i++) {
                widgetObject[widgetAttributes.item(i).getNodeName()] = widgetAttributes.item(i).getTextContent();
            }

            //Set the name of widgetObject
            if (widgetElement.getAttributeNode("name") !== null) {
                widgetObject["name"] = widgetElement.getAttributeNode("name").getTextContent();
            }

            //Set the element of widgetObject
            widgetObject["element"] = widgetElement.getNodeName();

            //Set the id of widgetObject
            widgetObject["id"] = widgetID;

            //Add the ability to add a child to this widget at runtime.
            widgetObject["appendChild"] = function(child) {
                var childNodes = program.XMLFragToDocument(child).getChildNodes();
                for (var i = 0; i < childNodes.getLength(); i++) {
                    Packages.us.derfers.tribex.rapids.GUI.Swing.GUI.loadInComposite(widgetObject.widget, childNodes.item(0));
                }
            }

            //Add the temporary widgetMap to the widgets object under its id.
            __widgetList[widgetID] = widgetObject;
        },

        getWidgetId : function(widgetElement, prependID) {
            //Define the ID of the button
            var widgetID = null;
            if (widgetElement.getAttributeNode("id") !== null) {
                widgetID = prependID+widgetElement.getAttributeNode("id").getNodeValue();

                //If not, assign an incremental id: __ID__#
            } else {
                widgetID = prependID+"__NOID__"+this.__NO__ID;
                this.__NO__ID += 1;
            }
            return widgetID;
        },
};


//Hold current UI widgets
var __widgetList = {};

