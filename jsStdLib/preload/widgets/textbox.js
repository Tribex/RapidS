/**
 * Provides the ability to use XML label tags to create JTextFields.
 */

require(Packages.javax.swing.JTextField);

widgets.registerWidget("JTextField", "textfield", "A simple JTextField", function (parentComposite, widgetElement, engine) {
	var widget = new JTextField();
	parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

	//Set button text with the content of the <button></button> tags
	widget.setText(widgetElement.getTextContent());

	widgets.initializeWidget(widget, widgetElement, engine);
	return widget;
});

