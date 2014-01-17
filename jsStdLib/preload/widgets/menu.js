/**
 * Provides the ability to use XML menu tags to create JMenus.
 */

require(Packages.javax.swing.JMenu);
require(Packages.javax.swing.JMenuItem);
require(Packages.us.derfers.tribex.rapids.jvStdLib.Window);

widgets.registerWidget("JMenu", "menu", "A simple JMenu", function (parentComposite, widgetElement, engine) {
    var widget = new JMenu();

    if (widgetElement.getParentNode().getNodeName() == "body") {
        parentComposite = window.getElementById(widgetElement.getParentNode().getParentNode().getAttributeNode("id").getTextContent()).getJMenuBar();
    }

    if (widgetElement.getAttributeNode("value") != null) {
        widget.setText(widgetElement.getAttributeNode("value").getTextContent());
    } else {
        Utilities.showError("Error: No value tag in menu element.");
    }
    parentComposite.add(widget);

    GUI.loadInComposite(widget, widgetElement, engine);

    widgets.initializeWidget(widget, widgetElement, engine);
    return widget;
});

widgets.registerWidget("JMenuItem", "menuitem", "A simple JMenuItem", function (parentComposite, widgetElement, engine) {
    var widget = new JMenuItem();

    widget.setText(widgetElement.getTextContent());

    parentComposite.add(widget);

    widgets.initializeWidget(widget, widgetElement, engine);
    return widget;
});
