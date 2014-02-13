/** Provides widget padding styles */

__styleList.registerLayoutStyle("padding-x", "Sets the internal x padding of a widget.", function (constraint, value) {
		constraint.ipadx = Integer.valueOf(value);
});

__styleList.registerLayoutStyle("padding-y", "Sets the internal y padding of a widget.", function (constraint, value) {
	constraint.ipady = Integer.valueOf(value);
});