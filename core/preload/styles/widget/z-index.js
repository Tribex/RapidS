/** Provides widget z-index style - Doesn't really work well at all yet. */

//TODO: Make this more flexible and advanced.. And work right.
__styleList.registerWidgetStyle("z-index", "Sets the z-index of the widget.", function (widget, value) {
	//Get the parent of the widget
	var parent = widget.getParent();
	
	//Get the value set by CSS for the z-index
	var ZIndex = Integer.valueOf(value);
	
	//If ZIndex is larger than the amount of widgets in the parent, decrease it to avoid a NPE.
	if (ZIndex > parent.getComponentCount()-1) {
		ZIndex = parent.getComponentCount()-1;
		
	//If ZIndex is less than 0, set it to 0 to avoid a NPE or IVE
	} else if (ZIndex < 0) {
		ZIndex = 0;
	}
	parent.setComponentZOrder(widget, ZIndex);
	
	return widget;
});
