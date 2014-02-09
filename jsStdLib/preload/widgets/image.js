/**
 * Provides the ability to use XML img tags to create images.
 */

require(Packages.java.io.File);
require(Packages.javax.imageio.ImageIO);
require(Packages.javax.swing.ImageIcon);
require(Packages.javax.swing.JLabel);
require(Packages.us.derfers.tribex.rapids.Globals);

widgetTypes.registerWidget("img", function (parentComposite, widgetElement, engine) {
    //if the src is there
    if (widgetElement.getAttributeNode("src") != null) {
        try {
            //create a new image object
            var image = new ImageIO.read(new File(Globals.getCWD(widgetElement.getAttributeNode("src").getNodeValue())));

            var widget = new JLabel(new ImageIcon(image));
            parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

            widgets.initializeWidget(widget, widgetElement, engine);

            return widget;
        } catch (e) {
            Utilities.debugMsg(e.message);
            Utilities.showError("Error: Image "+Globals.getCWD(widgetElement.getAttributeNode("src").getNodeValue())+" does not exist \n\nor is improperly formatted.");
            return false;
        }
    } else {
        Utilities.showError("Error: Image tag does not have a 'src' element.");
        return false;
    }

});

