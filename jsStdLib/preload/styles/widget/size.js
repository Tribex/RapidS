/** Provides widget width and height styles */

require(Packages.java.awt.Dimension);

styles.registerWidgetStyle("width", "Sets the preferred width of the widget.", function (widget, value) {
	
	widget.setPreferredSize(new Dimension(Integer.valueOf(value), widget.getPreferredSize().height));

	return widget;
});

styles.registerWidgetStyle("height", "Sets the preferred height of the widget.", function (widget, value) {
	
	widget.setPreferredSize(new Dimension(widget.getPreferredSize().width, Integer.valueOf(value)));

	return widget;
});
