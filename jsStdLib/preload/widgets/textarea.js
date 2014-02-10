/**
 * Provides the ability to use XML label tags to create JTextAreas.
 */

require(Packages.javax.swing.JTextArea);
require(Packages.javax.swing.JScrollPane);

widgetTypes.registerWidget("textarea", function (parentComposite, widgetElement, engine) {

    //Create a new textarea
    widget = new JTextArea();

    //Create a new scrollpane and add the textarea to it
    scrollPane = new JScrollPane(widget);

    //Set the text of the textArea
    widget.setText(widgetElement.getTextContent());

    //Add the scrollpane to the parentComposite
    parentComposite.add(scrollPane, Layouts.getWidgetConstraint(widgetElement));

    widgetOps.initializeWidget(widget, widgetElement, engine);

    return widget;
});

