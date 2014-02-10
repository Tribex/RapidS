/**
 * Provides the ability to use XML tabpane tags to create JTabbedPanes.
 */

require(Packages.javax.swing.JTabbedPane);
require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);

widgetTypes.registerWidget("tabpane", function (parentComposite, widgetElement, engine) {
    var widget = new JTabbedPane();

    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Load all elements inside of the composite/widget.  INFINITE NESTING!
    GUI.loadInComposite(widget, widgetElement, engine);

    widgetOps.initializeWidget(widget, widgetElement, engine);
    return widget;
}
);

