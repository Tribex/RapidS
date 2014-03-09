/** --widgets/0_init.js (Preload)--
 * Creates the widgets object for use by further widgets.
 * The 0 is used to force the file to be loaded first.
 */

//Import global classes
require(Packages.us.derfers.tribex.rapids.Globals);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.Layouts);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.WidgetOps);
require(Packages.java.awt.GridBagConstraints);
require(Packages.org.w3c.dom.Node);


/**
 * A list of registered widgets. Populated by __widgetTypes.registerWidget()
 * @namespace
 */
var __widgetTypes = {
        //Used to register a new widget type.
        registerWidget : function (element, loader, styleBlacklist, styleWhitelist, legalChildren) {
            this[element] = {element : element, loader : loader, styleBlacklist : styleBlacklist, styleWhitelist : styleWhitelist, legalChildren : legalChildren}
        },

}

/**
 * Widget Operations
 * @namespace
 */
var __widgetOps = {
        defaultLegalChildren : ["*"],
        //Populated by a call from Java class Loader to css.parseString.
        styles : null,
        //Used for widgets that do not have a unique Id set.
        __NO__ID : 0,

        /**
         * Applies styles, stores, and parses widgets.
         * @param widget {JComponent} A Swing widget.
         * @param widgetElement {Element} The XML element that the widget came from.
         * @param parentID {string} The id of the parent widget.
         * @param prependID {string} A string that will be prepended to this widget's ID. Currently not used.
         */
        initializeWidget : function (widget, widgetElement, parentID, prependID) {
            if (prependID === null || prependID === undefined) {
                prependID = "";
            }

            //Iterate through listener types and set listeners if they exist
            for (var i=0; i < Globals.listenerTypesArray.length; i++) {
                var listenerType = Globals.listenerTypesArray[i];
                //Add a listener for listenerType if specified
                if (widgetElement.getAttributeNode(listenerType) != null) {
                    WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue());
                }

            }
            //Get the proper Id for this widget.
            var id = this.getWidgetId(widgetElement, prependID);

            //Store the widget in __widgetList
            this.storeWidget(id, widgetElement, widget, parentID);

            //Get the styles for the widget's element.
            this.getWidgetStyles(id, __widgetList[id].element, "");

            //Get the styles for the widget's class. (Can override element)
            this.getWidgetStyles(id, __widgetList[id].class, ".");

            //Get the styles for the widget's id. (Can override element and class)
            this.getWidgetStyles(id, id, "#");

            this.applyWidgetStyles(id);

            return id;
        },

        //Store a widget in the widgetList
        storeWidget : function(widgetID, widgetElement, widget, parentID) {
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

            //Set the id of widgetObject
            widgetObject["id"] = widgetID;

            //Define Proxy for widgetObject.name so that we can override the getter and setter to call functions on change.
            Object.defineProperty(widgetObject, "class", {
                get : function () {
                    return this._class;
                },
                set : function (val) {
                    this._class = val;
                    __widgetOps.getWidgetStyles(this.id, this.class, ".");
                    __widgetOps.applyWidgetStyles(this.id);
                }
            });

            //Set the class of widgetObject
            if (widgetElement.getAttributeNode("class") !== null) {
                widgetObject["class"] = widgetElement.getAttributeNode("class").getTextContent();
            }

            //Set the parent of the widget
            widgetObject["parent"] = parentID;

            //Add this widget to the parent's children.
            if (__widgetList[parentID] != null) {
                if(__widgetList[parentID]["children"] == null) {
                    __widgetList[parentID]["children"] = [];
                }
                __widgetList[parentID]["children"].push(widgetID);
            }

            //Add the ability to add a child to this widget at runtime.
            widgetObject["appendChild"] = function(child) {
                var childNodes = program.XMLFragToDocument(child).getChildNodes();
                if (childNodes != null) {
                    for (var i = 0; i < childNodes.getLength(); i++) {
                        __widgetOps.loadInComposite(widgetObject.widget, childNodes.item(0), this.id);
                    }
                }
            }

            //Define the setAttribute method so that people can be more programmatic.
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
            //If the styles have not already been defined, create a new object (prevents overwriting of previous styles).
            if (__widgetList[id].styles === null || __widgetList[id].styles === undefined) {
                __widgetList[id].styles = {};
            }

            var getElementStyles = function(element, key) {
                //Whether or not this style is whitelisted for this widget.
                var styleWhitelisted = false;

                //Whether or not to keep the style after this iteration is complete.
                var keepStyle = true;

                //Iterate through the whitelist and see if this style is whitelisted.
                if (__widgetTypes[element] != null && __widgetTypes[element].styleWhitelist != null) {
                    for (var iwl=0; iwl < __widgetTypes[element].styleWhitelist.length; iwl++) {
                        //If this style is in the whitelist
                        if (key == __widgetTypes[element].styleWhitelist[iwl]) {
                            //confirm that we have whitelisted this style, so that the blacklist is ignored.
                            styleWhitelisted = true;
                            //Keep the style
                            keepStyle = true;
                            //Exit the loop
                            break;

                            //If the style is not in this iteration
                        } else {
                            //If the element has a parent, check the parent for this style
                            if (__widgetTypes[element].styleWhitelist[iwl].startsWith("+")) {

                                if (__widgetTypes[element].styleWhitelist[iwl].split("+").length > 1) {
                                    var parent = __widgetTypes[element].styleWhitelist[iwl].split("+")[1];
                                    //Iterate through the same styles in the parent
                                    keepStyle = getElementStyles(parent, key);
                                }
                            }
                        }
                    }
                }

                //If the style is not whitelisted, check and see if it is blacklisted.
                if (!styleWhitelisted) {
                    if (__widgetTypes[element] != null && __widgetTypes[element].styleBlacklist != null) {
                        for (var ibl=0; ibl < __widgetTypes[element].styleBlacklist.length; ibl++) {
                            //If the style is in the blacklist
                            if (key == __widgetTypes[element].styleBlacklist[ibl] || __widgetTypes[element].styleBlacklist[ibl] == "*") {
                                //Do not keep the style, and exit the loop.
                                keepStyle = false;
                                break;

                                //If the style is not in this iteration of the blacklist
                            } else {
                                //Check and see if there is a parent blacklist
                                if (__widgetTypes[element].styleBlacklist[iwl] != null && __widgetTypes[element].styleBlacklist[iwl].startsWith("+")) {
                                    if (__widgetTypes[element].styleBlacklist[iwl].split("+").length > 1) {
                                        var parent = __widgetTypes[element].styleBlacklist[iwl].split("+")[1];
                                        //Iterate through the parent blacklist and keep or not appropraitely.
                                        keepStyle = getElementStyles(parent, key);
                                    }
                                }
                            }
                        }
                    }
                }

                //Return whether or not to keep the style.
                return keepStyle;
            }
            //Add styles to the widgetList[id] styles object. Does not distinguish between style inheritance types.
            if (this.styles != null && this.styles[prependType+type] != null) {

                for(var key in this.styles[prependType+type]) {
                    var widgetData = __widgetList[id];

                    if (getElementStyles(widgetData.element, key)) {
                        __widgetList[id].styles[key] = this.styles[prependType+type][key];
                    }
                }
            }


        },

        //Apply the styles to the widget
        applyWidgetStyles : function(id) {
            for (var key in __widgetList[id].styles) {
                if (__styleList.widgetStyles[key] !== null && __styleList.widgetStyles[key] !== undefined) {
                    __widgetList[id].widget = __styleList.widgetStyles[key].apply(__widgetList[id].widget, __widgetList[id].styles[key])
                }
            }
        },

        //Apply the styles to the widgetConstraint. Called from the widget's constructor.
        applyWidgetConstraint : function(id) {
            //Create a new constraint with default values.
            var constraint = new GridBagConstraints();
            constraint.anchor = GridBagConstraints.LINE_START;
            constraint.fill = GridBagConstraints.BOTH;
            constraint.ipadx = 5;
            constraint.ipadx = 5;
            constraint.weightx = 0.1;
            constraint.weighty = 0.1;
            constraint.gridx = 0;
            constraint.gridy = GridBagConstraints.RELATIVE;

            //Apply the styles
            for (var key in __widgetList[id].styles) {
                if (__styleList.layoutStyles[key] !== null && __styleList.layoutStyles[key] !== undefined) {
                    __styleList.layoutStyles[key].apply(constraint, __widgetList[id].styles[key])
                }
            }

            //Return the constraint for use by widgets.
            return constraint;
        },

        /**
         * Loads all XML widgets into the parent composite.
         * @param parentComposite {JComponent} Any widget that can accept children
         * @param node {Element} The body or any other composite node.
         * @param id {string} The id of parentComposite
         * @param legalChildren {array} An array of valid or invalid children tag names.
         */
        loadInComposite : function(parentComposite, node, id) {
            var childElementList = node.getChildNodes();
            //Loop through all children of the root element.
            for (var counter=0; counter < childElementList.getLength(); counter++) {
                //Isolate the node
                var widgetElement = childElementList.item(counter);

                //Make sure it is a proper element.
                if (widgetElement.getNodeType() == Node.ELEMENT_NODE) {
                    //If the tagname is found in widgetTypes
                    if (__widgetTypes[widgetElement.getNodeName()] != null) {
                        if (__widgetList[id] != null && __widgetTypes[__widgetList[id].element] != null) {
                            var legalChildren = __widgetTypes[__widgetList[id].element].legalChildren;
                            //console.log(legalChildren);
                        } else {
                            var legalChildren = this.defaultLegalChildren;
                        }

                        if ((legalChildren.contains("*") && !legalChildren.contains("!"+widgetElement.getNodeName()))
                                || legalChildren.contains(widgetElement.getNodeName())) {

                            //Make sure the JavaScript widgetType object is really a widget Type
                            if (__widgetTypes[widgetElement.getNodeName()].element == widgetElement.getNodeName()) {
                                //Run the JavaScript function to draw and display the widget
                                __widgetTypes[widgetElement.getNodeName()].loader(parentComposite, widgetElement, id);
                            }

                            //If the widget is not found in widgetTypes
                        } else {
                            program.showError("Error: Illegal child widget for "+id+": "+widgetElement.getNodeName());
                        }
                    }else {
                        program.showError("Error: Widget: "+widgetElement.getNodeName()+" does not exist or is not registered.");
                    }

                }

            }
            //position and draw the widgets
            parentComposite.doLayout();
        },

};


//Hold current UI widgets
var __widgetList = {};

