/**
 * Provides the ability to use XML label tags to create JTextFields.
 */

require(Packages.javax.swing.JTextField);

__widgetTypes.registerWidget("textfield", function (parentComposite, widgetElement, engine) {
    var widget = new JTextField();
    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Set button text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    __widgetOps.initializeWidget(widget, widgetElement, engine);
    return widget;
});

