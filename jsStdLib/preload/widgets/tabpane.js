/**
 * Provides the ability to use XML tabpane tags to create JTabbedPanes.
 */

require(Packages.javax.swing.JTabbedPane);

widgets.registerWidget("JTabbedPane", "tabpane", "A simple JTabbedPane", function (parentComposite, widgetElement, engine) {
    var widget = new JTabbedPane();

    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Load all elements inside of the composite/widget.  INFINITE NESTING!
    GUI.loadInComposite(widget, widgetElement, engine);

    widgets.initializeWidget(widget, widgetElement, engine);
    return widget;
});

