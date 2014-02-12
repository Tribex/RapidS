/** Provides widget visibility style - Doesn't really work well at all yet. */

//TODO: Make this work right.
__styleList.registerWidgetStyle("visibility", "Sets the visibility of the widget.", function (widget, value) {
	
	if (value.toUpperCase() == "HIDDEN") {
		widget.setVisible(false);
		
	} else if (value.toUpperCase() == "VISIBLE") {
		widget.setVisible(true);
	} else {
		widget.setVisible(false);
	}
	
	return widget;
});
