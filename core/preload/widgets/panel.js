/**
 * Provides the ability to use XML panel tags to create JPanels with nested child widgets.
 */

require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.GUI);

__widgetTypes.registerWidget("panel", function (parentComposite, widgetElement, parentID) {
        //Create a new Panel
        var widget = new JPanel();

        //Set the layout of the panel to GridBagLayout TODO: Add more layout types
        widget.setLayout(new GridBagLayout());

        //Initialize the widget.
        var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);

        //Add the panel to the window with all of its constraints.
        parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

        //Load all elements inside of the composite/widget.  INFINITE NESTING!
        GUI.loadInComposite(widget, widgetElement, parentID);

        return widget;
});
