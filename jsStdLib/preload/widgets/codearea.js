/**
 * Provides the ability to use XML label tags to create JTextAreas.
 */

require(Packages.javax.swing.JEditorPane);
require(Packages.javax.swing.JScrollPane);

widgets.registerWidget("JEditorPane", "codearea", "A simple JEditorPane with syntax highlighting support.", function (parentComposite, widgetElement, engine) {

    //Create a new textarea
    widget = new JEditorPane();

    //Create a new scrollpane and add the textarea to it
    scrollPane = new JScrollPane(widget);

    //Set the text of the textArea
    widget.setText(widgetElement.getTextContent());

    //Add the scrollpane to the parentComposite
    parentComposite.add(scrollPane, Layouts.getWidgetConstraint(widgetElement));

    widgets.initializeWidget(widget, widgetElement, engine);

    return widget;
});

