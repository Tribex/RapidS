/**
 * Provides the ability to use XML button tags to create JButtons.
 */

require(Packages.javax.swing.JButton);

widgets.registerWidget("JButton", "button", "A simple JButton", function (parentComposite, widgetElement, engine) {
    var widget = new JButton();
    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Set button text with the content of the <button></button> tags
    widget.setText(widgetElement.getTextContent());

    widgets.initializeWidget(widget, widgetElement, engine);
    return widget;
});

