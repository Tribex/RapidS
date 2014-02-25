/**
 * Provides the ability to use XML tab tags to create JPanels with nested child widgets.
 */

require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);
require(Packages.us.derfers.tribex.rapids.GUI.Swing.GUI);

__widgetTypes.registerWidget("tab", function (parentComposite, widgetElement, parentID) {
        //Create a new Panel
        var widget = new JPanel();

        //Set the layout of the panel to GridBagLayout TODO: Add more layout types
        widget.setLayout(new GridBagLayout());

        //Add the panel to the tab with all of its constraints and set its name.
        if (widgetElement.getAttributeNode("value") != null) {
            parentComposite.addTab(widgetElement.getAttributeNode("value").getTextContent(), null, widget, null);
        } else {
            parentComposite.addTab("New Tab", null, widget, null);
        }

        //Load all elements inside of the composite/widget.  INFINITE NESTING!
        var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);

        __widgetOps.loadInComposite(widget, widgetElement, id, ["menuitem"], ["*"]);

        return widget;
});
