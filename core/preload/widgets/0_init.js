/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);
require(Packages.java.awt.GridBagConstraints);

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
        //Populated by a call from Java class Loader to css.parseString.
        styles : null,
        //Used for widgets that do not have a unique Id set.
        __NO__ID : 0,

        //For initializing and saving widgets.
        initializeWidget : function (widget, widgetElement, engine, prependID) {
            if (prependID === null || prependID === undefined) {
                prependID = "";
            }

            //Iterate through listener types and set listeners if they exist
            for (var i=0; i < Globals.listenerTypesArray.length; i++) {
                var listenerType = Globals.listenerTypesArray[i];
                //Add a listener for listenerType if specified
                if (widgetElement.getAttributeNode(listenerType) != null) {
                    WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
                }

            }
            //Get the proper Id for this widget.
            var id = this.getWidgetId(widgetElement, prependID);

            //Store the widget in __widgetList
            this.storeWidget(id, widgetElement, widget);

            //Get the styles for the widget's element.
            this.getWidgetStyles(id, __widgetList[id].element, "");

            //Get the styles for the widget's class. (Can override element)
            this.getWidgetStyles(id, __widgetList[id].class, ".");

            //Get the styles for the widget's id. (Can override element and class)
            this.getWidgetStyles(id, id, "#");
        },

        //Store a widget in the widgetList
        storeWidget : function(widgetID, widgetElement, widget) {
            //Create an object to hold the widget and its attributes.
            __widgetList[widgetID] = {};
            var widgetObject = __widgetList[widgetID];

            Utilities.debugMsg("Adding widget "+widgetID+" to __widgetList object.", 3);

            //Add the widget itself to the widgetObject
            widgetObject["widget"] = widget;

            //Get a list of the attributes
            var widgetAttributes = widgetElement.getAttributes();

            //Iterate through all the attributes of the widget and add them to the widgetObject
            for (var i=0; i < widgetAttributes.getLength(); i++) {
                widgetObject[widgetAttributes.item(i).getNodeName()] = widgetAttributes.item(i).getTextContent();
            }

            //Define Proxy for widgetObject.name so that we can override the getter and setter to call functions on change.
            Object.defineProperty(widgetObject, "name", {
                get : function () {
                    return this._name;
                },
                set : function (val) {
                    this._name = val;
                    this.widget.setName(val);
                }
            });

            if (widgetElement.getAttributeNode("name") !== null) {
                widgetObject["name"] = widgetElement.getAttributeNode("name").getTextContent();
            } else {
                widgetObject["name"] = widgetID;
            }

            //Set the element of widgetObject
            widgetObject["element"] = widgetElement.getNodeName();

            widgetObject["id"] = widgetID;

            //Define Proxy for widgetObject.name so that we can override the getter and setter to call functions on change.
            Object.defineProperty(widgetObject, "class", {
                get : function () {
                    return this._class;
                },
                set : function (val) {
                    this._class = val;
                    WidgetOps.getWidgetStyles(this.id);
                }
            });

            if (widgetElement.getAttributeNode("class") !== null) {
                widgetObject["class"] = widgetElement.getAttributeNode("class").getTextContent();
            }

            //Add the ability to add a child to this widget at runtime.
            widgetObject["appendChild"] = function(child) {
                var childNodes = program.XMLFragToDocument(child).getChildNodes();
                for (var i = 0; i < childNodes.getLength(); i++) {
                    Packages.us.derfers.tribex.rapids.GUI.Swing.GUI.loadInComposite(widgetObject.widget, childNodes.item(0));
                }
            }

            widgetObject["setAttribute"] = function(attribute, data) {
                widgetObject[attribute] = data;
            }
        },

        //Get the id of a widget from it's widgetElement.
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

        //Sets the styles to be applied for widgetID
        getWidgetStyles : function(id, type, prependType) {
            var widgetData = __widgetList[id];
            widgetData.styles = {};
            for(var key in this.styles[prependType+type]) {
                widgetData.styles[key] = this.styles[prependType+type][key];
                __widgetList[id] = widgetData;
                console.log("Applying: "+key+ ": " + __widgetList[id].styles[key]);
                if (__styleList.widgetStyles[key] !== null && __styleList.widgetStyles[key] !== undefined) {
                    __styleList.widgetStyles[key].apply(__widgetList[id].widget, widgetData.styles[key])

                } else if (__styleList.layoutStyles[key] !== null && __styleList.layoutStyles[key] !== undefined){
                    var widgetConstraint = new GridBagConstraints();
                    __styleList.layoutStyles[key].apply(widgetConstraint, widgetData.styles[key])
                }
            }
        }
};


//Hold current UI widgets
var __widgetList = {};

