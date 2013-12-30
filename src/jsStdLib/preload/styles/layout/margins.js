/** Provides widget margin styles */

require(Packages.us.derfers.tribex.rapids.Utilities);

styles.registerLayoutStyle("margin", 
		"Sets margin of the widget. (INT INT INT INT)", 
		function (constraint, value) {
			//Split string (int int) or (int int int int) into an array 
			var tempMargins = value.split(" ");

			//If the user has specified different values for every side, set them
			if (tempMargins.length == 4) {
				constraint.insets.top = Integer.valueOf(tempMargins[0]);
				constraint.insets.left = Integer.valueOf(tempMargins[1]);
				constraint.insets.bottom = Integer.valueOf(tempMargins[2]);
				constraint.insets.right = Integer.valueOf(tempMargins[3]);
		
				//If the user has specified horizontal and vertical values, set them
			} else if (tempMargins.length == 2) {
				constraint.insets.top = Integer.valueOf(tempMargins[0]);
				constraint.insets.left = Integer.valueOf(tempMargins[1]);
				constraint.insets.bottom = Integer.valueOf(tempMargins[0]);
				constraint.insets.right = Integer.valueOf(tempMargins[1]);
			}
		}
);

styles.registerLayoutStyle("margin-top", 
		"Sets the top margin of the widget.", 
		function (constraint, value) {
			constraint.insets.top = Integer.valueOf(value);
		}
);

styles.registerLayoutStyle("margin-bottom", 
		"Sets the bottom margin of the widget.", 
		function (constraint, value) {
			constraint.insets.bottom = Integer.valueOf(value);
		}
);

styles.registerLayoutStyle("margin-left", 
		"Sets the left margin of the widget.", 
		function (constraint, value) {
			constraint.insets.left = Integer.valueOf(value);
		}
);

styles.registerLayoutStyle("margin-right", 
		"Sets the right margin of the widget.", 
		function (constraint, value) {
			constraint.insets.right = Integer.valueOf(value);
		}
);