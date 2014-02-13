/** Provides widget border styles */

require(Packages.java.awt.Color);
require(Packages.javax.swing.border.Border);
require(Packages.javax.swing.BorderFactory);
require(Packages.us.derfers.tribex.rapids.Utilities);

//TODO: Make this more flexible and advanced.
__styleList.registerWidgetStyle("border", "Sets the border of the widget.", function (widget, value) {
    //Split the border string between color and width
    var borderinfo = value.split(" ");

    //Create a new border
    if (borderinfo.length > 1) {
        var border = BorderFactory.createLineBorder(Color.decode(borderinfo[1]), Integer.valueOf(borderinfo[0]));
        widget.setBorder(border);
    } else {
        Utilities.showError("Invalid value for CSS border: "+value);
    }

    return widget;
});
