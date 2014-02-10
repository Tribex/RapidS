/**
 * Provides the ability to use XML button tags to create JButtons.
 */

require(Packages.javax.swing.JButton);

widgetTypes.registerWidget("button", function (parentComposite, widgetElement, engine) {
    var widget = new JButton();
    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Set button text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    widgetOps.initializeWidget(widget, widgetElement, engine);
    return widget;
});

