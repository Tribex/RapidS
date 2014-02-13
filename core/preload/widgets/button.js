/**
 * Provides the ability to use XML button tags to create JButtons.
 */

require(Packages.javax.swing.JButton);

__widgetTypes.registerWidget("button", function (parentComposite, widgetElement, engine) {
    var widget = new JButton();

    //Set button text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    var id = __widgetOps.initializeWidget(widget, widgetElement, engine);

    parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

    return widget;
});

