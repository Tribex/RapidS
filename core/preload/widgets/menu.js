/**
 * Provides the ability to use XML menu tags to create JMenus.
 */

require(Packages.javax.swing.JMenu);
require(Packages.javax.swing.JMenuItem);
require(Packages.us.derfers.tribex.rapids.jvCoreLib.Window);

__widgetTypes.registerWidget("menu", function (parentComposite, widgetElement, parentID) {
    var widget = new JMenu();

    if (widgetElement.getParentNode().getNodeName() == "body") {
        parentComposite = program.getElementById(widgetElement.getParentNode().getParentNode().getAttributeNode("id").getTextContent()).widget.getJMenuBar();
    }

    if (widgetElement.getAttributeNode("value") != null) {
        widget.setText(widgetElement.getAttributeNode("value").getTextContent());
    } else {
        Utilities.showError("Error: No value tag in menu element.");
        return null;
    }
    parentComposite.add(widget);

    var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);

    __widgetOps.loadInComposite(widget, widgetElement, id, ["menuitem"], ["*"]);

    return widget;
});

__widgetTypes.registerWidget("menuitem", function (parentComposite, widgetElement, parentID) {
    var widget = new JMenuItem();

    widget.setText(widgetElement.getTextContent());

    parentComposite.add(widget);

    __widgetOps.initializeWidget(widget, widgetElement, parentID);
    return widget;
});
