package us.derfers.tribex.rapids;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds variables that must be accessed by all classes at all times.
 * Don't modify this unless you know what you're doing.
 * 
 * @author TribeX
 */
public class Globals {
	
	//XXX: SYSTEM :XXX\\
	/**The selected current working directory. DON'T modify this, as it is automatically set.*/
	public static String selCWD = null;
	
	/**
	 * Getter for the selCWD. Useful for JavaScripts that need it.
	 * @return selCWD
	 */
	public static String getCWD() {
		return selCWD;
	}
	//XXX: STYLES :XXX\\
	/**Holds all styles TODO: Make window-specific*/
	public static Map<String, Map<String, String>> stylesMap = new HashMap<String, Map<String, String>>();
	
	//XXX: WIDGETS :XXX\\
	/**Holds all possible widget listener types*/
	public static String[] listenerTypesArray = {"onmouseup", "onmousedown", "onmouseover", "onmouseout", "onselection", "onclick"};
}


