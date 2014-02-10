/**
 * Provides the ability to use XML label tags to create JLabels.
 */

require(Packages.javax.swing.JLabel);

__widgetTypes.registerWidget("label", function (parentComposite, widgetElement, engine) {
    var widget = new JLabel();
    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Set button text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    __widgetOps.initializeWidget(widget, widgetElement, engine);
    return widget;
});

