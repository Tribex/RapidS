require(Packages.javax.swing.JButton);


widgets.registerWidget("button", "button", "A simple JButton", function (parentComposite, widgetElement, engine) {
		var widget = new JButton();
		parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

		//Set button text with the content of the <button></button> tags
		widget.setText(widgetElement.getTextContent());

		//Iterate through listener types and set listeners if they exist
		for (var i=0; i < Globals.listenerTypesArray.length; i++) {
			var listenerType = Globals.listenerTypesArray[i];
			//Add a listener for listenerType if specified
			if (widgetElement.getAttributeNode(listenerType) != null) {
				WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
			}

		}
		WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
});

widgets.registerWidget("label", "label", "A simple JLabel", function (parentComposite, widgetElement, engine) {
	var widget = new JButton();
	parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

	//Set button text with the content of the <button></button> tags
	widget.setText(widgetElement.getTextContent());

	//Iterate through listener types and set listeners if they exist
	for (var i=0; i < Globals.listenerTypesArray.length; i++) {
		var listenerType = Globals.listenerTypesArray[i];
		//Add a listener for listenerType if specified
		if (widgetElement.getAttributeNode(listenerType) != null) {
			WidgetOps.addMethodListener(listenerType, widget, widgetElement.getAttributeNode(listenerType).getNodeValue(), engine);
		}

	}
	WidgetOps.addWidgetToMaps(widgetElement, widget, engine);
});
