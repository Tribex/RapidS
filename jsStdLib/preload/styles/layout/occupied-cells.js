/** Provides widget cell occupation styles */

require(java.lang.Integer);

__styleList.registerLayoutStyle("occupied-cells-x", "Sets amount of cells a widget will take up in the x direction.", function (constraint, value) {
		constraint.gridwidth = Integer.valueOf(value);
});

__styleList.registerLayoutStyle("occupied-cells-y", "Sets amount of cells a widget will take up in the y direction.", function (constraint, value) {
	constraint.gridheight = Integer.valueOf(value);
});