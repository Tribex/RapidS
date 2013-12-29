/** Provides layout fill style */

require(Packages.java.awt.GridBagConstraints);

styles.registerLayoutStyle("fill", "Sets direction for the widget to automatically grab space from.", 
		function (constraint, value) {
			//If the widget is to fill horizontally
			if (value.toUpperCase() === "HORIZONTAL") {
				constraint.fill = GridBagConstraints.HORIZONTAL;

			//If the widget is to fill vertically
			} else if (value.toUpperCase() === "VERTICAL"){
				constraint.fill = GridBagConstraints.VERTICAL;

			//If the widget is to fill both horizontally and vertically
			} else if (value.toUpperCase() === "BOTH"){
				constraint.fill = GridBagConstraints.BOTH;

			//If the widget is not to fill
			} else if (value.toUpperCase() === "NONE"){
				constraint.fill = GridBagConstraints.NONE;
			}
		}
);