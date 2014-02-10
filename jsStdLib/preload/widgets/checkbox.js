require(Packages.javax.swing.JCheckBox);

__widgetTypes.registerWidget("checkbox", function (parentComposite, widgetElement, engine) {
    //Create a new Checkbox.
    var widget = new JCheckBox();

    //Set the text of the widget to the contents of the <checkbox></checkbox> tags.
    widget.setText(widgetElement.getTextContent());

    //Add the panel to the window with all of its constraints.
    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    //Add the widget to all maps and set event listeners.
    __widgetOps.initializeWidget(widget, widgetElement, engine);

    //Return the widget for forwards compatibility.
    return widget;
});
