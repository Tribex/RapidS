/** Provides widget coloring styles */

require(Packages.java.awt.Color);

styles.registerWidgetStyle("background-color", "Sets the background color of the widget.", function (widget, value) {
	//Set the background color of the widget
	widget.setBackground(Color.decode(value));

	//Necessarry for some widgets to display the background, no adverse effects afaik.
	widget.setOpaque(true);
	
	//ALWAYS RETURN THE WIDGET
	return widget;
});

styles.registerWidgetStyle("foreground-color", "Sets the foreground color of the widget.", function (widget, value) {
	//Set the foreground color of the widget
	widget.setForeground(Color.decode(value));

	//ALWAYS RETURN THE WIDGET
	return widget;
});

styles.registerWidgetStyle("color", "Sets the foreground color of the widget. (Alias of foreground-color)", function (widget, value) {
	//Set the foreground color of the widget
	widget.setForeground(Color.decode(value));
	
	//ALWAYS RETURN THE WIDGET
	return widget;
});

//NOT WORKING
styles.registerWidgetStyle("opacity", "Sets the opacity of the widget", function (widget, value) {
	
	if (value.toUpperCase().indexOf("OPAQ") > -1) {
		widget.setOpaque(true);
	} else if (value.toUpperCase().indexOf("TRANS") > -1) {
		widget.setOpaque(false);
	}
	
	//ALWAYS RETURN THE WIDGET
	return widget;
});