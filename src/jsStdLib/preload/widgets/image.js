/**
 * Provides the ability to use XML img tags to create images.
 */

require(Packages.java.io.File);
require(Packages.javax.imageio.ImageIO);
require(Packages.javax.swing.ImageIcon);
require(Packages.javax.swing.JLabel);

widgets.registerWidget("Image", "img", "A simple image.", function (parentComposite, widgetElement, engine) {
	
	//if the src is there
	if (widgetElement.getAttributeNode("src") != null) {
		//create a new image object
		var image = new ImageIO.read(new File(widgetElement.getAttributeNode("src").getNodeValue()));
	}

	var widget = new JLabel(new ImageIcon(image));
	parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

	widgets.initializeWidget(widget, widgetElement, engine);
	return widget;
});

