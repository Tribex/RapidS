/** Provides widget cell occupation styles */

require(Packages.java.awt.GridBagConstraints);

styles.registerLayoutStyle("position-x", "Sets the x position of the widget in the parent panel's grid.", function (constraint, value) {
	if (value.indexOf("rel") != -1) {
		constraint.gridx = GridBagConstraints.RELATIVE;
	} else {
		constraint.gridx = Integer.valueOf(value);
	}
});

styles.registerLayoutStyle("position-y", "Sets the y position of the widget in the parent panel's grid.", function (constraint, value) {
	if (value.indexOf("rel") != -1) {
		constraint.gridy = GridBagConstraints.RELATIVE;
	} else {
		constraint.gridy = Integer.valueOf(value);
	}
});