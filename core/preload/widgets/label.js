/**
 * Provides the ability to use XML label tags to create JLabels.
 */

require(Packages.javax.swing.JLabel);

__widgetTypes.registerWidget("label", function (parentComposite, widgetElement, parentID) {
    var widget = new JLabel();

    //Set button text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    //Initialize and add the widget.
    var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);
    parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

    return widget;
});

