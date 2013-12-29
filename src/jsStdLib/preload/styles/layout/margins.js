/** Provides widget margin styles */

require(Packages.us.derfers.tribex.rapids.Utilities);

styles.registerLayoutStyle("margin", 
		"Sets margin of the widget. (INT INT INT INT)", 
		function (constraint, value) {
			//Split string (int int) or (int int int int) into an array 
			var tempMargins = value.split(" ");
			var margins = {
					
			};
			//If the user has specified different values for every side, set them
			if (tempMargins.length == 4) {
				margins["top"] = Integer.valueOf(tempMargins[0]);
				margins["left"] = Integer.valueOf(tempMargins[1]);
				margins["bottom"] = Integer.valueOf(tempMargins[2]);
				margins["right"] = Integer.valueOf(tempMargins[3]);
		
				//If the user has specified horizontal and vertical values, set them
			} else if (tempMargins.length == 2) {
				margins["top"] = Integer.valueOf(tempMargins[0]);
				margins["left"] = Integer.valueOf(tempMargins[1]);
				margins["bottom"] = Integer.valueOf(tempMargins[0]);
				margins["right"] = Integer.valueOf(tempMargins[1]);
			}
	}
);
