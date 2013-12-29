/** Provides widget padding styles */

styles.registerLayoutStyle("padding-x", "Sets the internal x padding of a widget.", function (constraint, value) {
		constraint.ipadx = Integer.valueOf(value);
});

styles.registerLayoutStyle("padding-y", "Sets the internal y padding of a widget.", function (constraint, value) {
	constraint.ipady = Integer.valueOf(value);
});