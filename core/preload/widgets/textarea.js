/**
 * Provides the ability to use XML label tags to create JTextAreas.
 */

require(Packages.javax.swing.JTextArea);
require(Packages.javax.swing.JScrollPane);

__widgetTypes.registerWidget("textarea", function (parentComposite, widgetElement, parentID) {

    //Create a new textarea
    widget = new JTextArea();

    //Create a new scrollpane and add the textarea to it
    scrollPane = new JScrollPane(widget);

    //Set the text of the textArea
    widget.setText(widgetElement.getTextContent());

    //Initialize the widget
    var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);

    //Add the scrollpane to the parentComposite
    parentComposite.add(scrollPane, __widgetOps.applyWidgetConstraint(id));

    return widget;
});

