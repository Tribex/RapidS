/**
 * Provides the ability to use XML tabpane tags to create JTabbedPanes.
 */

require(Packages.javax.swing.JTabbedPane);
require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);

__widgetTypes.registerWidget("tabpane", function (parentComposite, widgetElement, parentID) {
    var widget = new JTabbedPane();

    //Initialize and add the widget
    var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);
    parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

    //Load all elements inside of the composite/widget.  INFINITE NESTING!
    __widgetOps.loadInComposite(widget, widgetElement, id, ["tab"], ["*"]);

    return widget;
},
[], []);

