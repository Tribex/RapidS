/**
 * Provides the ability to use XML label tags to create JTextFields.
 */

require(Packages.javax.swing.JTextField);

__widgetTypes.registerWidget("textfield", function (parentComposite, widgetElement, parentID) {
    var widget = new JTextField();

    //Set field text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    //Initialize and add widget.
    var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);
    parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

    return widget;
});

