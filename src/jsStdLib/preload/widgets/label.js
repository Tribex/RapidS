/**
 * Provides the ability to use XML label tags to create JLabels.
 */

require(Packages.javax.swing.JLabel);

widgets.registerWidget("JLabel", "label", "A simple JLabel", function (parentComposite, widgetElement, engine) {
	var widget = new JLabel();
	parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

	//Set button text with the content of the <button></button> tags
	widget.setText(widgetElement.getTextContent());

	widgets.initializeWidget(widget, widgetElement, engine);
	return widget;
});

