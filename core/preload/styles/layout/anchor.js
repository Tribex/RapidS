/** Provides widget anchor styles */

require(Packages.us.derfers.tribex.rapids.Utilities);

__styleList.registerLayoutStyle("anchor", 
		"Sets anchor point of the widget.", 
		function (constraint, value) {
			var f = null;
			try {
				f = constraint.getClass().getField(value.toUpperCase().replace('-', '_'));
			} catch (e) {
				// TODO Auto-generated catch block
				Utilities.showError("Invalid Anchor value: "+value+" - "+e.toString());
			} 
			
			try {
				constraint.anchor = f.getInt(constraint);
			} catch (e) {}
		}
);
