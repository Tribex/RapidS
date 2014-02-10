/**
 * Provides the ability to use XML spinner tags to create JSpinners.
 */

require(Packages.javax.swing.JSpinner);
require(Packages.javax.swing.SpinnerNumberModel);
require(Packages.java.lang.Integer);

widgetTypes.registerWidget("spinner", function (parentComposite, widgetElement, engine) {
    //Create a new composite for sub-elements
    var model = new SpinnerNumberModel();

    if (widgetElement.getAttributeNode("value") != null) {
        model.setValue(Integer.valueOf(widgetElement.getAttributeNode("value").getNodeValue()));
    }

    if (widgetElement.getAttributeNode("max") != null) {
        model.setMaximum(Integer.valueOf(widgetElement.getAttributeNode("max").getNodeValue()));
    }

    if (widgetElement.getAttributeNode("min") != null) {
        model.setMinimum(Integer.valueOf(widgetElement.getAttributeNode("min").getNodeValue()));
    }

    //Add widget to maps
    var widget = new JSpinner(model);

    //Add the panel to the window with all of its constraints.
    parentComposite.add(widget, Layouts.getWidgetConstraint(widgetElement));

    widgetOps.initializeWidget(widget, widgetElement, engine);

    return widget;
});
