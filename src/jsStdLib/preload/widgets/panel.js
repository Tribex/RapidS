/**
 * Provides the ability to use XML panel tags to create JPanels with nested child widgets.
 */

require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);

widgets.registerWidget("JPanel", "panel", "A JPanel which can hold other widgets.", function (parentComposite, widgetElement, engine) {
		//Create a new Panel
		var widget = new JPanel();

		//Set the layout of the panel to GridBagLayout TODO: Add more layout types
		widget.setLayout(new GridBagLayout());

		//Add the panel to the window with all of its constraints.
		parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

		//Load all elements inside of the composite/widget.  INFINITE NESTING!
		GUI.loadInComposite(widget, widgetElement, engine);
		
		
		widgets.initializeWidget(widget, widgetElement, engine);

		return widget;
});