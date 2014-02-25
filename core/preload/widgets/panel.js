/**
 * Provides the ability to use XML panel tags to create JPanels with nested child widgets.
 */

require(Packages.javax.swing.JPanel);
require(Packages.java.awt.GridBagLayout);

__widgetTypes.registerWidget("panel", function (parentComposite, widgetElement, parentID) {
        //Create a new Panel

        var id = __widgetOps.getWidgetId(widgetElement);

        var widget = new JavaAdapter(JPanel, {
            //This overrides the default paint method. May cause some bugs, not entirely sure.
            paint : function(graphics) {
                //Paint the component itself.
                this.paintComponent(graphics);

                //Paint the component's border.
                this.paintBorder(graphics);

                //If there is a function to paint below the children of this component, run it, passing the graphics.
                if (program.getElementById(id).paintBelow != null) {
                    program.getElementById(id).paintBelow(graphics);
                }

                //Paint the children of this component.
                this.paintChildren(graphics);

                //If there is a function to paint above the children of this component, run it, passing the graphics.
                if (program.getElementById(id).paintAbove != null) {
                    program.getElementById(id).paintAbove(graphics);
                }
            },
        });

        //Set the layout of the panel to GridBagLayout TODO: Add more layout types
        widget.setLayout(new GridBagLayout());

        //Initialize the widget.
        var id = __widgetOps.initializeWidget(widget, widgetElement, parentID);

        //Add the panel to the window with all of its constraints.
        parentComposite.add(widget, __widgetOps.applyWidgetConstraint(id));

        //Load all elements inside of the composite/widget.  INFINITE NESTING!
        __widgetOps.loadInComposite(widget, widgetElement, id);

        return widget;
});
