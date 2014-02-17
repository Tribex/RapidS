/**
 * Provides the ability to use XML tabpane tags to create JTabbedPanes.
 */

require(Packages.javax.swing.JTabbedPane);
require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);

__widgetTypes.registerWidget("tabpane", function (parentComposite, widgetElement, engine) {
    var widget = new JTabbedPane();

    //Load all elements inside of the composite/widget.  INFINITE NESTING!
    GUI.loadInComposite(widget, widgetElement);

    //Initialize and add the widget
    var id = __widgetOps.initializeWidget(widget, widgetElement, engine);
    parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

    return widget;
},
[], []);

