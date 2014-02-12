/** Provides widget weight styles */

require(Packages.java.lang.Float);

__styleList.registerLayoutStyle("weight-x", 
		"Sets x the weight of a widget. (The relative amount of space it takes up)", 
		function (constraint, value) {
			constraint.weightx = Float.valueOf(value);
		}
);

__styleList.registerLayoutStyle("weight-y", 
		"Sets y the weight of a widget. (The relative amount of space it takes up)", 
		function (constraint, value) {
			constraint.weighty = Float.valueOf(value);
		}
);
